package com.ericlam.mc.bungee.dnmc.commands;

import com.ericlam.mc.bungee.dnmc.commands.caxerx.DefaultCommand;
import com.ericlam.mc.bungee.dnmc.commands.dnmcbs.UpdateChatFormatCommand;
import com.ericlam.mc.bungee.dnmc.commands.dnmcbs.skins.SkinMainCommand;
import com.ericlam.mc.bungee.dnmc.commands.dnmcbs.versions.VersionCommand;
import com.ericlam.mc.bungee.dnmc.permission.Perm;

public class DNMCBCommand extends DefaultCommand {

    public DNMCBCommand() {
        super(null, "dnmcb", Perm.ADMIN, "dnmc 主指令", "dragonitemcbungee", "dnmcbungee");
        this.addSub(new UpdateChatFormatCommand(this));
        this.addSub(new SkinMainCommand(this));
        this.addSub(new VersionCommand(this));
    }
}
