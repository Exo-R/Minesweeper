package ru.game.model;

import ru.game.records.Record;
import ru.game.gametype.GameType;

public interface Model extends ModelForView{

    void addNewRecord(String name, int time);

    Record getRecordByGameType(GameType gameType);

    void openCell(int x, int y);

    void toggleFlag(int x, int y);

    void openCellsAroundCell(int x, int y);

    void restart();

    void restart(GameType gameType);

}
