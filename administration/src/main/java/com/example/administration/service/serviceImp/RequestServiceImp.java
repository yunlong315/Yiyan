package com.example.administration.service.serviceImp;

import com.example.administration.mapper.RequestMapper;
import com.example.administration.model.domain.RequestForm;
import com.example.administration.service.RequestService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
@Service
public class RequestServiceImp implements RequestService {
    @Resource
    private RequestMapper requestMapper;

    @Override
    public int addRequest(RequestForm request) {
        return requestMapper.addRequest(request);
    }

    @Override
    public List<RequestForm> getRequestsByUserId(int userId) {
        return requestMapper.getRequestsByUserId(userId);
    }

    @Override
    public RequestForm getRequest(int id) {
        return requestMapper.getRequest(id);
    }

    @Override
    public List<RequestForm> getAdminRequests(Integer adminId, Integer type, Integer status) {
        return requestMapper.getAdminRequests(adminId, type, status);
    }

    @Override
    public boolean updateRequest(int id, int status, String reply, Date updateTime) {
        return requestMapper.updateRequest(id, status, reply, updateTime);
    }
}