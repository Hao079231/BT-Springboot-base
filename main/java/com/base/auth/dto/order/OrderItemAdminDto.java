package com.base.auth.dto.order;

import com.base.auth.dto.ABasicAdminDto;
import com.base.auth.dto.product.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderItemAdminDto extends ABasicAdminDto {
  @ApiModelProperty(name = "Order item id")
  private Long id;
  @JsonIgnoreProperties({"description", "shortDescription", "price", "saleOff", "image"})
  private ProductDto productDto;
  @ApiModelProperty(name = "Order item quantity")
  private Integer quantity;
  @ApiModelProperty(name = "Product price")
  private Double singlePrice;
  @ApiModelProperty(name = "Product sale off")
  private Integer saleOff;
  private OrderAdminDto orderAdminDto;
}
