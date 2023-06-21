package de.troy_dev.game_manager.service.actions;

import de.troy_dev.game_manager.data.Game;

public abstract class ComplexAction<T> implements GameAction {

    public abstract T associatedState();

    public abstract boolean isFinished(Game game);
}
