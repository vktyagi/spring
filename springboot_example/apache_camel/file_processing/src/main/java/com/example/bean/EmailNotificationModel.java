package com.example.bean;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailNotificationModel {
    private String signature;
    private String emailSubject;
    private String toEmailID;
    private String bottomLine;
    private List<EmailBodyModel> emailBodyList;
    
}
