package com.ericlam.mc.bungee.dnmc.commands.dnmcbs.skins;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.dnmc.container.OfflinePlayer;
import com.ericlam.mc.bungee.dnmc.exceptions.SkinUpdateTooFastException;
import com.ericlam.mc.bungee.dnmc.main.DragoniteMC;
import com.ericlam.mc.bungee.dnmc.permission.Perm;
import net.md_5.bungee.api.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SkinUpdateCommand extends CommandNode {

    public SkinUpdateCommand(CommandNode parent) {
        super(parent, "update", Perm.ADMIN, "更新皮膚", "<player>", "refresh");
    }

    @Override
    public void executeCommand(CommandSender sender, List<String> args) {
        String name = args.get(0);
        DragoniteMC.getAPI().getPlayerManager().getOfflinePlayer(name).thenComposeAsync((off)->{
            if (off.isEmpty()){
                return CompletableFuture.failedFuture(new IllegalStateException());
            }

            OfflinePlayer player = off.get();
            try {
                return DragoniteMC.getAPI().getSkinValueManager().updateSkin(player.getUniqueId());
            } catch (SkinUpdateTooFastException e) {
                return CompletableFuture.failedFuture(e);
            }
        }).whenComplete(((skin, throwable) -> {
            if (throwable != null){
                if (throwable instanceof IllegalStateException){
                    MessageBuilder.sendMessage(sender, DragoniteMC.getAPI().getMainConfig().getNoThisPlayer());
                }else if (throwable instanceof SkinUpdateTooFastException){
                    MessageBuilder.sendMessage(sender, DragoniteMC.getAPI().getMainConfig().getPrefix() + throwable.getMessage());
                }
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
