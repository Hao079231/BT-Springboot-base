package com.base.auth.repository;

import com.base.auth.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  boolean existsByCode(String code);
}
