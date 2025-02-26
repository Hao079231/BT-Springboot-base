package com.base.auth.repository;

import com.base.auth.model.CartItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
  Optional<CartItem> findByCartIdAndProductId(Long cartId, Long productId);
  List<CartItem> findByCartId(Long cartId);
}
