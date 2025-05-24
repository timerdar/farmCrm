package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.repository.ConsumerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerService {

    @Autowired
    private ConsumerRepository consumerRepository;

    public Consumer createConsumer(Consumer consumer){
        return consumerRepository.save(consumer);
    }

    public List<Consumer> getConsumersOfDistrict(String district){
        return consumerRepository.findAllByDistrictByOrderByNameAsc(district);
    }

    public List<String> getDistricts(){
        return consumerRepository.getDistrictsList();
    }

    public Consumer getConsumerByName(String name){
        return consumerRepository.findByNamePrefix(name);
    }
}
