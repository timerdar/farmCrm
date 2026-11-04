package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Order;
import com.timerdar.farmCrm.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByConsumerId(long consumerId);

    List<Order> findByProductId(long productId);

    List<Order> findByConsumerIdAndStatusOrderByIdAsc(long consumerId, OrderStatus status);

    List<Order> findByProductIdAndStatusOrderByIdAsc(long productId, OrderStatus status);

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

    @Query(value = "select distinct o.consumer_id, c.delivery_order_number, c.name\n" +
            "from orders o\n" +
            "join consumers c on o.consumer_id = c.id\n" +
            "where o.status = 'DELIVERY' or o.status = 'DONE'\n" +
            "order by c.delivery_order_number asc, c.name asc;", nativeQuery = true)
    List<Long> getDeliveryConsumerIds();

	@Query(value = "select distinct o.consumer_id, c.name\n" +
			"from orders o\n" +
			"join consumers c on o.consumer_id = c.id\n" +
			"where o.status = 'CREATED'\n" +
			"order by c.name asc;", nativeQuery = true)
	List<Long> getCreatedConsumerIds();

    @Query(value = "select * from orders where status = 'DELIVERY' or status = 'DONE';", nativeQuery = true)
    List<Order> getDeliveryOrders();

	@Query("""
			SELECT c.id, c.name, c.address, c.phone, c.deliveryOrderNumber, COALESCE(SUM(o.cost), 0),
			       SUM(CASE WHEN o.status = com.timerdar.farmCrm.model.OrderStatus.DELIVERY THEN 1 ELSE 0 END)
			FROM Order o, Consumer c
			WHERE c.id = o.consumerId
			  AND o.status IN (com.timerdar.farmCrm.model.OrderStatus.DELIVERY, com.timerdar.farmCrm.model.OrderStatus.DONE)
			GROUP BY c.id, c.name, c.address, c.phone, c.deliveryOrderNumber
			ORDER BY c.deliveryOrderNumber, c.name
			""")
	List<Object[]> findDeliveryStops();

	@Query("""
			SELECT o, p FROM Order o, Product p
			WHERE p.id = o.productId AND o.consumerId = :consumerId AND o.status IN :statuses
			ORDER BY o.id
			""")
	List<Object[]> findConsumerDeliveryOrders(@Param("consumerId") long consumerId,
			@Param("statuses") Collection<OrderStatus> statuses);

	@Query("""
			SELECT o, p, c FROM Order o, Product p, Consumer c
			WHERE p.id = o.productId AND c.id = o.consumerId AND o.status IN :statuses
			ORDER BY c.deliveryOrderNumber, c.name, o.id
			""")
	List<Object[]> findDeliveryOrdersWithNames(@Param("statuses") Collection<OrderStatus> statuses);

	@Query("""
			SELECT p.name, p.createdCount, c.name, o.count, o.status
			FROM Order o, Product p, Consumer c
			WHERE p.id = o.productId AND c.id = o.consumerId AND o.status IN :statuses
			ORDER BY p.name, c.name
			""")
	List<Object[]> findDeliverySummaryRows(@Param("statuses") Collection<OrderStatus> statuses);

}