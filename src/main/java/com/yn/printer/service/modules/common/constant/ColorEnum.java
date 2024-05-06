package com.yn.printer.service.modules.common.constant;

import lombok.AllArgsConstructor;

import java.awt.*;

@AllArgsConstructor
public enum ColorEnum {
    CLEAR("透明"),
    RED("红色"),
    BLUE("蓝色"),
    WHITE("白色");

    private final String name;

    public String getName() {
        return name;
    }

    public static Color getAwtColor(ColorEnum colorEnum) {
        if (colorEnum == null) return null;
        switch (colorEnum) {
            case RED:
                return Color.RED;
            case BLUE:
                return Color.BLUE;
            case WHITE:
                return Color.WHITE;
            default:
                return null;
        }
    }
}
