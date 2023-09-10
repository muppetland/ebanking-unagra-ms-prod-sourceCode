package com.unagra.banking.institutions.api;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableEncryptableProperties
@EnableDiscoveryClient
public class BankingInstitutionsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingInstitutionsApiApplication.class, args);
	}

}
