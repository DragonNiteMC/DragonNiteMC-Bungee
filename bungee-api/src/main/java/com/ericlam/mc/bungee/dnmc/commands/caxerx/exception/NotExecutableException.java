package com.ericlam.mc.bungee.dnmc.commands.caxerx.exception;

import com.ericlam.mc.bungee.dnmc.commands.caxerx.CommandNodeBuilder;

/**
 * 沒有執行函式
 *
 * @author Eric Lam
 * @see CommandNodeBuilder#build()
 */
public class NotExecutableException extends RuntimeException {
    private String command;

    public NotExecutableException(String command) {
        super("執行函式 為 Null");
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
