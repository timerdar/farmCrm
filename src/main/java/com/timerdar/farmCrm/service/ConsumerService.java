package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.repository.ConsumerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ConsumerService {

    @Autowired
    private ConsumerRepository consumerRepository;

    public Consumer createConsumer(CreateConsumerRequest request){
        request.check();
        Consumer consumer = new Consumer(0, request.getName(), request.getAddress(), request.getPhone(), 0);
        return consumerRepository.save(consumer);
    }

    public List<Consumer> getAllConsumers(){
        return consumerRepository.findAllSorted();
    }

    public Consumer getConsumerById(long id){
        Optional<Consumer> c = consumerRepository.findById(id);
        if(c.isPresent()){
            return c.get();
        }else{
            throw new EntityNotFoundException("Пользователь не найден");
        }
    }

    @Transactional
    public int updateAddress(long id, String address){
        return consumerRepository.updateAddress(id, address);
    }

    @Transactional
    public int updatePhone(long id, String phone){
        return consumerRepository.updatePhone(id, phone);
    }

    public boolean isConsumerExists(long id){
        return consumerRepository.existsById(id);
    }

    @Transactional
    public int increaseTotalSum(long consumerId, int value){
        return consumerRepository.increaseTotalSum(consumerId, value);
    }
}