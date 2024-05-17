package at.ase.respond.datafeeder.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RabbitConfig {

    @Value("${rabbit.queues.incidents}")
    private String incidentsQueue;

    @Value("${rabbit.routes.incidents}")
    private String incidentsRoute;

    @Value("${rabbit.exchange}")
    private String exchange;

    @Value("${rabbit.backoff-policy.init-interval}")
    private Integer initInterval;

    @Value("${rabbit.backoff-policy.max-interval}")
    private Integer maxInterval;

    @Value("${rabbit.backoff-policy.multiplier}")
    private Integer multiplier;

    @Value("${rabbit.retry-policy.max-attempts}")
    private Integer maxAttempts;

    @Bean
    public Queue incidentsQueue() {
        return new Queue(incidentsQueue, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding incidentsBinding(Queue resourceQueue, Exchange exchange) {
        return BindingBuilder.bind(resourceQueue).to(exchange).with(incidentsRoute + ".*").noargs();
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate template(ConnectionFactory connectionFactory, MessageConverter converter) {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(initInterval);
        backOffPolicy.setMaxInterval(maxInterval);
        backOffPolicy.setMultiplier(multiplier);

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(maxAttempts);

        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(backOffPolicy);
        retryTemplate.setRetryPolicy(retryPolicy);

        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRetryTemplate(retryTemplate);
        rabbitTemplate.setMessageConverter(converter);

        return rabbitTemplate;
    }

}
