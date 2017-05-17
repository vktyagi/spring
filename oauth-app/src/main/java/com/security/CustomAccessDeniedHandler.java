package com.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

public class CustomAccessDeniedHandler extends OAuth2AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request,
			HttpServletResponse response, AccessDeniedException authException)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		PrintWriter out = response.getWriter();
		StringBuffer buffer = new StringBuffer("{");
		buffer.append("\"error\"" + ":\"access_denied\""+",");
		buffer.append("\"error_description\"" +":\" Access is denied\""+",");
		buffer.append("\"error_code\"" +":403");
		buffer.append("}");
		out.print(buffer.toString());
		
	}
	

}
