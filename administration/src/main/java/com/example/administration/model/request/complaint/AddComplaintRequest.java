package com.example.administration.model.request.complaint;

import jdk.dynalink.linker.LinkerServices;
import lombok.Data;

import java.util.List;

@Data
public class AddComplaintRequest {
    private String content;
    private String targetId;
    private List<String> attachmentUrls;
    private String wrongPaperId;
}
