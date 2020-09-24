package com.ericlam.mc.bungee.hnmc.commands.caxerx;


import com.ericlam.mc.bungee.hnmc.commands.caxerx.exception.CommandArgumentException;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.exception.CommandPermissionException;
import com.ericlam.mc.bungee.hnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author caxerx
 */
public abstract class CommandNode {

    private String description;
    private String placeholder;
    private String command;


    private ArrayList<CommandNode> subCommands = new ArrayList<>();


    private ArrayList<String> alias = new ArrayList<>();

    private String permission;


    private CommandNode parent;


    /**
     * @param parent      父類節點
     * @param command     指令
     * @param permission  權限
     * @param description 介紹
     * @param placeholder       用法
     * @param alias       縮寫
     */
    public CommandNode(CommandNode parent, String command, String permission, String description, String placeholder, String... alias) {
        this.parent = parent;
        this.command = command;
        this.alias.add(command);
        this.alias.addAll(List.of(alias));
        this.permission = permission;
        this.description = description;
        this.placeholder = placeholder;
    }

    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public ArrayList<CommandNode> getSubCommands() {
        return subCommands;
    }

    public CommandNode getParent() {
        return parent;
    }

    void setParent(CommandNode node) {
        this.parent = node;
    }

    public ArrayList<String> getAlias() {
        return alias;
    }

    public String getPermission() {
        return permission;
    }

    @Deprecated
    public void addAlias(String ali) {
        if (!alias.contains(ali)) {
            this.alias.add(ali);
        }
    }

    @Deprecated
    public void addAllAliases(List<String> aliases) {
        aliases.forEach(ali -> {
            if (!alias.contains(ali)) {
                alias.add(ali);
            }
        });
    }

    /**
     * @param c 分支指令
     */
    public void addSub(CommandNode c) {
        subCommands.add(c);
    }

    /**
     * @param sender 指令發送者
     * @param args   指令參數
     */
    public abstract void executeCommand(CommandSender sender, List<String> args);

    /**
     * @param sender 指令發送者
     * @param args   指令參數
     * @return Tab 列
     */
    public abstract List<String> executeTabCompletion(CommandSender sender, List<String> args);

    public void invokeCommand(CommandSender sender, List<String> args) {
        if (args == null || args.size() == 0) {
            if (permission == null || Perm.hasPermission(sender, permission)) {
                if (this.getPlaceholder() != null) {
                    String[] placeholders = Arrays.stream(this.getPlaceholder().split(" ")).filter(holder -> holder.startsWith("<") && holder.endsWith(">")).toArray(String[]::new);
                    if (placeholders.length > 0) {
                        throw new CommandArgumentException(String.join(" ", placeholders));
                    }
                }
                executeCommand(sender, args);
                return;
            } else {
                throw new CommandPermissionException(permission);
            }
        }

        for (CommandNode subCommand : subCommands) {
            if (subCommand.match(args.get(0))) {
                List<String> passArg = new ArrayList<>(args);
                passArg.remove(0);
                if (subCommand.getPlaceholder() != null) {
                    String[] placeholders = Arrays.stream(subCommand.getPlaceholder().split(" ")).filter(holder -> holder.startsWith("<") && holder.endsWith(">")).toArray(String[]::new);
                    if (passArg.size() < placeholders.length) {
                        throw new CommandArgumentException(String.join(" ", placeholders));
                    }
                }

                subCommand.invokeCommand(sender, passArg);
                return;
            }
        }

        if (permission == null || Perm.hasPermission(sender, permission)) {
            if (this.getPlaceholder() != null) {
                String[] placeholders = Arrays.stream(this.getPlaceholder().split(" ")).filter(holder -> holder.startsWith("<") && holder.endsWith(">")).toArray(String[]::new);
                if (args.size() < placeholders.length) {
                    throw new CommandArgumentException(String.join(" ", placeholders));
                }
            }
            executeCommand(sender, args);
        } else {
            throw new CommandPermissionException(permission);
        }
    }

    public List<String> invokeTabCompletion(CommandSender sender, List<String> args) {
        if (args == null || args.size() == 0) {
            if (permission != null && Perm.hasPermission(sender, permission)) {
                return executeTabCompletion(sender, args);
            } else {
                throw new CommandPermissionException(permission);
            }
        }

        for (CommandNode subCommand : subCommands) {
            if (subCommand.match(args.get(0))) {
                List<String> passArg = new ArrayList<>(args);
                passArg.remove(0);
                return subCommand.invokeTabCompletion(sender, passArg);
            }
        }

        if (permission == null || Perm.hasPermission(sender, permission)) {
            return executeTabCompletion(sender, args);
        } else {
            throw new CommandPermissionException(permission);
        }
    }

    public boolean match(String args) {
        for (String ali : alias) {
            if (args.equalsIgnoreCase(ali)) {
                return true;
            }
        }
        return false;
    }
}
