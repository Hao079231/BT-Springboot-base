package com.base.auth.form.order;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderForm {
  @NotNull(message = "id cannot be null")
  @ApiModelProperty(required = true)
  private Long id;

  @NotNull(message = "status cannot be null")
  @ApiModelProperty(required = true)
  private Integer state;
}
