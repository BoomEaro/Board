package ru.boomearo.board.objects.boards;

import ru.boomearo.board.utils.StringLength;

public abstract class AbstractTitleHolder extends AbstractHolder<String> {

    public AbstractTitleHolder(AbstractPage page) {
        super(page);
    }

    @Override
    public String createHolderData(String text) {
        return fixColors(text, StringLength.getMaxDataLength().getMaxTitleLength()).getFirst();
    }
}
