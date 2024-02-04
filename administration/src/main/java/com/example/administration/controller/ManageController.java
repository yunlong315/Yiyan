package com.example.administration.controller;

import com.example.administration.model.micro.CheckAdminReply;
import com.example.administration.model.shared.RestBean;
import com.example.administration.model.domain.ComplaintForm;
import com.example.administration.model.domain.RequestForm;
import com.example.administration.model.request.manage.ManageComplaintRequest;
import com.example.administration.model.request.manage.ManageRequestRequest;
import com.example.administration.service.ComplaintService;
import com.example.administration.service.RequestService;
import com.example.administration.service.micro.Producer;
import jakarta.annotation.Resource;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class ManageController {
    @Resource
    ComplaintService complaintService;
    @Resource
    RequestService requestService;
    @Resource
    Producer producer;

    //获取管理员负责的投诉，,可使用type和，status筛选
    @GetMapping("/complaints")
    public ResponseEntity<RestBean> getComplaints(@RequestParam(required = false) Integer type, @RequestParam(required = false) Integer status, @RequestHeader("token") String token) {
        if (token == null || token.equals("")){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()) {
            return RestBean.failure(HttpStatusCode.valueOf(403), "你不是管理员");
        }
        int adminId = checkAdminReply.getId();
        List<ComplaintForm> complaintForms = complaintService.getAdminComplaints(adminId, type, status);
        return RestBean.success(complaintForms);
    }

    //获取管理员负责的申请，,可使用type和，status筛选
    @GetMapping("/requests")
    public ResponseEntity<RestBean> getRequests(@RequestParam(required = false) Integer type, @RequestParam(required = false) Integer status, @RequestHeader("token") String token) {
        if (token == null || token.isEmpty()){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()) {
            return RestBean.failure(HttpStatusCode.valueOf(403), "你不是管理员");
        }
        int adminId = checkAdminReply.getId();
        List<RequestForm> requestForms = requestService.getAdminRequests(adminId, type, status);
        return RestBean.success(requestForms);
    }

    //处理投诉
    @PostMapping("/handleComplaint")
    public ResponseEntity<RestBean> handleRequest(@RequestBody ManageComplaintRequest manageComplaintRequest, @RequestHeader("token") String token) {
        if (token == null || token.isEmpty()){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()) {
            return RestBean.failure(HttpStatusCode.valueOf(403), "你不是管理员");
        }

        int id = manageComplaintRequest.getId();
        String reply = manageComplaintRequest.getReply();
        int status = manageComplaintRequest.getStatus();
        Date updateTime = Date.valueOf(LocalDate.now());
        ComplaintForm complaintForm = complaintService.getComplaint(id);
        int type = complaintForm.getType();
        boolean res = true;
        if (status == 2) {
            //投诉通过
            if (type == 4) {
                //解除门户认领
                res = producer.deAuthorAuthenticate(String.valueOf(complaintForm.getUserId()), complaintForm.getTargetId());
                if (res){
                    producer.sendMessage(complaintForm.getAdminId(), complaintForm.getUserId(), "投诉成功，已解除冒领门户绑定关系", 1);
                }else {
                    return RestBean.failure(HttpStatusCode.valueOf(500), "操作失败");

                }

            } else if (type == 5) {
                //解除文献认领关系
                res = producer.unClaimWork(String.valueOf(complaintForm.getUserId()), complaintForm.getTargetId());
                if(res){
                    producer.sendMessage(complaintForm.getAdminId(), complaintForm.getUserId(), "投诉成功，已解除文献认领关系", 1);
                }else {
                    return RestBean.failure(HttpStatusCode.valueOf(500), "操作失败");

                }
            }else {
                producer.sendMessage(complaintForm.getAdminId(), complaintForm.getUserId(), "投诉成功", 1);
            }
        } else {
            producer.sendMessage(complaintForm.getAdminId(), complaintForm.getUserId(), "投诉被拒绝", 1);
        }
        complaintService.updateComplaint(id, status, reply, updateTime);
        return RestBean.success("保存成功");
    }

    // 处理申请
    @PostMapping("/handleRequest")
    public ResponseEntity<RestBean> handleRequest(@RequestBody ManageRequestRequest manageRequestRequest, @RequestHeader("token") String token) {
        if (token == null || token.isEmpty()){
            return RestBean.failure(HttpStatusCode.valueOf(403),"没有token");
        }
        CheckAdminReply checkAdminReply = producer.checkAdmin(token);
        if (!checkAdminReply.isAdmin()) {
            return RestBean.failure(HttpStatusCode.valueOf(403), "你不是管理员");
        }
        int id = manageRequestRequest.getId();
        String reply = manageRequestRequest.getReply();
        int status = manageRequestRequest.getStatus();
        Date updateTime = Date.valueOf(LocalDate.now());
        RequestForm requestForm = requestService.getRequest(id);
        boolean res = producer.authorAuthenticate(String.valueOf(requestForm.getUserId()), requestForm.getTargetId());
        if (!res) {
            return RestBean.failure(HttpStatusCode.valueOf(500), "操作失败");
        }
        requestService.updateRequest(id, status, reply, updateTime);
        producer.sendAuthMessage(requestForm.getAdminId(), requestForm.getUserId(),requestForm.getTargetId(),"门户认领认证通过");
        producer.sendMessage(requestForm.getAdminId(), requestForm.getUserId(), reply, 5);
        return RestBean.success("保存成功");
    }


}
