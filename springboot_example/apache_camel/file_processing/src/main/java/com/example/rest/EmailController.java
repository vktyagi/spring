package com.example.rest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.example.bean.EmailNotificationModel;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
public class EmailController {
    private static final Logger log = LoggerFactory.getLogger(EmailController.class);

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration freemarkerConfig;

    @Autowired
    private Environment environment;

    public boolean prepairEmail(EmailNotificationModel emailNotificationModel) {
	boolean emailSentFlag = true;
	try {
	    sendEmail(emailNotificationModel);
	} catch (Exception ex) {
	    emailSentFlag = false;
	    log.error("Error in sending email: {} ", ex);
	}
	return emailSentFlag;
    }
    
    
    private void sendEmail(EmailNotificationModel emailNotificationModel) throws Exception {
  	MimeMessage message = sender.createMimeMessage();
  	Template emailTemplate = null;

  	if (null != emailNotificationModel && StringUtils.isNotBlank(emailNotificationModel.getToEmailID())) {
  	    message.setFrom(new InternetAddress(environment.getProperty("spring.mail.MAIL_BOX")));
  	    final String[] recieverList = emailNotificationModel.getToEmailID().split(";");
  	    emailNotificationModel.setSignature(emailNotificationModel.getSignature());

  	    for (String reciever : recieverList) {
  		reciever = reciever.trim();
  		if (StringUtils.isNotBlank(reciever)) {
  		    try {
  			final InternetAddress internetAddress = new InternetAddress(reciever);
  			log.info("Adding Recipient [" + reciever + "] ");
  			message.addRecipient(Message.RecipientType.TO, internetAddress);
  		    } catch (final Exception e) {
  			log.info("Skipping  Recipient [" + reciever + "] beacuse not A Valid Email Address ::  " + e.getMessage());
  		    }
  		}
  	    }
  	    freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/templates/");
  	    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

  	    emailTemplate = freemarkerConfig.getTemplate("email_notification_template.ftl");
  	    wrapEmailTemplateModel(emailNotificationModel, emailTemplate, helper);
  	    sender.send(message);

  	} else {
  	    log.info("Recipient Email Id has not found for orchestration id : {}");
  	}

      }



    public void wrapEmailTemplateModel(EmailNotificationModel emailNotificationModel, Template emailTemplate, MimeMessageHelper helper) throws IOException, TemplateException, MessagingException {
	String html = FreeMarkerTemplateUtils.processTemplateIntoString(emailTemplate, emailNotificationModel);
	helper.setText(html, true);
	helper.setSubject(emailNotificationModel.getEmailSubject());
    }
}
