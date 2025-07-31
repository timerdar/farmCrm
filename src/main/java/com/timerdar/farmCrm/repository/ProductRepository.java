package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "update Product set createdCount = ?2 where id = ?1;", nativeQuery = true)
    Product updateCreatedCount(int productId, int createdCount);

}
