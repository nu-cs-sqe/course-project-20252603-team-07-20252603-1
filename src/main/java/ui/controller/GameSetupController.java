package ui.controller;

import domain.model.Board;
import domain.model.GameSetupModel;
import domain.model.Player;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.resources.ResourceDeck;

import java.util.List;

/**
 * Controller for game setup following MVC pattern.
 * Delegates all business logic to GameSetupModel, maintaining separation of concerns.
 */
public class GameSetupController {

    /**
     * Validates that the player count is within the valid range (3-4 players).
     *
     * @param model the game setup model
     * @return true if player count is valid, false otherwise
     */
    public boolean validatePlayerCount(GameSetupModel model) {
        int count = model.getPlayerCount();
        return count >= 3 && count <= 4;
    }

    /**
     * Adds a player to the game.
     *
     * @param model the game setup model
     * @param name the player's name
     * @param color the player's color
     */
    public void addPlayer(GameSetupModel model, String name, String color) {
        model.addPlayer(name, color);
    }

    /**
     * Adds a player with color validation to ensure exclusivity.
     *
     * @param model the game setup model
     * @param name the player's name
     * @param color the player's color
     * @return true if player was added successfully, false if color is unavailable
     */
    public boolean addPlayerWithColorValidation(GameSetupModel model, String name, String color) {
        if (!model.isColorAvailable(color)) {
            return false;
        }
        model.addPlayer(name, color);
        return true;
    }

    /**
     * Gets a player's name by index.
     *
     * @param model the game setup model
     * @param index the player index
     * @return the player's name
     */
    public String getPlayerName(GameSetupModel model, int index) {
        return model.getPlayer(index).getName();
    }

    /**
     * Gets the number of players in the game.
     *
     * @param model the game setup model
     * @return the player count
     */
    public int getPlayerCount(GameSetupModel model) {
        return model.getPlayerCount();
    }

    /**
     * Initializes the game board.
     *
     * @param model the game setup model
     */
    public void initializeBoard(GameSetupModel model) {
        // Board initialization is handled by the model
        // Controller just triggers the initialization
        model.getBoard();
    }

    /**
     * Gets the game board.
     *
     * @param model the game setup model
     * @return the game board
     */
    public Board getBoard(GameSetupModel model) {
        return model.getBoard();
    }

    /**
     * Gets the number of hexes on the board.
     *
     * @param board the game board
     * @return the number of hexes
     */
    public int getBoardHexCount(Board board) {
        return board.getHexCount();
    }

    /**
     * Gets the hex order from the board.
     *
     * @param board the game board
     * @return the list of hex types in order
     */
    public List<String> getHexOrder(Board board) {
        return board.getHexOrder();
    }

    /**
     * Initializes the resource deck.
     *
     * @param model the game setup model
     */
    public void initializeResourceDeck(GameSetupModel model) {
        model.setResourceDeck(new ResourceDeck());
    }

    /**
     * Gets the resource deck.
     *
     * @param model the game setup model
     * @return the resource deck
     */
    public ResourceDeck getResourceDeck(GameSetupModel model) {
        return model.getResourceDeck();
    }

    /**
     * Initializes the development card deck.
     *
     * @param model the game setup model
     */
    public void initializeDevelopmentCardDeck(GameSetupModel model) {
        model.setDevelopmentCardDeck(new DevelopmentCardDeck());
    }

    /**
     * Gets the development card deck.
     *
     * @param model the game setup model
     * @return the development card deck
     */
    public DevelopmentCardDeck getDevelopmentCardDeck(GameSetupModel model) {
        return model.getDevelopmentCardDeck();
    }

    /**
     * Determines the turn order for players.
     *
     * @param model the game setup model
     */
    public void determineTurnOrder(GameSetupModel model) {
        model.determineTurnOrder();
    }

    /**
     * Gets the turn order.
     *
     * @param model the game setup model
     * @return the list of players in turn order
     */
    public List<Player> getTurnOrder(GameSetupModel model) {
        return model.getTurnOrder();
    }
}
