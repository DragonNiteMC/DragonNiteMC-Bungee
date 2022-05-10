package com.ericlam.mc.bungee.dnmc.managers;

import com.ericlam.mc.bungee.dnmc.SQLDataSource;
import com.ericlam.mc.bungee.dnmc.container.ChatFormat;
import com.ericlam.mc.bungee.dnmc.main.DragoniteMC;
import com.google.inject.Inject;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.query.QueryOptions;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class PrefixManager implements ChatFormatManager {
    private final HashMap<String, ChatFormat> prefixList = new HashMap<>();


    private LuckPerms luckPermsApi;

    private final SQLDataSource sqlDataSource;

    @Inject
    public PrefixManager(SQLDataSource sqlDataSource) {
        this.sqlDataSource = sqlDataSource;
        DragoniteMC.plugin.getLogger().info("Getting chat format data......");
        try (Connection connection = sqlDataSource.getConnection();
             PreparedStatement exist = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `Chat_format` (`Group` VARCHAR(15) NOT NULL PRIMARY KEY , `Format` TEXT NOT NULL , `Priority` INT NOT NULL )");
             PreparedStatement select = connection.prepareStatement("SELECT * FROM `Chat_format`")) {
            exist.execute();
            ResultSet resultSet = select.executeQuery();
            while (resultSet.next()) {
                String name = resultSet.getString("Group");
                String format = resultSet.getString("Format");
                int priority = resultSet.getInt("Priority");
                prefixList.put(name, new ChatFormat(priority, format));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CompletableFuture<Void> updateChatformatTask() {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = sqlDataSource.getConnection();
                 PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Chat_format`")) {
                ResultSet resultSet = statement.executeQuery();
                this.prefixList.clear();
                while (resultSet.next()) {
                    String name = resultSet.getString("Group");
                    String format = resultSet.getString("Format");
                    int priority = resultSet.getInt("Priority");
                    prefixList.put(name, new ChatFormat(priority, format));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void setUpLuckPermsAPI(LuckPerms api) {
        this.luckPermsApi = api;
    }

    private String getPrimaryGroup(Set<String> groups) {
        Comparator<String> comparator = Collections.reverseOrder((pg, g) -> {
            var pgPriority = Optional.ofNullable(prefixList.get(pg)).map(ChatFormat::getPriority).orElse(0);
            var gPriority = Optional.ofNullable(prefixList.get(g)).map(ChatFormat::getPriority).orElse(0);
            return Integer.compare(pgPriority, gPriority);
        });
        return groups.stream().min(comparator).orElse(null);
    }

    @Override
    public String[] getPrefixSuffix(ProxiedPlayer player) {
        User user = luckPermsApi.getUserManager().getUser(player.getUniqueId());
        if (user == null) {
            return new String[]{"", ""};
        }
        Set<String> groups = user.getNodes().stream()
                .filter(NodeType.INHERITANCE::matches)
                .map(NodeType.INHERITANCE::cast)
                .map(InheritanceNode::getGroupName)
                .collect(Collectors.toSet());
        var primaryGroup = getPrimaryGroup(groups);

        if (primaryGroup == null || !prefixList.containsKey(primaryGroup))
            return new String[]{"", ""}; //if null, use back normal format


        ImmutableContextSet contexts = luckPermsApi.getContextManager().getContext(player);
        Group group = luckPermsApi.getGroupManager().getGroup(primaryGroup);
        CachedDataManager groupData = group != null ? group.getCachedData() : null;

        String gprefix = "";
        String gsuffix = "";

        if (groupData != null) {
            gprefix = groupData.getMetaData(QueryOptions.contextual(contexts)).getPrefix();
            gsuffix = groupData.getMetaData(QueryOptions.contextual(contexts)).getSuffix();
        }

        String prefix = user.getCachedData().getMetaData(QueryOptions.contextual(contexts)).getPrefix();
        String suffix = user.getCachedData().getMetaData(QueryOptions.contextual(contexts)).getSuffix();

        String formatList = ChatColor.translateAlternateColorCodes('&', prefixList.get(primaryGroup).getFormat()
                .replaceAll("<g-prefix>", (gprefix != null ? gprefix : ""))
                .replaceAll("<g-suffix>", (gsuffix != null ? gsuffix : "")
                        .replaceAll("<prefix>", (prefix != null ? prefix : ""))
                        .replaceAll("<suffix>", (suffix != null ? suffix : ""))));

        String[] list = formatList.split("<player>");
        if (list.length != 2) {
            return new String[]{"", ""};
        }
        return list;
    }

    @Override
    public String getPrefix(ProxiedPlayer player) {
        return getPrefixSuffix(player)[0];
    }

    @Override
    public String getSuffix(ProxiedPlayer player) {
        return getPrefixSuffix(player)[1];
    }


}
