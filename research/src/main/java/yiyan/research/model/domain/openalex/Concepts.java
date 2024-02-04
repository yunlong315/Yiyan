package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class Concepts {
    private String id;
    private String wikidata;
    private String displayName;
    private Integer level;
    private String description;
    private Integer worksCount;
    private Integer citedByCount;
    private String imageUrl;
    private String imageThumbnailUrl;
    private String worksApiUrl;
    private String updatedDate;
}