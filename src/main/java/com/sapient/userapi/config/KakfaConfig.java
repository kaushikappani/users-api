package com.sapient.userapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KakfaConfig {

	@Bean
	public NewTopic initiateTopic() {
		return TopicBuilder.name("loaddata").build();
	}

}
