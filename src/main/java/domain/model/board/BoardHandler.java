package domain.model.board;

import domain.model.player.PlayerColor;

import java.util.List;

public class BoardHandler {
    public boolean buildSettlement(PlayerColor currentPlayerColor, int nodeID) {
        return true;
    }

    public int getHexCount() {
        return 0;
    }

    public List<String> getHexOrder() {
        return null;
    }

    public void addRoad(PlayerColor currentPlayerColor, int startingNodeID, int endingNodeID) {
    }

    public void buildCity(PlayerColor playerColor, int i) {
    }

    public PlayerColor getNodeOwner(int nodeId) {
        return null;
    }

    public boolean isRoadOwnedBy(PlayerColor color, int startNodeId, int endNodeId) {
        return false;
    }
}
