package uz.jumanazar.ecommerceapp;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableJpaRepositories("uz.jumanazar.ecommerceapp.model.persistence.repositories")
@EntityScan("uz.jumanazar.ecommerceapp.model.persistence")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ECommerceApplication {
	private static final Logger log = LogManager.getLogger(ECommerceApplication.class);

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		log.info("eCommerce Application started.");
		SpringApplication.run(ECommerceApplication.class, args);
	}

}
