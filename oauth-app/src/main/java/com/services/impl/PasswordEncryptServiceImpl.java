package com.services.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.services.PasswordEncryptService;

@Service("passwordEncryptServiceImpl")
public class PasswordEncryptServiceImpl implements PasswordEncryptService {

    @Value("#{application.keystoreLocation}")
    private String keystoreLocation;

    @Value("#{application.keystorePassword}")
    private String keystorePassword;

    @Value("#{application.secretKeyAlias}")
    private String secretKeyAlias;

    public String getKeystoreLocation() {
	return keystoreLocation;
    }

    public void setKeystoreLocation(String keystoreLocation) {
	this.keystoreLocation = keystoreLocation;
    }

    public String getKeystorePassword() {
	return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
	this.keystorePassword = keystorePassword;
    }

    public String getSecretKeyAlias() {
	return secretKeyAlias;
    }

    public void setSecretKeyAlias(String secretKeyAlias) {
	this.secretKeyAlias = secretKeyAlias;
    }

    @Override
    public String encryptPassword(String plainText) throws Exception {
	FileInputStream fis = new FileInputStream(getKeystoreLocation());
	return encryptByEntry(getSecretKeyAlias(), fis, getKeystorePassword(), plainText);

    }

    /**
     * Encrypt plain text
     * 
     * @param entry
     * @param keyStoreLocation
     * @param keyStorePassword
     * @param plainText
     * @return
     * @throws Exception
     */

    @Override
    public String encryptByEntry(String entry, InputStream stream, String keyStorePassword, String plainText) throws Exception {
	KeyStore ks = loadKeystore(stream, keyStorePassword);
	KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(keyStorePassword.toCharArray());
	KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry) ks.getEntry(entry, keyStorePP);
	SecretKey secretKey = ske.getSecretKey();
	Cipher cipher = Cipher.getInstance("AES");
	cipher.init(Cipher.ENCRYPT_MODE, secretKey);
	byte[] plainTextByte = plainText.getBytes();
	byte[] encryptedByte = cipher.doFinal(plainTextByte);
	Base64.Encoder encoder = Base64.getEncoder();
	String encryptedText = encoder.encodeToString(encryptedByte);
	return encryptedText;
    }

    private KeyStore loadKeystore(InputStream stream, String keyStorePassword) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, FileNotFoundException {
	KeyStore ks = KeyStore.getInstance("JCEKS");
	ks.load(null, keyStorePassword.toCharArray());
	ks.load(stream, keyStorePassword.toCharArray());
	return ks;
    }

}
