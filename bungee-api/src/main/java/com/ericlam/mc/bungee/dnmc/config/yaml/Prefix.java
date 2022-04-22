package com.ericlam.mc.bungee.dnmc.config.yaml;

import java.lang.annotation.*;

/**
 * 用於 訊息類別 config 時標註 前綴 路徑
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Prefix {
    String path();
}
