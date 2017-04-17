package com.security.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Properties;

import com.security.utils.KeyStoreUtils;

public class KeyStoreUtilsAppForKeyEncryption {

	public static void main(String[] args) throws Exception {

		final String propertiesLocation = "src/test/resources/app-template.properties";
		final String encPropertiesLocation = "src/test/resources/app-template-enc.properties";
		final String KEYSTORELOCATION_KEY = "keystoreLocation";
		final String KEYSTOREPASSWORD_KEY = "keystorePassword";
		final String SECRETKEYALIAS_KEY   = "secretKeyAlias";
		//STEP 0.
		//Create a keyStore of type jceks using keytool or any GUI app.
		Properties prop = new Properties();
		InputStream input = null;
		OutputStream output = null;

		try {
			input = new FileInputStream(propertiesLocation);
			output = new FileOutputStream(encPropertiesLocation);

			// load a properties file
			prop.load(input);

			String keystoreLocation =  prop.getProperty(KEYSTORELOCATION_KEY);
			String keystorePassword =  prop.getProperty(KEYSTOREPASSWORD_KEY);
			String secretKeyAlias   =  prop.getProperty(SECRETKEYALIAS_KEY);

			System.out.println("keystoreLocation {" + keystoreLocation + "} keystorePassword {" + keystorePassword + "} secretKeyAlias {" + secretKeyAlias + "}");

			KeyStoreUtils.createNewKeystoreEntryWithEmbeddedPwd(secretKeyAlias, keystoreLocation, keystorePassword);
			System.out.println("Done 1 -> Created a key entry in keystore with the name '" + secretKeyAlias + "'");

			Enumeration keys =  prop.keys();
			while (keys.hasMoreElements()){
				String key = (String)keys.nextElement();
				if ((key.equalsIgnoreCase(SECRETKEYALIAS_KEY) || key.equalsIgnoreCase(KEYSTOREPASSWORD_KEY) || key.equalsIgnoreCase(KEYSTORELOCATION_KEY) || key.contains("ENC"))){
					continue;
				}
				System.out.println(" k:" + key);
				String value = prop.getProperty(key);
				System.out.println(" v:" + value);
				//Encrypt
				String encrypted = KeyStoreUtils.encryptByEntry(secretKeyAlias, keystoreLocation, keystorePassword, value);
				System.out.println("Done 2 -> Encrypted PLAIN {" + value + "} => ENCRYPTED {" + encrypted + "}");

				prop.setProperty(key+"ENC", "ENC(" + encrypted + ")");

			}
			prop.store(output, "Updated properties with encrypted values");

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


