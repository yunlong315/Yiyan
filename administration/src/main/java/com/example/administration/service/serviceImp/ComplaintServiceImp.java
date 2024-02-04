package com.example.administration.service.serviceImp;

import com.example.administration.mapper.ComplaintMapper;
import com.example.administration.model.domain.ComplaintForm;
import com.example.administration.service.ComplaintService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class ComplaintServiceImp implements ComplaintService {
    @Resource
    private ComplaintMapper complaintMapper;
    @Override
    public int addComplaint(ComplaintForm complaint) {
        return complaintMapper.addComplaint(complaint);
    }

    @Override
    public List<ComplaintForm> getComplaintsByUserId(int userId) {
        return complaintMapper.getComplaintsByUserId(userId);
    }

    @Override
    public ComplaintForm getComplaint(int id) {
        return complaintMapper.getComplaint(id);
    }

    @Override
    public List<ComplaintForm> getAdminComplaints(Integer adminId, Integer type, Integer status) {
        return complaintMapper.getAdminComplaints(adminId,type,status);
    }

    @Override
    public boolean updateComplaint(int id, int status, String reply, Date updateTime) {
        return complaintMapper.updateComplaint(id,status,reply,updateTime);
    }

    @Override
    public boolean deleteComplaint(int id) {
        return complaintMapper.deleteComplaint(id);
    }


}
