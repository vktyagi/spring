package com.exception;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.ExceptionModel;

public class CustomOauthException implements OAuth2ExceptionRenderer {

	@Override
	public void handleHttpEntityResponse(HttpEntity<?> responseEntity,
			ServletWebRequest webRequest) throws Exception {
		HttpServletResponse response = webRequest.getResponse();
		HttpServletRequest request = webRequest.getRequest();
		ExceptionModel em = (ExceptionModel) request.getAttribute("em");

		HttpHeaders header = responseEntity.getHeaders();
		HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
		HttpStatus status = ((ResponseEntity<?>) responseEntity)
				.getStatusCode();
		response.setStatus(status.value());

		PrintWriter out = response.getWriter();
		if (null == em) {
			String str = header.getFirst("WWW-Authenticate");
			String[] arr = str.split(",");
			StringBuilder builderString = new StringBuilder("{");
			for (int i = 1; i < arr.length; i++) {
				String temp = arr[i];
				String[] tempArray = temp.split("=");
				String temp1 = tempArray[0];
				temp1 = "\"" + temp1.trim() + "\"";
				builderString.append(temp1 + ":");
				temp1 = tempArray[1];
				temp1 = "\"" + temp1.substring(1, temp1.length() - 1) + "\"";
				builderString.append(temp1 + ",");
			}
			builderString.append("\"error_code\"" + ":" + status);
			builderString.append("}");
			JSONParser j = new JSONParser();
			JSONObject obj = (JSONObject) j.parse(builderString.toString());
			em = new ExceptionModel();
			em.setStatus(status.toString());
			em.setError(obj.get("error").toString());
			em.setMessage(obj.get("error_description").toString());
			out.print(new ObjectMapper().writeValueAsString(em));

		} else {
			out.print(new ObjectMapper().writeValueAsString(em));
		}
		out.flush();
		out.close();
		if (responseEntity instanceof ResponseEntity
				&& outputMessage instanceof ServerHttpResponse) {
			((ServerHttpResponse) outputMessage)
					.setStatusCode(((ResponseEntity<?>) responseEntity)
							.getStatusCode());
		}
		HttpHeaders entityHeaders = responseEntity.getHeaders();
		if (!entityHeaders.isEmpty()) {
			outputMessage.getHeaders().putAll(entityHeaders);
		}

	}

	private HttpOutputMessage createHttpOutputMessage(
			NativeWebRequest webRequest) throws Exception {
		HttpServletResponse servletResponse = (HttpServletResponse) webRequest
				.getNativeResponse();
		return new ServletServerHttpResponse(servletResponse);
	}

}
