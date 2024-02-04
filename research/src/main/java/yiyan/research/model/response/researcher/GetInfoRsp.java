package yiyan.research.model.response.researcher;

import lombok.Data;
import yiyan.research.model.domain.openalex.Authors;
import yiyan.research.model.domain.openalex.AuthorsCountsByYear;

import java.util.List;

@Data
public class GetInfoRsp {
    private String id;
    private String orcid;
    private String displayName;
    private String displayNameAlternatives;
    private String professionalTitle;
    private String displayInstitution;
    private String personalHomepage;
    private String googleScholar;
    private String email;
    private String introduction;
    private String education;
    private String experience;
    private String avatarUrl;
    private boolean isFollowing;
    private boolean isClaimed;
    private boolean isMine;


    private List<AuthorsCountsByYear> authorsCountsByYearList;

    public void addAuthor(Authors author){
        this.id = author.getId();
        this.orcid = author.getOrcid();
        this.displayName = author.getDisplayName();
        this.displayNameAlternatives = author.getDisplayNameAlternatives();
        this.professionalTitle = author.getProfessionalTitle();
        this.displayInstitution = author.getDisplayInstitution();
        this.personalHomepage = author.getHomepage();
        this.googleScholar = author.getGoogleScholar();
        this.email = author.getEmail();
        this.introduction = author.getIntroduction();
        this.education = author.getEducation();
        this.experience = author.getExperience();
        this.avatarUrl = author.getAvatar();
    }
}
