package com.ericlam.mc.bungee.dnmc.commands.caxerx.functional;

import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNodeBuilder;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

/**
 * @see CommandNodeBuilder
 */
@FunctionalInterface
public interface AdvCmdExecutor<D extends CommandSender> {
    void execute(D sender, List<String> args);
}
