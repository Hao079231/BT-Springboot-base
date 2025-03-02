package com.base.auth.dto.customerAddress;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CustomerAddressDto {
  @ApiModelProperty(name = "id")
  private Long id;
  @ApiModelProperty(name = "address")
  private String address;
  @ApiModelProperty(name = "type")
  private Integer type;
  @ApiModelProperty(name = "isDefault")
  private Boolean isDefault;
}
