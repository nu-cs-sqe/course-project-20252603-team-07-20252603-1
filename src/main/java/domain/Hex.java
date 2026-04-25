package domain;

public class HexDTO {
    private final String resource;
    private final int roll_number;
    private int[] player_settlements;
    private int[] player_cities;

    public HexDTO(String resource, int roll_number) {
        this.resource = resource;
        this.roll_number = roll_number;
        this.player_settlements = new int[3]; // can at most have three settlements on a hex
        this.player_cities = new int[3];      // likewise, can have at most three cities
    }

    void

}
