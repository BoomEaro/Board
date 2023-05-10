package ru.boomearo.board.objects.boards;

import ru.boomearo.board.utils.StringLength;

public abstract class AbstractValueHolder extends AbstractHolder<HolderResult> {

    public AbstractValueHolder(AbstractPage page) {
        super(page);
    }

    @Override
    protected HolderResult createHolderData(String text) {
        if (text == null) {
            return new HolderResult("null", "");
        }

        int maxLength = StringLength.getMaxDataLength().getMaxValueLength();

        FixedStrings fixedFirst = fixColors(text, maxLength);
        String prefix = fixedFirst.getFirst();

        if (fixedFirst.getSecond() == null) {
            return new HolderResult(prefix, "");
        }

        FixedStrings fixedSecond = fixColors(fixedFirst.getSecond(), maxLength);
        String suffix = fixedSecond.getFirst();

        return new HolderResult(prefix, suffix);
    }

}
