package com.example.administration.service;

import com.example.administration.model.domain.RequestForm;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public interface RequestService {
    int addRequest(RequestForm request);
    List<RequestForm> getRequestsByUserId(int userId);

    RequestForm getRequest(int id);
    List<RequestForm> getAdminRequests(Integer adminId, Integer type, Integer status);

    boolean updateRequest(int id, int status, String reply, Date updateTime);
}
