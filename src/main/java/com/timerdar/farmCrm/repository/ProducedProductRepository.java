package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.ProducedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProducedProductRepository extends JpaRepository<ProducedProduct, Long> {
    @Query(value = "select sum(amount) from ProducedProduct where productId = ?1", nativeQuery = true)
    int getSumCountOfProducedProduct(long productId);
}
