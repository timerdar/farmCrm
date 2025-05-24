package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByConsumerId(long consumerId);
    List<Order> findByProductId(long productId);
    List<Order> findByConsumerIdAndStatus(long consumerId, OrderStatus status);
    List<Order> findByProductIdAndStatus(long productId, OrderStatus status);
    @Query("select sum(amount) from Order where productId = :1 and status = :2")
    int getOrderedProductAmountByIdAndStatus(long productId, OrderStatus status);
    @Query("select sum(cost) from Order where consumerId = :1 and status = :2")
    double getSummaryOrderCostOfConsumerByIdAndStatus(long consumerId, OrderStatus status);
}
