package ru.boomearo.board.objects.boards;

import ru.boomearo.board.managers.BoardManager;

public enum DefaultScoreSequenceFactory implements ScoreSequenceFactory {


    FROM_ZERO {

        @Override
        public ScoreSequence create() {
            return new ScoreSequence() {

                private int value = 0;

                @Override
                public void next() {
                    this.value++;
                }

                @Override
                public int getCurrentScore() {
                    return this.value;
                }

            };
        }

    },
    TO_ZERO {

        @Override
        public ScoreSequence create() {
            return new ScoreSequence() {

                private int value = BoardManager.MAX_ENTRY_SIZE;

                @Override
                public void next() {
                    this.value--;
                }

                @Override
                public int getCurrentScore() {
                    return this.value;
                }

            };
        }

    },
    FROM_NEGATIVE_ZERO {

        @Override
        public ScoreSequence create() {
            return new ScoreSequence() {

                private int value = -1;

                @Override
                public void next() {
                    this.value--;
                }

                @Override
                public int getCurrentScore() {
                    return this.value;
                }

            };
        }

    },
    TO_NEGATIVE_ZERO {

        @Override
        public ScoreSequence create() {
            return new ScoreSequence() {

                private int value = -BoardManager.MAX_ENTRY_SIZE;

                @Override
                public void next() {
                    this.value++;
                }

                @Override
                public int getCurrentScore() {
                    return this.value;
                }

            };
        }

    };

}
