package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {

    @Query(value = "SELECT \n" +
            "    c.id, \n" +
            "    c.name, \n" +
            "    c.address, \n" +
            "    c.phone, \n" +
            "    c.total_sum, \n" +
            "    c.delivery_order_number, \n" +
            "    COUNT(o.id) FILTER (WHERE o.status = 'CREATED') AS created_orders_count\n" +
            "FROM consumers c\n" +
            "LEFT JOIN orders o ON o.consumer_id = c.id\n" +
            "GROUP BY c.id, c.name, c.address, c.phone\n" +
            "ORDER BY created_orders_count DESC;", nativeQuery = true)
    List<Consumer> findAllSorted();

    @Modifying
    @Query(value = "UPDATE consumers SET address = ?2 WHERE id = ?1;", nativeQuery = true)
    int updateAddress(long id, String address);

    @Modifying
    @Query(value = "UPDATE consumers SET phone = ?2 WHERE id = ?1;", nativeQuery = true)
    int updatePhone(long id, String phone);

    @Modifying
    @Query(value = "UPDATE consumers SET total_sum = total_sum + ?2 WHERE id = ?1;", nativeQuery = true)
    int increaseTotalSum(long consumerId, int value);

    @Modifying
    @Query(value = "UPDATE consumers SET delivery_order_number = ?2 WHERE id = ?1;", nativeQuery = true)
    int changeOrderNumber(long consumerId, int number);
}
