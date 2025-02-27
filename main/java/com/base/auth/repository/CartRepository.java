package com.base.auth.repository;

import com.base.auth.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
  boolean existsByCode(String code);

  @Modifying
  @Query("DELETE FROM Cart ct WHERE ct.id = :customerId")
  void deleteCartById(@Param("customerId") Long id);
}
