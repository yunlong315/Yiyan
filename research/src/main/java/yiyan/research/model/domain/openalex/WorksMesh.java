package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class WorksMesh {
    private String workId;
    private String descriptorUi;
    private String descriptorName;
    private String qualifierUi;
    private String qualifierName;
    private String isMajorTopic;
}