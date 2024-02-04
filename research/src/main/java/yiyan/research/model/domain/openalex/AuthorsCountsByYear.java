package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class AuthorsCountsByYear {
    private String authorId;
    private Integer year;
    private Integer worksCount;
    private Integer citedByCount;
}