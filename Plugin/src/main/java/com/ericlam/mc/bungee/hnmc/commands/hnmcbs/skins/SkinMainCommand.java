package com.ericlam.mc.bungee.hnmc.commands.hnmcbs.skins;

import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.DefaultCommand;
import com.ericlam.mc.bungee.hnmc.permission.Perm;

public class SkinMainCommand extends DefaultCommand {
    public SkinMainCommand(CommandNode parent) {
        super(parent, "skin", Perm.ADMIN, "查看皮膚指令幫助");
        this.addSub(new SkinDropCommand(this));
        this.addSub(new SkinUpdateCommand(this));
        this.addSub(new SkinSearchCommand(this));
        this.addSub(new SkinListCommand(this));
    }
}
