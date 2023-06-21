package de.troy_dev.game_manager.service.actions.complex;

import de.troy_dev.game_manager.data.Game;
import de.troy_dev.game_manager.data.GameState;
import de.troy_dev.game_manager.service.actions.ComplexAction;
import org.springframework.stereotype.Component;

@Component
public class JoiningPhase extends ComplexAction<GameState> {

    @Override
    public GameState associatedState() {
        return GameState.JOINING;
    }

    @Override
    public boolean isFinished(Game game) {
        return game.getState() != GameState.JOINING;
    }

    public void startGame(Game game) {
        game.setState(GameState.RUNNING);
    }
}
