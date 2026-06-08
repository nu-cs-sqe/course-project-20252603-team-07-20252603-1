package domain.model;

import domain.model.board.BoardHandler;
import domain.model.exceptions.*;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.Player;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
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
    private Map<PlayerColor, Player> ColorToPlayerObjMock = new HashMap<>();
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
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.expect(redStateMock.getSettlementCount()).andReturn(0);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            EasyMock.expect(redStateMock.getResourceCount(r)).andReturn(1);
        }

        EasyMock.expect(boardMock.buildSettlement(PlayerColor.RED, 0)).andReturn(true);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            redStateMock.updateResources(r, -1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        redStateMock.increaseSettlementCount();
        EasyMock.expectLastCall();

        EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
        oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildSettlement(0);

        EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_test02_BoardHandlerFails_EnoughResources_UnderMaxCount_ExpectError(){
        Player whiteStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
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
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

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
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.ORANGE, orangeStateMock
        );

        EasyMock.expect(orangeStateMock.getSettlementCount()).andReturn(0);

        EasyMock.expect(orangeStateMock.getResourceCount(Resource.BRICK)).andReturn(0);

        EasyMock.replay(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

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
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.expect(redStateMock.getSettlementCount()).andReturn(5);

        EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

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
        Player blueStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.BLUE, blueStateMock
        );

        EasyMock.expect(blueStateMock.getSettlementCount()).andReturn(4);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            EasyMock.expect(blueStateMock.getResourceCount(r)).andReturn(1);
        }

        EasyMock.expect(boardMock.buildSettlement(PlayerColor.BLUE, 0)).andReturn(true);

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            blueStateMock.updateResources(r, -1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        blueStateMock.increaseSettlementCount();
        EasyMock.expectLastCall();

        EasyMock.replay(blueStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildSettlement(0);

        EasyMock.verify(blueStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_test06_IncorrectPhase_ExpectError(){
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                woolDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

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
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            EasyMock.expect(redStateMock.getResourceCount(r)).andReturn(1);
        }

        boardMock.addRoad(PlayerColor.RED, 0, 1);
        EasyMock.expectLastCall();

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            redStateMock.updateResources(r, -1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        EasyMock.replay(redStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);



        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildRoad(0, 1);

        EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

    }

    @Test
    void attemptBuildRoad_test02_BoardHandlerFails_ExpectError(){
        Player whiteStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
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
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);



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
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.ORANGE, orangeStateMock
        );

        EasyMock.expect(orangeStateMock.getResourceCount(Resource.BRICK)).andReturn(0);

        EasyMock.replay(orangeStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

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
        Player blueStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.BLUE, blueStateMock
        );

        EasyMock.replay(blueStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

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
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(3);
        EasyMock.expect(redStateMock.getResourceCount(Resource.GRAIN)).andReturn(2);

        boardMock.buildCity(PlayerColor.RED, 0);
        EasyMock.expectLastCall();

        redStateMock.updateResources(Resource.ORE, -3);
        EasyMock.expectLastCall();
        oreDeckMock.replenish(3);
        EasyMock.expectLastCall();

        redStateMock.updateResources(Resource.GRAIN, -2);
        EasyMock.expectLastCall();
        grainDeckMock.replenish(2);
        EasyMock.expectLastCall();

        EasyMock.replay(redStateMock, boardMock, oreDeckMock, grainDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildCity(0);

        EasyMock.verify(redStateMock, boardMock, oreDeckMock, grainDeckMock);
    }

    @Test
    void attemptBuildCity_test02_NotEnoughOre_ExpectError(){
        Player whiteStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.WHITE, whiteStateMock
        );

        EasyMock.expect(whiteStateMock.getResourceCount(Resource.ORE)).andReturn(2);

        EasyMock.replay(whiteStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class,
                () -> model.attemptBuildCity(0));

        assertEquals("Insufficient resources", exception.getMessage());

        EasyMock.verify(whiteStateMock);
    }

    @Test
    void attemptBuildCity_test03_NotEnoughGrain_ExpectError(){
        Player whiteStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.WHITE, whiteStateMock
        );

        EasyMock.expect(whiteStateMock.getResourceCount(Resource.ORE)).andReturn(4);
        EasyMock.expect(whiteStateMock.getResourceCount(Resource.GRAIN)).andReturn(1);

        EasyMock.replay(whiteStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class,
                () -> model.attemptBuildCity(0));

        assertEquals("Insufficient resources", exception.getMessage());

        EasyMock.verify(whiteStateMock);
    }

    @Test
    void attemptBuildCity_test04_EnoughResources_BoardFails_ExpectError(){
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.ORANGE, orangeStateMock
        );

        EasyMock.expect(orangeStateMock.getResourceCount(Resource.ORE)).andReturn(3);
        EasyMock.expect(orangeStateMock.getResourceCount(Resource.GRAIN)).andReturn(2);

        boardMock.buildCity(PlayerColor.ORANGE, 0);
        EasyMock.expectLastCall().andThrow(new IllegalArgumentException());

        EasyMock.replay(orangeStateMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows (IllegalCityPlacementException.class,
                () -> model.attemptBuildCity(0));

        assertEquals("Can not place city at specified node", exception.getMessage());

        EasyMock.verify(orangeStateMock, boardMock);
    }

    @Test
    void attemptBuildCity_test05_IllegalPhase_ExpectError(){
        Player blueStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.ORANGE, blueStateMock
        );;



        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentGamePhase(GamePhase.ROAD_BUILDING_DEV_CARD);
        Exception exception = assertThrows (IllegalGamePhaseException.class,
                () -> model.attemptBuildCity(0));

        assertEquals("Not proper phase for that action", exception.getMessage());

    }

    // --- phase transition tests ---

    @Test
    void newGameModel_startsInBeforeRollPhase() {
        BoardHandler board = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(board);
        GameModel model = new GameModel(List.of(new Player("Alice", PlayerColor.RED)), board);
        assertEquals(GamePhase.BEFORE_ROLL, model.getCurrentPhase());
        EasyMock.verify(board);
    }

    @Test
    void performTurn_rollSix_nonSeven_transitionsToGeneralPlay() {
        BoardHandler board = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(board);
        GameModel model = new GameModel(List.of(new Player("Alice", PlayerColor.RED)), board);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        model.performTurn(6);
        assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
        EasyMock.verify(board);
    }

    // BVA: minimum non-7 dice total
    @Test
    void performTurn_rollTwo_BVAMin_transitionsToGeneralPlay() {
        BoardHandler board = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(board);
        GameModel model = new GameModel(List.of(new Player("Alice", PlayerColor.RED)), board);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        model.performTurn(2);
        assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
        EasyMock.verify(board);
    }

    // BVA: maximum dice total
    @Test
    void performTurn_rollTwelve_BVAMax_transitionsToGeneralPlay() {
        BoardHandler board = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(board);
        GameModel model = new GameModel(List.of(new Player("Alice", PlayerColor.RED)), board);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        model.performTurn(12);
        assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
        EasyMock.verify(board);
    }

    @Test
    void performTurn_rollSeven_transitionsToMoveRobber() {
        BoardHandler board = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(board);
        GameModel model = new GameModel(List.of(new Player("Alice", PlayerColor.RED)), board);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        model.performTurn(7);
        assertEquals(GamePhase.MOVE_ROBBER, model.getCurrentPhase());
        EasyMock.verify(board);
    }

    @Test
    void attemptBuildRoad_beforeRoll_expectError() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, playerMock);
        EasyMock.replay(playerMock, boardMock, lumberDeckMock, brickDeckMock,
                grainDeckMock, oreDeckMock, woolDeckMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        assertThrows(IllegalGamePhaseException.class, () -> model.attemptBuildRoad(0, 1));
        EasyMock.verify(playerMock, boardMock, lumberDeckMock, brickDeckMock,
                grainDeckMock, oreDeckMock, woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_beforeRoll_expectError() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, playerMock);
        EasyMock.replay(playerMock, boardMock, lumberDeckMock, brickDeckMock,
                grainDeckMock, oreDeckMock, woolDeckMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        assertThrows(IllegalGamePhaseException.class, () -> model.attemptBuildSettlement(0));
        EasyMock.verify(playerMock, boardMock, lumberDeckMock, brickDeckMock,
                grainDeckMock, oreDeckMock, woolDeckMock);
    }

    @Test
    void attemptBuildCity_beforeRoll_expectError() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.ORANGE, playerMock);
        EasyMock.replay(playerMock, boardMock, lumberDeckMock, brickDeckMock,
                grainDeckMock, oreDeckMock, woolDeckMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        assertThrows(IllegalGamePhaseException.class, () -> model.attemptBuildCity(0));
        EasyMock.verify(playerMock, boardMock, lumberDeckMock, brickDeckMock,
                grainDeckMock, oreDeckMock, woolDeckMock);
    }

    @Test
    void performTurn_rollingTwiceInOneTurn_expectError() {
        BoardHandler board = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(board);
        GameModel model = new GameModel(List.of(new Player("Alice", PlayerColor.RED)), board);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        model.performTurn(6);
        assertThrows(IllegalGamePhaseException.class, () -> model.performTurn(6));
        EasyMock.verify(board);
    }

    @Test
    void endTurn_fromGeneralPlay_advancesPlayerAndResetsToBeforeRoll() {
        Player alice = new Player("Alice", PlayerColor.RED);
        Player bob = new Player("Bob", PlayerColor.BLUE);
        BoardHandler board = EasyMock.createMock(BoardHandler.class);
        EasyMock.replay(board);
        GameModel model = new GameModel(List.of(alice, bob), board);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.endTurn();
        assertEquals(GamePhase.BEFORE_ROLL, model.getCurrentPhase());
        assertEquals(bob, model.getCurrentPlayer());
        EasyMock.verify(board);
    }

    @Test
    void endTurn_fromBeforeRoll_expectError() {
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
        assertThrows(IllegalGamePhaseException.class, () -> model.endTurn());
    }

    @Test
    void endTurn_fromMoveRobber_expectError() {
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);
        assertThrows(IllegalGamePhaseException.class, () -> model.endTurn());
    }
}
