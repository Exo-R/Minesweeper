package ru.game.controller;

import lombok.extern.log4j.Log4j2;
import ru.game.model.Model;
import ru.game.records.Record;
import ru.game.gametype.GameType;

@Log4j2
public class ControllerImpl implements Controller {

    private final Model model;

    public ControllerImpl(Model model) {
        this.model = model;

        log.info("The controller has been initialized!");
    }

    public void actionWithLeftClickMouse(int x, int y) {
        model.openCell(x, y);
    }

    public void actionWithRightClickMouse(int x, int y) {
        model.toggleFlag(x, y);
    }

    public void actionWithMiddleClickMouse(int x, int y) {
        model.openCellsAroundCell(x, y);
    }

    public void restart(GameType gameType) {
        model.restart(gameType);
    }

    public void restart() {
        model.restart();
    }

    public void addNewRecord(String username, int time) {
        model.addNewRecord(username, time);
    }

    public Record getRecordByGameType(GameType gameType) {
        return model.getRecordByGameType(gameType);
    }

}
