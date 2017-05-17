package com.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomBCryptPasswordEncoder implements PasswordEncoder {
    @Autowired
    PasswordEncryptService passwordEncryptService;

    @Override
    public String encode(CharSequence rawPassword) {
	return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
	try {
	    if (encodedPassword.equalsIgnoreCase(passwordEncryptService.encryptPassword(rawPassword.toString()))) {
		return true;
	    }
	} catch (Exception e) {
	}
	return false;
    }

}
