package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
    List<Consumer> findAllByDistrictOrderByNameAsc(String district);
    @Query("select distinct district from Consumer")
    List<String> getDistrictsList();
    @Query(value = "select * from Consumer where starts_with(name, ?1)", nativeQuery = true)
    List<Consumer> findByNamePrefix(String prefix);
}
