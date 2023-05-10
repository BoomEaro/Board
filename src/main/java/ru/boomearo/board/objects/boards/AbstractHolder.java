package ru.boomearo.board.objects.boards;

import org.bukkit.ChatColor;
import ru.boomearo.board.objects.PlayerBoard;

public abstract class AbstractHolder<T> {

    protected final AbstractPage page;
    protected final PlayerBoard playerBoard;

    private T cache = null;
    private long cacheTime = System.currentTimeMillis();

    public AbstractHolder(AbstractPage page) {
        this.page = page;
        this.playerBoard = this.page.getPlayerBoard();
    }

    public AbstractPage getPage() {
        return this.page;
    }

    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    public T getHolderResult() {
        //Если кеша нет, создаем его делая запрос. Потом возвращаем кеш
        if (this.cache == null) {
            createAndAddCache(getValidText());
            return this.cache;
        }
        //Если время кеша вышло, обновляем кеш делая запрос. Если время еще не вышло, возвращает кешированное значение
        if ((System.currentTimeMillis() - this.cacheTime) > getMaxCacheTime()) {
            createAndAddCache(getValidText());
            this.cacheTime = System.currentTimeMillis();
        }

        return this.cache;
    }

    private void createAndAddCache(String text) {
        this.cache = createHolderData(text);
    }

    protected String getValidText() {
        try {
            String text = getText();
            if (text != null) {
                return text;
            }

            return "null";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error";
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
        StringBuilder second = new StringBuilder(text.substring(maxLength, text.length()));

        if (first.charAt(first.length() - 1) == '§') {
            first.deleteCharAt(first.length() - 1);
            second.insert(0, '§');
        }

        second.insert(0, ChatColor.getLastColors(first.toString()));

        return new FixedStrings(first.toString(), second.toString());
    }

    public static class FixedStrings {
        private final String first;
        private final String second;

        public FixedStrings(String first, String second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return this.first;
        }

        public String getSecond() {
            return this.second;
        }
    }
}
