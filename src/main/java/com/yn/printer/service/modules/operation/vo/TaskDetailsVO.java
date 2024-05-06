package com.yn.printer.service.modules.operation.vo;

import com.yn.printer.service.modules.meta.entity.Area;
import com.yn.printer.service.modules.meta.enums.PaperType;
import com.yn.printer.service.modules.operation.enums.ConsumableType;
import com.yn.printer.service.modules.operation.enums.OperationsType;
import com.yn.printer.service.modules.operation.enums.ProcessingStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
@NoArgsConstructor
@Data
public class TaskDetailsVO {
    @ApiModelProperty(value = "运维类型")
    private OperationsType taskType;
    @ApiModelProperty(value = "运维消息")
    private String message;
    @ApiModelProperty(value = "擺放位置")
    private String address;
    @ApiModelProperty(value = "设备编号")
    private String code;
    @ApiModelProperty(value = "任务id")
    private Long id;
    @ApiModelProperty(value = "已读未读")
    private Boolean readed;
    @ApiModelProperty(value = "已处理未处理")
    private ProcessingStatus taskStatus;
    @ApiModelProperty(value = "完成时间")
    private LocalDateTime completionTime;
    @ApiModelProperty(value = "創建时间")
    private LocalDateTime createdOn;
    @ApiModelProperty(value = "纸张类型")
    private PaperType paperType;
    @ApiModelProperty(value = "耗材类型")
    private ConsumableType consumable;
    @ApiModelProperty(value = "设备名")
    private String deviceName;
    @ApiModelProperty(value = "渠道商名")
    private String channelName;
    @ApiModelProperty(value = "剩余纸张/耗材")
    private Integer residue;
    @ApiModelProperty(value = "加纸/耗材数量")
    private Integer additionsNumber = 0;
    @ApiModelProperty(value = "省")
    private Area province;
    @ApiModelProperty(value = "市")
    private Area city;
    @ApiModelProperty(value = "区")
    private Area county;
    @ApiModelProperty(value = "街道")
    private Area street;
    @ApiModelProperty(value = "详细地址")
    private String detailAddress;
    @ApiModelProperty(value = "最大余量")
    private Integer maxResidue = 0;
    @ApiModelProperty(value = "运维信息")
    private String information;

    public TaskDetailsVO(OperationsType taskType,
                         String message,
                         String address,
                         Long id,
                         String code,
                         Boolean readed,
                         ProcessingStatus taskStatus,
                         LocalDateTime completionTime,
                         LocalDateTime createdOn,
                         PaperType paperType,
                         ConsumableType consumable,
                         String deviceName,
                         String channelName,
                         Area province,
                         Area city,
                         Area county,
                         Area street,
                         String information

) {
        this.taskType = taskType;
        this .message = message;
        this.address = address;
        this.id = id;
        this.code = code;
        this.readed = readed;
        this.taskStatus = taskStatus;
        this.completionTime = completionTime;
        this.paperType = paperType;
        this.createdOn = createdOn;
        this.consumable = consumable;
        this.deviceName = deviceName;
        this.channelName = channelName;
    this.province = province;
    this.city = city;
    this.county = county;
    this.street = street;
    this.information = information;
    generateDetailAddress();
    }
    private void generateDetailAddress() {
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
