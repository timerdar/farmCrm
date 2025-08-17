package com.timerdar.farmCrm.controller;

import com.timerdar.farmCrm.dto.ConsumerChangeRequest;
import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consumers")
@CrossOrigin()
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @PostMapping
    public Consumer createConsumer(@RequestBody CreateConsumerRequest request){
        return consumerService.createConsumer(request);
    }

    @GetMapping
    public List<Consumer> getConsumersList(){
        return consumerService.getAllConsumers();
    }

    @GetMapping("/{id}")
    public Consumer getConsumer(@PathVariable long id){
        return consumerService.getConsumerById(id);
    }

    @PostMapping("/change-phone")
    public ResponseEntity<?> changePhone(@RequestBody ConsumerChangeRequest request){
        int changed = consumerService.updatePhone(request.getId(), request.getPhone());
        if(changed > 0){
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/change-address")
    public ResponseEntity<?> changeAddress(@RequestBody ConsumerChangeRequest request){
        int changed = consumerService.updateAddress(request.getId(), request.getAddress());
        if(changed > 0) {
            return ResponseEntity.ok().build();
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
