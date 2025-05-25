package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Price;
import com.timerdar.farmCrm.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PriceService priceService;

    @InjectMocks
    private OrderService orderService;

    @Spy
    @InjectMocks
    private OrderService orderServiceSpy;

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
    void moveOrderToDelivery_weighed() {
        //arrange
        Price price = new Price(12L, "Бройлер", 300, true);
        when(priceService.getPriceById(12L)).thenReturn(price);

        Order order = new Order();
        order.setId(10L);
        order.setAmount(10);
        order.setWeight(2.5);
        order.setProductId(12L);
        when(orderRepository.getReferenceById(10L)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order mockedOrder = new Order();

        doReturn(order).when(orderServiceSpy).changeStatus(10L, OrderStatus.DELIVERY);
        //act
        Order res = orderServiceSpy.moveOrderToDelivery(10L);
        assertEquals(750, res.getCost());

    }
}