package com.yn.printer.service.modules.common.api.tentcentMap.service;

import com.alibaba.fastjson.JSONObject;
import com.yn.printer.service.common.vo.AreaVO;
import com.yn.printer.service.modules.meta.entity.Area;
import com.yn.printer.service.modules.meta.repository.AreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class inverseAddressResolution {


    @Autowired
    AreaRepository areaRepository;

    private static String getString(String lat, String lng, String key) {
        String hsUrl = "https://apis.map.qq.com/ws/geocoder/v1/?location=" + lat + "," + lng + "&key=" + key + "&get_poi=1";

        URL url;

        BufferedReader in = null;

        StringBuilder sb = new StringBuilder();
        try {

            url = new URL(hsUrl);

            in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));

            String str;
            while ((str = in.readLine()) != null) {

                sb.append(str);
            }
        } catch (Exception ignored) {
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ignored) {

            }
        }
        return sb.toString();
    }

    public AreaVO getAreaVO(String lat, String lng, String key) {
        final String result = getString(lat, lng, key);
        JSONObject object = JSONObject.parseObject(result);
        JSONObject resultObject = object.getJSONObject("result");
        JSONObject addressComponent = resultObject.getJSONObject("address_component");
        JSONObject formattedAddresses = resultObject.getJSONObject("formatted_addresses");
        AreaVO areaVO = new AreaVO();
        String province = addressComponent.getString("province");
        String city = addressComponent.getString("city");
        String county = addressComponent.getString("district");
        String street = addressComponent.getString("street");
        String address = formattedAddresses.getString("recommend");
        areaVO.setAddress(address);
        Area provinceArea = areaRepository.findByName(province);
        Area cityArea = areaRepository.findByName(city);
        areaVO.setProvince(provinceArea);
        areaVO.setCity(cityArea);
        Area countyArea = areaRepository.findByNameAndArea(county, cityArea);
        Area streetArea = areaRepository.findByNameAndArea(street, countyArea);
        if (streetArea == null) {
            Area area = new Area();
            area.setName(street);
            area.setArea(countyArea);
            areaRepository.save(area);
            streetArea = area;
        }
        areaVO.setCounty(countyArea);
        areaVO.setStreet(streetArea);
        return areaVO;
    }
}
