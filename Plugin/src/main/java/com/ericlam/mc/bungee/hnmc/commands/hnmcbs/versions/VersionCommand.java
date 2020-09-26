package com.ericlam.mc.bungee.hnmc.commands.hnmcbs.versions;

import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.DefaultCommand;
import com.ericlam.mc.bungee.hnmc.permission.Perm;

public class VersionCommand extends DefaultCommand {

    public VersionCommand(CommandNode parent) {
        super(parent, "versions", Perm.DEVELOPER, "插件版本指令", "ver", "version");
        this.addSub(new VersionCheckCommand(this));
        this.addSub(new VersionFetchCommand(this));
        this.addSub(new VersionUpdateCommand(this));
    }
}
