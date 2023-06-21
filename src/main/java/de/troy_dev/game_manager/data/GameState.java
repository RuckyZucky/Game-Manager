package de.troy_dev.game_manager.data;

import de.troy_dev.game_manager.service.StateEnum;

public enum GameState implements StateEnum {
    INIT,
    JOINING,
    RUNNING,
    FINISHED
}
