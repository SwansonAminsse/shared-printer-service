package com.yn.printer.service.modules.operation.vo;

import com.yn.printer.service.modules.operation.enums.DeviceStatus;
import com.yn.printer.service.modules.meta.entity.Area;
import com.yn.printer.service.modules.meta.enums.PaperType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@ApiModel(value = "DeviceVo", description = "设备vo")
public class DeviceVo {

    @ApiModelProperty(value = "设备id")
    private Long id;

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "设备编号")
    private String code;

    @ApiModelProperty(value = "经度")
    private BigDecimal jd = BigDecimal.ZERO;

    @ApiModelProperty(value = "维度")
    private BigDecimal wd = BigDecimal.ZERO;

    @ApiModelProperty(value = "客服电话")
    private String servicePhone;

    @ApiModelProperty(value = "设备状态 " +
            "NOT_ACTIVE(未激活)" +
            "ONLINE(在线)" +
            "OFFLINE(离线)" +
            "RUN(运行)" +
            "ABNORMAL(异常)" +
            "STOP(停用)")
    private DeviceStatus status;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "服务商名称")
    private String terminalMerchants;

    @ApiModelProperty(value = "剩余纸张")
    private List<PaperTableVo> paperNumber;

    @ApiModelProperty(value = "剩余耗材")
    private List<ConsumablesValueVo> consumablesValue;



    @ApiModelProperty(value = "省")
    private Area province;
    @ApiModelProperty(value = "市")
    private Area city;
    @ApiModelProperty(value = "区")
    private Area county;
    @ApiModelProperty(value = "街道")
    private Area street;
    @ApiModelProperty(value = "完整地址")
    private String detailAddress;
    @ApiModelProperty(value = "最大余量")
    Map<PaperType,Integer> maxReduction;

    public void generateDetailAddress() {
        StringBuilder detailAddressBuilder = new StringBuilder();

        if (province != null) {
            detailAddressBuilder.append(province.getName());
        }

        if (city != null) {
            detailAddressBuilder.append(city.getName());
        }


        if (street != null) {
            detailAddressBuilder.append(street.getName());
        }

        if (address != null && !address.isEmpty()) {
            detailAddressBuilder.append(address);
        }

        this.detailAddress = detailAddressBuilder.toString();
    }
}
