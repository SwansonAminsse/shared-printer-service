package com.yn.printer.service.modules.operation.util;

import java.math.BigDecimal;

public class DistanceCalculator {
    private static final double EARTH_RADIUS = 6371.0;

    /**
     * 计算两个经纬度之间的距离
     *
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 两点之间的距离，单位：千米
     */
    public static double calculateDistance(double lat1, double lon1, BigDecimal lat2, BigDecimal lon2) {
        // 将角度转换为弧度
        double radLat1 = Math.toRadians(lat1);
        double radLon1 = Math.toRadians(lon1);
        double radLat2 = Math.toRadians(lat2.doubleValue());
        double radLon2 = Math.toRadians(lon2.doubleValue());

        // 计算经纬度的差值
        double dLat = radLat2 - radLat1;
        double dLon = radLon2 - radLon1;

        // 使用 Haversine 公式计算距离
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(radLat1) * Math.cos(radLat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 距离（单位：千米）
        return EARTH_RADIUS * c;
    }


}