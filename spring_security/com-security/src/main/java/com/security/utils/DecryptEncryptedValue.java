package com.security.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DecryptEncryptedValue {

    public static void main(String[] args) throws Exception {
	Properties prop = new Properties();
	InputStream input = null;
	String encryptedText = null;

	try {
	    if (args.length >= 2) {

		input = new FileInputStream(args[0]);
		encryptedText = args[1];
		prop.load(input);
		String keystoreLocation = prop.getProperty("keystoreLocation");
		String keystorePassword = prop.getProperty("keystorePassword");
		String secretKeyAlias = prop.getProperty("secretKeyAlias");
		// Decrypt
		String decryptedValue = KeyStoreUtils.decryptByEntry(secretKeyAlias, keystoreLocation, keystorePassword, encryptedText);
		System.out.println("Done 2 -> Encrypted PLAIN {" + encryptedText + "} => DECRYPTED " + decryptedValue);
	    } else {
		System.out.println("Missing argument!!");
	    }
	} catch (IOException ex) {
	    ex.printStackTrace();
	} finally {
	    if (input != null) {
		try {
		    input.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
	}

    }
}
