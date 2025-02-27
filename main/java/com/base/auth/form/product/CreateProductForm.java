package com.base.auth.form.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@ApiModel
public class CreateProductForm {
  @NotEmpty(message = "Product name cannot null")
  @ApiModelProperty(name= "product name", required = true)
  private String name;

  @NotEmpty(message = "Product description cannot null")
  @ApiModelProperty(name= "product description", required = true)
  private String description;

  @NotEmpty(message = "Product short description cannot null")
  @ApiModelProperty(name= "product short description", required = true)
  private String shortDescription;

  @NotNull(message = "Product price cannot null")
  @ApiModelProperty(name= "product price", required = true)
  private Float price;

  @NotNull(message = "Product sale off cannot null")
  @ApiModelProperty(name= "product sale off", required = true)
  private Integer saleOff;

  @ApiModelProperty(name= "product image", required = true)
  private String image;
}
