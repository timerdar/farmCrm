package com.timerdar.farmCrm.unit.service.service;

import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.repository.ConsumerRepository;
import com.timerdar.farmCrm.service.ConsumerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceTest {

    @Mock
    private ConsumerRepository repository;

    @InjectMocks
    private ConsumerService consumerService;

    @Test
    void createConsumer_shouldThrowIllegalArgument_whenConsumerNotValid() {

        Consumer consumer = new Consumer(1L, "", "", "", "");
        Consumer consumer1 = new Consumer(2L, null, null, null, null);

        assertThrows(IllegalArgumentException.class, () -> {
            consumerService.createConsumer(consumer);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            consumerService.createConsumer(consumer1);
        });
    }

    @Test
    void createConsumer_shouldReturnConsumer_whenConsumerValid(){
        Consumer consumer = new Consumer(1L, "Consumer1", "Улица1", "Черниковка", "12312312312");
        when(repository.save(consumer)).thenReturn(consumer);

        Consumer created = consumerService.createConsumer(consumer);

        assertEquals(consumer, created);
    }

    @Test
    void getConsumersOfDistrict() {
        Consumer consumer1 = new Consumer(1L, "Марина", "Черниковская", "Черниковка", "123123223");
        Consumer consumer2 = new Consumer(2L, "Марина1", "Черниковская", "Черниковка", "123123223");
        List<Consumer> consumers = new ArrayList<>();
        consumers.add(consumer1);
        consumers.add(consumer2);
        when(repository.findAllByDistrictOrderByNameAsc("Черниковка")).thenReturn(consumers);

        List<Consumer> actual = consumerService.getConsumersOfDistrict("Черниковка");

        assertEquals(consumers, actual);
    }

    @Test
    void getDistricts() {
        List<String> listOfDistricts = new ArrayList<>();
        listOfDistricts.add("Черниковка");
        listOfDistricts.add("Инорс");
        when(repository.getDistrictsList()).thenReturn(listOfDistricts);

        List<String> actual = consumerService.getDistricts();

        assertEquals(listOfDistricts, actual);

    }

    @Test
    void getConsumerByName() {
        Consumer consumer = new Consumer(1L, "Марина", "Адрес", "Черниковка", "1231231233");
        List<Consumer> consumers = new ArrayList<>();
        consumers.add(consumer);
        when(repository.findByNamePrefix(any(String.class))).thenReturn(consumers);

        List<Consumer> actual = consumerService.getConsumerByName("Мари");

        assertEquals(consumers, actual);
    }
}