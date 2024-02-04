package yiyan.research.mapper;

import org.apache.ibatis.annotations.*;
import yiyan.research.model.domain.WorksInfo;
import yiyan.research.model.domain.openalex.Works;
import yiyan.research.model.domain.openalex.WorksAuthorships;
import yiyan.research.model.domain.openalex.WorksConcepts;
import yiyan.research.model.domain.openalex.WorksPrimaryLocations;
import yiyan.research.model.entity.AuthorWorkStatistic.CoAuthorWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.CoInsWorkCount;
import yiyan.research.model.entity.AuthorWorkStatistic.ConceptWorkCount;
import yiyan.research.model.entity.InterestConceptByYear;
import yiyan.research.model.esEntity.ESWork;
import yiyan.research.model.response.search.GetRelatedWorksRsp;

import java.util.List;

@Mapper
public interface WorkMapper {
    //works
    @Select("select * from works where id=#{id}")
    Works getWorkById(String id);

    //works_alternate_host_venues


    //works_authorships
    @Select("select * from works_authorships where work_id=#{id}")
    List<WorksAuthorships> getWorksAuthorshipsByWorkId(String id);
    @Select("select work_id from works_authorships wa, works_open_access woa " +
            "where wa.author_id=#{id} && wa.work_id=woa.work_id && woa.is_oa = 'True'")
    List<WorksAuthorships> getOAWorksByAuthorId(String id);
    @Select("""
            select wa.author_id as id, a.display_name,wa.raw_affiliation_string
                     from works_authorships wa, authors a
                     where wa.work_id=#{workId} && wa.author_id=a.id
            group by wa.work_id, wa.author_id, a.display_name, wa.author_position,wa.raw_affiliation_string
            """)
    List<ESWork.Author> getAuthorInWork(String workId);

    @Select("""
            SELECT wa1.author_id,a.display_name as authorName, COUNT(*) AS count
            FROM works_authorships wa1,authors a
            WHERE EXISTS (
                SELECT 1
                FROM works_authorships wa2
                WHERE wa2.work_id = wa1.work_id
                  AND wa2.author_id = #{author_id}
            )
            AND wa1.author_id != #{author_id}
            AND wa1.author_id = a.id
            GROUP BY wa1.author_id,a.display_name
            order by count desc;""")
    List<CoAuthorWorkCount> getCoAuthorWorkCountList(String authorId);

    /**
     * 获取作者合作机构作品数量列表
     *
     * @param authorId 作者ID
     * @return 合作机构作品数量列表
     */
    @Select("""
            SELECT tmp.institution_id AS insId, i.display_name as insName, COUNT(*) AS count
             from (
                     select institution_id, work_id,count(*)
                         FROM works_authorships wa1
                         WHERE EXISTS (
                             SELECT 1
                            FROM works_authorships wa2
                             WHERE wa2.work_id = wa1.work_id
                              AND wa2.author_id = #{authorId}
                         )
                         GROUP BY wa1.institution_id,wa1.work_id
                         ) tmp,institutions i
             where tmp.institution_id = i.id
             group by tmp.institution_id,i.display_name
             order by count desc;
             """)
    List<CoInsWorkCount> getCoInsWorkCountList(String authorId);

    /**
     * 获取作者的作品与概念关联数量
     *
     * @param authorId 作者ID
     * @return 返回作者的作品与概念关联数量列表
     */
    @Select("""
            select wc.concept_id,c.display_name as conceptName,count(*) as count
                                          from works_authorships wa , works_concepts wc, concepts c
                                          where wa.author_id = #{authorId} and wa.work_id = wc.work_id and wc.score > 0.3
                                           and c.id = wc.concept_id
                                          group by wc.concept_id,c.display_name
                                          order by count desc
                                          """)
    List<ConceptWorkCount> getConceptWorkCount(String authorId);

    @Select("""
            select c.id as conceptId,
                   w.publication_year as year,
                   c.display_name as conceptName,
                   count(*) as workCount
            from concepts c,works_concepts wc,works w,works_authorships wa,
                 (select c.id,count(*) as count
                from works_concepts wc, concepts c,works w,works_authorships wa
                where wc.concept_id = c.id
                and wc.work_id = w.id
                and wa.work_id = w.id
                and wa.author_id = 'A5053148274'
                and wc.score > 0.3
                and w.publication_year >= 2017
                and c.level = 2
                group by c.id
                order by count desc
                limit 5) conceptIds
            where  c.level = 2
            and c.id = conceptIds.id
            and wc.concept_id = c.id
            and w.publication_year >= 2017
            and wc.work_id = w.id
            and wa.author_id = #{authorId}
            and wa.work_id = w.id
            group by c.id,w.publication_year,c.display_name
            """)
    List<InterestConceptByYear> getInterestConceptByYear(String authorId);
    /**
     * 根据作者ID和年份获取作者发表的作品列表
     *
     * @param authorId 作者ID
     * @param year 发表年份
     * @return 作品列表
     */
    @Select("""
            SELECT wa.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works_authorships wa,works w
            where wa.author_id = #{authorId} and wa.work_id = w.id and w.publication_year = #{year}""")
    List<ESWork> getWorksByAuthorIdByYear(String authorId, int year);

