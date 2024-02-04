package yiyan.research.model.esEntity;

import lombok.Data;
import lombok.experimental.Accessors;
import yiyan.research.model.domain.openalex.Institutions;
import yiyan.research.utils.JsonHandler;

import java.util.List;

@Data
@Accessors(chain = true)
public class ESInstitution {

    private String id;
    private String name;
    private String[] acronyms;
    private String country;
    private String type;
    private String imageUrl;
    private Integer worksCount;
    private Integer citedByCount;
    private Integer hIndex;

    public static ESInstitution parse(Institutions institution){
        ESInstitution i = new ESInstitution();
        i.id = institution.getId();
        i.name = institution.getDisplayName();
        i.acronyms = JsonHandler.parse(institution.getDisplayNameAcronyms()).toArray(new String[0]);
        i.country = institution.getCountryCode();
        i.type = institution.getType();
        i.imageUrl = institution.getImageUrl();
        i.citedByCount = institution.getCitedByCount();
        i.worksCount = institution.getWorksCount();
        i.hIndex = institution.getHIndex();
        return i;
    }
}
