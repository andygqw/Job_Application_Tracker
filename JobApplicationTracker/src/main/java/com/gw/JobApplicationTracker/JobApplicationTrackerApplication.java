package com.gw.JobApplicationTracker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class JobApplicationTrackerApplication {

	private static final Logger logger = LoggerFactory.getLogger(JobApplicationTrackerApplication.class);

	public static void main(String[] args) {

		SpringApplication.run(JobApplicationTrackerApplication.class, args);

		logger.warn("Ooooooooooh!!!!!");
	}
}
