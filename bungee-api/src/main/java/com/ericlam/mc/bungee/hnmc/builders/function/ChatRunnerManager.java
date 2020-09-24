package com.ericlam.mc.bungee.hnmc.builders.function;

import java.util.UUID;

/**
 *  註冊可點擊動作使用。
 *
 * @see com.ericlam.mc.bungee.hnmc.builders.MessageBuilder#run(ChatRunner)
 */
public interface ChatRunnerManager {

    void registerClicks(UUID id, ChatRunner runner, int clicks);

    void registerTimeout(UUID id, ChatRunner runner, int timeout);

}
