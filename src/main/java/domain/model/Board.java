package domain.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents the game board for Catan.
 * Follows Single Responsibility Principle - manages only board state and configuration.
 */
public class Board {

    private static final int STANDARD_HEX_COUNT = 19;
    private final List<String> hexOrder;

    /**
     * Constructs a new Board with the standard Catan hex configuration.
     * Initializes 19 hexes with standard resource distribution.
     */
    public Board() {
        this.hexOrder = initializeHexOrder();
    }

    /**
     * Initializes the hex order with standard Catan resource distribution:
     * - 4 WHEAT hexes
     * - 4 SHEEP hexes
     * - 4 WOOD hexes
     * - 3 BRICK hexes
     * - 3 ORE hexes
     * - 1 DESERT hex
     * Total: 19 hexes
     *
     * @return the ordered list of hex types
     */
    private List<String> initializeHexOrder() {
        List<String> hexes = new ArrayList<>(Arrays.asList(
            "WHEAT", "WHEAT", "WHEAT", "WHEAT",
            "SHEEP", "SHEEP", "SHEEP", "SHEEP",
            "WOOD", "WOOD", "WOOD", "WOOD",
            "BRICK", "BRICK", "BRICK",
            "ORE", "ORE", "ORE",
            "DESERT"
        ));

        // Shuffle to randomize board layout
        Collections.shuffle(hexes);

        return hexes;
    }

    /**
     * Gets the number of hexes on the board.
     *
     * @return the hex count (always 19 for standard Catan)
     */
    public int getHexCount() {
        return STANDARD_HEX_COUNT;
    }

    /**
     * Gets the hex order.
     *
     * @return an unmodifiable list of hex types in their board positions
     */
    public List<String> getHexOrder() {
        return Collections.unmodifiableList(hexOrder);
    }
}
