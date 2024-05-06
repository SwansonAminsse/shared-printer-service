package com.yn.printer.service.modules.operation.vo;

import com.yn.printer.service.modules.meta.entity.Area;
import com.yn.printer.service.modules.operation.entity.DevicesList;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

import static com.yn.printer.service.modules.operation.util.DistanceCalculator.calculateDistance;

@Data
public class DevicesListVO {
    @ApiModelProperty(value = "详细地址")
    private String address = "";
    @ApiModelProperty(value = "设备经度")
    private BigDecimal jd;
    @ApiModelProperty(value = "设备纬度")
    private BigDecimal wd;
    @ApiModelProperty(value = "设备编码")
    private String code = "";
    @ApiModelProperty(value = "渠道商名称")
    private String name = "";
    @ApiModelProperty(value = "设备名称")
    private String deviceName = "";
    @ApiModelProperty(value = "距离")
    private double distance = 0.0;
    @ApiModelProperty(value = "省")
    private Area province = null;
    @ApiModelProperty(value = "市")
    private Area city = null;
    @ApiModelProperty(value = "区")
    private Area county = null;
    @ApiModelProperty(value = "街道")
    private Area street = null;
    @ApiModelProperty(value = "详细地址")
    private String detailAddress ="";


    public DevicesListVO(String address, BigDecimal jd, BigDecimal wd, String code, String name,String deviceName, Area province, Area city, Area county, Area street) {
        this.address = address;
        this.jd = jd;
        this.wd = wd;
        this.code = code;
        this.name = name;
        this.deviceName = deviceName;
        this.province = province;
        this.city = city;
        this.county = county;
        this.street = street;
        generateDetailAddress();
    }


    public static DevicesListVO createWithDistance(DevicesListVO devicesList, double latitude, double longitude) {
        DevicesListVO deviceVO = new DevicesListVO(
                devicesList.getAddress(),
                devicesList.getJd(),
                devicesList.getWd(),
                devicesList.getCode(),
                devicesList.getName(),
        devicesList.getDeviceName(),
        devicesList.getProvince(),
                devicesList.getCity(),
                devicesList.getCounty(),
                devicesList.getStreet()
        );

        double distance = calculateDistance(latitude, longitude, devicesList.getWd(), devicesList.getJd());
        deviceVO.setDistance(distance);

        return deviceVO;
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
    public static DevicesListVO create(DevicesList devicesList, double latitude, double longitude) {
        DevicesListVO deviceVO = new DevicesListVO(
                devicesList.getAddress(),
                devicesList.getJd(),
                devicesList.getWd(),
                devicesList.getCode(),
                devicesList.getTerminalMerchants() != null ? devicesList.getTerminalMerchants().getName() : "",
                devicesList.getName(),
                devicesList.getProvince(),
                devicesList.getCity(),
                devicesList.getCounty(),
                devicesList.getStreet()
        );
        double distance = calculateDistance(latitude, longitude, devicesList.getWd(), devicesList.getJd());
        deviceVO.setDistance(distance);
        return deviceVO;
    }
}
