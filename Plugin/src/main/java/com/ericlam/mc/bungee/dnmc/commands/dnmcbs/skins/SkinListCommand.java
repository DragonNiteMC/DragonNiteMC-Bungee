package com.ericlam.mc.bungee.dnmc.commands.dnmcbs.skins;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.main.DragoniteMC;
import com.ericlam.mc.bungee.dnmc.managers.PlayerSkinManager;
import com.ericlam.mc.bungee.dnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class SkinListCommand extends CommandNode {

    public SkinListCommand(CommandNode parent) {
        super(parent, "list", Perm.ADMIN, "列出皮膚快取內的資料", null);
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        PlayerSkinManager skinManager = (PlayerSkinManager) DragoniteMC.getAPI().getSkinValueManager();
        new MessageBuilder(DragoniteMC.getAPI().getMainConfig().getPrefix()+"&a快取資料如下: ").nextLine().add(skinManager.getSkinList().toString()).sendPlayer(sender);
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
