package ru.boomearo.board.objects.boards;

import lombok.Getter;
import lombok.Value;
import org.bukkit.ChatColor;
import ru.boomearo.board.objects.PlayerBoard;

@Getter
public abstract class AbstractHolder<T> {

    protected final AbstractPage page;
    protected final PlayerBoard playerBoard;

    private T cache = null;
    private long cacheTime = 0;

    public AbstractHolder(AbstractPage page) {
        this.page = page;
        this.playerBoard = this.page.getPlayerBoard();
    }

    public T getHolderResult() {
        if ((System.currentTimeMillis() - this.cacheTime) > getMaxCacheTime()) {
            this.cache = createHolderData(getSafeText());
            this.cacheTime = System.currentTimeMillis();
        }

        return this.cache;
    }

    protected String getSafeText() {
        try {
            return getText();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected long getMaxCacheTime() {
        return 1000;
    }

    protected abstract T createHolderData(String text);

    protected abstract String getText();

    public static FixedStrings fixColors(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return new FixedStrings(text, null);
        }

        StringBuilder first = new StringBuilder(text.substring(0, maxLength));
        StringBuilder second = new StringBuilder(text.substring(maxLength));

        if (first.charAt(first.length() - 1) == '§') {
            first.deleteCharAt(first.length() - 1);
            second.insert(0, '§');
        }

        second.insert(0, ChatColor.getLastColors(first.toString()));

        return new FixedStrings(first.toString(), second.toString());
    }

    @Value
    public static class FixedStrings {
        String first;
        String second;

        public FixedStrings(String first, String second) {
            this.first = first;
            this.second = second;
        }
    }
}
