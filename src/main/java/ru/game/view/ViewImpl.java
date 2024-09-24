package ru.game.view;

import java.awt.event.ActionListener;

import lombok.extern.log4j.Log4j2;
import ru.game.controller.Controller;
import ru.game.gametype.GameType;
import ru.game.model.*;
import ru.game.records.Record;
import ru.game.view.enums.GameImage;
import ru.game.model.ModelListener;
import ru.game.view.timer.GameTimer;

@Log4j2
public class ViewImpl implements View, ModelListener {

    private final MainWindow mainWindow;
    private final SettingsWindow settingsWindow;
    private final HighScoresWindow highScoresWindow;
    private final RecordsWindow recordsWindow;
    private final Controller controller;
    private final ModelForView model;
    private final GameTimer gameTimer;

    public ViewImpl(ModelForView newModel, Controller newController) {
        model = newModel;
        controller = newController;

        mainWindow = new MainWindow();
        settingsWindow = new SettingsWindow(mainWindow);
        highScoresWindow = new HighScoresWindow(mainWindow);
        recordsWindow = new RecordsWindow(mainWindow);

        gameTimer = initGameTimer();

        recordsWindow.setNameListener((username) -> controller.addNewRecord(username, gameTimer.getSeconds()));

        mainWindow.setNewGameMenuAction((e) -> restartGame());
        mainWindow.setSettingsMenuAction((e) -> {
            settingsWindow.setGameTypeListener(this::restartGame);
            settingsWindow.setVisible(true);
        });
        mainWindow.setHighScoresMenuAction((e) -> {
            Record recordNovice = controller.getRecordByGameType(GameType.NOVICE);
            highScoresWindow.setNoviceRecord(recordNovice.getUsername(), recordNovice.getTime());
            Record recordMedium = controller.getRecordByGameType(GameType.MEDIUM);
            highScoresWindow.setMediumRecord(recordMedium.getUsername(), recordMedium.getTime());
            Record recordExpert = controller.getRecordByGameType(GameType.EXPERT);
            highScoresWindow.setExpertRecord(recordExpert.getUsername(), recordExpert.getTime());
            highScoresWindow.setVisible(true);
        });
        mainWindow.setExitMenuAction((e) -> mainWindow.dispose());
        mainWindow.setCellListener((x, y, buttonType) -> {
            switch (buttonType) {
                case LEFT_BUTTON -> controller.actionWithLeftClickMouse(x, y);
                case RIGHT_BUTTON -> controller.actionWithRightClickMouse(x, y);
                case MIDDLE_BUTTON -> controller.actionWithMiddleClickMouse(x, y);
            }
        });

        model.registerListener(this);

        GameType currentGameType = model.getGameType();

        mainWindow.createGameField(currentGameType.getRowsCount(), currentGameType.getColsCount());
        mainWindow.setBombsCount(currentGameType.getMinesCount());

        mainWindow.setVisible(true);
        mainWindow.setLocationRelativeTo(null);

        log.info("The view has been initialized!");
    }

    private GameTimer initGameTimer() {
        ActionListener taskPerformer = (e) -> {
            mainWindow.setTimerValue(gameTimer.getSeconds());
            gameTimer.addSecond();
        };
        return new GameTimer(taskPerformer);
    }

    private void restartGame() {
        controller.restart();
        gameTimer.reset();
        mainWindow.setTimerValue(gameTimer.getSeconds());
    }

    private void restartGame(GameType gameType) {
        controller.restart(gameType);
        gameTimer.reset();
        mainWindow.setTimerValue(gameTimer.getSeconds());
    }

    @Override
    public void gameTypeChanged() {
        GameType gameType = model.getGameType();

        mainWindow.createGameField(gameType.getRowsCount(), gameType.getColsCount());
        mainWindow.setBombsCount(gameType.getMinesCount());
    }

    @Override
    public void cellUpdated(int cellCol, int cellRow) {
        Cell updatedCell = model.getCell(cellCol, cellRow);

        if (updatedCell.isOpened()) {
            if (updatedCell.isBomb()) {
                mainWindow.setCellImage(cellCol, cellRow, GameImage.BOMB);
            } else {
                mainWindow.setCellImage(cellCol, cellRow, GameImage.values()[updatedCell.getBombsCount() + 2]);
            }
        } else if (updatedCell.isFlag()) {
            mainWindow.setCellImage(cellCol, cellRow, GameImage.MARKED);
        } else {
            mainWindow.setCellImage(cellCol, cellRow, GameImage.CLOSED);
        }
    }

    @Override
    public void gameFieldChanged() {
        GameState updatedGameState = model.getGameState();
        int updatedFlagsCount = model.getFlagsCount();

        mainWindow.setBombsCount(updatedFlagsCount);

        if (updatedGameState.equals(GameState.PLAYING) && !gameTimer.isRunning()) {
            gameTimer.start();
            return;
        }

        if (updatedGameState.equals(GameState.LOST)) {
            gameTimer.stop();
            initAndCallLoseWindow();
        } else if (updatedGameState.equals(GameState.WON)) {
            gameTimer.stop();
            recordsWindow.setVisible(true);
            initAndCallWinWindow();
        }
    }

    private void initAndCallLoseWindow() {
        LoseWindow loseWindow = new LoseWindow(mainWindow);
        loseWindow.setExitListener((e) -> mainWindow.dispose());
        loseWindow.setNewGameListener((e) -> restartGame());
        loseWindow.setVisible(true);
    }

    private void initAndCallWinWindow() {
        WinWindow winWindow = new WinWindow(mainWindow);
        winWindow.setExitListener((e) -> mainWindow.dispose());
        winWindow.setNewGameListener((e) -> restartGame());
        winWindow.setVisible(true);
    }

}
