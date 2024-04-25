package ru.boomearo.board.managers;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.*;

@Getter
public class ConfigManager {

    private boolean defaultToggle = true;
    private boolean enabledToggle = true;
    private int updateFreq = 50;
    private int threadPool = 2;

    private String boardTitle = "Title";
    private List<String> boardTeams = new ArrayList<>();

    private Map<String, String> messages = new HashMap<>();

    public void load(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        if (!configFile.exists()) {
            plugin.getLogger().info("Configuration not found, creating a new one...");
            plugin.saveDefaultConfig();
        }

        plugin.reloadConfig();

        FileConfiguration configuration = plugin.getConfig();

        this.defaultToggle = configuration.getBoolean("settings.defaultToggle");
        this.enabledToggle = configuration.getBoolean("settings.enabledToggle");
        this.updateFreq = configuration.getInt("settings.update_freq");
        this.threadPool = configuration.getInt("settings.thread_pool");

        this.boardTitle = colorize(configuration.getString("settings.default_board.title"));
        List<String> boardTeams = configuration.getStringList("settings.default_board.teams");
        List<String> colorizedBoardTeams = new ArrayList<>();
        for (String team : boardTeams) {
            colorizedBoardTeams.add(colorize(team));
        }
        this.boardTeams = Collections.unmodifiableList(colorizedBoardTeams);

        HashMap<String, String> tmp = new HashMap<>();
        ConfigurationSection messagesSection = configuration.getConfigurationSection("messages");
        if (messagesSection != null) {
            for (String key : messagesSection.getKeys(false)) {
                String message = messagesSection.getString(key);
                String coloredMessage = colorize(message);
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

    private static String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
