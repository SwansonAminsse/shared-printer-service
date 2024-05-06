package com.yn.printer.service.modules.operation.enums;

import lombok.AllArgsConstructor;

import java.util.Objects;
@AllArgsConstructor
public enum TutorialTypes {

    Host("Host"),


    Applet("Applet");

    private final String name;

    public String getName() {
        return name;
    }
}