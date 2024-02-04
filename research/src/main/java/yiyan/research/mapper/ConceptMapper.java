package yiyan.research.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import yiyan.research.model.domain.openalex.Concepts;

@Mapper
public interface ConceptMapper {
    @Select("SELECT * FROM concepts WHERE id=#{id}")
    Concepts getConceptById(String id);
}
