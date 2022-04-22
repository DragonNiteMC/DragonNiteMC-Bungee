package com.ericlam.mc.bungee.dnmc.listeners;

import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class PlayerChatListeners implements Listener {

    public PlayerChatListeners() {
    }

    @EventHandler
    public void onPlayerChat(final ChatEvent e){
        List<String> filterList = DragonNiteMC.getDnBungeeConfig().getFilter().filterList;
        String messages = e.getMessage();
        if (e.isCommand()) return;
        for (String word : filterList) {
            messages = messages.replaceAll(word,"*".repeat(word.length()));
        }
        e.setMessage(messages);
    }
}
