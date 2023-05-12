package ru.boomearo.board.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private boolean defaultToggle = true;
    private boolean enabledToggle = true;

    private Map<String, String> messages = new HashMap<>();

    public void load(JavaPlugin plugin) {
        File configFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            plugin.getLogger().info("Configuration not found, creating a new one...");
            plugin.saveDefaultConfig();
        }

        plugin.reloadConfig();

        FileConfiguration configuration = plugin.getConfig();

        this.defaultToggle = configuration.getBoolean("settings.defaultToggle");
        this.enabledToggle = configuration.getBoolean("settings.enabledToggle");

        HashMap<String, String> tmp = new HashMap<>();
        ConfigurationSection messagesSection = configuration.getConfigurationSection("messages");
        if (messagesSection != null) {
            for (String key : messagesSection.getKeys(false)) {
                String message = messagesSection.getString(key);
                String coloredMessage = ChatColor.translateAlternateColorCodes('&', message);
                tmp.put(key, coloredMessage);
            }
        }
        this.messages = tmp;
    }

    public String getMessage(String key) {
        String message = this.messages.get(key);
        if (message == null) {
            return "<invalid message translate key '" + key + "'>";
        }

        return message;
    }

    public boolean isDefaultToggle() {
        return this.defaultToggle;
    }

    public boolean isEnabledToggle() {
        return this.enabledToggle;
    }
}
