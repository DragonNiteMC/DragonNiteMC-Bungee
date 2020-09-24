package com.ericlam.mc.bungee.hnmc.commands.caxerx.functional;

import net.md_5.bungee.api.CommandSender;

import java.util.List;

/**
 * @author Eric Lam
 * @see com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNodeBuilder#tabComplete(TabCompleter)
 */
@FunctionalInterface
public interface TabCompleter {
    List<String> executeTabCompletion(CommandSender sender, List<String> args);
}
