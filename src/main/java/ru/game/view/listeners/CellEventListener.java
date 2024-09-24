package ru.game.view.listeners;

import ru.game.view.enums.ButtonType;

public interface CellEventListener {
    void onMouseClick(int x, int y, ButtonType buttonType);
}
