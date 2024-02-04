package yiyan.research.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//ai对文章的总结
@Data
@NoArgsConstructor

public class AiSummary {
    private int id;
    //文献id;
    private String workId;
    //ai一句话总结
    private String summary;

    public AiSummary(String workId,String summary){
        this.workId = workId;
        this.summary = summary;
    }
}
