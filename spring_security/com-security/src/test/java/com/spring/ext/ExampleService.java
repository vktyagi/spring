package com.spring.ext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * {@link Service} with hard-coded input data.
 */
@Component
public class ExampleService implements Service {

	@Value("${message}")
	private String message;

	@Value("${messageENC}")
	private String encMessage;


	public String getMessage() {
		return message;
	}

	 
	public String getMessageFromPropertiesEncrypted() {
		return encMessage;
	}

	
	public String getMessageDefault() {
		return "Hello world!";
	} 

}
