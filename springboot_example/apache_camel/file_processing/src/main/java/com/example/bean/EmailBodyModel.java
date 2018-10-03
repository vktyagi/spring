package com.example.bean;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude={"tableName","event", "user","oldValue","newValue","updated_at", "oldValueMap","newValueMap"})
public class EmailBodyModel {
    private String auditId;
    private String tableName;
    private String event;
    private String user;
    private Map<String, String> oldValueMap = new HashMap<String, String>();
    private Map<String, String> newValueMap = new HashMap<String, String>();
    private String oldValue;
    private String newValue;
    private String updated_at;
    private String listColumnName;
    private String reason;
    
}