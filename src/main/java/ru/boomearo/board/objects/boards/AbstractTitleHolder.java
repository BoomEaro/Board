package ru.boomearo.board.objects.boards;

import ru.boomearo.board.utils.StringLength;

public abstract class AbstractTitleHolder extends AbstractHolder<String> {

    public AbstractTitleHolder(AbstractPage page) {
        super(page);
    }

    @Override
    public String createHolderData(String text) {
        int maxLength = StringLength.getMaxDataLength().getMaxTitleLength();
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength); // TODO Fix colors
    }
}
