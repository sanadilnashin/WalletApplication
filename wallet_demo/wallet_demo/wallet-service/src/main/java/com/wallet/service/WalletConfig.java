package com.wallet.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Properties;

@Configuration
public class WalletConfig {
    //Consumer point of view
    Properties getKafkaProperties() {
        Properties properties = new Properties();
        //producer
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        //consumer
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //deserialised
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return properties;
    }
    //consumer always listen to the topic
    ConsumerFactory<String,String> getConsumerFactory()
    {
        return new DefaultKafkaConsumerFactory(getKafkaProperties());
    }
    //to holds your topic
    @Bean
    ConcurrentKafkaListenerContainerFactory getConcurrentFactory()
    {
        ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory=new ConcurrentKafkaListenerContainerFactory();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(getConsumerFactory());
        return concurrentKafkaListenerContainerFactory;
    }
    @Bean
    ObjectMapper getObjectMapper()
    {
        return new ObjectMapper();
    }
    ProducerFactory<String,String> getProducerFactory()
    {
        return new DefaultKafkaProducerFactory(getKafkaProperties());
    }

}
