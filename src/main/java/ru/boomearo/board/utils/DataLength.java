package ru.boomearo.board.utils;

import lombok.Value;

@Value
public class DataLength {
    int maxValueLength;
    int maxTitleLength;

    public DataLength(int maxValueLength, int maxTitleLength) {
        this.maxValueLength = maxValueLength;
        this.maxTitleLength = maxTitleLength;
    }
}
