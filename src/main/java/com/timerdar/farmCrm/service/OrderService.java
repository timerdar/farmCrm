package com.timerdar.farmCrm.service;

import com.timerdar.farmCrm.dto.ConsumerWithOrders;
import com.timerdar.farmCrm.dto.CreateOrderRequest;
import com.timerdar.farmCrm.dto.OrderChangeRequest;
import com.timerdar.farmCrm.dto.OrderWithName;
import com.timerdar.farmCrm.model.Consumer;
import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import com.timerdar.farmCrm.model.Product;
import com.timerdar.farmCrm.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
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
            newOrder.setWeight(0);
            newOrder.setStatus(OrderStatus.CREATED);
            return evalCost(orderRepository.save(newOrder).getId());
        }
    }

    public List<OrderWithName> getOrdersWithName(long id, String source, String status){
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        List<OrderWithName> ordersWithName = new ArrayList<>();
        List<Order> orders = source.equals("products") ? getOrdersOfProduct(id, orderStatus) : getOrdersOfConsumer(id, orderStatus);
        for(Order order: orders){
            String name = source.equals("consumers") ? productService.getProductById(order.getProductId()).getName() : consumerService.getConsumerById(order.getConsumerId()).getName();
            ordersWithName.add(new OrderWithName(order, name));
        }
        return ordersWithName;
    }

    public List<Order> getOrdersOfConsumer(long consumerId, OrderStatus status){
        return orderRepository.findByConsumerIdAndStatus(consumerId, status);
    }

    public List<Order> getOrdersOfProduct(long productId, OrderStatus status){
        return orderRepository.findByProductIdAndStatus(productId, status);
    }

    public Order changeStatus(OrderChangeRequest request){
        OrderStatus newStatus = OrderStatus.valueOf(request.getStatus());
        Order order = orderRepository.getReferenceById(request.getId());
        if (request.getStatus().equals("ARCHIVED")){
            consumerService.increaseTotalSum(order.getConsumerId(), order.getCost());
        }
        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    public Order changeAmount(OrderChangeRequest request){
        Order order = orderRepository.getReferenceById(request.getId());
        order.setCount(request.getAmount());
        return evalCost(orderRepository.save(order).getId());
    }

    public Order changeWeight(OrderChangeRequest request){
        Order order = orderRepository.getReferenceById(request.getId());
        order.setWeight(request.getWeight());
        return evalCost(orderRepository.save(order).getId());
    }

    public Order evalCost(long id){
        Order order = orderRepository.getReferenceById(id);
        Product product = productService.getProductById(order.getProductId());
        if (product.isWeighed()){
            order.setCost((int) (product.getCost() * order.getWeight()));
        }else{
            order.setCost(product.getCreatedCount() * order.getCount());
        }
        return orderRepository.save(order);
    }

    public List<ConsumerWithOrders> getDeliveryData() {
        List<Long> consumerIds = orderRepository.getConsumerIdsByStatus("DELIVERY");
        List<ConsumerWithOrders> consumers = new ArrayList<>();
        for (Long consumerId: consumerIds){
            Consumer consumer = consumerService.getConsumerById(consumerId);
            List<OrderWithName> orders = getOrdersWithName(consumerId, "consumers", "DELIVERY");
            consumers.add(new ConsumerWithOrders(consumer, orders));
        }
        return consumers;
    }

    public int clearDelivery(){
        int count = 0;
        for (Order order: orderRepository.getDeliveryOrders()) {
            OrderChangeRequest request = new OrderChangeRequest();
            request.setId(order.getId());
            request.setStatus("ARCHIVED");
            changeStatus(request);
            count++;
        }
        return count;
    }
}
