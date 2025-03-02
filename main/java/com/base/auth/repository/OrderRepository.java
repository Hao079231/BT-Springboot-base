package com.base.auth.repository;

import com.base.auth.model.Order;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

  boolean existsByCode(String code);

  List<Order> findByCustomerId(long currentUser);
}
