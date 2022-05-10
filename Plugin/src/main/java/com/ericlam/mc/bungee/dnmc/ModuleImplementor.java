package com.ericlam.mc.bungee.dnmc;

import com.ericlam.mc.bungee.dnmc.builders.function.ChatRunnerManager;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandRegister;
import com.ericlam.mc.bungee.dnmc.config.MainConfig;
import com.ericlam.mc.bungee.dnmc.implement.ChatRunnerClicker;
import com.ericlam.mc.bungee.dnmc.implement.caxerx.command.CommandExecutor;
import com.ericlam.mc.bungee.dnmc.listeners.JoinServerListeners;
import com.ericlam.mc.bungee.dnmc.main.DNBungeeConfig;
import com.ericlam.mc.bungee.dnmc.managers.*;
import com.ericlam.mc.bungee.dnmc.updater.DragoniteResourceManager;
import com.ericlam.mc.bungee.dnmc.updater.SpigotResourceManager;
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
        binder.bind(MainConfig.class).to(DNBungeeConfig.class).in(Scopes.SINGLETON);

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
        binder.bind(DragoniteResourceManager.class).in(Scopes.SINGLETON);

        preImplement.forEach((cls,obj)->binder.bind(cls).toInstance(obj));
    }

    public void register(Class cls, Object obj){
        preImplement.put(cls,obj);
    }
}
