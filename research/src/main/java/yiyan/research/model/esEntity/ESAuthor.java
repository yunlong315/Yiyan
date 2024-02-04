package yiyan.research.model.esEntity;

import lombok.Data;
import lombok.experimental.Accessors;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.utils.JsonHandler;

@Data
@Accessors(chain = true)
public class ESAuthor {

    private String id;
    private String name;
    private String[] alternatives;
    private String institution;
    private Integer worksCount;
    private Integer citedByCount;
    private Integer hIndex;
    private boolean isFollowing;

//    public static ESAuthor parse(Authors author){
//        ESAuthor a = new ESAuthor();
//        a.id = author.getId();
//        a.name = author.getDisplayName();
//        a.alternatives = JsonHandler.parse(author.getDisplayNameAlternatives()).toArray(new String[0]);
//        a.institution = author.getInstitutionName();
//        a.worksCount = author.getWorksCount();
//        a.citedByCount = author.getCitedByCount();
//        a.hIndex = author.getHIndex();
//        return a;
//    }
}
