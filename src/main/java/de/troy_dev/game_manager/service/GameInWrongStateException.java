package de.troy_dev.game_manager.service;

public class GameInWrongStateException extends Exception{

    @Override
    public String getMessage() {
        return "The game is in the wrong state.";
    }

    @Override
    public String getLocalizedMessage() {
        return "Das Spiel ist im falschen Zustand.";
    }
}
