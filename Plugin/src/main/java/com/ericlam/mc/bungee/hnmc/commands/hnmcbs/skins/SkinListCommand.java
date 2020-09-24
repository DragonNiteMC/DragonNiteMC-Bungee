package com.ericlam.mc.bungee.hnmc.commands.hnmcbs.skins;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.ericlam.mc.bungee.hnmc.managers.PlayerSkinManager;
import com.ericlam.mc.bungee.hnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class SkinListCommand extends CommandNode {

    public SkinListCommand(CommandNode parent) {
        super(parent, "list", Perm.ADMIN, "列出皮膚快取內的資料", null);
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        PlayerSkinManager skinManager = (PlayerSkinManager) HyperNiteMC.getAPI().getSkinValueManager();
        new MessageBuilder(HyperNiteMC.getAPI().getMainConfig().getPrefix()+"&a快取資料如下: ").nextLine().add(skinManager.getSkinList().toString()).sendPlayer(sender);
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
