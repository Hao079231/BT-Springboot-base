package com.base.auth.repository;

import com.base.auth.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>,
    JpaSpecificationExecutor<OrderItem> {

}
