package domain.model;

/**
 * Represents a player in the game.
 * Follows Single Responsibility Principle - manages only player-specific data.
 */
public class Player {

    private final String name;
    private final String color;

    /**
     * Constructs a new Player with the specified name and color.
     *
     * @param name the player's name
     * @param color the player's color
     */
    public Player(String name, String color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Gets the player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's color.
     *
     * @return the player's color
     */
    public String getColor() {
        return color;
    }
}
