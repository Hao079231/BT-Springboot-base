package com.base.auth.controller;

import com.base.auth.constant.UserBaseConstant;
import com.base.auth.dto.ApiMessageDto;
import com.base.auth.dto.ResponseListDto;
import com.base.auth.dto.order.OrderItemAdminDto;
import com.base.auth.dto.order.OrderItemDto;
import com.base.auth.exception.NotFoundException;
import com.base.auth.form.order.UpdateOrderForm;
import com.base.auth.mapper.OrderMapper;
import com.base.auth.model.Cart;
import com.base.auth.model.CartItem;
import com.base.auth.model.Customer;
import com.base.auth.model.Order;
import com.base.auth.model.OrderItem;
import com.base.auth.model.Product;
import com.base.auth.model.criteria.OrderItemCriteria;
import com.base.auth.repository.CartItemRepository;
import com.base.auth.repository.CartRepository;
import com.base.auth.repository.CustomerRepository;
import com.base.auth.repository.OrderItemRepository;
import com.base.auth.repository.OrderRepository;
import com.base.auth.utils.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/order")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class OrderController {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private CartItemRepository cartItemRepository;

  @Autowired
  private OrderItemRepository orderItemRepository;

  @Autowired
  private OrderMapper orderMapper;

  @PostMapping(value = "/create", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('O_C')")
  public ApiMessageDto<String> placeOrder(){
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Account not authenticated");
      return apiMessageDto;
    }

    String username = authentication.getName();

    Customer customer = customerRepository.findByAccountUsername(username)
        .orElseThrow(() -> new NotFoundException("Customer not found"));

    Cart cart = cartRepository.findById(customer.getId())
        .orElseThrow(() -> new NotFoundException("Cart not found"));

    List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
    if (cartItems.isEmpty()){
      throw new NotFoundException("Cart is empty");
    }

    Order order = new Order();
    order.setCustomer(customer);
    order.setCode(StringUtils.generateUniqueCode(orderRepository::existsByCode));
    order.setState(UserBaseConstant.ORDER_BOOKING);

    double totalMoney = 0;
    int totalSaleOff = 0;

    List<OrderItem> orderItems = new ArrayList<>();
    for (CartItem cartItem : cartItems) {
      Product product = cartItem.getProduct();

      OrderItem orderItem = new OrderItem();
      orderItem.setOrder(order);
      orderItem.setProduct(product);
      orderItem.setQuantity(cartItem.getQuantity());
      orderItem.setSinglePrice(Double.valueOf(product.getPrice()));
      orderItem.setSaleOff(product.getSaleOff());

      totalMoney += cartItem.getQuantity() * product.getPrice();
      totalSaleOff += cartItem.getQuantity() * product.getSaleOff();

      orderItems.add(orderItem);
    }

    order.setTotalMoney(totalMoney);
    order.setTotalSaleOff(totalSaleOff);
    orderRepository.save(order);
    orderItemRepository.saveAll(orderItems);

    cartItemRepository.deleteAll(cartItems);
    apiMessageDto.setMessage("Order placed success`");
    return apiMessageDto;
  }

  @PutMapping("/approve")
  public ApiMessageDto<String> approveOrder(@Valid @RequestBody UpdateOrderForm form) {
    ApiMessageDto<String> apiMessageDto = new ApiMessageDto<>();
    Order order = orderRepository.findById(form.getId())
        .orElseThrow(() -> new RuntimeException("Order not found"));

    int currentState = order.getState();
    int newState = form.getState();

    List<Integer> validStates = Arrays.asList(
        UserBaseConstant.ORDER_BOOKING,
        UserBaseConstant.ORDER_APPROVED,
        UserBaseConstant.ORDER_DELIVERY,
        UserBaseConstant.ORDER_DONE,
        UserBaseConstant.ORDER_CANCEL
    );

    if (!validStates.contains(newState)) {
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Invalid state transition");
      return apiMessageDto;
    }

    boolean isValidTransition = (newState == currentState + 1)
        || (currentState == UserBaseConstant.ORDER_DELIVERY && newState == UserBaseConstant.ORDER_CANCEL);

    if (!isValidTransition) {
      apiMessageDto.setResult(false);
      apiMessageDto.setMessage("Invalid state transition");
      return apiMessageDto;
    }

    order.setState(newState);
    orderRepository.save(order);
    apiMessageDto.setMessage("Order approval successful");

    return apiMessageDto;
  }

  @GetMapping(value = "/admin/list", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('O_AL')")
  public ApiMessageDto<ResponseListDto<List<OrderItemAdminDto>>> getListOrderItemByAdmin(OrderItemCriteria request, Pageable pageable){
    ApiMessageDto<ResponseListDto<List<OrderItemAdminDto>>> apiMessageDto = new ApiMessageDto<>();
    Page<OrderItem> orders = orderItemRepository.findAll(request.getSpecification(), pageable);
    List<OrderItemAdminDto> list = orderMapper.fromOrderItemToAdminListDto(orders.getContent());
    ResponseListDto<List<OrderItemAdminDto>> pageResult = new ResponseListDto<>(list, orders.getTotalElements(), orders.getTotalPages());
    apiMessageDto.setMessage("List order item");
    apiMessageDto.setData(pageResult);
    return apiMessageDto;
  }

  @GetMapping(value = "/admin/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('O_AV')")
  public ApiMessageDto<OrderItemAdminDto> getOrderItemByAdmin(@PathVariable Long id){
    ApiMessageDto<OrderItemAdminDto> apiMessageDto = new ApiMessageDto<>();
    OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(()
    -> new NotFoundException("Order item id not found"));
    apiMessageDto.setData(orderMapper.fromOrderItemToAdminDto(orderItem));
    apiMessageDto.setMessage("Order item information");
    return apiMessageDto;
  }

  @GetMapping(value = "/customer/list", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('O_CL')")
  public ApiMessageDto<ResponseListDto<List<OrderItemDto>>> getListOrderItemByCustomer(OrderItemCriteria request, Pageable pageable){
    ApiMessageDto<ResponseListDto<List<OrderItemDto>>> apiMessageDto = new ApiMessageDto<>();
    Page<OrderItem> orders = orderItemRepository.findAll(request.getSpecification(), pageable);
    List<OrderItemDto> list = orderMapper.fromOrderItemToListDto(orders.getContent());
    ResponseListDto<List<OrderItemDto>> pageResult = new ResponseListDto<>(list, orders.getTotalElements(), orders.getTotalPages());
    apiMessageDto.setMessage("List order item");
    apiMessageDto.setData(pageResult);
    return apiMessageDto;
  }

  @GetMapping(value = "/customer/get/{id}", produces= MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasRole('O_CV')")
  public ApiMessageDto<OrderItemDto> getOrderItemByCustomer(@PathVariable Long id){
    ApiMessageDto<OrderItemDto> apiMessageDto = new ApiMessageDto<>();
    OrderItem orderItem = orderItemRepository.findById(id).orElseThrow(()
        -> new NotFoundException("Order item id not found"));
    apiMessageDto.setData(orderMapper.fromOrderItemToDto(orderItem));
    apiMessageDto.setMessage("Order item information");
    return apiMessageDto;
  }
}
