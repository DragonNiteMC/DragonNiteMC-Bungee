package com.ericlam.mc.bungee.hnmc.commands.hnmcbs.skins;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.commands.caxerx.CommandNode;
import com.ericlam.mc.bungee.hnmc.container.OfflinePlayer;
import com.ericlam.mc.bungee.hnmc.exceptions.SkinUpdateTooFastException;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.ericlam.mc.bungee.hnmc.permission.Perm;
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
        HyperNiteMC.getAPI().getPlayerManager().getOfflinePlayer(name).thenComposeAsync((off)->{
            if (off.isEmpty()){
                return CompletableFuture.failedFuture(new IllegalStateException());
            }

            OfflinePlayer player = off.get();
            try {
                return HyperNiteMC.getAPI().getSkinValueManager().updateSkin(player.getUniqueId());
            } catch (SkinUpdateTooFastException e) {
                return CompletableFuture.failedFuture(e);
            }
        }).whenComplete(((skin, throwable) -> {
            if (throwable != null){
                if (throwable instanceof IllegalStateException){
                    MessageBuilder.sendMessage(sender, HyperNiteMC.getAPI().getMainConfig().getNoThisPlayer());
                }else if (throwable instanceof SkinUpdateTooFastException){
                    MessageBuilder.sendMessage(sender, HyperNiteMC.getAPI().getMainConfig().getPrefix() + throwable.getMessage());
                }
                return;
            }
            MessageBuilder.sendMessage(sender, HyperNiteMC.getAPI().getMainConfig().getPrefix()+ "§a刪除成功。");
        }));
    }

    @Override
    public List<String> executeTabCompletion(CommandSender sender, List<String> args) {
        return null;
    }
}
