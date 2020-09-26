package com.ericlam.mc.bungee.hnmc;

import com.ericlam.mc.bungee.hnmc.builders.function.ChatRunnerManager;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandRegister;
import com.ericlam.mc.bungee.hnmc.config.ConfigFactory;
import com.ericlam.mc.bungee.hnmc.config.MainConfig;
import com.ericlam.mc.bungee.hnmc.managers.ChatFormatManager;
import com.ericlam.mc.bungee.hnmc.managers.PlayerManager;
import com.ericlam.mc.bungee.hnmc.managers.ResourceManager;
import com.ericlam.mc.bungee.hnmc.managers.SkinValueManager;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * 本服 API
 */
public interface HyperNiteAPI {

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
