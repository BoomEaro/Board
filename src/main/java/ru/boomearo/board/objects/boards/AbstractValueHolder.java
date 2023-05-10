package ru.boomearo.board.objects.boards;

import org.bukkit.ChatColor;

import ru.boomearo.board.utils.StringLength;

public abstract class AbstractValueHolder extends AbstractHolder<HolderResult> {

    public AbstractValueHolder(AbstractPage page) {
        super(page);
    }

    @Override
    protected HolderResult createHolderData(String text) {
        int maxLength = StringLength.getMaxDataLength().getMaxValueLength();

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
