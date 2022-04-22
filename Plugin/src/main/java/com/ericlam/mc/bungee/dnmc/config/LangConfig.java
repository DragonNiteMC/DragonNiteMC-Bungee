package com.ericlam.mc.bungee.dnmc.config;

import com.ericlam.mc.bungee.dnmc.config.yaml.MessageConfiguration;
import com.ericlam.mc.bungee.dnmc.config.yaml.Prefix;
import com.ericlam.mc.bungee.dnmc.config.yaml.Resource;

@Resource(locate = "lang.yml")
@Prefix(path = "prefix")
public class LangConfig extends MessageConfiguration {
}
