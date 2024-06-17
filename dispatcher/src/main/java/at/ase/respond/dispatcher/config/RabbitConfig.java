package at.ase.respond.dispatcher.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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

    @Value("${rabbit.queues.requests}")
    private String resourceRequestsQueue;

    @Value("${rabbit.routes.requests}")
    private String resourceRequestsRoute;

    @Value("${rabbit.queues.resources.status}")
    private String resourcesStatusQueue;

    @Value("${rabbit.queues.resources.location}")
    private String resourcesLocationQueue;

    @Value("${rabbit.routes.resources.status}")
    private String resourcesStatusRoute;

    @Value("${rabbit.routes.resources.location}")
    private String resourcesLocationRoute;

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
    Queue resourceRequestsQueue() {
        return new Queue(resourceRequestsQueue, true);
    }

    @Bean
    public Queue resourceStatusQueue() {
        return new Queue(resourcesStatusQueue, true);
    }

    @Bean
    public Queue resourceLocationQueue() {
        return new Queue(resourcesLocationQueue, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding incidentsBinding(Queue incidentsQueue, Exchange exchange) {
        return BindingBuilder.bind(incidentsQueue).to(exchange).with(incidentsRoute).noargs();
    }

    @Bean
    public Binding resourceRequestsBinding(Queue resourceRequestsQueue, Exchange exchange) {
        return BindingBuilder.bind(resourceRequestsQueue).to(exchange).with(resourceRequestsRoute).noargs();
    }

    @Bean
    public Binding resourcesStatusBinding(Queue resourceStatusQueue, Exchange exchange) {
        return BindingBuilder.bind(resourceStatusQueue).to(exchange).with(resourcesStatusRoute).noargs();
    }

    @Bean
    public Binding resourcesLocationBinding(Queue resourceLocationQueue, Exchange exchange) {
        return BindingBuilder.bind(resourceLocationQueue).to(exchange).with(resourcesLocationRoute).noargs();
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
