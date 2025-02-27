package com.base.auth.dto.product;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProductDto {
  @ApiModelProperty(name = "Product id")
  private Long id;
  @ApiModelProperty(name = "Product name")
  private String name;
  @ApiModelProperty(name = "Product description")
  private String description;
  @ApiModelProperty(name = "Product short description")
  private String shortDescription;
  @ApiModelProperty(name = "Product price")
  private Float price;
  @ApiModelProperty(name = "Product sale off")
  private Integer saleOff;
  @ApiModelProperty(name = "Product image")
  private String image;
}
