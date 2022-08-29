package com.format_project.meeting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class MeetingApplication{

	public static void main(String[] args) {
		SpringApplication.run(MeetingApplication.class, args);
	}

}
