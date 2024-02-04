package yiyan.research.model.response;

import lombok.Data;

@Data
public class StdStrRsp {
    private String message;
    public StdStrRsp(String message) {
        this.message = message;
    }
}
