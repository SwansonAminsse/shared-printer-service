package com.yn.printer.service.common.vo;

import com.yn.printer.service.modules.meta.entity.Area;
import lombok.Data;

@Data
public class AreaVO {
    private Area province;
    private Area city;
    private Area county;
    private Area street;
    private String address;
}