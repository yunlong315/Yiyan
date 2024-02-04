package yiyan.research.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMsg implements Serializable {

    private List<Message> messages;

    private String searchId;

    private static final long serialVersionUID = 1L;
}