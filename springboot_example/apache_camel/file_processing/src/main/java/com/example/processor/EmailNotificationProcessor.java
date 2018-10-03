package com.example.processor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.bean.EmailBodyModel;
import com.example.bean.EmailNotificationModel;
import com.example.entities.AuditTable;
import com.example.rest.EmailController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author Vinit Tyagi
 *
 */

@Component("emailNotificationProcessor")
public class EmailNotificationProcessor {
    public static final Logger log = Logger.getLogger(EmailNotificationProcessor.class);

    /*@Autowired
    private SqlSessionTemplate template;*/

    @Autowired
    private EmailController emailController;


    public void processNotification(Exchange exchange){

	String notificationTimeInHours = exchange.getProperty("notificationTimeInHours", String.class);
	List<AuditTable> auditTableModelList = new ArrayList<AuditTable>();
	EmailNotificationModel emailNotificationModel = new EmailNotificationModel();
	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DATE, -1);
	Date date = cal.getTime();
	String dateString = "last past hour";
	if ("1".equalsIgnoreCase(notificationTimeInHours)) {
	//    auditTableModelList = dcsnTemplate.selectList("template.selectAllAuditRecords);
	    emailNotificationModel.setEmailSubject("NO changes detected for " + dateString);
	}
	log.info("EmailNotification : total records from DB: " + auditTableModelList.size() + " for " + notificationTimeInHours + " jobs.");
	emailNotificationModel.setToEmailID("vtyagi");
	emailNotificationModel.setBottomLine("Test");
	emailNotificationModel.setSignature("Hello");
	
	if (StringUtils.isEmpty(emailNotificationModel.getToEmailID())) {
	    log.error("Required alteast one emailId for 'email.to_email' key in PROPERTIES_CONFIGURATION table");
	}
	if (null != auditTableModelList) {
	    emailNotificationModel.setEmailSubject("changes detected for " + dateString);
	    List<EmailBodyModel> emailBodyList = new ArrayList<EmailBodyModel>();
	    for (AuditTable auditTable : auditTableModelList) {
		EmailBodyModel emailBodyModel = new EmailBodyModel();
		emailBodyModel.setAuditId(auditTable.getAuditid());
		if (emailBodyList.contains(emailBodyModel)) {
		    EmailBodyModel emModel = emailBodyList.get(emailBodyList.indexOf(emailBodyModel));
		    Map<String, String> oldValueMap = emModel.getOldValueMap();
		    Map<String, String> newValueMap = emModel.getNewValueMap();
		    oldValueMap.put(auditTable.getColname(), null != auditTable.getOldcolval() ? auditTable.getOldcolval().replaceAll("_del", "") : auditTable.getOldcolval());
		    newValueMap.put(auditTable.getColname(), null != auditTable.getNewcolval() ? auditTable.getNewcolval().replaceAll("_del", "") : auditTable.getNewcolval());
		    emModel.getOldValueMap().putAll(oldValueMap);
		    emModel.getNewValueMap().putAll(newValueMap);
		    if ("DELETE".equalsIgnoreCase(emModel.getEvent())) {
			if ("UPDATED_BY".equalsIgnoreCase(auditTable.getColname())) {
			    emModel.setUser(null != auditTable.getOldcolval() ? auditTable.getOldcolval().replaceAll("_del", "") : auditTable.getOldcolval());
			}
		    } else {
			if ("UPDATED_BY".equalsIgnoreCase(auditTable.getColname())) {
			    emModel.setUser(null != auditTable.getNewcolval() ? auditTable.getNewcolval().replaceAll("_neu", "") : auditTable.getNewcolval());
			}
		    }
		    try {
			ObjectMapper objectMapper = new ObjectMapper();
			String oldValue = objectMapper.writeValueAsString(oldValueMap);
			String newValue = objectMapper.writeValueAsString(newValueMap);
			emModel.setOldValue(oldValue);
			emModel.setNewValue(newValue);
		    } catch (JsonProcessingException e) {
			e.printStackTrace();
		    }
		} else {
		    EmailBodyModel emailModel = new EmailBodyModel();
		    Map<String, String> oldValueMap = new HashMap<String, String>();
		    Map<String, String> newValueMap = new HashMap<String, String>();
		    oldValueMap.put(auditTable.getColname(), null != auditTable.getOldcolval() ? auditTable.getOldcolval().replaceAll("_del", "") : auditTable.getOldcolval());
		    newValueMap.put(auditTable.getColname(), null != auditTable.getNewcolval() ? auditTable.getNewcolval().replaceAll("_del", "") : auditTable.getNewcolval());
		    emailModel.setAuditId(auditTable.getAuditid());
		    emailModel.setEvent(auditTable.getEvent());
		    emailModel.setUser(auditTable.getDbuser());
		    emailModel.setTableName(auditTable.getTablename());
		    emailModel.setUpdated_at(auditTable.getUpdate_timestamp());
		    emailModel.getOldValueMap().putAll(oldValueMap);
		    emailModel.getNewValueMap().putAll(newValueMap);
		    if ("DELETE".equalsIgnoreCase(emailModel.getEvent())) {
			if ("UPDATED_BY".equalsIgnoreCase(auditTable.getColname())) {
			    emailModel.setUser(null != auditTable.getOldcolval() ? auditTable.getOldcolval().replaceAll("_del", "") : auditTable.getOldcolval());
			}
		    } 
		    try {
			ObjectMapper objectMapper = new ObjectMapper();
			String oldValue = objectMapper.writeValueAsString(oldValueMap);
			String newValue = objectMapper.writeValueAsString(newValueMap);
			emailModel.setOldValue(oldValue);
			emailModel.setNewValue(newValue);
		    } catch (JsonProcessingException e) {
			log.error("EmailNotification : Error while processing email processing.");
		    }
		    emailBodyList.add(emailModel);
		}
	    }
	    emailNotificationModel.setEmailBodyList(emailBodyList);
	}
	if (!("1".equalsIgnoreCase(notificationTimeInHours) && null != auditTableModelList)) {
	    boolean emailSentFlag = emailController.prepairEmail(emailNotificationModel);
	    if (emailSentFlag) {
		log.error("EmailNotification : Email sent successfully for  " + notificationTimeInHours + " jobs.");
	    }
	}

    }

   
}
