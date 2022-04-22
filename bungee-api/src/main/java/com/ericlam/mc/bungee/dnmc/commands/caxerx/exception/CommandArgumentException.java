package com.ericlam.mc.bungee.dnmc.commands.caxerx.exception;

/**
 * 參數不足
 *
 * @author caxerx
 */
public class CommandArgumentException extends RuntimeException {
    public CommandArgumentException(String msg) {
        super(msg);
    }
}
