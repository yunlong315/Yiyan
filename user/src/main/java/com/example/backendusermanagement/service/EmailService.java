package com.example.backendusermanagement.service;

import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public abstract  class EmailService {
//    public abstract boolean sendEmail(String to,String subject,String text);
    public abstract boolean sendEmail1(String to, String subject, String templateName, Map<String ,String> model) throws MessagingException, IOException, TemplateException;
    public abstract int activate(String token);


//    public abstract String parseHtml(String htmlName,String token,String to) throws TemplateException, IOException;
}
