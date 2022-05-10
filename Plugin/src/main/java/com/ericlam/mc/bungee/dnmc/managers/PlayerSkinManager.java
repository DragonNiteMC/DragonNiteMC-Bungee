package com.ericlam.mc.bungee.dnmc.managers;

import com.ericlam.mc.bungee.dnmc.HttpRequest;
import com.ericlam.mc.bungee.dnmc.SQLDataSource;
import com.ericlam.mc.bungee.dnmc.container.PlayerSkin;
import com.ericlam.mc.bungee.dnmc.container.SkinProperty;
import com.ericlam.mc.bungee.dnmc.exceptions.SkinUpdateTooFastException;
import com.ericlam.mc.bungee.dnmc.main.DragoniteMC;
import com.google.inject.Inject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class PlayerSkinManager implements SkinValueManager {

    private final SQLDataSource sqlDataSource;

    private final Map<UUID, PlayerSkin> skinPropertyMap = new ConcurrentHashMap<>();


    @Inject
    public PlayerSkinManager(SQLDataSource sqlDataSource, Plugin plugin){
        this.sqlDataSource = sqlDataSource;
        ProxyServer.getInstance().getScheduler().runAsync(plugin, ()->{
            try(Connection connection = sqlDataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Skin_data` (`PlayerUUID` VARCHAR(40) NOT NULL PRIMARY KEY , `PlayerName` TINYTEXT NOT NULL , `Value` LONGTEXT NOT NULL ,`Signature` LONGTEXT NOT NULL, `TimeStamp` BIGINT NOT NULL )")){
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public List<String> getSkinList(){
        return skinPropertyMap.keySet().stream().map(uuid->ProxyServer.getInstance().getPlayer(uuid)).filter(Objects::nonNull).map(CommandSender::getName).collect(Collectors.toList());
    }

    public CompletableFuture<Boolean> containPlayerSkin(String name){
        return CompletableFuture.supplyAsync(()->{
            try(Connection connection = sqlDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT `PlayerUUID` FROM Skin_data WHERE PlayerName=?")){
                statement.setString(1, name);
                return statement.executeQuery().next();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<PlayerSkin> getOrSaveSkinForPlayer(UUID uuid){
        if (skinPropertyMap.containsKey(uuid)) return CompletableFuture.completedFuture(skinPropertyMap.get(uuid));
        return CompletableFuture.supplyAsync(()->{
            try(Connection connection = sqlDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT `Value`, `Signature`, `TimeStamp` FROM `Skin_data` WHERE `PlayerUUID`=?")) {
                statement.setString(1, uuid.toString());
                ResultSet resultSet = statement.executeQuery();
                PlayerSkin playerSkin;
                if (resultSet.next()){
                    String value = resultSet.getString("Value");
                    long ts = resultSet.getLong("TimeStamp");
                    String sign = resultSet.getString("Signature");
                    playerSkin =  new SkinProperty(value, ts, true, sign);
                    if (Instant.now().toEpochMilli() - ts >= TimeUnit.DAYS.toMillis(7)) {
                        playerSkin = this.updateSkinTask(connection, uuid).get();
                    }
                }else{
                    playerSkin = this.updateSkinTask(connection, uuid).get();

                }
                this.skinPropertyMap.put(uuid, playerSkin);
                return  playerSkin;
            } catch (SQLException | InterruptedException | ExecutionException e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<PlayerSkin> getOrSaveSkinForPlayer(UUID uuid, String name){
        if (skinPropertyMap.containsKey(uuid)) return CompletableFuture.completedFuture(skinPropertyMap.get(uuid));
        return CompletableFuture.supplyAsync(()->{
            try(Connection connection = sqlDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT `Value`, `Signature`, `TimeStamp` FROM `Skin_data` WHERE `PlayerUUID`=? OR `PlayerName`=?")) {
                statement.setString(1, uuid.toString());
                statement.setString(2, name);
                ResultSet resultSet = statement.executeQuery();
                PlayerSkin playerSkin;
                if (resultSet.next()){
                    String value = resultSet.getString("Value");
                    long ts = resultSet.getLong("TimeStamp");
                    String sign = resultSet.getString("Signature");
                    playerSkin =  new SkinProperty(value, ts, true, sign);
                }else{
                    playerSkin = this.updateSkinTask(connection, uuid).get();

                }
                this.skinPropertyMap.put(uuid, playerSkin);
                return  playerSkin;
            } catch (SQLException | InterruptedException | ExecutionException e) {
                throw new CompletionException(e);
            }
        });
    }

    @Override
    public CompletableFuture<Boolean> dropSkin(UUID uuid){
        return CompletableFuture.supplyAsync(()->{
            try(Connection connection = sqlDataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("DELETE FROM Skin_data WHERE PlayerUUID=?")) {
                statement.setString(1, uuid.toString());
                boolean exist = statement.executeUpdate() > 0;
                if (exist) this.skinPropertyMap.remove(uuid);
                return exist;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        });
    }

    @Override
    public CompletableFuture<PlayerSkin> updateSkin(UUID uuid) throws SkinUpdateTooFastException{
        try(Connection connection = sqlDataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT `TimeStamp` FROM Skin_data WHERE PlayerUUID=?")){
            statement.setString(1, uuid.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
              if (resultSet.next()){
                  long ts = resultSet.getLong("Timestamp");
                  long now = Instant.now().toEpochMilli();
                  if (now - ts <= 86400000){
                      throw new SkinUpdateTooFastException("該玩家從換皮膚到現在的時間還不夠一日。");
                  }
              }
            }
            return this.updateSkinTask(connection, uuid);
        } catch (SQLException e) {
            return CompletableFuture.failedFuture(e);
        }
    }

    private CompletableFuture<PlayerSkin> updateSkinTask(Connection connection, UUID uuid){
        return HttpRequest.getSkinProperty(uuid).thenCombineAsync(DragoniteMC.getAPI().getPlayerManager().getOfflinePlayer(uuid),(skinProperty, offlinePlayer) -> {
            offlinePlayer.ifPresent(player->{
                if (skinProperty.isPremium() && player.isPremium()){
                    try(PreparedStatement save = connection.prepareStatement("INSERT INTO `Skin_data` VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE `PlayerName`=?, `Value`=?, `Signature`=?, `TimeStamp`=?")){
                        save.setString(1, uuid.toString());
                        save.setString(2, player.getName());
                        save.setString(3, skinProperty.getValue());
                        save.setString(4, skinProperty.getSignature());
                        save.setLong(5, skinProperty.getTimestamp());
                        save.setString(6, player.getName());
                        save.setString(7, skinProperty.getValue());
                        save.setString(8, skinProperty.getSignature());
                        save.setLong(9, skinProperty.getTimestamp());
                        save.execute();
                    } catch (SQLException e) {
                        throw new CompletionException(e);
                    }
                }
            });
            this.skinPropertyMap.put(uuid, skinProperty);
            return skinProperty;
        });
    }

    @Override
    public void applySkin(final ProxiedPlayer player, final PlayerSkin skin){
        if (player == null || skin == null) return;
        InitialHandler handler = (InitialHandler)player.getPendingConnection();
        if (handler == null) return;
        LoginResult result = this.editResult(handler.getLoginProfile(), skin);
        try {
            Field profileField = handler.getClass().getDeclaredField("loginProfile");
            profileField.setAccessible(true);
            profileField.set(handler, result);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public LoginResult editResult(final LoginResult result, final PlayerSkin skin){
        if (result == null || skin == null) return result;
        if (result.getProperties().length < 1) return result;
        LoginResult.Property texture = result.getProperties()[0];
        texture.setValue(skin.getValue());
        texture.setSignature(skin.getSignature());
        result.setProperties(new LoginResult.Property[]{texture});
        return result;
    }









}
