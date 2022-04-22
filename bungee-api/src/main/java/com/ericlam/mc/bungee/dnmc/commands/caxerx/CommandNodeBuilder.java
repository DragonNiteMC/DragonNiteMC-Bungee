package com.ericlam.mc.bungee.dnmc.commands.caxerx;

import com.ericlam.mc.bungee.dnmc.commands.caxerx.exception.NotExecutableException;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.functional.CmdExecutor;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.functional.TabCompleter;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

/**
 * 指令節點建造器
 *
 * @author Eric Lam
 * @see CommandNode
 */
public class CommandNodeBuilder {

    private String command;
    private String permission;
    private String description;
    private String placeholder;
    private CommandNode parent;
    private TabCompleter tabCompleter;
    private CmdExecutor cmdExecutor;
    private String[] alias = new String[0];

    /**
     * @param command 指令
     */
    public CommandNodeBuilder(String command) {
        this.command = command;
    }

    /**
     * @param alias 縮寫
     * @return this
     */
    public CommandNodeBuilder alias(String... alias) {
        this.alias = alias;
        return this;
    }

    /**
     * @param permission 權限
     * @return this
     */
    public CommandNodeBuilder permission(String permission) {
        this.permission = permission;
        return this;
    }

    /**
     * @param description 介紹
     * @return this
     */
    public CommandNodeBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * @param placeholder 用法
     * @return this
     */
    public CommandNodeBuilder placeholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    /**
     * @param parent 父類指令
     * @return this
     */
    public CommandNodeBuilder parent(CommandNode parent) {
        this.parent = parent;
        return this;
    }

    /**
     * @param executor tab 執行
     * @return this
     */
    public CommandNodeBuilder tabComplete(TabCompleter executor) {
        this.tabCompleter = executor;
        return this;
    }

    /**
     * @param cmdExecutor 指令執行
     * @return this
     */
    public CommandNodeBuilder execute(CmdExecutor cmdExecutor) {
        this.cmdExecutor = cmdExecutor;
        return this;
    }

    /**
     * @return 指令節點
     * @throws NotExecutableException     指令無法執行
     */
    public CommandNode build() {
        if (cmdExecutor == null) throw new NotExecutableException(command);
        return new CommandNode(parent, command, permission, description, placeholder, alias) {
            @Override
            public void executeCommand(CommandSender sender, List<String> args) {
                cmdExecutor.executeCommand(sender, args);
            }

            @Override
            public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
                return tabCompleter == null ? null : tabCompleter.executeTabCompletion(sender, args);
            }
        };
    }

}
//如何使用
/*
class CommandNodeUse{
    CommandNode getNode(){
        return new CommandNodeBuilder("hello")
                .description("向一個人 say hello")
                .parent(null)
                .placeholder("<player>")
                .tabComplete((sender, args) -> null)
                .permission("say.hello").execute((sender, args) -> {
                    String name = args.get(0);
                    ProxiedPlayer target = ProxyServer.getInstance().getPlayer(name);
                    if (target == null){
                        MessageBuilder.sendMessage(sender,"對方不在線!");
                        return;
                    }
                    MessageBuilder.sendMessage(sender,"已向 "+name+" say hello !");
                    MessageBuilder.sendMessage(target,sender.getName()+" 向你 say hello 了!");
                }).build();
    }
}

 */



