package com.ericlam.mc.bungee.hnmc.config;

import com.ericlam.mc.bungee.hnmc.config.yaml.MessageConfiguration;
import com.ericlam.mc.bungee.hnmc.config.yaml.Prefix;
import com.ericlam.mc.bungee.hnmc.config.yaml.Resource;

@Resource(locate = "lang.yml")
@Prefix(path = "prefix")
public class LangConfig extends MessageConfiguration {
}
