package yiyan.research.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.convert.PeriodUnit;

@Data
@NoArgsConstructor
public class QuestionHistory {
    private int id;
    private String workId;
    private int userId;
    private String history;
    public QuestionHistory(String workId,int userId,String history){
        this.workId = workId;
        this.userId = userId;
        this.history = history;
    }
}
