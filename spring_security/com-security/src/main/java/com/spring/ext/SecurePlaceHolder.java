package com.spring.ext;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.security.exceptions.KeyStoreInitException;
import com.security.utils.KeyStoreUtils;

public class SecurePlaceHolder extends PropertyPlaceholderConfigurer {

	private static final String ENC_PREFIX = "ENC(";
	@Value("${keystoreLocation}")
	private String keystoreLocation;
	@Value("${keystorePassword}")
	private String keystorePassword;
	@Value("${secretKeyAlias}")
	private String secretKeyAlias;

	private KeyStore ks = null;

	@SuppressWarnings("rawtypes")
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);

		boolean hasEncryptedProperties = hasEncodedProperties(props);
		logger.info("hasEncryptedProperties: " + hasEncryptedProperties);
		
		if (!hasEncryptedProperties){
			logger.warn("Properties don't have encrypted values!");
			//System.exit(1);
		}

		try {
			initKeystore(props);
		} catch (KeyStoreInitException e1) {
			logger.error("Error processing key store. System exit now");
			e1.printStackTrace();
		}

		Enumeration keys =  props.keys();
		while (keys.hasMoreElements()){
			String key =  (String)keys.nextElement();
			String value =  props.getProperty(key);

			if(value.startsWith(ENC_PREFIX)){
				Pattern pattern = Pattern.compile("ENC\\((.*)\\)");
				Matcher matcher = pattern.matcher(value);
				if(matcher.find()){
					String encryptedText = matcher.group(1);
					try {
						String decrypted = KeyStoreUtils.decryptByEntry(secretKeyAlias, keystoreLocation, keystorePassword, encryptedText);
						//convertProperty(key, decrypted);
					    //System.out.println(" decrypted "+key+"="+decrypted);
						//props.put(key, decrypted);
						props.setProperty(key, decrypted);
					} catch (Exception e) {
						logger.error("Exception during decrypting values from properties files.");
						e.printStackTrace();
						//System.exit(1);
					}
				}else {
					logger.error("No pattern matching 'ENC(*)' found. Please check you configuration properties. System will exit now.");
					System.exit(1);
				}
			}
		}
//System.out.println(props);
	}

	/**
	 * Returns true when properties contains at least one encoded value.
	 * @param props Properties for given application
	 * @return true, if properties contains encoded value, false overwise
	 */
	private boolean hasEncodedProperties(Properties props) {
		@SuppressWarnings("rawtypes")
		Enumeration keys = props.keys();
		while(keys.hasMoreElements()){
			String key = (String) keys.nextElement();
			String propValue = props.getProperty(key);
			if(propValue.startsWith(ENC_PREFIX)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Initialize the key store
	 * @param props Properties with encrypted values
	 * @throws KeyStoreInitException
	 */
	private void initKeystore(Properties props) throws KeyStoreInitException {
		keystorePassword = props.getProperty("keystorePassword");
		keystoreLocation = props.getProperty("keystoreLocation");
		secretKeyAlias = props.getProperty("secretKeyAlias");

		try {
			ks = loadKeyStore();
		} catch (IOException e) {
			e.printStackTrace();
			throw new KeyStoreInitException(e.getMessage());
		} catch (KeyStoreException e) {
			e.printStackTrace();
			throw new KeyStoreInitException(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new KeyStoreInitException(e.getMessage());
		} catch (CertificateException e) {
			e.printStackTrace();
			throw new KeyStoreInitException(e.getMessage());
		}
	}


	private KeyStore loadKeyStore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		logger.debug("Loading {" + keystorePassword + "} {" + keystoreLocation + "}");
		KeyStore ks = KeyStore.getInstance("JCEKS");
		ks.load(null, keystorePassword.toCharArray());
		FileInputStream fIn = new FileInputStream(keystoreLocation);
		ks.load(fIn, keystorePassword.toCharArray());
		return ks;
	}

}
