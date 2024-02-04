package com.example.administration.service;

import com.example.administration.model.domain.ComplaintForm;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public interface ComplaintService {
    int addComplaint(ComplaintForm complaint);
    List<ComplaintForm> getComplaintsByUserId(int userId);

    ComplaintForm getComplaint(int id);
    List<ComplaintForm> getAdminComplaints(Integer adminId, Integer type, Integer status);

    boolean updateComplaint(int id, int status, String reply, Date updateTime);
    boolean deleteComplaint(int id);
}
