package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import ru.boomearo.board.managers.ConfigManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public abstract class CommandNodeBukkit extends CommandNode<CommandSender> {

    protected final Plugin plugin;
    protected final ConfigManager configManager;
    protected final String permission;

    public CommandNodeBukkit(Plugin plugin, ConfigManager configManager, CommandNodeBukkit root, String name, List<String> aliases, String permission) {
        super(root, name, aliases);
        this.plugin = plugin;
        this.configManager = configManager;
        this.permission = permission;
    }

    public CommandNodeBukkit(Plugin plugin, ConfigManager configManager, CommandNodeBukkit root, String name, String permission) {
        this(plugin, configManager, root, name, Collections.emptyList(), permission);
    }

    public CommandNodeBukkit(Plugin plugin, ConfigManager configManager, CommandNodeBukkit root, String name, List<String> aliases) {
        this(plugin, configManager, root, name, aliases, null);
    }

    public CommandNodeBukkit(Plugin plugin, ConfigManager configManager, CommandNodeBukkit root, String name) {
        this(plugin, configManager, root, name, Collections.emptyList());
    }

    @Override
    public void onExecuteException(CommandSender sender, String[] args, Exception e) {
        this.plugin.getLogger().log(Level.SEVERE, "Exception on command executing", e);
    }

    @Override
    public Collection<String> onSuggestException(CommandSender sender, String[] args, Exception e) {
        this.plugin.getLogger().log(Level.SEVERE, "Exception on command suggesting", e);
        return Collections.emptyList();
    }

    @Override
    public void onPermissionFailedExecute(CommandSender sender, String[] args) {
        sender.sendMessage("§cУ вас не достаточно прав!");
    }

    @Override
    public Collection<String> onPermissionFailedSuggest(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        if (this.permission == null) {
            return true;
        }
        return sender.hasPermission(this.permission);
    }

    public void sendCurrentHelp(CommandSender sender) {
        List<String> descriptions = getDescription();
        if (descriptions != null) {
            for (String text : descriptions) {
                sender.sendMessage(text);
            }
        }
    }

    public void sendHelp(CommandSender sender) {
        for (String text : getDescriptionListFromRoot(sender)) {
            sender.sendMessage(text);
        }
    }
}
