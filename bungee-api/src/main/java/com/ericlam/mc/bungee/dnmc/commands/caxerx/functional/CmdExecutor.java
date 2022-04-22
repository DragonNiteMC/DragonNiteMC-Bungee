package com.ericlam.mc.bungee.dnmc.commands.caxerx.functional;

import net.md_5.bungee.api.CommandSender;

import java.util.List;

/**
 * @author Eric Lam
 * @see com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNodeBuilder#execute(CmdExecutor)
 */
@FunctionalInterface
public interface CmdExecutor {
    void executeCommand(CommandSender sender, List<String> args);
}
