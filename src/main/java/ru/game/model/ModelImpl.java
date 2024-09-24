package ru.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.extern.log4j.Log4j2;
import ru.game.records.GameRecords;
import ru.game.records.Record;
import ru.game.gametype.GameType;

@Log4j2
public class ModelImpl implements Model {

    private GameField gameField;
    private GameState gameState;
    private GameType gameType;
    private final GameRecords gameRecords;
    private final List<ModelListener> listeners;

    public ModelImpl(GameType newGameType) {
        gameType = newGameType;
        gameField = new GameField(gameType.getRowsCount(), gameType.getColsCount(), gameType.getMinesCount());
        gameState = GameState.STARTING;
        gameRecords = new GameRecords();
        listeners = new ArrayList<>();

        log.info("The model of the playing field has been initialized! Difficult: {}", gameType);
    }

    @Override
    public void addNewRecord(String username, int time) {
        gameRecords.addNewRecord(gameType, username, time);
    }

    @Override
    public Record getRecordByGameType(GameType gameType) {
        return gameRecords.getRecordByGameType(gameType);
    }

    @Override
    public void openCell(int col, int row) {
        if (gameState.equals(GameState.STARTING)) {
            generateField(col, row);
            openAdjacentCells(col, row);
        } else if (gameState.equals(GameState.PLAYING) && !gameField.isOpened(col, row) && !gameField.isFlag(col, row)) {
            if (gameField.isBomb(col, row)) {
                gameField.setOpened(col, row, true);
                notifyAboutCellChange(col, row);
                gameState = GameState.LOST;
            } else {
                openAdjacentCells(col, row);
            }
        }
        notifyAboutGameFieldChange();
    }

    private void generateField(int startCol, int startRow) {
        gameState = GameState.PLAYING;
        fillCellsByBombs(startCol, startRow);
        fillCellsByBombsCount();
    }

    private void fillCellsByBombs(int startCol, int startRow) {
        int bombsCount = 0;
        while (bombsCount < gameField.getMinesCount()) {
            int colForBomb = new Random().nextInt(gameField.getColsCount());
            int rowForBomb = new Random().nextInt(gameField.getRowsCount());
            if (colForBomb != startCol || rowForBomb != startRow) {
                if (!gameField.isBomb(colForBomb, rowForBomb)) {
                    gameField.setBomb(colForBomb, rowForBomb, true);
                    ++bombsCount;
                }
            }
        }
    }

    private void fillCellsByBombsCount() {
        for (int tempCol = 0; tempCol < gameField.getColsCount(); ++tempCol) {
            for (int tempRow = 0; tempRow < gameField.getRowsCount(); ++tempRow) {
                if (!gameField.isBomb(tempCol, tempRow)) {
                    gameField.setBombsCount(tempCol, tempRow, fillCellByBombsCount(tempCol, tempRow));
                }
            }
        }
    }

    private int fillCellByBombsCount(int col, int row) {
        int countBombsAroundCell = 0;
        for (int tempCol = col - 1; tempCol <= col + 1; ++tempCol) {
            for (int tempRow = row - 1; tempRow <= row + 1; ++tempRow) {
                if ((tempCol != col || tempRow != row) && isBomb(tempCol, tempRow)) {
                    ++countBombsAroundCell;
                }
            }
        }
        return countBombsAroundCell;
    }

    private boolean isBomb(int col, int row) {
        return isExistedCell(col, row) && gameField.isBomb(col, row);
    }

    private boolean isExistedCell(int col, int row) {
        return col >= 0 && row >= 0 && col < gameField.getColsCount() && row < gameField.getRowsCount();
    }

