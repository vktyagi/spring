package com.services.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dao.OauthCustomDao;
import com.exception.BlockedClientException;
import com.models.Role;
import com.models.User;
import com.services.LoginAttemptLogService;

@Service("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {
    private static final Logger logger = Logger.getLogger(MyUserDetailsService.class);
    private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP",
	    "HTTP_CLIENT_IP", "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

    @Autowired
    private OauthCustomDao oauthCustomDao;

    @Autowired
    private LoginAttemptLogService loginAttemptLogService;

    @Autowired
    HttpServletRequest request;
    @Autowired
    PasswordEncryptServiceImpl passwordEncryptServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String uName) throws UsernameNotFoundException, BlockedClientException {
	String encPassword = "";

	UserDetails userDetails = null;
	User user = oauthCustomDao.findByUserName(uName);
	if (null == user) {
	    throw new BadCredentialsException("User '" + uName + "' Not Found!");
	}
	try {
	    encPassword = passwordEncryptServiceImpl.encryptPassword(request.getParameter("password"));
	} catch (Exception e) {
	    throw new BadCredentialsException("password mismatch");
	}
	String clientIPAddress = request.getHeader("source-ip");
	logger.info("source-ip: " + clientIPAddress);

	if (null == clientIPAddress || "".equals(clientIPAddress)) {
	    clientIPAddress = getClientIpAddress(request);
	}

	if (user.isEnabled()) {
	    loginAttemptLogService.logFailedAttempt(user, uName, encPassword, clientIPAddress);
	    if (!user.isLocked()) {
		loginAttemptLogService.verifyLoginAttempt(user, uName, encPassword);
	    }

	}

	List<GrantedAuthority> authorities = buildUserAuthority(user.getRole(), uName);
	userDetails = buildUserForAuthentication(user, authorities);
	return userDetails;
    }

    private org.springframework.security.core.userdetails.User buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
	return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), user.isEnabled(), true, true, !user.isLocked(), authorities);
    }



    // added role hierarchy
    private List<GrantedAuthority> buildUserAuthority(Set<Role> roles, String uName) {

	Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
	setAuths.add(new SimpleGrantedAuthority("ROLE_APP"));
	if (roles.size() > 0) {
	    for (Role role : roles) {
		setAuths.add(new SimpleGrantedAuthority(role.getName()));
	    }
	}
	List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(setAuths);

	return result;
    }

    private String getClientIpAddress(HttpServletRequest request) {
	for (String header : IP_HEADER_CANDIDATES) {
	    String ip = request.getHeader(header);
	    logger.info("IP: " + ip + "; Header: " + header);
	    if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
		return ip;
	    }
	}
	return request.getRemoteAddr();
    }

}
