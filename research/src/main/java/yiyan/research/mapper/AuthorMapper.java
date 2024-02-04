package yiyan.research.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.model.domain.openalex.AuthorsCountsByYear;
import yiyan.research.model.domain.openalex.AuthorsStats;
import yiyan.research.model.entity.CoAuthor;

import java.util.List;

@Mapper
public interface AuthorMapper {
    //authors
    @Select("select * from authors where id=#{id}")
    Authors getAuthorById(String id);
    @Update("""
            update authors
            set userId = #{userId}
            where id = #{authorId}
            """)
    int auth(String authorId,String userId);

    //authors_counts_by_year
    @Select("select * from authors_counts_by_year acby " +
            "where acby.author_id=#{id} and acby.works_count > 0 order by acby.year ASC")
    List<AuthorsCountsByYear> getAuthorsCountsByYearByAuthorId(String id);

    @Update("update authors " +
            "set email=#{email}," +
            "introduction=#{introduction}," +
            "homepage=#{homepage}," +
            "google_scholar=#{googleScholar}," +
            "professional_title=#{professionalTitle}, " +
            "display_institution=#{displayInstitution}," +
            "education = #{education}," +
            "experience = #{experience}," +
            "avatar = #{avatar} " +
            "where id=#{id}")
    int updateAuthor(Authors author);


    //authors_stats
    @Select("select * from authors_stats as ass where author_id = #{authorId}")
    AuthorsStats getAuthorsStatsByAuthorId(String authorId);


    //coAuthor

    @Select("""
        select  a.id as authorId,
                a.display_name           as authorName,
               a.avatar                 as authorImageUrl,
               case
                   when a.display_institution IS NOT NULL then ''
                   ELSE a.last_known_institution
                   end as institutionId,
               CASE
                   WHEN a.display_institution IS NOT NULL THEN a.display_institution
                   ELSE i.display_name
                   END                  as institutionName,
                a.works_count,
               coa.coWorksCount,
               a.cited_by_count,
               ass.h_index,
                a.professional_title
        from authors a, institutions i,authors_stats ass,
        (SELECT wa1.author_id, COUNT(*)AS coWorksCount
        FROM works_authorships wa1
        WHERE EXISTS (
            SELECT 1
            FROM works_authorships wa2
            WHERE wa2.work_id = wa1.work_id
              AND wa2.author_id = 'A5053148274'
        )
        AND wa1.author_id != 'A5053148274'
        GROUP BY wa1.author_id
        order by coWorksCount desc) coa
        where coa.author_id = a.id
            and i.id = a.last_known_institution
            and a.id = ass.author_id
            limit 20
            """)
    List<CoAuthor> getCoAuthorList(String authorId);

    @Update("""
            update authors
            set works_count = works_count +1
            where id = #{authorId}
            """)
    void addAuthorWorkCount(String authorId);

    @Update("""
            update authors
            set works_count = works_count -1
            where id = #{authorId}
            and works_count > 0
            """)
    void reduceWorksCount(String authorId);

    @Select("""
            Select *
            from authors a
            where a.userId = #{userId}
            """)
    Authors getAuthorByUserId(String userId);
}
