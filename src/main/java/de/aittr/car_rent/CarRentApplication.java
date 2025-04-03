package de.aittr.car_rent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CarRentApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarRentApplication.class, args);
	}

}
