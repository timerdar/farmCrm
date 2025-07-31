package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerService {

    @Autowired
    private ConsumerRepository consumerRepository;

    public Consumer createConsumer(CreateConsumerRequest request){
        Consumer consumer = new Consumer(1L, request.getName(), request.getAddress(), request.getPhone(), 0);
        return consumerRepository.save(consumer);
    }

    public List<Consumer> getAllConsumers(){
        return consumerRepository.findAllSorted();
    }

    public boolean isConsumerExists(long id){
            return consumerRepository.existsById(id);
    }
}