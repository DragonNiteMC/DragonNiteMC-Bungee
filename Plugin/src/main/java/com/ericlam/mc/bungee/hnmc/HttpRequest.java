package com.ericlam.mc.bungee.hnmc;

import com.ericlam.mc.bungee.hnmc.container.SkinProperty;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class HttpRequest {

    public static final String defaultSkin = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWI3YWY5ZTQ0MTEyMTdjN2RlOWM2MGFjYmQzYzNmZDY1MTk3ODMzMzJhMWIzYmM1NmZiZmNlOTA3MjFlZjM1In19fQ==";

    public static CompletableFuture<String> get(String link) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(link);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } catch (IOException e) {
                throw new CompletionException(e);
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static CompletableFuture<SkinProperty> getSkinProperty(UUID uuid) {
        return HttpRequest.get("https://sessionserver.mojang.com/session/minecraft/profile/".concat(uuid.toString().replace("-", "")).concat("?unsigned=false")).thenApplyAsync(res -> {
            long now = Instant.now().toEpochMilli();
            if (res.isEmpty()) return new SkinProperty(HttpRequest.defaultSkin, now, false, "");
            Gson gson = new Gson();
            Map<?, ?> map = gson.fromJson(res, Map.class);
            List<Object> list = (List<Object>) map.get("properties");
            Map<String, Object> properties = (Map<String, Object>) list.get(0);
            String value = (String) properties.get("value");
            String signature = (String) properties.get("signature");
            return new SkinProperty(value, now, true, signature);
        });
    }
}
