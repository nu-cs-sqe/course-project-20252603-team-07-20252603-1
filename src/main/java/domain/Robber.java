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

    void moveRobber(int HexId){
        currentHexId = HexId;
    }

}
