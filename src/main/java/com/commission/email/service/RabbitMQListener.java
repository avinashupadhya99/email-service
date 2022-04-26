package com.commission.email.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import datadog.trace.api.Trace;
import datadog.trace.api.DDTags;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.util.GlobalTracer;

@Service
public class RabbitMQListener implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQListener.class);

    @Trace(operationName = "rabbitmq.listen", resourceName = "RabbitMQListener.onMessage")
	public void onMessage(Message message) {
        Tracer tracer = GlobalTracer.get();
        Span span = tracer.buildSpan("rabbitmq.listen")
        .withTag(DDTags.SERVICE_NAME, "email-service")
        .withTag(DDTags.RESOURCE_NAME, "RabbitMQListener.onMessage")
        .start();
        logger.debug("SpanID - {}", span.context().toSpanId());
        try (Scope scope = tracer.activateSpan(span)) {
            String messageBodyStr = new String(message.getBody());
            logger.info("Consuming message - {}", messageBodyStr);
            JSONObject messageBodyJSON = new JSONObject(messageBodyStr);
            String email = messageBodyJSON.getString("email");
            long orderId = messageBodyJSON.getLong("orderId");
            span.setTag("order_id", orderId);
            logger.info("Sending email to {} for orderId - {}", email, orderId);
        } catch (Exception e) {
            logger.error("Internal server error with span tags");
        } finally {
            // Close span in a finally block
            span.finish();
        }
	}

}
