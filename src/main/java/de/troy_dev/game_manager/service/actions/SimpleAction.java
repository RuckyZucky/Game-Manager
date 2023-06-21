package de.troy_dev.game_manager.service.actions;

import de.troy_dev.game_manager.data.Game;

public abstract class SimpleAction implements GameAction {
    public abstract void doAction(Game game);
}
