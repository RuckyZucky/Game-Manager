package de.troy_dev.game_manager.service.actions.simple;

import de.troy_dev.game_manager.data.Game;
import de.troy_dev.game_manager.data.GameState;
import de.troy_dev.game_manager.service.actions.SimpleAction;
import org.springframework.stereotype.Component;

@Component
public class SetGameJoiningAction extends SimpleAction {
    @Override
    public void doAction(Game game) {
        game.setState(GameState.JOINING);
    }
}
