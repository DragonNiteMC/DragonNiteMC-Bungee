package com.ericlam.mc.bungee.hnmc.commands;

import com.ericlam.mc.bungee.hnmc.commands.caxerx.DefaultCommand;
import com.ericlam.mc.bungee.hnmc.commands.hnmcbs.UpdateChatFormatCommand;
import com.ericlam.mc.bungee.hnmc.commands.hnmcbs.skins.SkinMainCommand;
import com.ericlam.mc.bungee.hnmc.permission.Perm;

public class HNMCBCommand extends DefaultCommand {

    public HNMCBCommand() {
        super(null, "hnmcb", Perm.ADMIN, "hnmc 主指令", "hypernitemcbungee", "hnmcbungee");
        this.addSub(new UpdateChatFormatCommand(this));
        this.addSub(new SkinMainCommand(this));
    }
}
