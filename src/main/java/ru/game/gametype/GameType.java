package ru.game.gametype;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GameType {

    NOVICE(10, 10, 10),
    MEDIUM(16, 16, 40),
    EXPERT(16, 30, 99);

    private final int rowsCount;
    private final int colsCount;
    private final int minesCount;

}