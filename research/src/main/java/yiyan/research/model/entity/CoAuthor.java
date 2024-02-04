package yiyan.research.model.entity;

import lombok.Data;

@Data
public class CoAuthor {
    private String authorId;
    private String authorImageUrl;
    private String authorName;
    private long citedByCount;
    private long coWorksCount;
    private long hIndex;
    private String institutionId;
    private String institutionName;
    private String professionalTitle;
    private long worksCount;
}
