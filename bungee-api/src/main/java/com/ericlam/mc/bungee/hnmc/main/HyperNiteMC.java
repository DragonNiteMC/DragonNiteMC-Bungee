package com.ericlam.mc.bungee.hnmc.main;

import com.ericlam.mc.bungee.hnmc.HyperNiteAPI;
import com.ericlam.mc.bungee.hnmc.RedisDataSource;
import com.ericlam.mc.bungee.hnmc.SQLDataSource;
import com.ericlam.mc.bungee.hnmc.builders.function.ChatRunnerManager;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandRegister;
import com.ericlam.mc.bungee.hnmc.config.ConfigFactory;
import com.ericlam.mc.bungee.hnmc.config.MainConfig;
import com.ericlam.mc.bungee.hnmc.managers.ChatFormatManager;
import com.ericlam.mc.bungee.hnmc.managers.PlayerManager;
import com.ericlam.mc.bungee.hnmc.managers.SkinValueManager;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * 從這裏獲取所有 API
 */
public class HyperNiteMC implements HyperNiteAPI {

    public static HyperNiteAPI getAPI() {
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
}
