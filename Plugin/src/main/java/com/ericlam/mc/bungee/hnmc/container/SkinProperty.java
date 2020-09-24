package com.ericlam.mc.bungee.hnmc.container;

public class SkinProperty implements PlayerSkin {
    private final String value;
    private final long timestamp;
    private final boolean premium;
    private final String signature;

    public SkinProperty(String value, long timestamp, boolean premium, String signature) {
        this.value = value;
        this.timestamp = timestamp;
        this.premium = premium;
        this.signature = signature;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean isPremium() {
        return premium;
    }

    @Override
    public String getSignature() {
        return signature;
    }
}
