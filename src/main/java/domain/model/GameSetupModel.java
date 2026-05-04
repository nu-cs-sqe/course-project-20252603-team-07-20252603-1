package domain.model;

import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.resources.ResourceDeck;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Model class for game setup state.
 * Handles player management, board initialization, and game piece setup.
 * Follows Single Responsibility Principle - manages only setup-related state.
 */
public class GameSetupModel {

    private final List<Player> players;
    private final Set<String> usedColors;
    private Board board;
    private ResourceDeck resourceDeck;
    private DevelopmentCardDeck developmentCardDeck;
    private List<Player> turnOrder;

    /**
     * Constructs a new GameSetupModel with empty player list.
     */
    public GameSetupModel() {
        this.players = new ArrayList<>();
        this.usedColors = new HashSet<>();
        this.turnOrder = new ArrayList<>();
    }

    /**
     * Gets the number of players currently in the game.
     *
     * @return the player count
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Adds a player to the game with the specified name and color.
     *
     * @param name the player's name
     * @param color the player's color
     */
    public void addPlayer(String name, String color) {
        Player player = new Player(name, color);
        players.add(player);
        usedColors.add(color);
    }

    /**
     * Gets a player by index.
     *
     * @param index the player index
     * @return the player at the specified index
     */
    public Player getPlayer(int index) {
        return players.get(index);
    }

    /**
     * Checks if a color is available for selection.
     *
     * @param color the color to check
     * @return true if the color is available, false if already used
     */
    public boolean isColorAvailable(String color) {
        return !usedColors.contains(color);
    }

    /**
     * Gets the game board, initializing it if necessary.
     *
     * @return the game board
     */
    public Board getBoard() {
        if (board == null) {
            board = new Board();
        }
        return board;
    }

    /**
     * Gets the resource deck.
     *
     * @return the resource deck
     */
    public ResourceDeck getResourceDeck() {
        return resourceDeck;
    }

    /**
     * Sets the resource deck.
     *
     * @param resourceDeck the resource deck to set
     */
    public void setResourceDeck(ResourceDeck resourceDeck) {
        this.resourceDeck = resourceDeck;
    }

    /**
     * Gets the development card deck.
     *
     * @return the development card deck
     */
    public DevelopmentCardDeck getDevelopmentCardDeck() {
        return developmentCardDeck;
    }

    /**
     * Sets the development card deck.
     *
     * @param developmentCardDeck the development card deck to set
     */
    public void setDevelopmentCardDeck(DevelopmentCardDeck developmentCardDeck) {
        this.developmentCardDeck = developmentCardDeck;
    }

    /**
     * Determines the turn order for players.
     * For now, maintains the order in which players were added.
     * Future implementation may randomize or allow dice rolls.
     */
    public void determineTurnOrder() {
        turnOrder = new ArrayList<>(players);
    }

    /**
     * Gets the turn order.
     *
     * @return the list of players in turn order
     */
    public List<Player> getTurnOrder() {
        return turnOrder;
    }
}
