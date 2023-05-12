package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import ru.boomearo.board.managers.ConfigManager;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class CommandNodeBukkit extends CommandNode<CommandSender> {

    protected final ConfigManager configManager;
    protected final String permission;

    public CommandNodeBukkit(ConfigManager configManager, CommandNodeBukkit root, String name, List<String> aliases, String permission) {
        super(root, name, aliases);
        this.configManager = configManager;
        this.permission = permission;
    }

    public CommandNodeBukkit(ConfigManager configManager, CommandNodeBukkit root, String name, String permission) {
        this(configManager, root, name, Collections.emptyList(), permission);
    }

    public CommandNodeBukkit(ConfigManager configManager, CommandNodeBukkit root, String name, List<String> aliases) {
        this(configManager, root, name, aliases, null);
    }

    public CommandNodeBukkit(ConfigManager configManager, CommandNodeBukkit root, String name) {
        this(configManager, root, name, Collections.emptyList());
    }

    @Override
    public void onExecuteException(CommandSender sender, String[] args, Exception e) {
        e.printStackTrace();
    }

    @Override
    public Collection<String> onSuggestException(CommandSender sender, String[] args, Exception e) {
        e.printStackTrace();
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
        List<String> descs = getDescription();
        if (descs != null) {
            for (String text : descs) {
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
