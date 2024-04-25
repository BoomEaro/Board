package ru.boomearo.board.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import ru.boomearo.board.managers.BoardManager;
import ru.boomearo.board.managers.ConfigManager;
import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.objects.boards.AbstractPage;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CommandPage extends CommandNodeBukkit {

    private final BoardManager boardManager;

    public CommandPage(ConfigManager configManager, CommandNodeBukkit root, BoardManager boardManager) {
        super(configManager, root, "page", "board.command.page");
        this.boardManager = boardManager;
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList(this.configManager.getMessage("command_page"));
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player pl)) {
            return;
        }

        if (args.length != 1) {
            sendCurrentHelp(sender);
            return;
        }

        Integer page;
        try {
            page = Integer.parseInt(args[0]);
        } catch (Exception ignored) {
            pl.sendMessage(this.configManager.getMessage("argument_should_be_a_number"));
            return;
        }

        PlayerBoard pb = this.boardManager.getPlayerBoard(pl.getUniqueId());
        if (pb == null) {
            pl.sendMessage(this.configManager.getMessage("board_should_be_enabled"));
            return;
        }

        CompletableFuture<Integer> maxSizeFuture = pb.getMaxPageIndex();
        maxSizeFuture.whenComplete((maxSize, exception) -> {
            int fixedPage = page - 1;
            if (fixedPage > maxSize) {
                pl.sendMessage(this.configManager.getMessage("page_not_found").replace("%page%", String.valueOf(page)));
                return;
            }
            if (fixedPage < 0) {
                pl.sendMessage(this.configManager.getMessage("page_not_found").replace("%page%", String.valueOf(page)));
                return;
            }

            CompletableFuture<AbstractPage> pageToFuture = pb.getPageByIndex(fixedPage);
            pageToFuture.whenComplete((pageTo, exception2) -> {
                if (!pageTo.isVisibleToPlayer()) {
                    pl.sendMessage(this.configManager.getMessage("page_is_empty"));
                    return;
                }

                pb.toPage(fixedPage, pageTo);
                pl.sendMessage(this.configManager.getMessage("page_successfully_changed").replace("%page%", String.valueOf(page)));
            });
        });
    }
}
