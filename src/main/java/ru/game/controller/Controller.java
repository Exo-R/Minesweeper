package ru.game.controller;

import ru.game.records.Record;
import ru.game.gametype.GameType;

public interface Controller {

    void actionWithLeftClickMouse(int x, int y);

    void actionWithRightClickMouse(int x, int y);

    void actionWithMiddleClickMouse(int x, int y);

    void restart(GameType gameType);

    void restart();

    void addNewRecord(String name, int time);

    Record getRecordByGameType(GameType gameType);

}
