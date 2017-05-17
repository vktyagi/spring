package com.services;

import javax.servlet.http.HttpServletRequest;

import com.models.User;

public interface ApiService {
	public String creatJWTToken(User user) throws Exception;

	public Object getJWT(HttpServletRequest request,String name,String resourceId, String oAuthToken) throws Exception;
}
