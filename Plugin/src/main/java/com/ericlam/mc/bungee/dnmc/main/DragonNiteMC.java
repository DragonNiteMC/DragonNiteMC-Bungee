package com.ericlam.mc.bungee.dnmc.main;

import com.ericlam.mc.bungee.dnmc.DragonNiteAPI;
import com.ericlam.mc.bungee.dnmc.ModuleImplementor;
import com.ericlam.mc.bungee.dnmc.RedisDataSource;
import com.ericlam.mc.bungee.dnmc.SQLDataSource;
import com.ericlam.mc.bungee.dnmc.builders.function.ChatRunnerManager;
import com.ericlam.mc.bungee.dnmc.commands.*;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandRegister;
import com.ericlam.mc.bungee.dnmc.config.ConfigFactory;
import com.ericlam.mc.bungee.dnmc.config.MainConfig;
import com.ericlam.mc.bungee.dnmc.implement.ChatRunnerClicker;
import com.ericlam.mc.bungee.dnmc.listeners.*;
import com.ericlam.mc.bungee.dnmc.managers.*;
import com.ericlam.mc.bungee.dnmc.updater.DragonNiteResourceManager;
import com.ericlam.mc.bungee.dnmc.updater.SpigotResourceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.Optional;

public class DragonNiteMC extends Plugin implements DragonNiteAPI {

    public static Plugin plugin;

    private static DragonNiteAPI diPlugin;
    private static DNBungeeConfig dnBungeeConfig;
    private SQLDataSource dataSource;
    private ChatRunnerManager chatRunnerManager;
    private MainConfig mainConfig;
    private CommandRegister commandRegister;
    private PlayerManager playerManager;
    private ChatFormatManager chatFormatManager;
    private SkinValueManager skinValueManager;
    private RedisDataSource redisDataSource;
    private JoinServerListeners joinServerListeners;
    private JoinMeManager joinMeManager;
    private SpigotResourceManager spigotResourceManager;
    private DragonNiteResourceManager dragonNiteResourceManager;

    private final ModuleImplementor moduleImplementor = new ModuleImplementor();

    public static DNBungeeConfig getDnBungeeConfig() {
        return dnBungeeConfig;
    }

    public static DragonNiteAPI getAPI() {
        return diPlugin;
    }

    private void register(Class c, Object o) {
        moduleImplementor.register(c, o);
    }

    @Override
    public void onLoad() {
        diPlugin = this;
        register(Plugin.class, this);
        register(DragonNiteAPI.class, this);
        Injector injector = Guice.createInjector(moduleImplementor);
        dataSource = injector.getInstance(SQLDataSource.class);
        chatRunnerManager = injector.getInstance(ChatRunnerManager.class);
        commandRegister = injector.getInstance(CommandRegister.class);
        plugin = injector.getInstance(Plugin.class);
        playerManager = injector.getInstance(PlayerManager.class);
        chatFormatManager = injector.getInstance(ChatFormatManager.class);
        mainConfig = injector.getInstance(MainConfig.class);
        skinValueManager = injector.getInstance(SkinValueManager.class);
        joinMeManager = injector.getInstance(JoinMeManager.class);
        joinServerListeners = injector.getInstance(JoinServerListeners.class);
        dnBungeeConfig = (DNBungeeConfig) mainConfig;
        spigotResourceManager = injector.getInstance(SpigotResourceManager.class);
        dragonNiteResourceManager = injector.getInstance(DragonNiteResourceManager.class);
        if (dnBungeeConfig.getDatabaseConfig().Redis.enabled){
            redisDataSource = injector.getInstance(RedisDataSource.class);
        }
    }

    @Override
    public void onEnable() {

        ((PrefixManager)chatFormatManager).setUpLuckPermsAPI(LuckPermsProvider.get());
        OfflinePlayerManager manager = (OfflinePlayerManager)playerManager;
        this.getProxy().getScheduler().runAsync(this, manager::createTable);
        PluginManager pluginManager = this.getProxy().getPluginManager();
        pluginManager.registerCommand(this, new JoinMeCommand(joinMeManager, getDnBungeeConfig().getJoinMeConfig()));
        pluginManager.registerCommand(this, new PingCommand());
        pluginManager.registerCommand(this, new HubCommand());
        DragonNiteMC.getAPI().getCommandRegister().registerCommand(this, new DNMCBCommand());
        DragonNiteMC.getAPI().getCommandRegister().registerCommand(this, new ReloadChatFilterCommand());
        pluginManager.registerListener(this, joinServerListeners);
        pluginManager.registerListener(this, new AvoidKickListeners());
        pluginManager.registerListener(this, new PlayerChatListeners());
        pluginManager.registerListener(this, new PlayerDataListener(manager));
        pluginManager.registerListener(this, (ChatRunnerClicker)chatRunnerManager);
        pluginManager.registerListener(this, new VersionUpdateListener(this));

        this.getLogger().info("DragonNiteMC-Bungee v" + this.getDescription().getVersion() + " has been enabled.");
    }

    @Override
    public SQLDataSource getSQLDataSource() {
        return dataSource;
    }

    @Override
    public RedisDataSource getRedisDataSource() {
        return Optional.ofNullable(redisDataSource).orElseThrow(()-> new IllegalStateException("Redis has not enabled in config."));
    }

    @Override
    public ChatRunnerManager getChatRunnerManager() {
        return chatRunnerManager;
    }

    @Override
    public CommandRegister getCommandRegister() {
        return commandRegister;
    }

    @Override
    public MainConfig getMainConfig() {
        return mainConfig;
    }

    @Override
    public ConfigFactory getConfigFactory(Plugin plugin) {
        return new ConfigBuilder(plugin);
    }

    @Override
    public ChatFormatManager getChatFormatManager() {
        return chatFormatManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    @Override
    public SkinValueManager getSkinValueManager() {
        return skinValueManager;
    }

    @Override
    public ResourceManager getResourceManager(ResourceManager.Type type) {
        return type == ResourceManager.Type.SPIGOT ? spigotResourceManager : dragonNiteResourceManager;
    }
}
