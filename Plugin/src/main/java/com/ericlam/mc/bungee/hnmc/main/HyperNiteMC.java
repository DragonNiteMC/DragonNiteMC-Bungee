package com.ericlam.mc.bungee.hnmc.main;

import com.ericlam.mc.bungee.hnmc.HyperNiteAPI;
import com.ericlam.mc.bungee.hnmc.ModuleImplementor;
import com.ericlam.mc.bungee.hnmc.RedisDataSource;
import com.ericlam.mc.bungee.hnmc.SQLDataSource;
import com.ericlam.mc.bungee.hnmc.builders.function.ChatRunnerManager;
import com.ericlam.mc.bungee.hnmc.commands.*;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandRegister;
import com.ericlam.mc.bungee.hnmc.config.ConfigFactory;
import com.ericlam.mc.bungee.hnmc.config.MainConfig;
import com.ericlam.mc.bungee.hnmc.implement.ChatRunnerClicker;
import com.ericlam.mc.bungee.hnmc.listeners.*;
import com.ericlam.mc.bungee.hnmc.managers.*;
import com.ericlam.mc.bungee.hnmc.updater.HyperNiteResourceManager;
import com.ericlam.mc.bungee.hnmc.updater.SpigotResourceManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.Optional;

public class HyperNiteMC extends Plugin implements HyperNiteAPI {

    public static Plugin plugin;

    private static HyperNiteAPI diPlugin;
    private static HNBungeeConfig hnBungeeConfig;
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
    private HyperNiteResourceManager hyperNiteResourceManager;

    private final ModuleImplementor moduleImplementor = new ModuleImplementor();

    public static HNBungeeConfig getHnBungeeConfig() {
        return hnBungeeConfig;
    }

    public static HyperNiteAPI getAPI() {
        return diPlugin;
    }

    private void register(Class c, Object o) {
        moduleImplementor.register(c, o);
    }

    @Override
    public void onLoad() {
        diPlugin = this;
        register(Plugin.class, this);
        register(HyperNiteAPI.class, this);
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
        hnBungeeConfig = (HNBungeeConfig) mainConfig;
        spigotResourceManager = injector.getInstance(SpigotResourceManager.class);
        hyperNiteResourceManager = injector.getInstance(HyperNiteResourceManager.class);
        if (hnBungeeConfig.getDatabaseConfig().Redis.enabled){
            redisDataSource = injector.getInstance(RedisDataSource.class);
        }
    }

    @Override
    public void onEnable() {

        ((PrefixManager)chatFormatManager).setUpLuckPermsAPI(LuckPermsProvider.get());
        OfflinePlayerManager manager = (OfflinePlayerManager)playerManager;
        this.getProxy().getScheduler().runAsync(this, manager::createTable);
        PluginManager pluginManager = this.getProxy().getPluginManager();
        pluginManager.registerCommand(this, new JoinMeCommand(joinMeManager, getHnBungeeConfig().getJoinMeConfig()));
        pluginManager.registerCommand(this, new PingCommand());
        pluginManager.registerCommand(this, new HubCommand());
        HyperNiteMC.getAPI().getCommandRegister().registerCommand(this, new HNMCBCommand());
        HyperNiteMC.getAPI().getCommandRegister().registerCommand(this, new ReloadChatFilterCommand());
        pluginManager.registerListener(this, joinServerListeners);
        pluginManager.registerListener(this, new AvoidKickListeners());
        pluginManager.registerListener(this, new PlayerLimitListeners());
        pluginManager.registerListener(this, new PlayerDataListener(manager));
        pluginManager.registerListener(this, (ChatRunnerClicker)chatRunnerManager);
        pluginManager.registerListener(this, new VersionUpdateListener(this));

        this.getLogger().info("HyperNiteMC-Bungee v" + this.getDescription().getVersion() + " has been enabled.");
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
        return type == ResourceManager.Type.SPIGOT ? spigotResourceManager : hyperNiteResourceManager;
    }
}
