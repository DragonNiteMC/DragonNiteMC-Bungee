package com.ericlam.mc.bungee.dnmc.implement;

import com.ericlam.mc.bungee.dnmc.builders.MessageBuilder;
import com.ericlam.mc.bungee.dnmc.builders.function.ChatRunner;
import com.ericlam.mc.bungee.dnmc.builders.function.ChatRunnerManager;
import com.ericlam.mc.bungee.dnmc.main.DragonNiteMC;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ChatRunnerClicker implements ChatRunnerManager, Listener {

    private Map<UUID, ChatRunner> runnerMap = new HashMap<>();
    private Map<UUID, Integer> clickTimes = new HashMap<>();
    private Map<UUID, Integer> maxClickTimes = new HashMap<>();


    @Override
    public void registerClicks(UUID id, ChatRunner runner,int clicks) {
        runnerMap.put(id, runner);
        maxClickTimes.put(id, clicks);
    }

    @Override
    public void registerTimeout(UUID id, ChatRunner runner, int timeout) {
        runnerMap.put(id,runner);
        ProxyServer.getInstance().getScheduler().schedule(DragonNiteMC.plugin,()->runnerMap.remove(id),timeout, TimeUnit.SECONDS);
    }

    @EventHandler
    public void onCommandPreProcess(ChatEvent e) {
        if (!(e.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) e.getSender();
        if (!e.getMessage().startsWith("/command-run-bungee")) return;
        String[] params = e.getMessage().split("_");
        if (params.length != 2) return;
        e.setCancelled(true);
        UUID uuid;
        try {
            uuid = UUID.fromString(params[1]);
        } catch (IllegalArgumentException ex) {
            return;
        }
        ChatRunner runner = runnerMap.get(uuid);
        if (runner == null) {
            MessageBuilder.sendMessage(player, DragonNiteMC.getAPI().getMainConfig().getPrefix()+"&c文字點擊已過期。");
            return;
        }
        runner.run(player);
        if (!maxClickTimes.containsKey(uuid)) return;
        clickTimes.putIfAbsent(uuid,0);
        clickTimes.computeIfPresent(uuid,(uuid1, integer) -> ++integer);
        if (clickTimes.get(uuid) >= maxClickTimes.get(uuid)){
            runnerMap.remove(uuid);
            clickTimes.remove(uuid);
            maxClickTimes.remove(uuid);
        }
    }

}
