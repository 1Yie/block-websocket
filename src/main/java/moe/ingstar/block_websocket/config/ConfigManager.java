package moe.ingstar.block_websocket.config;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class ConfigManager {
    private static final String CONFIG_FILE_PATH = "config/websocket_config/config.yml";
    private ModConfig config;

    public static ModConfig loadConfig() {
        try {
            Path configFilePath = FileSystems.getDefault().getPath(CONFIG_FILE_PATH);

            if (!Files.exists(configFilePath)) {
                createDefaultConfig();
            }

            Yaml yaml = new Yaml();
            return yaml.loadAs(Files.newInputStream(Path.of(CONFIG_FILE_PATH)), ModConfig.class);

        } catch (IOException e) {
            e.printStackTrace();
            return new ModConfig();
        }
    }

    public static void saveConfig(ModConfig config) {
        try {
            Path configFilePath = FileSystems.getDefault().getPath(CONFIG_FILE_PATH);

            Path configDirectoryPath = configFilePath.getParent();
            Files.createDirectories(configDirectoryPath);

            Yaml yaml = new Yaml();
            String yamlString = yaml.dump(configToMap(config));

            Files.writeString(configFilePath, yamlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateWebSocketPort(int newPort) {
        ModConfig config = loadConfig();
        config.serverPort = newPort;

        saveConfig(config);
    }

    private static void createDefaultConfig() {
        try {
            ModConfig defaultConfig = new ModConfig();
            defaultConfig.serverPort = 8990;

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);

            Map<String, Object> configMap = configToMap(defaultConfig);
            String yamlString = yaml.dump(configMap);

            Files.writeString(Path.of(CONFIG_FILE_PATH), yamlString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> configToMap(ModConfig config) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("serverPort", config.serverPort);
        return map;
    }
}
