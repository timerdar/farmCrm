package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "update Products set created_count = ?2 where id = ?1;", nativeQuery = true)
    int updateCreatedCount(long productId, int createdCount);

    @Modifying
    @Query(value = "update Products set cost = ?2 where id = ?1;", nativeQuery = true)
    int updatePrice(long productId, double cost);

    @Query(value = "select * from products where id in (select product_id from orders where status = 'DELIVERY' or status = 'DONE')", nativeQuery = true)
    List<Product> getProductsListFromDelivery();

	@Modifying
	@Query(value = "update Products set name = ?2, cost = ?3, created_count = ?4 where id = ?1;", nativeQuery = true)
	int updateProduct(long productId, String name, double cost, int createdCount);

}
