package com.base.auth.mapper;

import com.base.auth.dto.order.OrderAdminDto;
import com.base.auth.dto.order.OrderDto;
import com.base.auth.dto.order.OrderItemAdminDto;
import com.base.auth.dto.order.OrderItemDto;
import com.base.auth.model.Order;
import com.base.auth.model.OrderItem;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface OrderMapper {

  @Mapping(source = "id", target = "id")
  @Mapping(source = "totalMoney", target = "totalMoney")
  @Mapping(source = "totalSaleOff", target = "totalSaleOff")
  @Mapping(source = "state", target = "state")
  @Named("fromOrderToDto")
  OrderDto fromOrderToDto(Order order);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "product", target = "productDto", qualifiedByName = "fromProductToProductDto")
  @Mapping(source = "quantity", target = "quantity")
  @Mapping(source = "singlePrice", target = "singlePrice")
  @Mapping(source = "saleOff", target = "saleOff")
  @Mapping(source = "order", target = "orderDto", qualifiedByName = "fromOrderToDto")
  @Named("fromOrderItemToDto")
  OrderItemDto fromOrderItemToDto(OrderItem orderItem);

  @IterableMapping(elementTargetType = OrderItemDto.class, qualifiedByName = "fromOrderItemToDto")
  List<OrderItemDto> fromOrderItemToListDto(List<OrderItem> orderItems);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "totalMoney", target = "totalMoney")
  @Mapping(source = "totalSaleOff", target = "totalSaleOff")
  @Mapping(source = "customer.account.fullName", target = "customerName")
  @Mapping(source = "state", target = "state")
  @Mapping(source = "modifiedDate", target = "modifiedDate")
  @Mapping(source = "createdDate", target = "createdDate")
  @Named("fromOrderToAdminDto")
  OrderAdminDto fromOrderToAdminDto(Order order);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "product", target = "productDto", qualifiedByName = "fromProductToProductDto")
  @Mapping(source = "quantity", target = "quantity")
  @Mapping(source = "singlePrice", target = "singlePrice")
  @Mapping(source = "saleOff", target = "saleOff")
  @Mapping(source = "order", target = "orderAdminDto", qualifiedByName = "fromOrderToAdminDto")
  @Named("fromOrderItemToAdminDto")
  OrderItemAdminDto fromOrderItemToAdminDto(OrderItem orderItem);

  @IterableMapping(elementTargetType =  OrderItemAdminDto.class, qualifiedByName = "fromOrderItemToAdminDto")
  List<OrderItemAdminDto> fromOrderItemToAdminListDto(List<OrderItem> orderItems);
}
