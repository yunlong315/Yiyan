package yiyan.research.model.domain.openalex;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class Institutions {
    private String id;
    private String ror;
    private String displayName;
    private String countryCode;
    private String type;
    private String homepageUrl;
    private String imageUrl;
    private String imageThumbnailUrl;
    private String displayNameAcronyms;
    private String displayNameAlternatives;
    private int worksCount;
    private int citedByCount;
    private int hIndex;
    private int i10Index;
    private String worksApiUrl;
    private Date updatedDate;
}
