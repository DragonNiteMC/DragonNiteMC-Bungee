package com.ericlam.mc.bungee.dnmc.main;

import com.ericlam.mc.bungee.dnmc.DragonNiteAPI;
import com.ericlam.mc.bungee.dnmc.RedisDataSource;
import com.ericlam.mc.bungee.dnmc.SQLDataSource;
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
 * 從這裏獲取所有 API
 */
public class DragonNiteMC implements DragonNiteAPI {

    public static DragonNiteAPI getAPI() {
        throw new RuntimeException("RUNTIME ERROR");
    }


    @Override
    public SQLDataSource getSQLDataSource() {
        return null;
    }

    @Override
    public RedisDataSource getRedisDataSource() {
        return null;
    }

    @Override
    public ChatRunnerManager getChatRunnerManager() {
        return null;
    }

    @Override
    public CommandRegister getCommandRegister() {
        return null;
    }

    @Override
    public MainConfig getMainConfig() {
        return null;
    }

    @Override
    public ConfigFactory getConfigFactory(Plugin plugin) {
        return null;
    }

    @Override
    public ChatFormatManager getChatFormatManager() {
        return null;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return null;
    }

    @Override
    public SkinValueManager getSkinValueManager() {
        return null;
    }

    @Override
    public ResourceManager getResourceManager(ResourceManager.Type type) {
        return null;
    }
}
