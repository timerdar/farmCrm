package com.timerdar.farmCrm.unit.service.service;

import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Price;
import com.timerdar.farmCrm.repository.OrderRepository;
import com.timerdar.farmCrm.service.ConsumerService;
import com.timerdar.farmCrm.service.OrderService;
import com.timerdar.farmCrm.service.PriceService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private PriceService priceService;

    @Mock
    private ConsumerService consumerService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Spy
    @InjectMocks
    private OrderService orderServiceSpy;

    @Test
    void createOrder_shouldThrowIllegalArgument_whenRequestIsNotFullyEntered() {
        CreateOrderRequest request = new CreateOrderRequest(1L, 1L, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(request);
        });
    }

    @Test
    void createOrder_shouldThrowEntityNotFound_whenPriceNotExists(){
        CreateOrderRequest request1 = new CreateOrderRequest(12L, 12L, 10, 0);
        lenient().when(priceService.isPriceExists(12L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(request1);
        });

    }

    @Test
    void createOrder_shouldThrowEntityNotFound_whenConsumerNotExists(){
        CreateOrderRequest request1 = new CreateOrderRequest(13L, 13L, 10, 0);
        lenient().when(priceService.isPriceExists(13L)).thenReturn(true);
        lenient().when(consumerService.isConsumerExists(12L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> {
            orderService.createOrder(request1);
        });
    }

    @Test
    void createOrder_shouldReturnOrder_whenRequestIsValid(){
        CreateOrderRequest request = new CreateOrderRequest(1L, 1L, 2, 0);
        Order order = new Order(1L, 1L, 1L, 2, 200, 0, OrderStatus.CREATED, LocalDate.now());

        lenient().when(priceService.isPriceExists(1L)).thenReturn(true);
        lenient().when(consumerService.isConsumerExists(1L)).thenReturn(true);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        lenient().when(orderRepository.getReferenceById(1L)).thenReturn(order);
        doReturn(order).when(orderServiceSpy).evalCost(1L);

        Order actual = orderServiceSpy.createOrder(request);

        assertEquals(order, actual);

    }

    @Test
    void changeStatus() {
        Order order = new Order(1L, 1L, 1L, 0, 0, 2.234, OrderStatus.CREATED, LocalDate.now());
        when(orderRepository.getReferenceById(1L)).thenReturn(order);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        Order actual = orderService.changeStatus(1L, OrderStatus.DELIVERY);

        assertEquals(OrderStatus.DELIVERY, actual.getStatus());
    }

    @Test
    void changeAmount() {
        Order order = new Order(1L, 1L, 1L, 2, 0, 0, OrderStatus.CREATED, LocalDate.now());
        when(orderRepository.getReferenceById(1L)).thenReturn(order);
        int newAmount = 5;

        doReturn(order).when(orderServiceSpy).evalCost(1L);
        when(orderRepository.save(any(Order.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0)
        );

        Order changed = orderServiceSpy.changeAmount(1L, newAmount);

        assertEquals(newAmount, changed.getAmount());
    }

    @Test
    void changeWeight() {
        Order order = new Order(1L, 1L, 1L, 0, 0, 2.234, OrderStatus.CREATED, LocalDate.now());
        when(orderRepository.getReferenceById(1L)).thenReturn(order);
        double newWeight = 5;

        doReturn(order).when(orderServiceSpy).evalCost(1L);
        when(orderRepository.save(any(Order.class))).thenAnswer(
                invocationOnMock -> invocationOnMock.getArguments()[0]
        );

        Order changed = orderServiceSpy.changeWeight(1L, newWeight);

        assertEquals(newWeight, changed.getWeight());
    }

    @Test
    void evalCost_shouldChangeCost_whenIsNotWeighed() {
        Order order = new Order(1L, 1L, 1L, 2, 0, 0, OrderStatus.CREATED, LocalDate.now());
        when(orderRepository.getReferenceById(1L)).thenReturn(order);
        when(priceService.getPriceById(1L)).thenReturn(new Price(1L, "Тушенка", 200, false));
        when(orderRepository.save(any(Order.class))).thenAnswer(
                invocation -> invocation.getArguments()[0]
        );

        Order actual = orderService.evalCost(1L);

        assertEquals(400, actual.getCost());
    }

    @Test
    void evalCost_shouldChangeCost_whenIsWeighed(){
        Order order = new Order(1L, 1L, 1L, 0, 0, 2.643, OrderStatus.CREATED, LocalDate.now());
        when(orderRepository.getReferenceById(1L)).thenReturn(order);
        when(priceService.getPriceById(1L)).thenReturn(new Price(1L, "Курица", 10, true));
        when(orderRepository.save(any(Order.class))).thenAnswer(
                invocation -> invocation.getArguments()[0]
        );

        Order actual = orderService.evalCost(1L);

        assertEquals(26.43, actual.getCost());
    }
}