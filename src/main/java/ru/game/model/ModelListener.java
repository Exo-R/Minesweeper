package ru.game.model;

public interface ModelListener {

    void gameFieldChanged();

    void gameTypeChanged();

    void cellUpdated(int cellCol, int cellRow);

}
