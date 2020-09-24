package com.ericlam.mc.bungee.hnmc.managers;

import com.ericlam.mc.bungee.hnmc.container.OfflineData;
import com.ericlam.mc.bungee.hnmc.container.OfflinePlayer;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class OfflinePlayerManager implements PlayerManager {

    private final List<OfflineData> nameMapping = new ArrayList<>();

    private final Set<UUID> saved = new HashSet<>();

    @Override
    public CompletableFuture<Optional<OfflinePlayer>> getOfflinePlayer(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(getPlayerData(uuid)));
    }

    @Override
    public CompletableFuture<Optional<OfflinePlayer>> getOfflinePlayer(String name) {
        return CompletableFuture.supplyAsync(() -> Optional.ofNullable(getPlayerData(name)));
    }

    public void createTable() {
        try (Connection connection = HyperNiteMC.getAPI().getSQLDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `PlayerData` (`UUID` VARCHAR(40) NOT NULL PRIMARY KEY , `Name` TINYTEXT NOT NULL , `Premium` BOOLEAN NOT NULL , `LastLogin` BIGINT NOT NULL )")) {
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Optional<OfflineData> requestPlayerData(String name) {
        HyperNiteMC.plugin.getLogger().info("Requesting to Mojang API...");
        Optional<OfflineData> opt = nameMapping.stream().filter(data -> data.equalName(name)).findAny();
        if (opt.isPresent()) return opt;
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder str = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
            Map map = new Gson().fromJson(str.toString(), Map.class);
            UUID uuid;
            boolean premium;
            long lastlogin = Timestamp.from(Instant.now()).getTime();
            if (map == null) {
                HyperNiteMC.plugin.getLogger().info("Player is not premium.");
                uuid = UUID.randomUUID();
                premium = false;
            } else {
                HyperNiteMC.plugin.getLogger().info("Player is premium.");
                String id = ((String) map.get("id"));
                StringBuilder sb = new StringBuilder(id);
                String uid = sb.insert(8, "-").insert(13, "-").insert(18, "-").insert(23, "-").toString();
                uuid = UUID.fromString(uid);
                premium = true;
            }
            OfflineData data = new OfflineData(name, uuid, premium, lastlogin);
            nameMapping.add(data);
            this.saveToSQL(data);
            return Optional.of(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void saveToSQL(OfflinePlayer data) {
        if (saved.contains(data.getUniqueId())) return;
        try (Connection connection = HyperNiteMC.getAPI().getSQLDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO `PlayerData` VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE `Name`=?, `Premium`=?, `LastLogin`=?")) {
            statement.setString(1, data.getUniqueId().toString());
            statement.setString(2, data.getName());
            statement.setBoolean(3, data.isPremium());
            statement.setLong(4, data.lastLogin());
            statement.setString(5, data.getName());
            statement.setBoolean(6, data.isPremium());
            statement.setLong(7, data.lastLogin());
            statement.execute();
            this.saved.add(data.getUniqueId());
            HyperNiteMC.plugin.getLogger().info(data.getName() + "'s data saved");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<UUID> getPlayerUUID(String name) {
        Optional<UUID> uuid = nameMapping.stream().filter(data -> data.equalName(name)).map(OfflineData::getUniqueId).findAny();
        if (uuid.isPresent()) return uuid;
        try (Connection connection = HyperNiteMC.getAPI().getSQLDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT UUID FROM `PlayerData` WHERE Name=? AND Premium=?")) {
            statement.setString(1, name);
            statement.setBoolean(2, false);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) return Optional.of(UUID.fromString(resultSet.getString("UUID")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private OfflinePlayer getPlayerData(UUID uuid) {
        return nameMapping.stream().filter(d -> d.equalUUID(uuid)).findAny().orElseGet(() -> {
            try (Connection connection = HyperNiteMC.getAPI().getSQLDataSource().getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM `PlayerData` WHERE `UUID`=?")) {
                statement.setString(1, uuid.toString());
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    String name = resultSet.getString("Name");
                    boolean premium = resultSet.getBoolean("Premium");
                    long lastLogin = resultSet.getLong("LastLogin");
                    OfflineData data = new OfflineData(name, uuid, premium, lastLogin);
                    nameMapping.add(data);
                    return data;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    private OfflinePlayer getPlayerData(String name) {
        return nameMapping.stream().filter(d -> d.equalName(name)).findAny().orElseGet(() -> {
            try (Connection connection = HyperNiteMC.getAPI().getSQLDataSource().getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM `PlayerData` WHERE `Name`=?")) {
                statement.setString(1, name);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                    boolean premium = resultSet.getBoolean("Premium");
                    long lastLogin = resultSet.getLong("LastLogin");
                    OfflineData data = new OfflineData(name, uuid, premium, lastLogin);
                    nameMapping.add(data);
                    return data;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    public Optional<OfflineData> getPlayerDataOrRequest(String player) {
        Optional<OfflineData> opt = nameMapping.stream().filter(data -> data.equalName(player)).findAny();
        if (opt.isPresent()) return opt;
        try (Connection connection = HyperNiteMC.getAPI().getSQLDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM `PlayerData` WHERE `Name`=?")) {
            statement.setString(1, player);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                String name = resultSet.getString("Name");
                boolean premium = resultSet.getBoolean("Premium");
                long lastLogin = resultSet.getLong("LastLogin");
                OfflineData data = new OfflineData(name, uuid, premium, lastLogin);
                nameMapping.add(data);
                return Optional.of(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.requestPlayerData(player);
    }


}
