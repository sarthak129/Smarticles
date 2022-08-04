package com.asdc.smarticle;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.jasypt.iv.RandomIvGenerator;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.asdc.smarticle.pswdencrydecry.CipherConfig;
import com.asdc.smarticle.pswdencrydecry.CipherConfigFactory;
import com.asdc.smarticle.user.PooledPBEStringFactory;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CipherConfigUnitTest {

	@MockBean
	PooledPBEStringFactory pooledPBEStringFactory;

	@MockBean
	private CipherConfigFactory cipherConfigFactory;

	@MockBean
	private SimpleStringPBEConfig simpleStringPBEConfig;

	@MockBean
	PooledPBEStringEncryptor pooledPBEStringEncryptor;

	@Autowired
	CipherConfig cipherConfig;

	@Test
	void testCipherConfig() {

		Mockito.when(cipherConfigFactory.getCipherConfigInstance()).thenReturn(simpleStringPBEConfig);
		simpleStringPBEConfig.setPassword("smarticlesmarticle");
		simpleStringPBEConfig.setAlgorithm("PBEWithHMACSHA512AndAES_256");
		simpleStringPBEConfig.setKeyObtentionIterations("1000");
		simpleStringPBEConfig.setPoolSize("4");
		simpleStringPBEConfig.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
		simpleStringPBEConfig.setIvGenerator(new RandomIvGenerator());
		Assert.assertEquals(simpleStringPBEConfig, cipherConfig.getCipherConfig());
	}
	
	@Test
	void testEncode() {
		Mockito.when(cipherConfigFactory.getCipherConfigInstance()).thenReturn(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringFactory.getPBEStrinInstance()).thenReturn(pooledPBEStringEncryptor);
		pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringEncryptor.encrypt("123456")).thenReturn("abcdefg");
		Assert.assertEquals("abcdefg", cipherConfig.encode("123456"));
	}
	
	@Test
	void testdecodePaswd() {
		Mockito.when(cipherConfigFactory.getCipherConfigInstance()).thenReturn(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringFactory.getPBEStrinInstance()).thenReturn(pooledPBEStringEncryptor);
		pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringEncryptor.decrypt("123456")).thenReturn("abcdefg");
		Assert.assertEquals("abcdefg", cipherConfig.decodePswd("123456"));
	}
	
	@Test
	void testMaches() {
		String rawPass = "abcd";
		String encodedPass = "xyz";
		Mockito.when(cipherConfigFactory.getCipherConfigInstance()).thenReturn(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringFactory.getPBEStrinInstance()).thenReturn(pooledPBEStringEncryptor);
		pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig);
		Mockito.doReturn(rawPass).when(Mockito.spy(cipherConfig)).decodePswd(encodedPass);
		Assert.assertFalse(cipherConfig.matches(rawPass, encodedPass));
		
		Mockito.doReturn("password").when(Mockito.spy(cipherConfig)).decodePswd("9KCNANtZK9YKhBJ5z0WZvYYrcyTIkUofJznfypS6Pi70Hi4g+/hc5Nv/fWWEtatX");
		Assert.assertFalse(cipherConfig.matches("password", "9KCNANtZK9YKhBJ5z0WZvYYrcyTIkUofJznfypS6Pi70Hi4g+/hc5Nv/fWWEtatX"));
	}
}
