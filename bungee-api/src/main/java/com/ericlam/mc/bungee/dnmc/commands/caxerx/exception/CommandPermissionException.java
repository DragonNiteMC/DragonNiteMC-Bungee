package com.ericlam.mc.bungee.dnmc.commands.caxerx.exception;

/**
 * 沒有權限
 *
 * @author caxerx
 */
public class CommandPermissionException extends RuntimeException {
    public CommandPermissionException(String msg) {
        super(msg);
    }
}