    private void openAdjacentCells(int col, int row) {
        setOpenedCell(col, row);
        removeTrueFlag(col, row);
        notifyAboutCellChange(col, row);
        if (isWinning()) {
            gameState = GameState.WON;
        } else {
            if (isCountBombsEqualsZero(col, row)) {
                for (int tempCol = col - 1; tempCol <= col + 1; ++tempCol) {
                    for (int tempRow = row - 1; tempRow <= row + 1; ++tempRow) {
                        if (isExistedCell(tempCol, tempRow) && !gameField.isOpened(tempCol, tempRow)) {
                            if (isCountBombsEqualsZero(tempCol, tempRow)) {
                                openAdjacentCells(tempCol, tempRow);
                            } else {
                                setOpenedCell(tempCol, tempRow);
                                removeTrueFlag(tempCol, tempRow);
                                notifyAboutCellChange(tempCol, tempRow);
                                if (isWinning()) {
                                    gameState = GameState.WON;
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void setOpenedCell(int col, int row) {
        if (!gameField.isOpened(col, row)) {
            gameField.setOpened(col, row, true);
            gameField.incOpenedCellsCount();
        }
    }

    private void removeTrueFlag(int col, int row) {
        if (gameField.isFlag(col, row)) {
            gameField.setFlag(col, row, false);
            gameField.incFlagsCount();
        }
    }

    private void notifyAboutCellChange(int col, int row) {
        for (ModelListener listener : listeners) {
            listener.cellUpdated(col, row);
        }
    }

    private boolean isWinning() {
        return gameField.getColsCount() *
                gameField.getRowsCount() -
                gameField.getMinesCount() -
                gameField.getOpenedCellsCount() == 0;
    }

    private boolean isCountBombsEqualsZero(int col, int row) {
        return gameField.getBombsCount(col, row) == 0;
    }

    private void notifyAboutGameFieldChange() {
        for (ModelListener listener : listeners) {
            listener.gameFieldChanged();
        }
    }

    @Override
    public void toggleFlag(int col, int row) {
        if (!gameField.isOpened(col, row)) {
            boolean invertValue = !gameField.isFlag(col, row);
            if (invertValue) {
                gameField.decFlagsCount();
            } else {
                gameField.incFlagsCount();
            }
            if (gameField.getFlagsCount() >= 0) {
                gameField.setFlag(col, row, invertValue);
                notifyAboutCellChange(col, row);
            } else {
                gameField.incFlagsCount();
            }
        }
        notifyAboutGameFieldChange();
    }

    @Override
    public void openCellsAroundCell(int col, int row) {
        if (gameState.equals(GameState.PLAYING) && gameField.isOpened(col, row)) {
            int countFlagsAroundCell = 0;
            for (int tempCol = col - 1; tempCol <= col + 1; ++tempCol) {
                for (int tempRow = row - 1; tempRow <= row + 1; ++tempRow) {
                    if (isExistedCell(tempCol, tempRow) && gameField.isFlag(tempCol, tempRow)) {
                        ++countFlagsAroundCell;
                    }
                }
            }
            if (countFlagsAroundCell == gameField.getBombsCount(col, row)) {
                for (int tempCol = col - 1; tempCol <= col + 1; ++tempCol) {
                    for (int tempRow = row - 1; tempRow <= row + 1; ++tempRow) {
                        if (isExistedCell(tempCol, tempRow) && !gameField.isFlag(tempCol, tempRow)) {
                            openAdjacentCells(tempCol, tempRow);
                            if (gameField.isBomb(tempCol, tempRow)) {
                                gameState = GameState.LOST;
                                notifyAboutGameFieldChange();
                                return;
                            }
                            if (isWinning()) {
                                gameState = GameState.WON;
                                notifyAboutGameFieldChange();
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void restart() {
        gameField = new GameField(gameType.getRowsCount(), gameType.getColsCount(), gameType.getMinesCount());
        notifyAboutGameType();
        gameState = GameState.STARTING;
    }

    private void notifyAboutGameType(){
        for (ModelListener listener : listeners) {
            listener.gameTypeChanged();
        }
    }

    @Override
    public void restart(GameType newGameType) {
        if (!newGameType.equals(gameType)) {
            gameType = newGameType;
        }
        gameField = new GameField(gameType.getRowsCount(), gameType.getColsCount(), gameType.getMinesCount());
        notifyAboutGameType();
        gameState = GameState.STARTING;
    }

    @Override
    public void registerListener(ModelListener listener) {
        listeners.add(listener);
    }

    @Override
    public GameType getGameType(){
        return gameType;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }

    @Override
    public int getFlagsCount() {
        return gameField.getFlagsCount();
    }

    @Override
    public Cell getCell(int col, int row) {
        return gameField.getCell(col, row);
    }

}
