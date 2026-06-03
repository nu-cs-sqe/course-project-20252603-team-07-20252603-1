package domain;

import domain.model.GameModel;
import domain.model.board.BoardHandler;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.PlayerState;
import domain.model.resources.Resource;
import org.easymock.EasyMock;

import java.util.EnumSet;
import java.util.List;


public class GameModelTests {

    // attemptBuildSettlement() tests

    void attemptBuildSettlement_test01_BoardHandlerSucceeds_EnoughResources_UnderMaxCount_ExpectSuccess(){
        BoardHandler boardMock = EasyMock.createMock(BoardHandler.class);
        Player redMock = EasyMock.createMock(Player.class);
        PlayerState redStateMock = EasyMock.createMock(PlayerState.class);

        EasyMock.expect(redStateMock.getColor()).andReturn(PlayerColor.RED);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            EasyMock.expect(redStateMock.reduceResources(r, 1)).andReturn(true);
        }
        EasyMock.expect(boardMock.buildSettlement(PlayerColor.RED, 0)).andReturn(true);

        EasyMock.replay(boardMock, redStateMock);

        GameModel model = new GameModel(List.of(redMock), boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.attemptBuildSettlement(0);

        EasyMock.verify(boardMock, redStateMock);
    }

}
