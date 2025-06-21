package com.timerdar.farmCrm.repository;

import com.timerdar.farmCrm.model.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {}
