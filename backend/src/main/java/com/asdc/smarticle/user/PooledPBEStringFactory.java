package com.asdc.smarticle.user;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.stereotype.Component;

@Component
public class PooledPBEStringFactory {

	
public PooledPBEStringEncryptor getPBEStrinInstance() {
		
		return  new PooledPBEStringEncryptor();
	}
}
