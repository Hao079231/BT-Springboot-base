package com.base.auth.dto.nation;

import com.base.auth.dto.ABasicAdminDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class NationAdminDto extends ABasicAdminDto {
    @ApiModelProperty(name = "name")
    private String name;
    @ApiModelProperty(name = "description")
    private String description;
    @ApiModelProperty(name = "type")
    private Integer type;
}
