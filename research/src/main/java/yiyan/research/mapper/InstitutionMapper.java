package yiyan.research.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import yiyan.research.model.domain.openalex.Institutions;
import yiyan.research.model.domain.openalex.InstitutionsCountsByYear;
import yiyan.research.model.domain.openalex.InstitutionsGeo;
import yiyan.research.model.domain.openalex.InstitutionsStats;
import yiyan.research.model.entity.AuthorInInstitution;
import yiyan.research.model.entity.CoIns;

import java.util.List;

@Mapper
public interface InstitutionMapper {
    @Select("select * from institutions where id=#{id}")
    Institutions getInstitutionById(String id);
    @Select("select * from institutions_geo where institution_id=#{id}")
    InstitutionsGeo getInstitutionGeoById(String id);

    @Results({
            @Result(property = "institutionId", column = "institution_id"),
            @Result(property = "hIndex", column = "hIndex"),
            @Result(property = "yr_mean_citedness", column = "2yr_mean_citedness"),
            @Result(property = "i10Index", column ="i10_index")
            // 其他映射配置...
    })
    @Select("""
            Select * from institutions_stats where institution_id = #{insId}
            """)
    InstitutionsStats getInsStatsById(String insId);

    @Select("""
            select count(*)
            from(
             SELECT  COUNT(*) AS count
                         from (
                                 select institution_id, work_id,count(*)
                                     FROM works_authorships wa1
                                     WHERE EXISTS (
                                         SELECT 1
                                        FROM
                                        works_authorships wa2
                                         WHERE wa2.work_id = wa1.work_id
                                          AND wa2.institution_id = #{insId}
                                     )
                                     GROUP BY wa1.institution_id,wa1.work_id
                                     ) tmp
                         group by tmp.institution_id
                         order by count desc) tmp2;
            """)
    int getCoInsCountById(String insId);
    @Select("SELECT * " +
            "FROM institutions " +
            "JOIN institutions_stats ON institutions.id = institutions_stats.institution_id " +
            "LIMIT #{offset}, #{pageSize}")
    List<Institutions> getAllWithPagination(int offset, int pageSize);

    @Select("""
             SELECT tmp.institution_id AS insId,
                    i.display_name as insName,
                    i.image_thumbnail_url as imageUrl,
                    i.homepage_url as homepageUrl,
                    COUNT(*) AS coWorkCount
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
                         order by coWorkCount desc
                         limit 20
            """)
    List<CoIns> getAuthorCoInsList(String authorId);

    @Select("select * from institutions_counts_by_year where institution_id = #{id}")
    List<InstitutionsCountsByYear> getInsCountByYear(String id);

    @Select("select displayName from institutions where id = #{id}")
    String getInstitutionNameById(String id);

    @Select("select authors.id, authors.display_name, authors.avatar, authors.cited_by_count, authors.professional_title, authors.works_count, authors_stats.h_index " +
            "from authors " +
            "join authors_stats on authors.id = authors_stats.author_id " +
            "where authors.last_known_institution = #{id}")
    List<AuthorInInstitution> getInstMembers(String id);

    @Select("""
             SELECT tmp.institution_id AS insId,
                    i.display_name as insName,
                    i.image_thumbnail_url as imageUrl,
                    i.homepage_url as homepageUrl,
                    COUNT(*) AS coWorkCount
                         from (
                                 select institution_id, work_id,count(*)
                                     FROM works_authorships wa1
                                     WHERE EXISTS (
                                         SELECT 1
                                        FROM works_authorships wa2
                                         WHERE wa2.work_id = wa1.work_id
                                          AND wa2.institution_id = #{insId}
                                     )
                                     GROUP BY wa1.institution_id,wa1.work_id
                                     ) tmp,institutions i
                         where tmp.institution_id = i.id
                         group by tmp.institution_id,i.display_name
                         order by coWorkCount desc
                         limit 20;
            """)
    List<CoIns> getInsCoInsList(String insId);
}
