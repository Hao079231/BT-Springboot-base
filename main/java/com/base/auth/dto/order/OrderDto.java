package com.base.auth.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderDto {
  @ApiModelProperty(name = "Order id")
  private Long id;
  @ApiModelProperty(name = "Order total money")
  private Double totalMoney;
  @ApiModelProperty(name = "Order total sale off")
  private Double totalSaleOff;
  @ApiModelProperty(name = "Order state")
  private Integer state;
}
