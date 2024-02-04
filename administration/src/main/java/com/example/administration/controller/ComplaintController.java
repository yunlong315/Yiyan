package com.example.administration.controller;

import com.example.administration.model.domain.ComplaintForm;
import com.example.administration.model.micro.CheckAdminReply;
import com.example.administration.model.reply.ComplaintReply;
import com.example.administration.model.shared.RestBean;
import com.example.administration.model.request.complaint.AddComplaintRequest;
import com.example.administration.service.ComplaintService;
import com.example.administration.service.micro.Producer;
import jakarta.annotation.Resource;
import org.apache.ibatis.annotations.Delete;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/complaint")
public class ComplaintController {
    @Resource
    ComplaintService complaintService;
    @Resource
    private Producer producer;


    //提交评论投诉
    @PostMapping("/comment")
    public ResponseEntity<RestBean> addCommentComplaint(@RequestBody AddComplaintRequest addComplaintRequest, @RequestHeader("token") String token) {
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        int type = 0;
        //todo:分配管理员
        int adminId = 11;
        addComplaint(userId,type,adminId,addComplaintRequest);
        return RestBean.success(true);
    }
    //提交回复投诉
    @PostMapping("/reply")
    public ResponseEntity<RestBean> addReplyComplaint(@RequestBody AddComplaintRequest addComplaintRequest, @RequestHeader("token") String token) {
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        int type = 1;
        //todo:分配管理员
        int adminId = 11;
        addComplaint(userId,type,adminId,addComplaintRequest);
        return RestBean.success(true);
    }

    //提交文献投诉
    @PostMapping("/paper")
    public ResponseEntity<RestBean> addDocComplaint(@RequestBody AddComplaintRequest addComplaintRequest,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        int type = 2;
        int adminId = 11;
        addComplaint(userId,type,adminId,addComplaintRequest);
        return RestBean.success(true);
    }


    //提交附件投诉
    @PostMapping("/attachment")
    public ResponseEntity<RestBean> addAttachmentComplaint(@RequestBody AddComplaintRequest addComplaintRequest,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        int type = 3;
        int adminId = 11;
        addComplaint(userId,type,adminId,addComplaintRequest);
        return RestBean.success(true);
    }
    //投诉门户冒领
    @PostMapping("/wrongClaim/portal")
    public ResponseEntity<RestBean>  addWrongPortalClaim(@RequestBody AddComplaintRequest addComplaintRequest,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        int type = 4;
        int adminId = 11;
        addComplaint(userId,type,adminId,addComplaintRequest);
        return  RestBean.success(true);
    }
    @PostMapping("/wrongClaim/paper")
    //投诉文献冒领
    public ResponseEntity<RestBean> addWrongPaperClaim(@RequestBody AddComplaintRequest addComplaintRequest,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        int type = 5;
        int adminId = 11;
        addComplaint(userId,type,adminId,addComplaintRequest);
        return RestBean.success(true);
    }
    @GetMapping("")
    //查看用户投诉列表
    public ResponseEntity<RestBean> getCommentComplaints(@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        List<ComplaintForm> complaintForms = complaintService.getComplaintsByUserId(userId);
        return RestBean.success(complaintForms);
    }

    @PostMapping("")
    //删除投诉
    public ResponseEntity<RestBean> deleteComplaint(@RequestParam int id,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()){
            return RestBean.failure(HttpStatusCode.valueOf(403),"你不是管理员");
        }
        boolean res = complaintService.deleteComplaint(id);
        return RestBean.success(res);
    }

     //获取投诉详情
    @GetMapping("/detail")
    public ResponseEntity<RestBean> getComplaint(@RequestParam int id,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        int userId = checkAdminReply.getId();
        ComplaintForm complaintForm= complaintService.getComplaint(id);
        //管理员可以看到任何投诉详情
        if (checkAdminReply.isAdmin()){
            return RestBean.success(new ComplaintReply(complaintForm));
        }

        if (userId != complaintForm.getUserId()){
            return RestBean.failure(HttpStatusCode.valueOf(403),"权限不足");
        }

        return RestBean.success(new ComplaintReply(complaintForm));
    }

    private ComplaintForm addComplaint(int userId,int type,int adminId,AddComplaintRequest addComplaintRequest){
        ComplaintForm complaintForm = new ComplaintForm(userId,
                type,
                addComplaintRequest.getContent(),
                addComplaintRequest.getTargetId(),
                adminId,
                String.join(",",addComplaintRequest.getAttachmentUrls()),
                LocalDateTime.now());
        complaintService.addComplaint(complaintForm);
        return complaintForm;
    }
}
