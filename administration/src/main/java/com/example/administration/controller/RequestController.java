package com.example.administration.controller;

import com.example.administration.model.micro.CheckAdminReply;
import com.example.administration.model.reply.RequestReply;
import com.example.administration.model.shared.RestBean;
import com.example.administration.model.domain.RequestForm;
import com.example.administration.model.request.complaint.AddRequestRequest;
import com.example.administration.service.RequestService;
import com.example.administration.service.micro.Producer;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/request")
public class RequestController {
    @Resource
    RequestService requestService;
    @Resource
    Producer producer;

    // 提交成为作者请求(管理员认证)
    @PostMapping("/author")
    public ResponseEntity<RestBean> addAuthorRequest(@RequestBody AddRequestRequest addRequestRequest, @RequestHeader("token") String token) {
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        int type = 1;
        // todo: 分配管理员
        int adminId = 11;
        createRequest(userId, type, adminId, addRequestRequest);
        return RestBean.success("提交成功");
    }

    //反馈建议
    @PostMapping("/suggest")
    public ResponseEntity<RestBean> addSuggestRequest(@RequestBody AddRequestRequest addRequestRequest,@RequestHeader("token") String token){
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);
        int type = 2;
        // todo: 分配管理员
        int adminId = 11;
        createRequest(userId, type, adminId, addRequestRequest);
        return RestBean.success("提交成功");
    }

    // 获取用户请求列表
    @GetMapping()
    public ResponseEntity<RestBean> getUserRequests(@RequestHeader("token") String token) {
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        int userId = producer.getUserId(token);

        List<RequestForm> requestForms = requestService.getRequestsByUserId(userId);
        return RestBean.success(requestForms);
    }

    // 获取请求详情
    @GetMapping("/detail")
    public ResponseEntity<RestBean> getRequest(@RequestParam int id,@RequestHeader("token") String token) {
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()){
            return RestBean.failure(HttpStatusCode.valueOf(403),"你不是管理员");
        }
        RequestForm requestForm = requestService.getRequest(id);
        if (checkAdminReply.getId() != requestForm.getUserId()){
            return RestBean.failure(HttpStatusCode.valueOf(403),"你不是创建者");
        }
        return RestBean.success(new RequestReply(requestForm));
    }

    private RequestForm createRequest(int userId, int type, int adminId, AddRequestRequest addRequestRequest) {
        RequestForm requestForm = new RequestForm(userId,
                type,
                addRequestRequest.getContent(),
                addRequestRequest.getTargetId(),
                adminId,
                String.join(",",addRequestRequest.getAttachmentUrls()) ,
                LocalDateTime.now());
        requestService.addRequest(requestForm);
        return requestForm;
    }
}
