package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ConsumerService consumerService;

    public Order createOrder(CreateOrderRequest orderRequest){
        if(!orderRequest.isFullyEntered()){
            throw new IllegalArgumentException("Для создания заказа введены не все данные");
        }else if (!productService.isPriceExists(orderRequest.getProductId())){
            throw new EntityNotFoundException("Указанной позиции в прайс-листе нет");
        }else if(!consumerService.isConsumerExists(orderRequest.getConsumerId())){
            throw new EntityNotFoundException("Указанного заказчика нет в системе");
        }else {
            Order newOrder = new Order();
            newOrder.setProductId(orderRequest.getProductId());
            newOrder.setCreatedAt(LocalDate.now());
            newOrder.setConsumerId(orderRequest.getConsumerId());
            newOrder.setCount(orderRequest.getAmount());
            newOrder.setWeight(orderRequest.getWeight());
            return evalCost(orderRepository.save(newOrder).getId());
        }
    }

    public List<Order> getOrdersOfConsumer(long consumerId, OrderStatus status){
        return orderRepository.findByConsumerIdAndStatus(consumerId, status);
    }

    public List<Order> getOrdersOfProduct(long productId, OrderStatus status){
        return orderRepository.findByProductIdAndStatus(productId, status);
    }

    public Order changeStatus(long id, OrderStatus status){
        Order order = orderRepository.getReferenceById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order changeAmount(long id, int newAmount){
        Order order = orderRepository.getReferenceById(id);
        order.setCount(newAmount);
        return evalCost(orderRepository.save(order).getId());
    }

    public Order changeWeight(long id, double newWeight){
        Order order = orderRepository.getReferenceById(id);
        order.setWeight(newWeight);
        return evalCost(orderRepository.save(order).getId());
    }

    public Order evalCost(long id){
        Order order = orderRepository.getReferenceById(id);
        Product product = productService.getProductById(order.getProductId());
        if (product.isWeighed()){
            order.setCost(product.getPrice() * order.getWeight());
        }else{
            order.setCost(product.getPrice() * order.getCount());
        }
        return orderRepository.save(order);
    }
}
