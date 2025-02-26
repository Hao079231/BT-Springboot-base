package com.base.auth.dto.customer;

import com.base.auth.dto.account.AccountDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

@Data
public class CustomerDto {
  @JsonIgnoreProperties({"group"})
  private AccountDto accountDto;
  @ApiModelProperty(name = "birthday")
  private Date birthday;
  @ApiModelProperty(name = "gender")
  private Integer gender;
  @ApiModelProperty(name = "address")
  private String address;
}
