package ui.controller;

import domain.model.GameSetupModel;
import domain.model.board.BoardHandler;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.ResourceDeck;
import java.util.List;

/** Controller for game setup following MVC pattern. */
public class GameSetupController {

  public static final int MIN_PLAYERS = 3;
  public static final int MAX_PLAYERS = 4;

  /**
   * Validates that the player count is within the valid range (3-4 players).
   *
   * @param model the game setup model
   * @return true if player count is valid, false otherwise
   */
  public boolean validatePlayerCount(GameSetupModel model) {
    int count = model.getPlayerCount();
    return count >= MIN_PLAYERS && count <= MAX_PLAYERS;
  }

  /**
   * Adds a player to the game.
   *
   * @param model the game setup model
   * @param name the player's name
   * @param color the player's color
   */
  public void addPlayer(GameSetupModel model, String name, PlayerColor color) {
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
  public boolean addPlayerWithColorValidation(
      GameSetupModel model, String name, PlayerColor color) {
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
   * Gets the number of hexes on the board.
   *
   * @param board the game board
   * @return the number of hexes
   */
  public int getBoardHexCount(BoardHandler board) {
    return board.getHexCount();
  }

  /**
   * Gets the hex order from the board.
   *
   * @param board the game board
   * @return the list of hex types in order
   */
  public List<String> getHexOrder(BoardHandler board) {
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

  /**
   * Adds a player with full name and color validation.
   *
   * @param model the game setup model
   * @param name the player's name (trimmed)
   * @param color the player's color
   * @return the result of the add operation
   */
  public PlayerAddResult addPlayerWithFullValidation(
      GameSetupModel model, String name, PlayerColor color) {
    String trimmed = (name == null) ? "" : name.trim();
    if (trimmed.isEmpty()) {
      return PlayerAddResult.NAME_EMPTY;
    }
    if (!model.isNameAvailable(trimmed)) {
      return PlayerAddResult.NAME_TAKEN;
    }
    if (color == null) {
      return PlayerAddResult.COLOR_EMPTY;
    }
    if (!model.isColorAvailable(color)) {
      return PlayerAddResult.COLOR_TAKEN;
    }
    model.addPlayer(trimmed, color);
    return PlayerAddResult.SUCCESS;
  }

  /**
   * Removes all players from the model.
   *
   * @param model the game setup model
   */
  public void clearPlayers(GameSetupModel model) {
    model.clearPlayers();
  }
}
