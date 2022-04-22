package com.ericlam.mc.bungee.dnmc.builders;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class AdvMessageBuilder {
    private TextComponent textComponent;

    public AdvMessageBuilder(String msg) {
        String amsg = ChatColor.translateAlternateColorCodes('&', msg);
        textComponent = new TextComponent(amsg);
    }

    public AdvMessageBuilder add(String... msg) {
        for (String s : msg) {
            String m = ChatColor.translateAlternateColorCodes('&', s);
            textComponent.addExtra(m);
        }
        return this;
    }

    public AdvMessageBuilder add(BaseComponent baseComponent) {
        textComponent.addExtra(baseComponent);
        return this;
    }

    public AdvMessageBuilder add(MessageBuilder builder) {
        for (BaseComponent component : builder.build()) {
            textComponent.addExtra(component);
        }
        return this;
    }

    public AdvMessageBuilder nextLine() {
        textComponent.addExtra("\n");
        return this;
    }

    public TextComponent build() {
        return textComponent;
    }

    public void sendPlayer(CommandSender player) {
        player.sendMessage(textComponent);
    }


}
