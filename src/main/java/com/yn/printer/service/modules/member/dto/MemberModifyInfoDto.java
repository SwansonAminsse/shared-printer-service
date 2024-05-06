package com.yn.printer.service.modules.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : Jonas Chan
 * @since : 2023/12/14 17:09
 */
@Data
@ApiModel(value = "MemberModifyInfoDto", description = "用户信息修改")
public class MemberModifyInfoDto {

    @ApiModelProperty(value = "名称")
    String name;
    @ApiModelProperty(value = "头像路径")
    String imageUrl;

}
