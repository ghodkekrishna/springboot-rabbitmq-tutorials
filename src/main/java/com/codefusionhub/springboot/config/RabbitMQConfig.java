package com.codefusionhub.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.name}")
    private String queue;

    @Value("${rabbitmq.json.queue}")
    private String jsonQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exhange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    @Value("${rabbitmq.json.routing.key}")
    private String jsonRoutingKey;

    // spring bean for rabbitmq queue (string)
    @Bean
    public Queue queue(){
        return new Queue(queue);
    }

    // spring bean for rabbitmq queue (json)
    @Bean
    public Queue jsonQueue(){
        return new Queue(jsonQueue);
    }

    // spring bean for rabbitmq exchange
    @Bean
    public TopicExchange exchange(){
        return new TopicExchange(exhange);
    }

    //binding between queue and exchange using routing key. (string)
    @Bean
    public Binding binding(){
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }

    //binding between queue and exchange using routing key. (json)
    @Bean
    public Binding jsonBinding(){
        return BindingBuilder.bind(jsonQueue())
                .to(exchange())
                .with(jsonRoutingKey);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }
}
