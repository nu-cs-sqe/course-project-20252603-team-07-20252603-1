package domain.model;

import domain.model.board.BoardHandler;
import domain.model.exceptions.IllegalGamePhaseException;
import domain.model.exceptions.IllegalRoadPlacementException;
import domain.model.exceptions.IllegalSettlementPlacementException;
import domain.model.exceptions.InsufficientResourcesException;
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

        EasyMock.expect(redStateMock.getSettlementCount()).andReturn(0);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            EasyMock.expect(redStateMock.getResourceCount(r)).andReturn(1);
        }

        EasyMock.expect(boardMock.buildSettlement(PlayerColor.RED, 0)).andReturn(true);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            redStateMock.reduceResources(r, 1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        redStateMock.increaseSettlementCount();
        EasyMock.expectLastCall();

        EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
        oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
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

        EasyMock.expect(whiteStateMock.getSettlementCount()).andReturn(0);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            EasyMock.expect(whiteStateMock.getResourceCount(r)).andReturn(1);
        }

        EasyMock.expect(boardMock.buildSettlement(PlayerColor.WHITE, 0))
                .andThrow(new IllegalSettlementPlacementException("Can not place a settlement at this node"));

        EasyMock.replay(whiteStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(IllegalSettlementPlacementException.class,
                () -> model.attemptBuildSettlement(0));

        assertEquals("Can not place a settlement at this node", exception.getMessage());

        EasyMock.verify(whiteStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_test03_BoardHandlerSucceeds_NotEnoughResources_UnderMaxCount_ExpectError(){
        PlayerState orangeStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.ORANGE, orangeStateMock
        );

        EasyMock.expect(orangeStateMock.getSettlementCount()).andReturn(0);

        EasyMock.expect(orangeStateMock.getResourceCount(Resource.BRICK)).andReturn(0);

        EasyMock.replay(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class,
                () -> model.attemptBuildSettlement(0));

        assertEquals("Insufficient resources", exception.getMessage());

        EasyMock.verify(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_test04_BoardHandlerSucceeds_EnoughResources_AtMaxCount_ExpectError(){
        PlayerState redStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.expect(redStateMock.getSettlementCount()).andReturn(5);

        EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(IllegalSettlementPlacementException.class,
                () -> model.attemptBuildSettlement(0));

        assertEquals("Can not have more than 5 settlements", exception.getMessage());

        EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_test05_BoardHandlerSucceeds_EnoughResources_UnderMaxCount_ExpectSuccess(){
        PlayerState blueStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.BLUE, blueStateMock
        );

        EasyMock.expect(blueStateMock.getSettlementCount()).andReturn(4);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            EasyMock.expect(blueStateMock.getResourceCount(r)).andReturn(1);
        }

        EasyMock.expect(boardMock.buildSettlement(PlayerColor.BLUE, 0)).andReturn(true);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            blueStateMock.reduceResources(r, 1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        blueStateMock.increaseSettlementCount();
        EasyMock.expectLastCall();

        EasyMock.replay(blueStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildSettlement(0);

        EasyMock.verify(blueStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_test06_IncorrectPhase_ExpectError(){
        PlayerState redStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.RESOURCE_PRODUCTION);
        Exception exception = assertThrows(IllegalGamePhaseException.class,
                () -> model.attemptBuildSettlement(0));

        assertEquals("Not proper phase for that action", exception.getMessage());

        EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

    @Test
    void attemptBuildRoad_test01_BoardHandlerSucceeds_EnoughResources_ExpectSuccess(){
        PlayerState redStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            EasyMock.expect(redStateMock.getResourceCount(r)).andReturn(1);
        }

        boardMock.addRoad(PlayerColor.RED, 0, 1);
        EasyMock.expectLastCall();

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            redStateMock.reduceResources(r, 1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        EasyMock.replay(redStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);



        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildRoad(0, 1);

        EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

    }

    @Test
    void attemptBuildRoad_test02_BoardHandlerFails_ExpectError(){
        PlayerState whiteStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.WHITE, whiteStateMock
        );

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            EasyMock.expect(whiteStateMock.getResourceCount(r)).andReturn(1);
        }

        boardMock.addRoad(PlayerColor.WHITE, 0, 1);
        EasyMock.expectLastCall().andThrow(new IllegalRoadPlacementException("Can not place road at this edge"));


        EasyMock.replay(whiteStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);



        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(IllegalRoadPlacementException.class,
                () -> model.attemptBuildRoad(0, 1));

        assertEquals("Can not place road at this edge", exception.getMessage());

        EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

    }

    @Test
    void attemptBuildRoad_test03_BoardHandlerSucceeds_NotEnoughResources_ExpectError(){
        PlayerState orangeStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.ORANGE, orangeStateMock
        );

        EasyMock.expect(orangeStateMock.getResourceCount(Resource.BRICK)).andReturn(0);

        EasyMock.replay(orangeStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class,
                () -> model.attemptBuildRoad(0, 1));

        assertEquals("Insufficient resources", exception.getMessage());

        EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

    }

    @Test
    void attemptBuildRoad_test04_IncorrectGamePhase_ExpectError(){
        PlayerState blueStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.BLUE, blueStateMock
        );

        EasyMock.replay(blueStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.setCurrentGamePhase(GamePhase.RESOURCE_PRODUCTION);
        Exception exception = assertThrows(IllegalGamePhaseException.class,
                () -> model.attemptBuildRoad(0, 1));

        assertEquals("Not proper phase for that action", exception.getMessage());

        EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

    }

    @Test
    void attemptBuildCity_test01_EnoughResources_BoardSucceeds_ExpectSuccess(){
        PlayerState redStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(3);
        EasyMock.expect(redStateMock.getResourceCount(Resource.GRAIN)).andReturn(2);

        boardMock.buildCity(PlayerColor.RED, 0);
        EasyMock.expectLastCall();

        redStateMock.reduceResources(Resource.ORE, 3);
        EasyMock.expectLastCall();
        oreDeckMock.replenish(3);
        EasyMock.expectLastCall();

        redStateMock.reduceResources(Resource.GRAIN, 2);
        EasyMock.expectLastCall();
        grainDeckMock.replenish(2);
        EasyMock.expectLastCall();

        EasyMock.replay(redStateMock, boardMock, oreDeckMock, grainDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildCity(0);

        EasyMock.verify(redStateMock, boardMock, oreDeckMock, grainDeckMock);
    }

    @Test
    void attemptBuildCity_test02_NotEnoughOre_ExpectError(){
        PlayerState whiteStateMock = EasyMock.createMock(PlayerState.class);
        ColorToPlayerStateMock = Map.of(
                PlayerColor.WHITE, whiteStateMock
        );

        EasyMock.expect(whiteStateMock.getResourceCount(Resource.ORE)).andReturn(2);

        EasyMock.replay(whiteStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerStateMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class,
                () -> model.attemptBuildCity(0));

        assertEquals("Insufficient resources", exception.getMessage());

        EasyMock.verify(whiteStateMock);
    }
}
