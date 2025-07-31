package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
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
            "    COUNT(o.id) FILTER (WHERE o.status = 'CREATED') AS created_orders_count\n" +
            "FROM consumers c\n" +
            "LEFT JOIN orders o ON o.consumerid = c.id\n" +
            "GROUP BY c.id, c.name, c.address, c.phone\n" +
            "ORDER BY created_orders_count DESC;", nativeQuery = true)
    List<Consumer> findAllSorted();

}
