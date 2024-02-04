package yiyan.research.model.domain;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GptData {
    private List<Message> messages;

    private String model;

    private int max_tokens;

    private float temperature;

    private int top_p;

    private int n;

    boolean stream = false;
}
