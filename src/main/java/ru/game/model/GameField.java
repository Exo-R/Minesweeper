package ru.game.model;

public class GameField {

    private final int rowsCount;
    private final int colsCount;
    private final int minesCount;
    private Cell[][] field;
    private int flagsCount;
    private int openedCellsCount;

    public GameField(int rowsCount, int colsCount, int minesCount) {
        this.rowsCount = rowsCount;
        this.colsCount = colsCount;
        this.minesCount = minesCount;
        flagsCount = minesCount;
        openedCellsCount = 0;
        initializeField();
    }

    private void initializeField() {
        field = new Cell[colsCount][rowsCount];
        for (int tempCol = 0; tempCol < colsCount; ++tempCol) {
            for (int tempRow = 0; tempRow < rowsCount; ++tempRow) {
                field[tempCol][tempRow] = new Cell(tempCol, tempRow);
            }
        }
    }

    public boolean isBomb(int col, int row) {
        return field[col][row].isBomb();
    }

    public boolean isFlag(int col, int row) {
        return field[col][row].isFlag();
    }

    public boolean isOpened(int col, int row) {
        return field[col][row].isOpened();
    }

    public void incFlagsCount() {
        ++flagsCount;
    }

    public void decFlagsCount() {
        --flagsCount;
    }

    public void incOpenedCellsCount() {
        ++openedCellsCount;
    }

    public void setBomb(int col, int row, boolean isBomb) {
        field[col][row].setBomb(isBomb);
    }

    public int getBombsCount(int col, int row) {
        return field[col][row].getBombsCount();
    }

    public void setBombsCount(int col, int row, int bombsCount) {
        field[col][row].setBombsCount(bombsCount);
    }

    public void setFlag(int col, int row, boolean isFlag) {
        field[col][row].setFlag(isFlag);
    }

    public void setOpened(int col, int row, boolean isOpened) {
        field[col][row].setOpened(isOpened);
    }

    public int getFlagsCount() {
        return flagsCount;
    }

    public int getRowsCount() {
        return rowsCount;
    }

    public int getColsCount() {
        return colsCount;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public int getOpenedCellsCount() {
        return openedCellsCount;
    }

    public Cell getCell(int col, int row){
        return field[col][row];
    }

}
