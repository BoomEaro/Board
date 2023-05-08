package ru.boomearo.board.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class CommandNode<T> {

    protected final CommandNode<T> rootNode;
    protected final String name;
    protected final List<String> aliases;

    protected final Map<String, CommandNode<T>> nodes = new LinkedHashMap<>();
    protected final Map<String, CommandNode<T>> withAliasesNodes = new LinkedHashMap<>();

    public CommandNode(CommandNode<T> rootNode, String name) {
        this(rootNode, name, Collections.emptyList());
    }

    public CommandNode(CommandNode<T> rootNode, String name, List<String> aliases) {
        this.rootNode = rootNode;
        this.name = name;
        this.aliases = aliases;
    }

    public CommandNode<T> getRootNode() {
        return this.rootNode;
    }

    public String getName() {
        return this.name;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public Map<String, CommandNode<T>> getNodes() {
        return this.nodes;
    }

    public Map<String, CommandNode<T>> getWithAliasesNodes() {
        return this.withAliasesNodes;
    }

    public void execute(T sender, String[] args) {
        if (!hasPermission(sender)) {
            onPermissionFailedExecute(sender, args);
            return;
        }

        if (args.length == 0) {
            onExecuteSafe(sender, args);
            return;
        }
        CommandNode<T> nextNode = this.withAliasesNodes.get(args[0].toLowerCase());
        if (nextNode == null) {
            onExecuteSafe(sender, args);
            return;
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        nextNode.execute(sender, newArgs);
    }

    public void onExecuteSafe(T sender, String[] args) {
        try {
            onExecute(sender, args);
        }
        catch (Exception e) {
            onExecuteException(sender, args, e);
        }
    }

    public Collection<String> suggest(T sender, String[] args) {
        if (!hasPermission(sender)) {
            return onPermissionFailedSuggest(sender, args);
        }
        if (args.length == 0) {
            return onSuggestSafe(sender, args);
        }

        CommandNode<T> nextNode = this.withAliasesNodes.get(args[0].toLowerCase());
        if (nextNode == null) {
            return onSuggestSafe(sender, args);
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        return nextNode.suggest(sender, newArgs);
    }

    public Collection<String> onSuggestSafe(T sender, String[] args) {
        try {
            return onSuggest(sender, args);
        }
        catch (Exception e) {
            return onSuggestException(sender, args, e);
        }
    }

    public void addNode(CommandNode<T> node) {
        this.nodes.put(node.getName().toLowerCase(), node);
        this.withAliasesNodes.put(node.getName().toLowerCase(), node);
        for (String alias : node.getAliases()) {
            this.withAliasesNodes.put(alias.toLowerCase(), node);
        }
    }

    public List<String> getDescriptionList(T sender) {
        List<String> tmp = new ArrayList<>();
        if (hasPermission(sender)) {
            List<String> descs = getDescription();
            if (descs != null) {
                tmp.addAll(descs);
            }
        }

        for (CommandNode<T> node : this.nodes.values()) {
            tmp.addAll(node.getDescriptionList(sender));
        }
        return tmp;
    }

    public Collection<String> getDescriptionListFromRoot(T sender) {
        if (this.rootNode == null) {
            return getDescriptionList(sender);
        }

        return this.rootNode.getDescriptionList(sender);
    }

    public boolean hasPermission(T sender) {
        return true;
    }

    public Collection<String> onSuggest(T sender, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        Set<String> matches = new HashSet<>();
        String search = args[0].toLowerCase();
        for (Map.Entry<String, CommandNode<T>> entry : this.withAliasesNodes.entrySet()) {
            if (entry.getValue().hasPermission(sender)) {
                if (entry.getKey().toLowerCase().startsWith(search)) {
                    matches.add(entry.getKey());
                }
            }
        }
        return matches;
    }

    public abstract List<String> getDescription();

    public abstract void onExecute(T sender, String[] args);

    public abstract void onExecuteException(T sender, String[] args, Exception e);

    public abstract Collection<String> onSuggestException(T sender, String[] args, Exception e);

    public abstract void onPermissionFailedExecute(T sender, String[] args);

    public abstract Collection<String> onPermissionFailedSuggest(T sender, String[] args);

}
