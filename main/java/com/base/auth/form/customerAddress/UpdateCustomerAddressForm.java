package com.base.auth.form.customerAddress;

import com.base.auth.validation.CustomerAddressType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@ApiModel
public class UpdateCustomerAddressForm {
  @NotNull(message = "Customer address id cannot null")
  @ApiModelProperty(name = "id", required = true)
  private Long id;

  @NotNull(message = "province cannot null")
  @ApiModelProperty(name = "provinceId", required = true)
  private Long provinceId;

  @NotNull(message = "district cannot null")
  @ApiModelProperty(name = "districtId", required = true)
  private Long districtId;

  @NotNull(message = "commune cannot null")
  @ApiModelProperty(name = "communeId", required = true)
  private Long communeId;

  @NotEmpty(message = "address cannot null")
  @ApiModelProperty(name = "address", example = "Quan 9, TPHCM", required = true)
  private String address;

  @ApiModelProperty(name = "type", required = true)
  @CustomerAddressType
  private Integer type;

  @ApiModelProperty(name = "address default", required = true)
  private Boolean isDefault;
}
