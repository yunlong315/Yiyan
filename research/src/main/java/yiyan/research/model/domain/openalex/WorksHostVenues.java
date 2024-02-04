package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksHostVenues {
    private String workId;
    private String venueId;
    private String url;
    private String isOa;
    private String version;
    private String license;
}