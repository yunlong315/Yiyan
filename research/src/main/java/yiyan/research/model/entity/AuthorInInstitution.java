package yiyan.research.model.entity;

import lombok.Data;

@Data
public class AuthorInInstitution {
    private String id;
    private String displayName;
    private String avatar;
    private long citedByCount;
    private long hIndex;
    private String professionalTitle;
    private long worksCount;
}
