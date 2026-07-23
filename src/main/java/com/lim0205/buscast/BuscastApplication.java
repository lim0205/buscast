package com.lim0205.buscast;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BuscastApplication {

	public static void main(String[] args) {
		SpringApplication.run(BuscastApplication.class, args);
	}
}