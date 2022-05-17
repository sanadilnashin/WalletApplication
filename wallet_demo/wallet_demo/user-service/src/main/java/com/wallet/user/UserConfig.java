package com.wallet.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Properties;

@Configuration
public class UserConfig {
    //we just add the config files we provided the local host server with port using this properties we started kafka
    //fisrt thing we do define the properties .. comes from kafka dependiesy
    Properties getKafkaProperties() {
        Properties properties = new Properties();
        //mapping for my producer I need to specify under which server port my bootstrap server is running
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        //producerconfig is just like key value paired .. we are going to define what is the serialization we use.
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return properties;
    }
       //items in kafka
    //producer --for producer run we had provided some properties like port compile under class properties
    //we have to create a producer factory using this properties we have to create producer object and assign this properties to them.
    //topic
    //consumer
    ProducerFactory<String,String> getProducerFactory()
        {
            return new DefaultKafkaProducerFactory(getKafkaProperties());
        }
        //kafka templates create a connection b/w spring and kafka
    //passing producer obj spring will take care.
    //creating bean for kafka template
    @Bean
    KafkaTemplate<String,String> getKafkaTemplate()
    {
        return new KafkaTemplate<>(getProducerFactory());
    }
    @Bean
    ObjectMapper getObjectMapper()
    {
        return new ObjectMapper();
    }
    }


