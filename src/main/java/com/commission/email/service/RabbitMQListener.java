package com.commission.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;
import org.json.JSONObject;

@Service
public class RabbitMQListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);

	public void onMessage(Message message) {
        String messageBodyStr = new String(message.getBody());
        logger.info("Consuming message - {}", messageBodyStr);
        JSONObject messageBodyJSON = new JSONObject(messageBodyStr);
        String email = messageBodyJSON.getString("email");
        long orderId = messageBodyJSON.getLong("orderId");
        logger.info("Sending email to {} for orderId - {}", email, orderId);
	}

}
