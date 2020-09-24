package com.ericlam.mc.bungee.hnmc.commands.hnmcbs.skins;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.ericlam.mc.bungee.hnmc.managers.PlayerSkinManager;
import com.ericlam.mc.bungee.hnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class SkinSearchCommand extends CommandNode {

    public SkinSearchCommand(CommandNode parent) {
        super(parent, "search", Perm.ADMIN, "尋找皮膚資料庫內是否有該玩家的資料", "<player>", "find");
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        String name = args.get(0);
        PlayerSkinManager skinManager = (PlayerSkinManager) HyperNiteMC.getAPI().getSkinValueManager();
        skinManager.containPlayerSkin(name).whenComplete(((aBoolean, throwable) -> {
            if (throwable != null){
                throwable.printStackTrace();
                return;
            }
            String msg = HyperNiteMC.getAPI().getMainConfig().getPrefix() + "§a皮膚資料庫" + (aBoolean ? "存在著" : "並不存在") + "這個玩家。";
            MessageBuilder.sendMessage(sender, msg);
        }));
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
