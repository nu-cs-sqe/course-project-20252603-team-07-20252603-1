package domain.model;

import domain.model.board.BoardHandler;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.player.PlayerColor;
import domain.model.player.PlayerState;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class GameModelTests {

    // attemptBuildSettlement() tests
    private BoardHandler boardMock;
    private ResourceDeck lumberDeckMock;
    private ResourceDeck brickDeckMock;
    private ResourceDeck grainDeckMock;
    private ResourceDeck oreDeckMock;
    private ResourceDeck woolDeckMock;
    private Map<PlayerColor, PlayerState> ColorToPlayerStateMock = new HashMap<>();
    private Map<Resource, ResourceDeck> decks = new HashMap<>();



    @BeforeEach
    void setUp(){
        boardMock = EasyMock.createMock(BoardHandler.class);
        lumberDeckMock = EasyMock.createMock(ResourceDeck.class);
        brickDeckMock = EasyMock.createMock(ResourceDeck.class);
        grainDeckMock = EasyMock.createMock(ResourceDeck.class);
        oreDeckMock = EasyMock.createMock(ResourceDeck.class);
        woolDeckMock = EasyMock.createMock(ResourceDeck.class);
        decks = Map.of(
                Resource.LUMBER, lumberDeckMock,
                Resource.BRICK, brickDeckMock,
                Resource.GRAIN, grainDeckMock,
                Resource.WOOL, woolDeckMock,
                Resource.ORE, oreDeckMock
        );
    }

    @Test
    void attemptBuildSettlement_test01_BoardHandlerSucceeds_EnoughResources_UnderMaxCount_ExpectSuccess(){
        PlayerState redStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.expect(boardMock.buildSettlement(PlayerColor.RED, 0)).andReturn(true);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            EasyMock.expect(redStateMock.reduceResources(r, 1)).andReturn(true);
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
        oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.attemptBuildSettlement(0);

        EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_test02_BoardHandlerFails_EnoughResources_UnderMaxCount_ExpectError(){
        PlayerState whiteStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.WHITE, whiteStateMock
        );

        EasyMock.expect(boardMock.buildSettlement(PlayerColor.WHITE, 0))
                .andThrow(new IllegalSettlementPlacementException("Can not place a settlement at this node"));

        EasyMock.replay(whiteStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.WHITE);
        Exception exception = assertThrows(IllegalSettlementPlacementException.class,
                () -> model.attemptBuildSettlement(0));

        assertEquals("Can not place a settlement at this node", exception.getMessage());

        EasyMock.verify(whiteStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

}
