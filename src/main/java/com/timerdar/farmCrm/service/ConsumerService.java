package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateConsumerRequest;
import com.timerdar.farmCrm.dto.DeliveryOrderNumForConsumer;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.repository.ConsumerRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ConsumerService {

    @Autowired
    private ConsumerRepository consumerRepository;

    public Consumer createConsumer(CreateConsumerRequest request){
        request.check();
        Consumer consumer = new Consumer(0, request.getName(), request.getAddress(), request.getPhone(), 0);
        Consumer createdConsumer = consumerRepository.save(consumer);
        log.info("Создание заказчика: {}", createdConsumer);
        return createdConsumer;
    }

    public List<Consumer> getAllConsumers(){
        log.info("Получение списка заказчиков");
        return consumerRepository.findAllSorted();
    }

    public Consumer getConsumerById(long id){
        Optional<Consumer> c = consumerRepository.findById(id);
        if(c.isPresent()){
            log.info("Получение заказчика: id = {}", id);
            return c.get();
        }else{
            throw new EntityNotFoundException("Пользователь не найден");
        }
    }

    @Transactional
    public int updateAddress(long id, String address){
        log.info("Изменение адреса: id = {}, newAddress = {}", id, address);
        return consumerRepository.updateAddress(id, address);
    }

    @Transactional
    public int updatePhone(long id, String phone){
        log.info("Изменение телефона: id = {}, newPhone = {}", id, phone);
        return consumerRepository.updatePhone(id, phone);
    }

    public boolean isConsumerExists(long id){
        return consumerRepository.existsById(id);
    }

    @Transactional
    public int increaseTotalSum(long consumerId, int value){
        log.info("Увеличение суммы выкупа заказчика: id = {}, увеличение на {}", consumerId, value);
        return consumerRepository.increaseTotalSum(consumerId, value);
    }

    @Transactional
    public int changeOrderNumForConsumer(long consumerId, int num){
        return consumerRepository.changeOrderNumber(consumerId, num);
    }

    @Transactional
    public boolean setDeliveryConsumersOrder(List<DeliveryOrderNumForConsumer> nums) {
        for (DeliveryOrderNumForConsumer d : nums) {
            changeOrderNumForConsumer(d.getConsumerId(), d.getDeliveryOrderNumber());
        }
        log.info("Обновление порядка заказчиков: {}", Arrays.toString(nums.toArray()));
        return true;
    }
}