    /**
     * 根据作者ID获取按引用次数降序排列的工作列表
     *
     * @param authorId 作者ID
     * @return 按引用次数降序排列的工作列表
     */
    @Select("""
            SELECT wa.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works_authorships wa,works w
            where wa.author_id = #{authorId} and wa.work_id = w.id order by w.cited_by_count desc;""")
    List<ESWork> getWorksByAuthorIdOrderByCitedCount(String authorId);

    /**
     * 通过作者ID和概念ID获取符合条件的工作列表
     *
     * @param authorId 作者ID
     * @param concepts 概念ID
     * @return 符合条件的工作列表
     */
    @Select("""
            <script>
                        SELECT DISTINCT
                        wa.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
                        FROM works_concepts wc, works_authorships wa, works w
                        where wa.author_id = #{authorId}
                            and wa.work_id = wc.work_id
                            and w.id = wa.work_id
                            and wc.score > 0.3
                            and wc.concept_id in
                                <foreach item='item' collection='concepts' open='(' separator=',' close=')'>
                                #{item}
                                </foreach>
            </script>
            """)
    List<ESWork> getWorksByAuthorIdAndConcepts(@Param("authorId")String authorId,
                                               @Param("concepts") String[] concepts);

    /**
     * 根据作者ID和合著者ID获取相关作品列表
     *
     * @param authorId 作者ID
     * @param coAuthorIds 合著者ID
     * @return 作品列表
     */
    @Select("""
            <script>
            select w.id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
                 from works w
                 where w.id in(
                     select wa.work_id
                     from works_authorships wa,
                         (select work_id, count(*) as count
                         from works_authorships
                         where author_id = #{authorId}
                         group by work_id
                         ) wa1
                     where wa.author_id in
                         <foreach item='item' collection='coAuthorIds' open='(' separator=',' close=')'>
                         #{item}
                         </foreach>
                       and wa.work_id = wa1.work_id
                     group by wa.work_id
                     )
            </script>
            """)
    List<ESWork> getWorksByAuthorIdAndCoAuthors(@Param("authorId")String authorId,
                                                          @Param("coAuthorIds") String[] coAuthorIds);

    /**
     * 根据作者ID和合作机构ID获取作者发表的论文列表
     * 合作机构包括作者所在机构以及合作作者所在机构
     *
     * @param authorId 作者ID
     * @param coInsIds 机构ID
     * @return 作者发表的论文列表
     */
    @Select("""
            <script>
            select wa1.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
                FROM works_authorships wa1,works w
                WHERE EXISTS (
                    SELECT 1
                   FROM works_authorships wa2
                    WHERE wa2.work_id = wa1.work_id
                     AND wa2.author_id = #{authorId}
                )
                and wa1.institution_id in
                     <foreach item='item' collection='coInsIds' open='(' separator=',' close=')'>
                     #{item}
                     </foreach>
                and w.id = wa1.work_id
            GROUP BY wa1.institution_id,wa1.work_id,w.publication_year, w.title, w.cited_by_count
            order by w.publication_year desc;
            </script>
            """)
    List<ESWork> getWorksByAuthorIdAndCoInss(@Param("authorId") String authorId,
                                                       @Param("coInsIds") String[] coInsIds);

    @Select("""
            select wa.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works_authorships wa, works w
            where wa.author_id = #{authorId} and w.id = wa.work_id
            GROUP BY wa.work_id,w.publication_year, w.title, w.cited_by_count
            order by w.publication_year desc;""")
    //默认按出版年份降序
    List<ESWork> getWorksByAuthorIdDefault(String authorId);

    @Select("""
            SELECT wa.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works_authorships wa,works w
            where wa.author_id = #{authorId} and wa.work_id = w.id and w.publication_year = #{year}
            LIMIT 10 OFFSET #{prevNum}
            """)
    List<ESWork> getWorksByAuthorIdByYearByPervNum(String authorId, int year,
                                                   int prevNum);

