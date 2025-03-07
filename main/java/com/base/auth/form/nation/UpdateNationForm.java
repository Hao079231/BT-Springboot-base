package com.base.auth.form.nation;

import com.base.auth.validation.NationType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class UpdateNationForm {
    @NotNull(message = "id cant not be null")
    @ApiModelProperty(name = "id", required = true)
    private Long id;
    @NotEmpty(message = "name cant not be null")
    @ApiModelProperty(name = "name", required = true)
    private String name;
    @NotEmpty(message = "description cant not be null")
    @ApiModelProperty(name = "description", required = true)
    private String description;
    @NotNull(message = "type cant not be null")
    @ApiModelProperty(name = "type", required = true)
    @NationType
    private Integer type;
    @ApiModelProperty(name = "parent", required = true)
    private Long parentId;
}
