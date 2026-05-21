package domain;

public class Robber {
    private static final int DESERT_HEX_ID = 9;

    private int currentHexId;

    public Robber(){
        currentHexId = DESERT_HEX_ID;
    }

    int getRobberLocation(){
        return currentHexId;
    }

    void moveRobber(int hexId){
        if (hexId < 0 || hexId > 18){
            throw new IllegalArgumentException("Cannot move Robber to invalid HexId");
        }
        currentHexId = hexId;
    }

}