    /**
     * 根据作者ID获取按引用次数降序排列的工作列表
     *
     * @param authorId 作者ID
     * @return 按引用次数降序排列的工作列表
     */
    @Select("""
            SELECT wa.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works_authorships wa,works w
            where wa.author_id = #{authorId} and wa.work_id = w.id order by w.cited_by_count desc
            LIMIT 10 OFFSET #{prevNum}
            """)
    List<ESWork> getWorksByAuthorIdOrderByCitedCountByPervNum(String authorId,
                                                     int prevNum);

    /**
     * 通过作者ID和概念ID获取符合条件的工作列表
     *
     * @param authorId 作者ID
     * @param concepts 概念ID
     * @return 符合条件的工作列表
     */
    @Select("""
            <script>
                        SELECT DISTINCT
                        wa.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
                        FROM works_concepts wc, works_authorships wa, works w
                        where wa.author_id = #{authorId}
                            and wa.work_id = wc.work_id
                            and w.id = wa.work_id
                            and wc.score > 0.3
                            and wc.concept_id in
                                <foreach item='item' collection='concepts' open='(' separator=',' close=')'>
                                #{item}
                                </foreach>
                            LIMIT 10 OFFSET #{prevNum}
            </script>
            """)
    List<ESWork> getWorksByAuthorIdAndConceptsByPervNum(@Param("authorId")String authorId,
                                               @Param("concepts") String[] concepts,
                                               int prevNum);

    /**
     * 根据作者ID和合著者ID获取相关作品列表
     *
     * @param authorId 作者ID
     * @param coAuthorIds 合著者ID
     * @return 作品列表
     */
    @Select("""
            <script>
            select w.id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
                 from works w
                 where w.id in(
                     select wa.work_id
                     from works_authorships wa,
                         (select work_id, count(*) as count
                         from works_authorships
                         where author_id = #{authorId}
                         group by work_id
                         ) wa1
                     where wa.author_id in
                         <foreach item='item' collection='coAuthorIds' open='(' separator=',' close=')'>
                         #{item}
                         </foreach>
                       and wa.work_id = wa1.work_id
                     group by wa.work_id
                     )
                     LIMIT 10 OFFSET #{prevNum}
            </script>
            """)
    List<ESWork> getWorksByAuthorIdAndCoAuthorsByPervNum(@Param("authorId")String authorId,
                                                @Param("coAuthorIds") String[] coAuthorIds,
                                                int prevNum);

    /**
     * 根据作者ID和合作机构ID获取作者发表的论文列表
     * 合作机构包括作者所在机构以及合作作者所在机构
     *
     * @param authorId 作者ID
     * @param coInsIds 机构ID
     * @return 作者发表的论文列表
     */
    @Select("""
            <script>
            select wa1.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
                FROM works_authorships wa1,works w
                WHERE EXISTS (
                    SELECT 1
                   FROM works_authorships wa2
                    WHERE wa2.work_id = wa1.work_id
                     AND wa2.author_id = #{authorId}
                )
                and wa1.institution_id in
                     <foreach item='item' collection='coInsIds' open='(' separator=',' close=')'>
                     #{item}
                     </foreach>
                and w.id = wa1.work_id
            GROUP BY wa1.institution_id,wa1.work_id,w.publication_year, w.title, w.cited_by_count
            order by w.publication_year desc
            LIMIT 10 OFFSET #{prevNum}
            </script>
            """)
    List<ESWork> getWorksByAuthorIdAndCoInssByPervNum(@Param("authorId") String authorId,
                                             @Param("coInsIds") String[] coInsIds,
                                             int prevNum);

    @Select("""
            select wa.work_id as id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works_authorships wa, works w
            where wa.author_id = #{authorId} and w.id = wa.work_id
            GROUP BY wa.work_id,w.publication_year, w.title, w.cited_by_count
            order by w.publication_year desc
            LIMIT 10 OFFSET #{prevNum}
            """)
        //默认按出版年份降序
    List<ESWork> getWorksByAuthorIdDefaultByPervNum(String authorId,
                                           int prevNum);
    /***
     * 注：如果本文献未被更新过则使用默认值
     * @param id work id
     * @return work相关信息
     */
    @Select("select * from works_info where work_id = #{id}")
    WorksInfo getWorksInfo(String id);

    @Insert("INSERT INTO works_info (work_id) " +
            "VALUES (#{id});")
    void initWorksInfo(String id);
    @Insert("""
            INSERT INTO works_authorships (work_id, author_id, author_position,institution_id) 
            VALUES (#{workId}, #{authorId}, #{authorPosition},#{institutionId})
            """)
    int addWorkAuthorship(WorksAuthorships wa);

