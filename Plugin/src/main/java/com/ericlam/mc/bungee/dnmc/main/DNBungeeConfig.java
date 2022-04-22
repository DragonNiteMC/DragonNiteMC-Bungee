package com.ericlam.mc.bungee.dnmc.main;

import com.ericlam.mc.bungee.dnmc.DragonNiteAPI;
import com.ericlam.mc.bungee.dnmc.config.*;
import com.google.inject.Inject;
import net.md_5.bungee.api.plugin.Plugin;

public class DNBungeeConfig implements MainConfig {


    private final YamlManager yamlManager;

    public static LangConfig langConfig;

    @Inject
    public DNBungeeConfig(Plugin plugin, DragonNiteAPI api) {
        yamlManager = api.getConfigFactory(plugin)
                .register(AvoidKickConfig.class)
                .register(ChatFilterConfig.class)
                .register(DatabaseConfig.class)
                .register(JoinMeConfig.class)
                .register(LangConfig.class)
                .register(PlayerLimitConfig.class)
                .register(VersionCheckerConfig.class)
                .dump();
        langConfig = yamlManager.getConfigAs(LangConfig.class);
    }

    public AvoidKickConfig getAvoid_back() {
        return yamlManager.getConfigAs(AvoidKickConfig.class);
    }

    public ChatFilterConfig getFilter() {
        return yamlManager.getConfigAs(ChatFilterConfig.class);
    }

    public DatabaseConfig getDatabaseConfig(){
        return yamlManager.getConfigAs(DatabaseConfig.class);
    }

    public JoinMeConfig getJoinMeConfig(){
        return yamlManager.getConfigAs(JoinMeConfig.class);
    }

    public PlayerLimitConfig getLimit() {
        return yamlManager.getConfigAs(PlayerLimitConfig.class);
    }

    public VersionCheckerConfig getVersionChecker(){
        return yamlManager.getConfigAs(VersionCheckerConfig.class);
    }

    public void reloadChatFilter() {
        yamlManager.getConfigAs(ChatFilterConfig.class).reload();
    }

    @Override
    public String getPrefix() {
        return langConfig.getPrefix();
    }

    @Override
    public String getNotPlayer() {
        return langConfig.getPure("not-player");
    }

    @Override
    public String getNoPermission() {
        return langConfig.getPure("no-perm");
    }

    @Override
    public String getNoThisPlayer() {
        return langConfig.getPure("no-this-player");
    }

    @Override
    public void reloadConfig() {
        yamlManager.reloadConfigs();
    }

}
