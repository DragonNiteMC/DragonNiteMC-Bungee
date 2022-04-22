package com.ericlam.mc.bungee.dnmc.commands.dnmcbs.versions;

import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.DefaultCommand;
import com.ericlam.mc.bungee.dnmc.permission.Perm;

public class VersionCommand extends DefaultCommand {

    public VersionCommand(CommandNode parent) {
        super(parent, "versions", Perm.DEVELOPER, "插件版本指令", "ver", "version");
        this.addSub(new VersionCheckCommand(this));
        this.addSub(new VersionFetchCommand(this));
        this.addSub(new VersionUpdateCommand(this));
    }
}
