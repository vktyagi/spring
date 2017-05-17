package com.services;

import java.io.InputStream;

public interface PasswordEncryptService {

    public String encryptPassword(String plainText) throws Exception;
    
    public String encryptByEntry(String entry, InputStream stream, String keyStorePassword, String plainText) throws Exception;
}
