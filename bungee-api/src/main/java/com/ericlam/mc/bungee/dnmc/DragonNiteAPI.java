package com.ericlam.mc.bungee.dnmc;

import com.ericlam.mc.bungee.dnmc.builders.function.ChatRunnerManager;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandRegister;
import com.ericlam.mc.bungee.dnmc.config.ConfigFactory;
import com.ericlam.mc.bungee.dnmc.config.MainConfig;
import com.ericlam.mc.bungee.dnmc.managers.ChatFormatManager;
import com.ericlam.mc.bungee.dnmc.managers.PlayerManager;
import com.ericlam.mc.bungee.dnmc.managers.ResourceManager;
import com.ericlam.mc.bungee.dnmc.managers.SkinValueManager;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * 本服 API
 */
public interface DragonNiteAPI {

    SQLDataSource getSQLDataSource();

    RedisDataSource getRedisDataSource();

    ChatRunnerManager getChatRunnerManager();

    CommandRegister getCommandRegister();

    MainConfig getMainConfig();

    ConfigFactory getConfigFactory(Plugin plugin);

    ChatFormatManager getChatFormatManager();

    PlayerManager getPlayerManager();

    SkinValueManager getSkinValueManager();

    ResourceManager getResourceManager(ResourceManager.Type type);

}
