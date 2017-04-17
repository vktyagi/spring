package com.security.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author victor.chugunov Utility class for encrypting/decrypting data.
 *
 */
public class KeyStoreUtils {

	/**
	 * Creates a entry/record in the given key store containing 128 bit secret
	 * key, which will be used for encryption/decryption
	 * 
	 * @param entry
	 *            Alias name for secret key
	 * @param keyStoreLocation
	 *            Key store location - Path to given key store (name of key
	 *            store must be included into key store location)
	 * @param keyStorePassword
	 *            Key store password
	 * @throws Exception
	 *             Exception
	 */
	public static void createNewKeystoreEntryWithEmbeddedPwd(String entry,
			String keyStoreLocation, String keyStorePassword) throws Exception {

		KeyStore ks = loadKeystore(keyStoreLocation, keyStorePassword);

		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(128);
		SecretKey generatedSecret = keyGenerator.generateKey();

		KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(
				keyStorePassword.toCharArray());
		ks.setEntry(entry, new KeyStore.SecretKeyEntry(generatedSecret),
				keyStorePP);

		FileOutputStream fos = new java.io.FileOutputStream(keyStoreLocation);
		ks.store(fos, keyStorePassword.toCharArray());

	}

	/**
	 * Load key store for given key store location, key store password. Key
	 * store type must be 'JCEKS'
	 * 
	 * @param keyStoreLocation
	 *            key store location
	 * @param keyStorePassword
	 *            key store password
	 * @return Key store
	 * @throws KeyStoreException
	 * @throws IOException
	 *             if keystore doesn't exist (key store MUST be created before!)
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws FileNotFoundException
	 *             if keystore doesn't exist (key store MUST be created before!)
	 */
	private static KeyStore loadKeystore(String keyStoreLocation,
			String keyStorePassword) throws KeyStoreException, IOException,
			NoSuchAlgorithmException, CertificateException,
			FileNotFoundException {
		KeyStore ks = KeyStore.getInstance("JCEKS");
		ks.load(null, keyStorePassword.toCharArray());
		FileInputStream fIn = new FileInputStream(keyStoreLocation);
		ks.load(fIn, keyStorePassword.toCharArray());
		return ks;
	}

	/**
	 * Creates a entry/record in the given key store containing 128 bit secret
	 * key, which will be used for encryption/decryption. Key store must exist
	 * 
	 * @param entry
	 *            Alias name for secret key
	 * @param keyStoreLocation
	 *            Key store location - Path to given key store without key store
	 *            name
	 * @param keystoreName
	 *            Key store name
	 * @param keyStorePassword
	 *            Key store password
	 * @throws Exception
	 *             Exception
	 */
	public static void createNewKeystoreEntryWithEmbeddedPwd(String entry,
			String keyStoreLocation, String keystoreName,
			String keyStorePassword) throws Exception {

		keyStoreLocation = keyStoreLocation + File.separator + keystoreName;
		createNewKeystoreEntryWithEmbeddedPwd(entry, keyStoreLocation,
				keyStorePassword);
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
	public static String encryptByEntry(String entry, String keyStoreLocation,
			String keyStorePassword, String plainText) throws Exception {

		KeyStore ks = loadKeystore(keyStoreLocation, keyStorePassword);

		KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(
				keyStorePassword.toCharArray());
		KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry) ks.getEntry(
				entry, keyStorePP);
		SecretKey secretKey = ske.getSecretKey();

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		byte[] plainTextByte = plainText.getBytes();

		byte[] encryptedByte = cipher.doFinal(plainTextByte);
		Base64.Encoder encoder = Base64.getEncoder();
		String encryptedText = encoder.encodeToString(encryptedByte);
		return encryptedText;
	}

	/**
	 * Decrypt encrypted text by using secret key stored in given key store
	 * under entry alias
	 * 
	 * @param entry
	 * @param keyStoreLocation
	 *            Key store location
	 * @param keyStorePassword
	 *            Key store password
	 * @param encryptedText
	 *            Encrypted text
	 * @return Decrypted text
	 * @throws Exception
	 *             Exception thrown if key store location wrong or given
	 *             password don't match
	 */
	public static String decryptByEntry(String entry, String keyStoreLocation,
			String keyStorePassword, String encryptedText) throws Exception {

		KeyStore ks = loadKeystore(keyStoreLocation, keyStorePassword);

		KeyStore.PasswordProtection keyStorePP = new KeyStore.PasswordProtection(
				keyStorePassword.toCharArray());
		KeyStore.SecretKeyEntry ske = (KeyStore.SecretKeyEntry) ks.getEntry(
				entry, keyStorePP);
		SecretKey secretKey = ske.getSecretKey();

		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		Base64.Decoder decoder = Base64.getDecoder();
		byte[] encryptedTextByte = decoder.decode(encryptedText);
		byte[] decryptedByte = cipher.doFinal(encryptedTextByte);
		return new String(decryptedByte);
	}

}
