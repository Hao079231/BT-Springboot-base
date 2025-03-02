package com.base.auth.dto.order;

import com.base.auth.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OrderAdminDto extends ABasicAdminDto {
  @ApiModelProperty(name = "Order id")
  private Long id;
  @ApiModelProperty(name = "Order total money")
  private Double totalMoney;
  @ApiModelProperty(name = "Order total sale off")
  private Double totalSaleOff;
  @ApiModelProperty(name = "Customer name")
  private String customerName;
  @ApiModelProperty(name = "Order state")
  private Integer state;
}
