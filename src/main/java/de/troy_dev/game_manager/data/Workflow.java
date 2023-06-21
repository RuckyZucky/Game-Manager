package de.troy_dev.game_manager.data;

import de.troy_dev.game_manager.service.actions.GameAction;
import de.troy_dev.game_manager.service.actions.complex.JoiningPhase;
import de.troy_dev.game_manager.service.actions.simple.SetGameFinishedAction;
import de.troy_dev.game_manager.service.actions.simple.SetGameJoiningAction;

import java.util.List;

public enum Workflow {
    CLASSIC(List.of(
            SetGameJoiningAction.class,
            JoiningPhase.class,
            SetGameFinishedAction.class
    ));

    private final List<Class<? extends GameAction>> actions;

    Workflow(List<Class<? extends GameAction>> actions) {
        this.actions = actions;
    }

    public List<Class<? extends GameAction>> getActions() {
        return actions;
    }
}
