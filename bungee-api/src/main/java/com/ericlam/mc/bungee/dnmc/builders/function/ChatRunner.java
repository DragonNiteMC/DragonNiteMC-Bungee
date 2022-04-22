package com.ericlam.mc.bungee.dnmc.builders.function;

import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @see com.ericlam.mc.bungee.dnmc.builders.MessageBuilder#run(ChatRunner)
 * @see com.ericlam.mc.bungee.dnmc.builders.MessageBuilder#runClicks(int, ChatRunner)
 * @see com.ericlam.mc.bungee.dnmc.builders.MessageBuilder#runTimeout(int, ChatRunner)
 */
@FunctionalInterface
public interface ChatRunner {
    void run(ProxiedPlayer player);
}
