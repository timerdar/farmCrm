package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Price;
import com.timerdar.farmCrm.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PriceService priceService;

    public Order createOrder(CreateOrderRequest orderRequest){
        Order newOrder = new Order();
        newOrder.setCreatedAt(LocalDate.now());
        newOrder.setAmount(orderRequest.getAmount());
        newOrder.setProductId(orderRequest.getProductId());
        newOrder.setConsumerId(orderRequest.getConsumerId());
        newOrder.setStatus(OrderStatus.CREATED);
        return orderRepository.save(newOrder);
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

    public Order moveOrderToDelivery(long id){
        Order order = orderRepository.getReferenceById(id);
        if (order.getCost() == 0){
            Price price = priceService.getPriceById(order.getProductId());
            if (price.isWeighed()){
                order.setCost(price.getPrice() * order.getWeight());
            }else{
                order.setCost(price.getPrice() * order.getAmount());
            }
        }
        Order newOrder = orderRepository.save(order);
        return changeStatus(newOrder.getId(), OrderStatus.DELIVERY);
    }

    public Order moveOrderToDone(long id){
        return changeStatus(id, OrderStatus.DONE);
    }

    public Order moveOrderToCreated(long id){
        return changeStatus(id, OrderStatus.CREATED);
    }
}
