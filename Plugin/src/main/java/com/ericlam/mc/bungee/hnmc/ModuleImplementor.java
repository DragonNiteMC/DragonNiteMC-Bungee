package com.ericlam.mc.bungee.hnmc;

import com.ericlam.mc.bungee.hnmc.builders.function.ChatRunnerManager;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandRegister;
import com.ericlam.mc.bungee.hnmc.config.MainConfig;
import com.ericlam.mc.bungee.hnmc.implement.ChatRunnerClicker;
import com.ericlam.mc.bungee.hnmc.implement.caxerx.command.CommandExecutor;
import com.ericlam.mc.bungee.hnmc.listeners.JoinServerListeners;
import com.ericlam.mc.bungee.hnmc.main.HNBungeeConfig;
import com.ericlam.mc.bungee.hnmc.managers.*;
import com.ericlam.mc.bungee.hnmc.updater.HyperNiteResourceManager;
import com.ericlam.mc.bungee.hnmc.updater.SpigotResourceManager;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

import java.util.HashMap;
import java.util.Map;

public class ModuleImplementor implements Module {

    private final Map<Class,Object> preImplement = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public void configure(Binder binder) {
        binder.bind(MainConfig.class).to(HNBungeeConfig.class).in(Scopes.SINGLETON);

        binder.bind(CommandRegister.class).to(CommandExecutor.class).in(Scopes.SINGLETON);
        binder.bind(ChatRunnerManager.class).to(ChatRunnerClicker.class).in(Scopes.SINGLETON);
        binder.bind(SQLDataSource.class).to(DataSourceManager.class).in(Scopes.SINGLETON);
        binder.bind(PlayerManager.class).to(OfflinePlayerManager.class).in(Scopes.SINGLETON);
        binder.bind(ChatFormatManager.class).to(PrefixManager.class).in(Scopes.SINGLETON);
        binder.bind(SkinValueManager.class).to(PlayerSkinManager.class).in(Scopes.SINGLETON);
        binder.bind(RedisDataSource.class).to(RedisManager.class).in(Scopes.SINGLETON);

        /*
         * for singleton rather then api
         */

        binder.bind(JoinMeManager.class).in(Scopes.SINGLETON);
        binder.bind(JoinServerListeners.class).in(Scopes.SINGLETON);
        binder.bind(SpigotResourceManager.class).in(Scopes.SINGLETON);
        binder.bind(HyperNiteResourceManager.class).in(Scopes.SINGLETON);

        preImplement.forEach((cls,obj)->binder.bind(cls).toInstance(obj));
    }

    public void register(Class cls, Object obj){
        preImplement.put(cls,obj);
    }
}
