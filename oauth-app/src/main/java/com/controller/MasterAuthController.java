package com.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.model.Attributes;
import com.models.ExceptionModel;
import com.services.ApiService;
import com.services.impl.CustomTokenService;

@RestController
public class MasterAuthController {
    private static final Logger LOGGER = Logger.getLogger(MasterAuthController.class);
    @Autowired
    ApiService apiService;
    @Autowired
    CustomTokenService tokenServices;

    @RequestMapping(value = "/authmgmt/v1/jwt", method = RequestMethod.POST)
    public @ResponseBody Object getLoginUserInfo(HttpServletRequest request, @RequestBody(required = false) Attributes attr) throws Exception {
	
	String username=SecurityContextHolder.getContext().getAuthentication().getName();
	OAuth2AuthenticationDetails oAuthDetails = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	
	LOGGER.info("Received request to generate JWT for ENDPOINT: "+attr.getPath()+" and username: "+username);
	Object o = apiService.getJWT(request, username, attr.getPath(), oAuthDetails.getTokenValue());
	ExceptionModel em = ((ExceptionModel) request.getAttribute("em"));
	return new ResponseEntity<>(o, HttpStatus.valueOf(Integer.parseInt(em.getStatus())));
    }

    @RequestMapping(value = "/revoke", method = RequestMethod.POST)
    public @ResponseBody Object revoke(@RequestBody String token, @RequestParam(value = "token", required = false) String requestToken) throws Exception {
	LOGGER.info("Token from request body: " + token);
	LOGGER.info("Token from request param: " + requestToken);
	String tokenArr[] = token.split("=");
	if (tokenArr.length >= 2) {
	    tokenServices.revokeToken(tokenArr[1]);
	} else if (requestToken != null) {
	    tokenServices.revokeToken(requestToken);
	}
	return "{}";

    }
}