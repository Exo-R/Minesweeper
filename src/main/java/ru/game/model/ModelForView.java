package ru.game.model;

import ru.game.gametype.GameType;

public interface ModelForView {

    GameType getGameType();

    GameState getGameState();

    int getFlagsCount();

    Cell getCell(int x, int y);

    void registerListener(ModelListener listener);

}
