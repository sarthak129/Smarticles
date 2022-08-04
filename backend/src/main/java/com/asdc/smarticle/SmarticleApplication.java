package com.asdc.smarticle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;

@SpringBootApplication
@EnableEncryptableProperties
public class SmarticleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmarticleApplication.class, args);
	}

}
