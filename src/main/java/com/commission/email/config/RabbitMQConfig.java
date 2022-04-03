package com.commission.email.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.commission.email.service.RabbitMQListener;

@Configuration
public class RabbitMQConfig {

	@Value("${commission.rabbitmq.queue}")
	String queueName;

	@Value("${spring.rabbitmq.username}")
	String username;

	@Value("${spring.rabbitmq.password}")
	private String password;

    @Value("${spring.rabbitmq.host}")
	private String host;

    @Value("${spring.rabbitmq.port}")
	private String port;

	@Bean
	Queue queue() {
		return new Queue(queueName, false);
	}
	
	
	@Bean
	MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory ) {
		SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
		simpleMessageListenerContainer.setConnectionFactory(connectionFactory);
		simpleMessageListenerContainer.setQueues(queue());
		simpleMessageListenerContainer.setMessageListener(new RabbitMQListener());
		return simpleMessageListenerContainer;

	}

	// @Bean
	// ConnectionFactory connectionFactory() {
	// 	CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory();
    //     cachingConnectionFactory.setAddresses(host+":"+port);
	// 	cachingConnectionFactory.setUsername(username);
	// 	cachingConnectionFactory.setUsername(password);
	// 	return cachingConnectionFactory;
	// }
	

	// @Bean
	// MessageListenerContainer messageListenerContainer() {
	// 	SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer();
	// 	simpleMessageListenerContainer.setConnectionFactory(connectionFactory());
	// 	simpleMessageListenerContainer.setQueues(queue());
	// 	simpleMessageListenerContainer.setMessageListener(new RabbitMQListener());
	// 	return simpleMessageListenerContainer;

	// }
	
	
		
}

