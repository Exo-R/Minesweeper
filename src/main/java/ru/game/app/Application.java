package ru.game.app;

import ru.game.controller.Controller;
import ru.game.controller.ControllerImpl;
import ru.game.model.Model;
import ru.game.model.ModelImpl;
import ru.game.gametype.GameType;
import ru.game.view.ViewImpl;

public class Application {

    public static void main(String[] args) {

        Model model = new ModelImpl(GameType.NOVICE);
        Controller controller = new ControllerImpl(model);
        new ViewImpl(model, controller);

    }

}