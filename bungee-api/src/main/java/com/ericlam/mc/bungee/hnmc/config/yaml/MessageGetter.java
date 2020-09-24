package com.ericlam.mc.bungee.hnmc.config.yaml;

import java.util.List;

public interface MessageGetter {

    String getPrefix();

    String get(String path);

    String getPure(String path);

    List<String> getList(String path);

    List<String> getPureList(String path);
}
