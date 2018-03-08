package com.topdesk.raspee;
 
import com.topdesk.poschd.client.PoschdClient;
import com.topdesk.poschd.client.PoschdFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public PoschdClient poschdClient(Environment env) {
		return new PoschdFactory().createPoschd(env::getProperty, null);
	}
}