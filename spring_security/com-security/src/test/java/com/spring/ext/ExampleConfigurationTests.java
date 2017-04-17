package com.spring.ext;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("classpath:test-app-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ExampleConfigurationTests {

	@Autowired
	private Service service;

	@Autowired
	private ExampleService exampleService;

	@Test
	public void testSimpleProperties() throws Exception {
		assertNotNull(service);
	}

	@Test
	public void testSimpleMessage() throws Exception {
		assertNotNull(exampleService.getMessage());
	}

	@Test
	public void testSimpleMessage1() throws Exception {
		assertEquals("Hello world!", exampleService.getMessageDefault());
	}

	@Test
	public void testSimpleMessageFrom() throws Exception {
		assertEquals("Ola", exampleService.getMessage());
	}

	@Test
	public void testSimpleMessageSec() throws Exception {
		assertEquals("totaltag", exampleService.getMessageFromPropertiesEncrypted());
	}

	@Test
	public void testSimpleMessageSecFail() throws Exception {
		assertFalse("Encoded string match", "totaltag1".equals(exampleService.getMessageFromPropertiesEncrypted()));
	}


}






