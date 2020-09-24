package com.ericlam.mc.bungee.hnmc.commands.caxerx;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * 幫助訊息建造器
 *
 * @author caxerx
 */
class HelpOutputBuilder {
    private ComponentBuilder layout;
    private String mainCommand;

    HelpOutputBuilder(CommandNode mainCommandNode) {
        StringBuilder mainCommandBuilder = new StringBuilder(mainCommandNode.getAlias().get(0));
        CommandNode topNode = mainCommandNode;
        while (topNode.getParent() != null) {
            topNode = topNode.getParent();
            mainCommandBuilder.insert(0, topNode.getAlias().get(0) + " ");
        }
        mainCommand = mainCommandBuilder.toString();
        layout = new ComponentBuilder("");
        layout.append("============[ ").color(ChatColor.GRAY).append("/" + mainCommand + " 指令幫助").color(ChatColor.AQUA).append(" ]============").color(ChatColor.GRAY).append("\n").color(ChatColor.RESET);
    }

    HelpOutputBuilder append(CommandNode res) {
        // /cs list - description of jj
        // /cs info <scroll_name> description of jj
        String cmd = "/" + mainCommand + " ";
        for (String ali : res.getAlias()) {
            if (ali.contains(" ")) {
                continue;
            }
            cmd += ali;
            break;
        }
        ClickEvent event;
        HoverEvent hover;
        if (res.getPlaceholder() != null) {
            cmd = cmd + " " + res.getPlaceholder();
            event = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd);
            hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§a點擊以輸入"));
        } else {
            event = new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd);
            hover = new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§a點擊以執行"));
        }

        layout.color(ChatColor.WHITE).append(new ComponentBuilder(cmd).event(event).event(hover).create()).append(" - ").color(ChatColor.GRAY).append(res.getDescription()).color(ChatColor.YELLOW).append("\n").reset();
        return this;
    }


    TextComponent build() {
        return new TextComponent(layout.create());
    }

}
