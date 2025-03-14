package com.sapient.userapi.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.sapient.userapi.service.UserService;

@Service
public class KafkaConsumer {
	
	@Autowired
	private UserService userService;
	
	private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

	
	@KafkaListener(topics = "loaddata", groupId = "userapi")
	private void consumer(String message) {
		log.info("Message Delivered", message);
		if(message.equals("start")) {
			userService.loadUsers();
		}
	}

}
