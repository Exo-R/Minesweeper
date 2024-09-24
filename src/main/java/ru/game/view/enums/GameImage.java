package ru.game.view.enums;

import javax.swing.*;

public enum GameImage {
    CLOSED("images/cell.png"),
    MARKED("images/flag.png"),
    EMPTY("images/empty.png"),
    NUM_1("images/1.png"),
    NUM_2("images/2.png"),
    NUM_3("images/3.png"),
    NUM_4("images/4.png"),
    NUM_5("images/5.png"),
    NUM_6("images/6.png"),
    NUM_7("images/7.png"),
    NUM_8("images/8.png"),
    BOMB("images/mine.png"),
    TIMER("images/timer.png"),
    BOMB_ICON("images/mineImage.png"),
    ;

    private final String fileName;
    private ImageIcon imageIcon;

    GameImage(String fileName) {
        this.fileName = fileName;
    }

    public synchronized ImageIcon getImageIcon() {
        if (imageIcon == null) {
            imageIcon = new ImageIcon(ClassLoader.getSystemResource(fileName));
        }

        return imageIcon;
    }
}
