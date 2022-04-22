package com.ericlam.mc.bungee.dnmc.commands.caxerx;

import net.md_5.bungee.api.plugin.Plugin;

/**
 * 指令註冊器
 *
 * @author caxerx
 */
public interface CommandRegister {

    /**
     * 主指令必須沒有 parent (父類) 指令
     *
     * @param plugin 插件
     * @param node 主指令
     */
    void registerCommand(Plugin plugin, CommandNode node);

}
