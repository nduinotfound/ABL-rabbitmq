package com.randu.rabbitmq;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.queue}")
    private String queueName;

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public Queue queue() {
        return new Queue(queueName, true, false, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue)
                .to(exchange)
                .with(routingKey);
    }

    // Library Notification Beans
    @Value("${app.library.queue}")
    private String libraryQueue;

    @Value("${app.library.exchange}")
    private String libraryExchange;

    @Value("${app.library.routing-key}")
    private String libraryRoutingKey;

    @Bean
    public Queue libraryQueue() {
        return new Queue(libraryQueue, true);
    }

    @Bean
    public DirectExchange libraryExchange() {
        return new DirectExchange(libraryExchange);
    }

    @Bean
    public Binding libraryBinding(Queue libraryQueue, DirectExchange libraryExchange) {
        return BindingBuilder.bind(libraryQueue)
                .to(libraryExchange)
                .with(libraryRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}