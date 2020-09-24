package com.ericlam.mc.bungee.hnmc.managers;

import com.ericlam.mc.bungee.hnmc.config.YamlManager;
import com.ericlam.mc.bungee.hnmc.config.yaml.*;
import com.ericlam.mc.bungee.hnmc.main.HyperNiteMC;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YamlHandler implements YamlManager {

    private final Plugin plugin;

    private final Map<String, Class<? extends BungeeConfiguration>> ymls;
    private final Map<Class<? extends BungeeConfiguration>, BungeeConfiguration> map = new ConcurrentHashMap<>();
    private final ConfigurationProvider provider;
    private final ObjectMapper objectMapper;

    YamlHandler(Map<String, Class<? extends BungeeConfiguration>> ymls, final Plugin plugin) {
        this.ymls = ymls;
        this.plugin = plugin;
        this.provider = ConfigurationProvider.getProvider(YamlConfiguration.class);
        plugin.getLogger().info("正在初始化 yml");
        this.objectMapper = new ObjectMapper(new YAMLFactory()
                .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                .enable(JsonParser.Feature.ALLOW_YAML_COMMENTS))
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .enable(JsonParser.Feature.ALLOW_COMMENTS)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .setDefaultSetterInfo(JsonSetter.Value.construct(Nulls.AS_EMPTY, Nulls.AS_EMPTY))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.skipType(FileController.class);
        this.skipType(MessageGetter.class);
        this.registerModule(new SimpleModule());
        plugin.getLogger().info("正在初始化 yml");
        reloadConfigs();
    }

    public void skipType(Class<?> type) {
        objectMapper.configOverride(type)
                .setVisibility(JsonAutoDetect.Value.construct(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE))
                .setIsIgnoredType(true)
                .setSetterInfo(JsonSetter.Value.construct(Nulls.SKIP, Nulls.SKIP));
    }

    public void customSetter(Consumer<ObjectMapper> mapperConsumer) {
        mapperConsumer.accept(objectMapper);
    }

    public void registerModule(SimpleModule module) {
        objectMapper.registerModule(module);
    }

    @Override
    public boolean reloadConfigs() {
        boolean result = true;
        for (String yml : ymls.keySet()) {
            result = result && this.reloadConfig(yml);
        }
        return result;
    }

    private void serve(String msg) {
        plugin.getLogger().log(Level.SEVERE, msg);
    }

    private boolean reloadConfig(String yml) {
        Class<? extends BungeeConfiguration> cls = this.ymls.get(yml);
        if (cls == null) {
            serve("找不到 " + yml + " 的映射物件，請確保你已經註冊了 " + yml);
            return false;
        }
        return this.reloadConfig(cls);
    }

    private <T extends BungeeConfiguration> boolean reloadConfig(Class<T> config) {
        try {
            Optional<Map.Entry<String, Class<? extends BungeeConfiguration>>> yml = ymls.entrySet().stream().filter(s -> s.getValue() == config).findAny();
            if (yml.isEmpty()) {
                serve("找不到 " + config.getSimpleName() + " 的輸出文件路徑， 請確保你已經註冊了 " + config.getSimpleName());
                return false;
            }
            Map.Entry<String, Class<? extends BungeeConfiguration>> entry = yml.get();
            Resource resource = config.getAnnotation(Resource.class);
            File file = new File(plugin.getDataFolder(), entry.getKey());
            plugin.getDataFolder().mkdirs();
            if (!file.exists()) {
                var ins = plugin.getResourceAsStream(resource.locate());
                Files.copy(ins, file.toPath());
            }
            var ins = objectMapper.readValue(file, entry.getValue());
            class FileControllerImpl implements FileController {

                @Override
                public <C extends BungeeConfiguration> void save(C config) throws IOException {
                    objectMapper.writeValue(file, config);
                }

                @Override
                public <C extends BungeeConfiguration> void reload(C config) {
                    try {
                        reloadConfig(config.getClass());
                        var latest = getConfigAs(config.getClass());
                        for (Field f : latest.getClass().getDeclaredFields()) {
                            var dataField = latest.getClass().getDeclaredField(f.getName());
                            dataField.setAccessible(true);
                            var data = dataField.get(config);
                            f.setAccessible(true);
                            f.set(config, data);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }


            Field field = BungeeConfiguration.class.getDeclaredField("controller");
            field.setAccessible(true);
            field.set(ins, new FileControllerImpl());


            if (ins instanceof MessageConfiguration) {
                Configuration configuration = provider.load(file);
                class MessageGetterImpl implements MessageGetter {

                    @Override
                    public String getPrefix() {
                        var prefix = ins.getClass().getAnnotation(Prefix.class);
                        return Optional.ofNullable(prefix).map(pre -> translate(configuration.getString(pre.path()))).orElseGet(() -> HyperNiteMC.getHnBungeeConfig().getPrefix());
                    }

                    @Override
                    public String get(String path) {
                        return getPrefix() + getPure(path);
                    }

                    @Override
                    public String getPure(String path) {
                        return translate(configuration.getString(path));
                    }

                    @Override
                    public List<String> getList(String path) {
                        return getPureList(path).stream().map(l -> getPrefix() + l).collect(Collectors.toList());
                    }

                    @Override
                    public List<String> getPureList(String path) {
                        return configuration.getStringList(path).stream().map(YamlHandler.this::translate).collect(Collectors.toList());
                    }
                }

                field = MessageConfiguration.class.getDeclaredField("getter");
                field.setAccessible(true);
                field.set(ins, new MessageGetterImpl());
            }
            map.put(config, ins);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String translate(String msg) {
        return ChatColor.translateAlternateColorCodes('&', Optional.ofNullable(msg).orElse("null"));
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T extends BungeeConfiguration> T getConfig(String yml) {
        return (T) Optional.ofNullable(ymls.get(yml)).map(this.map::get).orElseThrow(() -> new IllegalStateException("找不到 " + yml + " 的映射物件，請確保你已經註冊了 " + yml));
    }

    @Override
    public <T extends BungeeConfiguration> T getConfigAs(Class<T> config) {
        return config.cast(Optional.ofNullable(this.map.get(config)).orElseThrow(() -> new IllegalStateException("找不到 " + config.getSimpleName() + " 的映射物件，請確保你已經註冊了 " + config.getSimpleName())));
    }
}
