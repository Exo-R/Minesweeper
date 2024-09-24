package ru.game.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cell {

    private int col;
    private int row;
    private int bombsCount;
    private boolean isOpened;
    private boolean isFlag;
    private boolean isBomb;

    public Cell(int col, int row) {
        this.col = col;
        this.row = row;
    }

}
