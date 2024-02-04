package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksLocation {
    private String workId;
    private String sourceId;
    private String landingPageUrl;
    private String pdfUrl;
    private Boolean isOa;
    private String version;
    private String license;
    // 省略构造方法、getter 和 setter
}

/*
    CREATE TABLE `works_locations` (
  `work_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `source_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `landing_page_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `pdf_url` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_oa` tinyint(1) DEFAULT NULL,
  `version` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `license` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  KEY `work_id_idx` (`work_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci


     */