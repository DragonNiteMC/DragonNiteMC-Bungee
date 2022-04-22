package com.ericlam.mc.bungee.dnmc.container;

public class ChatFormat {
    private int priority;
    private String format;

    public ChatFormat(int priority, String format) {
        this.priority = priority;
        this.format = format;
    }

    public int getPriority() {
        return priority;
    }

    public String getFormat() {
        return format;
    }
}
