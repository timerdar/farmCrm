package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByConsumerId(long consumerId);

    List<Order> findByProductId(long productId);

    List<Order> findByConsumerIdAndStatus(long consumerId, OrderStatus status);

    List<Order> findByProductIdAndStatus(long productId, OrderStatus status);

    @Query(value = "select sum(count) from Orders where product_id = :id and status = :status;", nativeQuery = true)
    Integer getOrderedProductAmountByIdAndStatus(@Param("id") long productId, @Param("status") String status);

    @Query(value = "select sum(cost) from Order where consumerId = :1 and status = :2;", nativeQuery = true)
    double getSummaryOrderCostOfConsumerByIdAndStatus(long consumerId, OrderStatus status);

    @Query(value = "select distinct o.consumer_id, c.delivery_order_number\n" +
            "from orders o\n" +
            "join consumers c on o.consumer_id = c.id\n" +
            "where o.status = :status\n" +
            "order by c.delivery_order_number asc;", nativeQuery = true)
    List<Long> getConsumerIdsByStatus(@Param("status") String status);

    @Query(value = "select * from orders where status = 'DELIVERY' or status = 'DONE';", nativeQuery = true)
    List<Order> getDeliveryOrders();

}