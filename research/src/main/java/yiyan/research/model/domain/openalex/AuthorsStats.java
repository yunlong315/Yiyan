package yiyan.research.model.domain.openalex;

import lombok.Data;

@Data
public class AuthorsStats {
    private String authorId;
    private int hIndex;
    private int i10Index;
}
