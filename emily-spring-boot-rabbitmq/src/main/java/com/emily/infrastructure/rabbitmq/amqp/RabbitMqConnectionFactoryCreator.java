package com.emily.infrastructure.rabbitmq.amqp;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.impl.CredentialsProvider;
import com.rabbitmq.client.impl.CredentialsRefreshService;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.RabbitConnectionFactoryBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.amqp.CachingConnectionFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.ConnectionFactoryCustomizer;
import org.springframework.boot.autoconfigure.amqp.RabbitConnectionFactoryBeanConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.core.io.ResourceLoader;

/**
 * @Description :  Rabbit工厂创建器
 * @Author :  Emily
 * @CreateDate :  Created in 2022/6/6 9:54 上午
 */
public class RabbitMqConnectionFactoryCreator {

    public RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer(RabbitProperties properties,
                                                                                ResourceLoader resourceLoader, ObjectProvider<CredentialsProvider> credentialsProvider,
                                                                                ObjectProvider<CredentialsRefreshService> credentialsRefreshService) {
        RabbitConnectionFactoryBeanConfigurer configurer = new RabbitConnectionFactoryBeanConfigurer(resourceLoader,
                properties);
        configurer.setCredentialsProvider(credentialsProvider.getIfUnique());
        configurer.setCredentialsRefreshService(credentialsRefreshService.getIfUnique());
        return configurer;
    }

    public CachingConnectionFactoryConfigurer rabbitConnectionFactoryConfigurer(RabbitProperties rabbitProperties,
                                                                         ObjectProvider<ConnectionNameStrategy> connectionNameStrategy) {
        CachingConnectionFactoryConfigurer configurer = new CachingConnectionFactoryConfigurer(rabbitProperties);
        configurer.setConnectionNameStrategy(connectionNameStrategy.getIfUnique());
        return configurer;
    }

    public CachingConnectionFactory rabbitConnectionFactory(
            RabbitConnectionFactoryBeanConfigurer rabbitConnectionFactoryBeanConfigurer,
            CachingConnectionFactoryConfigurer rabbitCachingConnectionFactoryConfigurer,
            ObjectProvider<ConnectionFactoryCustomizer> connectionFactoryCustomizers) throws Exception {

        RabbitConnectionFactoryBean connectionFactoryBean = new RabbitConnectionFactoryBean();
        rabbitConnectionFactoryBeanConfigurer.configure(connectionFactoryBean);
        connectionFactoryBean.afterPropertiesSet();
        ConnectionFactory connectionFactory = connectionFactoryBean.getObject();
        connectionFactoryCustomizers.orderedStream()
                .forEach((customizer) -> customizer.customize(connectionFactory));

        CachingConnectionFactory factory = new CachingConnectionFactory(connectionFactory);
        rabbitCachingConnectionFactoryConfigurer.configure(factory);

        return factory;
    }
}