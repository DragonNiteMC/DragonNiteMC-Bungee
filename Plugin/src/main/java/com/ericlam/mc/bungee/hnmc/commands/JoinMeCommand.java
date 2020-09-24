package com.ericlam.mc.bungee.hnmc.commands;

import com.ericlam.mc.bungee.hnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.hnmc.config.JoinMeConfig;
import com.ericlam.mc.bungee.hnmc.main.HNBungeeConfig;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.ericlam.mc.bungee.hnmc.managers.JoinMeManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.*;

public class JoinMeCommand extends Command {

    private final JoinMeManager joinMeManager;
    private final JoinMeConfig joinme;

    public JoinMeCommand(JoinMeManager joinMeManager, JoinMeConfig joinme) {
        super(joinme.cmd, joinme.permissionUse, joinme.alias.toArray(String[]::new));
        this.joinMeManager = joinMeManager;
        this.joinme = joinme;
    }


    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (!(commandSender instanceof ProxiedPlayer)) {
            MessageBuilder.sendMessage(commandSender, HNBungeeConfig.langConfig.get("not-player"));
            return;
        }

        if (!hasPermission(commandSender)) {
            MessageBuilder.sendMessage(commandSender, HNBungeeConfig.langConfig.get("require-vip"));
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) commandSender;
        if (joinMeManager.isGlobalCooling()) {
            MessageBuilder.sendMessage(player, joinme.get("fail-global-cool").replaceAll("<sec>", joinme.globalCooldown + ""));
            return;
        }
        UUID playerUUID = player.getUniqueId();
        if (joinMeManager.isCooldown(playerUUID)) {
            MessageBuilder.sendMessage(player, joinme.get("fail-ind-cool").replaceAll("<sec>", (int) (joinme.globalCooldown * 1.5) + ""));
            return;
        }

        ServerInfo player_server_info = player.getServer().getInfo();
        String player_server_name = player_server_info.getName();
        Set<ServerInfo> broadcastSers = new HashSet<>();
        Map<String, ServerInfo> all_servers = ProxyServer.getInstance().getServersCopy();
        for (String blacklist : joinme.blacklistServers) {
            if (player_server_name.matches(blacklist)) {
                MessageBuilder.sendMessage(player, joinme.get("fail-blacklist"));
                return;
            }
        }

        if (joinme.onlyBroadcastLobbies) {
            for (String server : all_servers.keySet()) {
                for (String lobbySer : joinme.lobbyServers) {
                    if (server.matches(lobbySer)) {
                        broadcastSers.add(all_servers.get(server));
                        break;
                    }
                }
            }
        } else {
            broadcastSers.addAll(all_servers.values());
        }

        broadcastSers.remove(player_server_info);

        if (broadcastSers.size() == 0) {
            MessageBuilder.sendMessage(player, joinme.get("fail-msg"));
            return;
        }
        Map<String, List<String>> categories = joinme.categories;
        String cateName = null;
        main:
        for (String cate : categories.keySet()) {
            List<String> values = categories.get(cate);
            for (String value : values) {
                if (player_server_name.matches(value)) {
                    cateName = cate;
                    break main;
                }
            }
        }
        final String finalCategory = cateName == null ? player_server_name : cateName;
        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (joinMeManager.uuidExist(uuid));
        joinMeManager.addJoinMe(uuid, player_server_info);
        joinMeManager.addCooldown(playerUUID);
        final String uid = uuid.toString().replaceAll("-", "");
        MessageBuilder.sendMessage(player, joinme.get("start"));

        for (ServerInfo broadcastSer : broadcastSers) {
            broadcastSer.getPlayers().forEach(p -> {
                String[] broadcast = joinme.getList("broadcast-msg").stream().map(line -> line.replaceAll("<player>", HyperNiteMC.getAPI().getChatFormatManager().getPrefix(player) + player.getDisplayName()).replaceAll("<category>", finalCategory).replaceAll("<server>", player_server_name)).toArray(String[]::new);
                String hover = joinme.getPure("hover-msg").replaceAll("<category>", finalCategory).replaceAll("<server>", player_server_name);
                BaseComponent[] msg = new MessageBuilder(broadcast).hoverText(hover).command(uid + "_" + playerUUID).build();
                player.sendMessage(msg);
                MessageBuilder.sendMessage(player, joinme.getPure("valid").replaceAll("<sec>", joinme.globalCooldown + ""));
            });
        }

        MessageBuilder.sendMessage(player, joinme.get("success"));

    }
}
