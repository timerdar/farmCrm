package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder() {

        //arrange
        CreateOrderRequest dto = new CreateOrderRequest(1L, 1L, 2);

        Order order = new Order();
        order.setId(10L);
        order.setCreatedAt(LocalDate.now());
        order.setProductId(1L);
        order.setConsumerId(1L);
        order.setAmount(2);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        //act
        Order result = orderService.createOrder(dto);
        //assert

        assertEquals(LocalDate.now(), result.getCreatedAt());
        assertEquals(10L, result.getId());
        assertEquals(1L, result.getConsumerId());
        assertEquals(1L, result.getProductId());
        assertEquals(2, result.getAmount());
        assertEquals(0, result.getCost());
    }

    @Test
    void getOrdersOfConsumer() {



    }

    @Test
    void getOrdersOfProduct() {
    }

    @Test
    void changeStatus() {
        //arrange
        Order order = new Order();
        order.setId(10L);
        order.setStatus(OrderStatus.CREATED);

        Order changedOrder = new Order();
        changedOrder.setId(10L);
        changedOrder.setStatus(OrderStatus.DELIVERY);

        when(orderRepository.save(any(Order.class))).thenReturn(changedOrder);
        when(orderRepository.getReferenceById(10L)).thenReturn(order);

        //act
        Order result = orderService.changeStatus(10L, OrderStatus.DELIVERY);

        //assert
        assertEquals(OrderStatus.DELIVERY, result.getStatus());
        assertEquals(10L, result.getId());
    }

    @Test
    void moveOrderToDelivery() {
    }
}