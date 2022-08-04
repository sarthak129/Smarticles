package com.asdc.smarticle.pswdencrydecry;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.asdc.smarticle.user.PooledPBEStringFactory;

/**
 * Contain static method to return instance of the SimpleStringPBEConfig.
 * 
 * @author Vivekkumar Patel, Sarthak Patel
 * @version 1.0
 * @since 2022-02-19
 */

public class CipherConfig implements PasswordEncoder{

	@Value("${enc.key}") 
	private String key;
	
	@Autowired
	CipherConfigFactory cipherConfigFactory;
	
	@Autowired
	PooledPBEStringFactory pooledPBEStringFactory;

	/**
	 * Set configuration to encrypt and decrypt password.
	 * 
	 * @return - Instance of SimpleStringPBEConfig.
	 */
	public SimpleStringPBEConfig getCipherConfig() {

		SimpleStringPBEConfig cipherConfig = cipherConfigFactory.getCipherConfigInstance();
		cipherConfig.setPassword(key);
		cipherConfig.setAlgorithm("PBEWithHMACSHA512AndAES_256");
		cipherConfig.setKeyObtentionIterations("1000");
		cipherConfig.setPoolSize("4");
		cipherConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
 		cipherConfig.setIvGenerator(new RandomIvGenerator());

	 	return cipherConfig;
	}

	@Override
	public String encode(CharSequence rawPassword) {
		// TODO Auto-generated method stub
		SimpleStringPBEConfig config = getCipherConfig();
		PooledPBEStringEncryptor cipher = pooledPBEStringFactory.getPBEStrinInstance();;
		cipher.setConfig(config);
		return cipher.encrypt((String) rawPassword);
	}

	public CharSequence decodePswd(String pswd) {

		SimpleStringPBEConfig config = getCipherConfig();
		PooledPBEStringEncryptor cipher = pooledPBEStringFactory.getPBEStrinInstance();;
		cipher.setConfig(config);
		return cipher.decrypt(pswd);

	}
	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// TODO Auto-generated method stub
		if(rawPassword.equals(decodePswd(encodedPassword))) {
			return true;
			
		}else
		{
			return false;
		}
		
	}
}
