package com.timerdar.farmCrm.controller;

import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/consumers")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping
    public List<Consumer> getConsumersList(){
        return consumerService.getAllConsumers();
    }

    @PostMapping
    public Consumer createConsumer(CreateConsumerRequest request){
        request.check();
        return consumerService.createConsumer(request);
    }


}
