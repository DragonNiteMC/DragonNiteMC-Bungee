package com.ericlam.mc.bungee.hnmc.commands.caxerx.functional;

import net.md_5.bungee.api.CommandSender;

import java.util.List;

@FunctionalInterface
public interface AdvTabCompleter<D extends CommandSender> {
    List<String> tabComplete(D sender, List<String> args);
}
