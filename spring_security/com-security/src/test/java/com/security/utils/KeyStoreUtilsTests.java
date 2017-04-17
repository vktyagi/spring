package com.security.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.security.utils.KeyStoreUtils;

public class KeyStoreUtilsTests {

	private static final String ENTRY1 = "entry1";
	private static final String TTAGSECKEY = "ttagsk";
	private static final String TTAGSECKEYWRONG = "ttagsk-wrong";
	private static final String PLAINTEXTTOENCRYPT = "totaltag";
	private static final String ENCRYPTEDTEXT ="gOeaxAwf7TFpSLua7hDF5Q==";
	private static final String TYPE_JCEKS = "JCEKS";
	private static final String keyStoreTESTPassword = "changeme";
	private static final String keyStoreTESTPasswordWrong = "changemeNO";
	private static final String keystoreTESTLocation = "src/test/resources/test-keystore.jceks";
	private static final String keystoreTESTLocationWithoutName = "src/test/resources";
	private static final String keystoreTESTName = "test-keystore.jceks";
	private static final String keystoreTESTLocationWrong = "src/test/resources/test-keystore1.jceks";
	private static final String ALIAS_DEV = "dev";

	// Keystore for testing
	private static KeyStore ks = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		loadKeystore();
	}

	private static void loadKeystore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, FileNotFoundException {
		ks = KeyStore.getInstance(TYPE_JCEKS);
		ks.load(null, keyStoreTESTPassword.toCharArray());
		reloadKeystore();
	}

	//Clean up keystore after tests
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		cleanKeystore();
	}

	private static void cleanKeystore() throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		cleanKeystore(ENTRY1 + '_');
	}

	private static void cleanKeystore(String aliasPattern) throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException, KeyStoreException {
		reloadKeystore();
		Enumeration<String> en = ks.aliases();
		while (en.hasMoreElements()) {
			String alias = en.nextElement();
			if (alias.startsWith(aliasPattern)) {
				ks.deleteEntry(alias);
			}
		}
		FileOutputStream fos = new java.io.FileOutputStream(keystoreTESTLocation);
		ks.store(fos, keyStoreTESTPassword.toCharArray());
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testKeyStoreExist() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		assertNotNull(ks);
	}

	@Test
	public void testKeyStoreType() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		String keyType = ks.getType();
		assertNotNull(keyType);
		assertEquals(TYPE_JCEKS, keyType);
	}

	@Test
	public void testKeyStoreNotEmpty() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		Enumeration<String> aliases = ks.aliases();
		assertNotNull(aliases);
	}

	@Test
	public void testKeyStoreHasEntry() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		boolean hasEntry = ks.containsAlias(ALIAS_DEV);
		assertTrue(hasEntry);
	}

	@Test
	public void testCreateNewKeystoreEntryWithEmbeddedPwd() throws Exception {
		String testEntry = ENTRY1 + '_' + new Date().getTime();
		KeyStoreUtils.createNewKeystoreEntryWithEmbeddedPwd(testEntry, keystoreTESTLocation, keyStoreTESTPassword);

		// You need to reload the key store
		reloadKeystore();
		boolean hasEntry = ks.containsAlias(testEntry);
		assertTrue(hasEntry);
	}

	/**
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 */
	private static void reloadKeystore() throws FileNotFoundException, IOException, NoSuchAlgorithmException, CertificateException {
		FileInputStream fIn = new FileInputStream(keystoreTESTLocation);
		ks.load(fIn, keyStoreTESTPassword.toCharArray());
	}

	@Test
	public void testCreateNewKeystoreEntryWithEmbeddedPwdWithKeystoreName() throws Exception {
		String testEntry = ENTRY1 + '_' + new Date().getTime();
		KeyStoreUtils.createNewKeystoreEntryWithEmbeddedPwd(testEntry, keystoreTESTLocationWithoutName, keystoreTESTName, keyStoreTESTPassword);

		reloadKeystore();
		boolean hasEntry = ks.containsAlias(testEntry);
		assertTrue(hasEntry);
	}

	@Test(expected = Exception.class)
	public void testCreateNewKeystoreEntryWithEmbeddedPwdWithWrongKeystorePwd() throws Exception {
		String testEntry = ENTRY1 + '_' + new Date().getTime();
		KeyStoreUtils.createNewKeystoreEntryWithEmbeddedPwd(testEntry, keystoreTESTLocation, keyStoreTESTPasswordWrong);

	}

	@Test(expected = Exception.class)
	public void testCreateNewKeystoreEntryWithEmbeddedPwdWithWrongKeystoreLocation() throws Exception {
		String testEntry = ENTRY1 + '_' + new Date().getTime();
		KeyStoreUtils.createNewKeystoreEntryWithEmbeddedPwd(testEntry, keystoreTESTLocationWrong, keyStoreTESTPassword);
	}

	@Test
	public void testEncryptByEntry() throws Exception {
		String encrypted = KeyStoreUtils.encryptByEntry(TTAGSECKEY, keystoreTESTLocation, keyStoreTESTPassword, PLAINTEXTTOENCRYPT);
		assertNotNull(encrypted);
		assertTrue(encrypted.equals(ENCRYPTEDTEXT));
	}

	@Test
	public void testEncryptByEntryWithWrongEntry() throws Exception {
		String encrypted = KeyStoreUtils.encryptByEntry(TTAGSECKEYWRONG, keystoreTESTLocation, keyStoreTESTPassword, PLAINTEXTTOENCRYPT);
		assertNotNull(encrypted);
		assertFalse(encrypted.equals(ENCRYPTEDTEXT));

	}

	@Test
	public void testDecryptByEntry() throws Exception {
		String decrypted = KeyStoreUtils.decryptByEntry(TTAGSECKEY, keystoreTESTLocation, keyStoreTESTPassword, ENCRYPTEDTEXT);
		assertNotNull(decrypted);
		assertTrue(decrypted.equals(PLAINTEXTTOENCRYPT));
	}

}
