package com.ericlam.mc.bungee.dnmc.commands.dnmcbs.skins;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.container.OfflinePlayer;
import com.ericlam.mc.bungee.dnmc.main.DragoniteMC;
import com.ericlam.mc.bungee.dnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkinDropCommand extends CommandNode {

    public SkinDropCommand(CommandNode parent) {
        super(parent, "drop", Perm.ADMIN, "刪除皮膚", "<player>", "delete", "remove");
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        String name = args.get(0);
        DragoniteMC.getAPI().getPlayerManager().getOfflinePlayer(name).thenComposeAsync((off)->{
            if (off.isEmpty()){
                return CompletableFuture.failedFuture(new IllegalStateException());
            }

            OfflinePlayer player = off.get();
            return DragoniteMC.getAPI().getSkinValueManager().dropSkin(player.getUniqueId());
        }).whenComplete(((aBoolean, throwable) -> {
            if (throwable != null){
                MessageBuilder.sendMessage(sender, DragoniteMC.getAPI().getMainConfig().getNoThisPlayer());
                return;
            }
            MessageBuilder.sendMessage(sender, DragoniteMC.getAPI().getMainConfig().getPrefix()+ "§a刪除成功。");
        }));
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
