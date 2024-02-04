package yiyan.research.model.entity;

import lombok.Data;

@Data
public class InterestConceptByYear {
    private int year;
    private String conceptId;
    private String conceptName;
    private int workCount;
}