    @Delete("""
            delete from works_authorships
            where author_id = #{authorId} and work_id = #{workId}
            """)
    int deleteWorkAuthorship(String authorId, String workId);


    @Update("update works_info " +
            "set is_comment_allowed = #{isCommentAllowed} " +
            "where work_id = #{id} ")
    void setWorkCommentAllowance(String id, boolean isCommentAllowed);

    @Update("update works_info " +
            "set view_count = view_count + 1 " +
            "where work_id = #{id}")
    void addWorkViewCount(String id);

    @Update("update works_info " +
            "set collection_count = collection_count + #{delta} " +
            "where work_id = #{id}")
    void updateWorkCollectionCount(int delta);



    @Select("""
            select w.id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works w,
            (SELECT wa.work_id
            from works_authorships wa
            where wa.institution_id = #{insId}
            group by wa.work_id) insWorks
            where insWorks.work_id = w.id
            and w.publication_year = #{year}
            """)
    List<ESWork> getWorksByInsIdByYear(String insId, int year);

    @Select("""
            select w.id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works w,
            (SELECT wa.work_id
            from works_authorships wa
            where wa.institution_id = #{insId}
            group by wa.work_id) insWorks
            where insWorks.work_id = w.id
            order by w.cited_by_count desc
            """)

    List<ESWork> getWorksByInsIdOrderByCitedCount(String insId);
    @Select("""
            select w.id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works w,
            (SELECT wa.work_id
            from works_authorships wa
            where wa.institution_id = #{insId}
            group by wa.work_id) insWorks
            where insWorks.work_id = w.id
            order by w.publication_year desc
            """)
    List<ESWork> getWorksByInsIdDefault(String insId);


    @Select("""
            select w.id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works w,
            (SELECT wa.work_id
            from works_authorships wa
            where wa.institution_id = #{insId}
            group by wa.work_id) insWorks
            where insWorks.work_id = w.id
            and w.publication_year = #{year}
            LIMIT 10 OFFSET #{prevNum}
            """)
    List<ESWork> getWorksByInsIdByYearByPrevNum(String insId, int year,int prevNum);

    @Select("""
            select w.id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works w,
            (SELECT wa.work_id
            from works_authorships wa
            where wa.institution_id = #{insId}
            group by wa.work_id) insWorks
            where insWorks.work_id = w.id
            order by w.cited_by_count desc
                        LIMIT 10 OFFSET #{prevNum}

            """)

    List<ESWork> getWorksByInsIdOrderByCitedCountByPrevNum(String insId, int prevNum);
    @Select("""
            select w.id,
                        w.publication_year,
                        w.title,
                        w.cited_by_count,
                        w.abstract_inverted_index as abstractText
            from works w,
            (SELECT wa.work_id
            from works_authorships wa
            where wa.institution_id = #{insId}
            group by wa.work_id) insWorks
            where insWorks.work_id = w.id
            order by w.publication_year desc
                        LIMIT 10 OFFSET #{prevNum}

            """)
    List<ESWork> getWorksByInsIdDefaultByPrevNum(String insId, int prevNum);
    @Select(
            """
            select * from works_concepts where work_id = #{workId}
            """
    )
    List<WorksConcepts> getWorksConceptsById(String workId);

    @Select(
            """
    select c.id,c.display_name
       from works_concepts wc, concepts c
       where wc.work_id= #{workId}
       and wc.concept_id = c.id
       and wc.score > 0.3
                    """
    )
    List<ESWork.Concept> getESWorksConceptsById(String workId);

    @Select("""
            select *
            from works_primary_locations wpl
            where
            wpl.work_id = #{workId}
            """)
    WorksPrimaryLocations getWorksPrimaryLocationsById(String workId);

    @Select("""
            Select s.id,s.display_name
            from works_primary_locations wpl,sources s
            where
            wpl.work_id = #{workId}
            and wpl.source_id = s.id
            """)
    ESWork.Source getESWorksSourceById(String workId);
    @Select("""
            Select *
            from works_info wi
            where wi.work_id = #{workId}
            """)
    WorksInfo getWorksInfoById(String workId);

    @Select("select authors.id, authors.display_name from works_authorships " +
            "JOIN authors ON works_authorships.author_id = authors.id " +
            "where works_authorships.work_id = #{id}")
    List<GetRelatedWorksRsp.Author> getAuthorsOfWork(String id);
}
