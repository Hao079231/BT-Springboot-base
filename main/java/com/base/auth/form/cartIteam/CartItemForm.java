package com.base.auth.form.cartIteam;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemForm {
  @NotNull(message = "Product id cannot null")
  @ApiModelProperty(name = "Product id", required = true)
  private Long productId;
  @NotNull(message = "Quantity cannot null")
  @ApiModelProperty(name = "Quantity", required = true)
  private Integer quantity;
}
