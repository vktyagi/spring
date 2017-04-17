This is README.md for cad-security project.

1. Problem/Intent
	1. 1 Some properties in configuration files maybe/should be encoded (for example AWS keys).

2. Design
	2.1 Encryption/decryption of sensetive information should be based on java key store. But this solution will bring a maintenance issues during keeping and deploying/redeploying key stores.
	2.2 This solution should be used only with properties contained encrypted values, otherwise ERROR message should logged in the log file/console and application should be shutdown.
	2.2 Encrypted value should be in format key=ENC(encoded_value) and encoded value shouldn't contain ')'
	2.3 Key store must be created using java standard tool - keytool from java distribution.
	2.4

3. Implementation details:
	3.1 Key store type - JCEKS
	3.2 Algorithm      - AES
	3.3 Key size       - 128


4. Testing configuration
	4.1 For testing KeyStoreUtils class test-keystore.jceks was used.
	4.2 For testing SecurePlaceHolder class devttag-keystore.jceks with password 'changeme' and secretKeyAlias=ttagsk was used to decrypt value.

5. How to create/use key store for development/QA/Prod

	5.1 To use keystore in any application, you must update security policy on any box where you planning to use.
	5.2 Identify the information you want to encrypt.
	5.3 Create a key store using keytool from java distribution. Key store type must be 'JCEKS'.
	5.4 Generate encrypted string
	5.4.1   Using KeyStoreUtils generate a new key entry in key store:
				KeyStoreUtils.createNewKeystoreEntryWithEmbeddedPwd(entry, keyStoreLocation, keyStorePassword)
				where entry is an alias for key - like "totaltag"
				      keyStoreLocation - path for your created keys store
				      keyStorePassword - password for your key store

	5.4.2 Generate a encrypted string
				String encrypted = encryptByEntry(entry, keyStoreLocation, keyStorePassword, plainText)
				where 	entry is the alias for the key
						keyStoreLocation - path for your created keys store
				      	keyStorePassword - password for your key store
				      	plainText - test to be encrypted

	5.4.3	Put encryoted string in your property files:
			propertyName=ENC(encryoted)