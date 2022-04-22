package com.ericlam.mc.bungee.dnmc.builders;

import com.ericlam.mc.bungee.dnmc.builders.function.ChatRunner;
import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;

import java.util.UUID;

/**
 * 訊息建造器
 *
 * @see AdvMessageBuilder
 */
public class MessageBuilder {
    private ComponentBuilder componentBuilder;
    private UUID id;
    private ChatRunner runner;
    private int timeoutSeconds = -1;
    private int timeoutClicks = -1;

    /**
     *
     * @param msg 原始訊息
     */
    public MessageBuilder(String msg) {
        String amsg = ChatColor.translateAlternateColorCodes('&', msg);
        componentBuilder = new ComponentBuilder(amsg);
    }

    public MessageBuilder() {
        componentBuilder = new ComponentBuilder("");
    }

    /**
     *
     * @param msgs 原始訊息串
     */
    public MessageBuilder(String... msgs) {
        componentBuilder = new ComponentBuilder("");
        for (int i = 0; i < msgs.length; i++) {
            String msg = ChatColor.translateAlternateColorCodes('&', msgs[i]);
            componentBuilder.append(TextComponent.fromLegacyText(msg));
            if (i != msgs.length - 1) {
                componentBuilder.append("\n");
            }
        }
    }

    /**
     *
     * @param sender 指令發送者
     * @param message 訊息
     */
    public static void sendMessage(CommandSender sender, String message) {
        String msg = ChatColor.translateAlternateColorCodes('&', message);
        sender.sendMessage(TextComponent.fromLegacyText(msg));
    }

    /**
     *
     * @param msg 訊息
     * @return this
     */
    public MessageBuilder add(String msg) {
        String amsg = ChatColor.translateAlternateColorCodes('&', msg);
        componentBuilder.append(TextComponent.fromLegacyText(amsg));
        return this;
    }

    /**
     *
     * @param msgs 訊息串
     * @return this
     */
    public MessageBuilder add(String... msgs) {
        for (int i = 0; i < msgs.length; i++) {
            String msg = ChatColor.translateAlternateColorCodes('&', msgs[i]);
            componentBuilder.append(TextComponent.fromLegacyText(msg));
            if (i != msgs.length - 1) {
                componentBuilder.append("\n");
            }
        }
        return this;
    }

    /**
     * 新增可點擊網址
     * @param website 網址
     * @return this
     */
    public MessageBuilder url(String website) {
        componentBuilder.event(new ClickEvent(ClickEvent.Action.OPEN_URL, website));
        return this;
    }

    /**
     * 新增可點擊預輸入指令
     * @param command 指令
     * @return this
     */
    public MessageBuilder suggest(String command) {
        componentBuilder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return this;
    }

    /**
     * 新增可點擊運行指令
     * @param command 指令
     * @return this
     */
    public MessageBuilder command(String command) {
        componentBuilder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return this;
    }

    /**
     * 新增可點擊翻頁
     * @param page 頁數
     * @return this
     */
    public MessageBuilder page(String page) {
        componentBuilder.event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, page));
        return this;
    }

    /**
     * 新增可見氣泡文字
     * @param texts 文字串
     * @return this
     */
    public MessageBuilder hoverText(String... texts) {
        ComponentBuilder builder = new ComponentBuilder("");
        for (int i = 0; i < texts.length; i++) {
            String msg = ChatColor.translateAlternateColorCodes('&', texts[i]);
            builder.append(msg);
            if (i != texts.length - 1) {
                builder.append("\n");
            }
        }
        componentBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, builder.create()));
        return this;
    }

    /**
     * 新增可見成就訊息
     * @param achievementNode 成就節點
     * @return this
     */
    public MessageBuilder showAdvancement(String achievementNode) {
        String value = "\"value\":" + achievementNode;
        componentBuilder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(value)));
        return this;
    }

    /**
     * 新增shift click 文字
     * @param insert 文字
     * @return this
     */
    public MessageBuilder insertWhenShiftClick(String insert) {
        componentBuilder.insertion(insert);
        return this;
    }

    /**
     * 換行
     * @return this
     */
    public MessageBuilder nextLine() {
        componentBuilder.append("\n");
        return this;
    }

    /**
     * 默認為 十分鐘 之後自動過期
     *
     * @param runner 運行函式
     * @return this
     */
    public MessageBuilder run(ChatRunner runner) {
        this.id = UUID.randomUUID();
        this.runner = runner;
        this.timeoutSeconds = 600;
        this.timeoutClicks = -1;
        return this;
    }

    /**
     * @param timeoutClicks 點擊多少次後自動過期
     * @param runner 運行函式
     * @return this
     */
    public MessageBuilder runClicks(int timeoutClicks, ChatRunner runner) {
        this.id = UUID.randomUUID();
        this.runner = runner;
        this.timeoutClicks = timeoutClicks;
        this.timeoutSeconds = -1;
        return this;
    }

    /**
     * @param timeoutSeconds 多少秒後失效
     * @param runner         運行函式
     * @return this
     */
    public MessageBuilder runTimeout(int timeoutSeconds, ChatRunner runner) {
        this.id = UUID.randomUUID();
        this.runner = runner;
        this.timeoutSeconds = timeoutSeconds;
        this.timeoutClicks = -1;
        return this;
    }

    /**
     * @return 訊息
     */
    public BaseComponent[] build() {
        if (this.runner != null) {
            this.command("/command-run-bungee_" + id.toString());
            if (timeoutSeconds > 0) {
                DragonNiteMC.getAPI().getChatRunnerManager().registerTimeout(id, runner, timeoutSeconds);
            } else if (timeoutClicks > 0) {
                DragonNiteMC.getAPI().getChatRunnerManager().registerClicks(id, runner, timeoutClicks);
            }
        }
        return componentBuilder.create();
    }

    /**
     *
     * @param player 玩家
     */
    public void sendPlayer(CommandSender player) {
        player.sendMessage(this.build());
    }


}
//如何使用
/*
class MessageBuilderUse{
    void use(Player sender){
        BaseComponent[] msg = new MessageBuilder("&e[ 公告全世界你是女裝大佬 ]").hoverText("&a來啊點我啊").run(player -> {
            Bukkit.broadcastMessage(net.md_5.bungee.api.ChatColor.AQUA + player.getName()+" 是女裝大佬！！");
            BaseComponent[] tp = new MessageBuilder("&e[ 點我傳送到「她」的位置 ]").hoverText("&a點啊快點啊").run(player1 -> {
                player1.teleport(player);
            }).build();
            Bukkit.getOnlinePlayers().forEach(p->p.sendMessage(tp));
        }).build();
        sender.sendMessage(msg);
    }
}

 */
