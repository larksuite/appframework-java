/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card;

public enum TemplateColor {

    BLUE("blue"),
    WATHET("wathet"),
    TURQUOISE("turquoise"),
    GREEN("green"),
    YELLOW("yellow"),
    ORANGE("orange"),
    RED("red"),
    CARMINE("carmine"),
    VIOLET("violet"),
    PURPLE("purple"),
    INDIGO("indigo"),
    GREY("grey")
    ;
    private String color;

    private TemplateColor(String color){
        this.color = color;
    }

    public String getColor(){
        return this.color;
    }
}
