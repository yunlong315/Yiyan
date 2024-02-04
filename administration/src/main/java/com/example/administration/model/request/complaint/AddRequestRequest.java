package com.example.administration.model.request.complaint;

import lombok.Data;

import java.util.List;

@Data
public class AddRequestRequest {
    private String targetId;
    private String content;
    private List<String> attachmentUrls;
}
