package yiyan.research.model.domain.openalex;
import lombok.Data;

@Data
public class Authors {
    private String id;
    private String orcid;
    private String displayName;
    private String displayNameAlternatives;
    private String lastKnownInstitution;
    private Integer worksCount;
    private Integer citedByCount;
    private String worksApiUrl;
    private String updatedDate;
    private Integer hIndex;

    //被此用户申请成为作者的用户id
    private String userId;

    private String email;
    private String introduction;
    private String homepage;
    private String googleScholar;
    private String professionalTitle;
    private String education;
    private String experience;

    private String displayInstitution;
    private String avatar;
}