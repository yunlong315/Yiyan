package yiyan.research.model.domain;

import lombok.Data;

import java.util.Date;

@Data
public class RequestForm {
    private int id;
    private String userId;
    private int type;//0:申请认领文献 1:申请成为作者
    private String content;
    private String authorId;
    private String workId;
    private int status;//0:正在审批 1:申请通过 -1:申请拒绝

    private String adminId;
    private String reply;

    private Date createTime;
    private Date updateTime;
    private String attachmentUrl;
    public RequestForm() {}
    public RequestForm(int type, String userId, String content, String authorId, String attachmentUrl, Date createTime) {
        this.userId = userId;
        this.type = type;
        this.content = content;
        this.authorId = authorId;
        this.status = 0;
        this.createTime = createTime;
        this.updateTime = createTime;
        this.attachmentUrl = attachmentUrl;

        this.adminId = "";
        this.reply = "";
        this.workId = "";
    }
}
