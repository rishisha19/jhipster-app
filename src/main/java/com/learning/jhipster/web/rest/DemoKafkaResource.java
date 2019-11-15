package com.learning.jhipster.web.rest;

import com.learning.jhipster.service.DemoKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo-kafka")
public class DemoKafkaResource {

    private final Logger log = LoggerFactory.getLogger(DemoKafkaResource.class);

    private DemoKafkaProducer kafkaProducer;

    public DemoKafkaResource(DemoKafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping("/publish")
    public void sendMessageToKafkaTopic(@RequestParam("message") String message) {
        log.debug("REST request to send to Kafka topic the message : {}", message);
        this.kafkaProducer.send(message);
    }
}
