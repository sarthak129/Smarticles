package com.asdc.smarticle.pswdencrydecry;

import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.stereotype.Component;

@Component
public class CipherConfigFactory {
	
	
public SimpleStringPBEConfig getCipherConfigInstance() {
		
		return  new SimpleStringPBEConfig();
	}
	
} 

