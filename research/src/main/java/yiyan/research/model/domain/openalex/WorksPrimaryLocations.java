package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksPrimaryLocations {
    private String workId;
    private String sourceId;
    private String landingPageUrl;
    private String pdfUrl;
    private Boolean isOa;
    private String version;
    private String license;

    // 省略构造方法、getter 和 setter
}
