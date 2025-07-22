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
        if (consumer.isValid()){
            return consumerRepository.save(consumer);
        }else{
            throw new IllegalArgumentException("Для создания заказчика введены не все данные");
        }
    }

    public List<Consumer> getConsumersOfDistrict(String district){
        return consumerRepository.findAllByDistrictOrderByNameAsc(district);
    }

    public List<String> getDistricts(){
        return consumerRepository.getDistrictsList();
    }

    public List<Consumer> getConsumerByName(String name){
        return consumerRepository.findByNamePrefix(name);
    }

    public boolean isConsumerExists(long id){
            return consumerRepository.existsById(id);
    }
}