package com.base.auth.controller;

import com.base.auth.dto.ApiMessageDto;
import com.base.auth.exception.NotFoundException;
import com.base.auth.form.cartIteam.CartItemForm;
import com.base.auth.model.Cart;
import com.base.auth.model.CartItem;
import com.base.auth.model.Product;
import com.base.auth.repository.CartItemRepository;
import com.base.auth.repository.CartRepository;
import com.base.auth.repository.ProductRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/cartItem")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CartItemController {
  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private CartRepository cartRepository;


  @PostMapping(value = "/update", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('CAI_U')")
  public ApiMessageDto<String> updateCartItem(@RequestBody List<CartItemForm> cartItemRequests, @RequestParam Long cartId) {
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    List<CartItem> existingItems = cartItemRepository.findByCartId(cartId);

    for (CartItemForm request : cartItemRequests) {
      Product product = productRepository.findById(request.getProductId())
          .orElseThrow(() -> new NotFoundException("Product id not found"));

      cartItemRepository.findByCartIdAndProductId(cartId, request.getProductId()).ifPresentOrElse(
          item -> item.setQuantity(item.getQuantity() + request.getQuantity()),
          () -> {
            Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart id not found"));
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cartItemRepository.save(newItem);
          }
      );
    }

    List<Long> productIds = cartItemRequests.stream()
        .map(CartItemForm::getProductId)
        .collect(Collectors.toList());

    existingItems.stream()
        .filter(item -> !productIds.contains(item.getProduct().getId()))
        .forEach(cartItemRepository::delete);

    apiMessageDto.setMessage("Update cart item success");
    return apiMessageDto;
  }
}
