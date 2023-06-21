package de.troy_dev.game_manager.data;

import lombok.Data;

@Data
public class Game {
    Workflow workflow;

    int progress;

    GameState state;
}
