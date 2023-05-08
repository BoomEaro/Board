package ru.boomearo.board.objects.boards;

import org.bukkit.ChatColor;

import ru.boomearo.board.objects.PlayerBoard;
import ru.boomearo.board.utils.StringLength;

public abstract class AbstractHolder {

    private HolderResult cache = null;
    private long cacheTime = System.currentTimeMillis();

    protected final AbstractPage page;
    protected final PlayerBoard playerBoard;

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

    public HolderResult getHolderResult() {
        //Если кеша нет, создаем его делая запрос. Потом возвращаем кеш
        if (this.cache == null) {
            createCacheAnimation(getValidText());
            return this.cache;
        }
        //Если время кеша вышло, обновляем кеш делая запрос. Если время еще не вышло, возвращает кешированное значение
        if ((System.currentTimeMillis() - this.cacheTime) > getMaxCacheTime()) {
            createCacheAnimation(getValidText());
            this.cacheTime = System.currentTimeMillis();
        }

        return this.cache;
    }

    private String getValidText() {
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

    protected abstract String getText();

    //По умолчанию размер кеша = 5 секунд. Хотя допустимо указывать еще милисекунды.
    //Этот метод может переопределить наследник, что бы иметь другое время.
    public long getMaxCacheTime() {
        return 1000 * 5;
    }

    private void createCacheAnimation(String text) {
        this.cache = createHolderData(text);
    }

    private HolderResult createHolderData(String text) {
        int maxLength = StringLength.getMaxSupportedStringLength();

        StringBuilder prefix = new StringBuilder(text.substring(0, text.length() >= maxLength ? maxLength : text.length()));
        StringBuilder suffix = new StringBuilder(text.length() > maxLength ? text.substring(maxLength) : "");

        if (prefix.length() > 1 && prefix.charAt(prefix.length() - 1) == '§') {

            prefix.deleteCharAt(prefix.length() - 1);
            suffix.insert(0, '§');
        }
        if (text.length() > maxLength) {
            suffix.insert(0, ChatColor.getLastColors(prefix.toString()));
        }

        String suf = suffix.toString();

        if (suf.length() > maxLength) {
            suf = suf.substring(0, maxLength);
        }

        return new HolderResult(prefix.length() > maxLength ? prefix.toString().substring(0, maxLength) : prefix.toString(), suf);
    }

}
