package de.troy_dev.game_manager.service;


import de.troy_dev.game_manager.data.Game;
import de.troy_dev.game_manager.data.Workflow;
import de.troy_dev.game_manager.service.actions.ComplexAction;
import de.troy_dev.game_manager.service.actions.GameAction;
import de.troy_dev.game_manager.service.actions.SimpleAction;
import de.troy_dev.game_manager.service.actions.complex.JoiningPhase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GameService {

    @FunctionalInterface
    private interface ComplexActionExecutor<T extends ComplexAction<?>> {
        void execute(T action);
    }

    private final ApplicationContext applicationContext;

    public GameService(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    private void nextAction(Game game) {
        Class<? extends GameAction> currentAction = null;
        if (log.isTraceEnabled()) {
            currentAction = getCurrentAction(game);
        }
        updateProgress(game);
        if (log.isTraceEnabled()) {
            Class<? extends GameAction> currentAction2 = getCurrentAction(game);
            log.trace("{} -> {}", currentAction, currentAction2);
        }
        executeAction(game);
    }

    private void executeAction(Game game) {
        Class<? extends GameAction> currentAction = getCurrentAction(game);
        if (currentAction == null) {
            return;
        }
        log.debug("Executing action {}", currentAction.getSimpleName());
        GameAction action = applicationContext.getBean(currentAction);
        if (action instanceof SimpleAction) {
            ((SimpleAction) action).doAction(game);
            nextAction(game);
        } else continueIfActionFinished(game, action);
    }

    public void checkCurrentActionFinished(Game game) {
        Class<? extends GameAction> currentAction = getCurrentAction(game);
        if (currentAction == null) {
            return;
        }
        GameAction action = applicationContext.getBean(currentAction);
        continueIfActionFinished(game, action);
    }

    private void continueIfActionFinished(Game game, GameAction action) {
        if (action instanceof ComplexAction && ((ComplexAction<?>) action).isFinished(game)) {
            nextAction(game);
        }
    }

    private Class<? extends GameAction> getCurrentAction(Game game) {
        Workflow gameWorkflow = game.getWorkflow();
        if ((game.getProgress()) >= gameWorkflow.getActions().size()) {
            return null;
        }
        Class<? extends GameAction> currentAction = gameWorkflow.getActions().get(game.getProgress());
        return currentAction;
    }

    private void updateProgress(Game game) {
        Workflow gameWorkflow = game.getWorkflow();
        Class<? extends GameAction> currentAction = gameWorkflow.getActions().get(game.getProgress());

        if (currentAction != ComplexAction.class || ((ComplexAction<?>) applicationContext.getBean(currentAction)).isFinished(game)) {
            game.setProgress(game.getProgress() + 1);
        }
    }

    public void startGameAction(Game game) throws Exception {
        executeComplexAction(game, JoiningPhase.class, (action) -> action.startGame(game));
    }

    private <T extends StateEnum, A extends ComplexAction<T>> void executeComplexAction(Game game, Class<A> clazz, ComplexActionExecutor<A> method) throws GameInWrongStateException {
        if (log.isTraceEnabled()) {
            log.trace("Executing complex action {} in {}", Thread.currentThread().getStackTrace()[2].getMethodName(), clazz.getSimpleName());
        }
        Class<? extends GameAction> currentAction = getCurrentAction(game);
        if (clazz != currentAction) {
            // confirm with updated Game Object
            currentAction = getCurrentAction(game);
            if (clazz != currentAction) {
                throw new IllegalArgumentException(String.format("Complex Action could not be executed. Current Action is not the desired class (%s != %s)", clazz.getSimpleName(), currentAction == null ? null : currentAction.getSimpleName()));
            }
        }
        A action = applicationContext.getBean(clazz);

        StateEnum currentState;
        currentState = game.getState();

        if (action.associatedState() != currentState)
            throw new GameInWrongStateException();
        method.execute(action);
        if (action.isFinished(game)) {
            log.trace("Method execution has finished complex action");
            nextAction(game);
        }
    }

}
