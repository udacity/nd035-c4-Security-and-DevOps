package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
@EntityScan("com.example.demo.model.persistence")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SareetaApplication {
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	public static void main(String[] args) {
		SpringApplication.run(SareetaApplication.class, args);
	}

}

//Logging manual:
//http://www.slf4j.org/manual.html

/*
https://knowledge.udacity.com/questions/292173    <---- getting logs into Splunk
https://knowledge.udacity.com/questions/594725
https://knowledge.udacity.com/questions/500568
https://knowledge.udacity.com/questions/363757
https://knowledge.udacity.com/questions/403780
https://knowledge.udacity.com/questions/344055
https://knowledge.udacity.com/questions/525320  <--- Good example of Splunk log language etc
https://knowledge.udacity.com/questions/418388  <--- Good example of Splunk log language etc
https://www.baeldung.com/log4j-no-appenders-found
 */
