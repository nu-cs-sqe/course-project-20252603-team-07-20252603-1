package domain.model;

import domain.model.board.BoardHandler;
import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.*;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.resources.Resource;
import domain.model.resources.ResourceDeck;
import org.easymock.EasyMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


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

        boardMock.buildSettlement(redStateMock, 0);
        EasyMock.expectLastCall();

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            redStateMock.updateResources(r, -1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        redStateMock.increaseSettlementCount();
        EasyMock.expectLastCall();

        redStateMock.updateVictoryPoints(1);
        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.SETUP);

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

        boardMock.buildSettlement(whiteStateMock, 0);
        EasyMock.expectLastCall().andThrow(new IllegalSettlementPlacementException("Can not place a settlement at this node"));

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

        boardMock.buildSettlement(blueStateMock, 0);
        EasyMock.expectLastCall();

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            blueStateMock.updateResources(r, -1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }

        blueStateMock.increaseSettlementCount();
        EasyMock.expectLastCall();
        blueStateMock.updateVictoryPoints(1);
        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.SETUP);

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
        boardMock.addRoad(redStateMock, 0, 1);
        EasyMock.expectLastCall();

        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            redStateMock.updateResources(r, -1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }
        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.SETUP);

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

        boardMock.addRoad(whiteStateMock, 0, 1);
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

    @Test // BVA TC8 — ROAD_BUILDING_DEV_CARD is an alternate valid phase for road building
    void attemptBuildRoad_roadBuildingDevCard_succeeds() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, playerMock);

        boardMock.addRoad(playerMock, 0, 1);
        EasyMock.expectLastCall();

        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.SETUP);

        EasyMock.replay(playerMock, boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.ROAD_BUILDING_DEV_CARD);
        model.attemptBuildRoad(0, 1);

        EasyMock.verify(playerMock, boardMock);
    }

    @Test
    void attemptBuildCity_test01_EnoughResources_BoardSucceeds_ExpectSuccess(){
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(3);
        EasyMock.expect(redStateMock.getResourceCount(Resource.GRAIN)).andReturn(2);

        boardMock.buildCity(redStateMock, 0);
        EasyMock.expectLastCall();

        redStateMock.updateResources(Resource.ORE, -3);
        EasyMock.expectLastCall();
        oreDeckMock.replenish(3);
        EasyMock.expectLastCall();

        redStateMock.updateResources(Resource.GRAIN, -2);
        EasyMock.expectLastCall();
        grainDeckMock.replenish(2);
        EasyMock.expectLastCall();

        redStateMock.updateVictoryPoints(1);

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

        boardMock.buildCity(orangeStateMock, 0);
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
        Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.attemptBuildRoad(0, 1));
        assertEquals("Not proper phase for that action", exception.getMessage());
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
        Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.attemptBuildSettlement(0));
        assertEquals("Not proper phase for that action", exception.getMessage());
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
        Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.attemptBuildCity(0));
        assertEquals("Not proper phase for that action", exception.getMessage());
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
        Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.performTurn(6));
        assertEquals("Not proper phase for that action", exception.getMessage());
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
        Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.endTurn());
        assertEquals("Not proper phase for that action", exception.getMessage());
    }

    @Test
    void endTurn_fromMoveRobber_expectError() {
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);
        Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.endTurn());
        assertEquals("Not proper phase for that action", exception.getMessage());
    }

    // --- BVA: resource amount boundaries for attemptBuildSettlement ---
    // Resource check order (EnumSet natural order): BRICK, GRAIN, LUMBER, WOOL

    @Test
    void attemptBuildSettlement_zeroGrain_insufficientResources() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.ORANGE, playerMock);
        EasyMock.expect(playerMock.getSettlementCount()).andReturn(0);
        EasyMock.expect(playerMock.getResourceCount(Resource.BRICK)).andReturn(1);
        EasyMock.expect(playerMock.getResourceCount(Resource.GRAIN)).andReturn(0);
        EasyMock.replay(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.attemptBuildSettlement(0));
        assertEquals("Insufficient resources", exception.getMessage());
        EasyMock.verify(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_zeroLumber_insufficientResources() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.BLUE, playerMock);
        EasyMock.expect(playerMock.getSettlementCount()).andReturn(0);
        EasyMock.expect(playerMock.getResourceCount(Resource.BRICK)).andReturn(1);
        EasyMock.expect(playerMock.getResourceCount(Resource.GRAIN)).andReturn(1);
        EasyMock.expect(playerMock.getResourceCount(Resource.LUMBER)).andReturn(0);
        EasyMock.replay(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.attemptBuildSettlement(0));
        assertEquals("Insufficient resources", exception.getMessage());
        EasyMock.verify(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_zeroWool_insufficientResources() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.WHITE, playerMock);
        EasyMock.expect(playerMock.getSettlementCount()).andReturn(0);
        EasyMock.expect(playerMock.getResourceCount(Resource.BRICK)).andReturn(1);
        EasyMock.expect(playerMock.getResourceCount(Resource.GRAIN)).andReturn(1);
        EasyMock.expect(playerMock.getResourceCount(Resource.LUMBER)).andReturn(1);
        EasyMock.expect(playerMock.getResourceCount(Resource.WOOL)).andReturn(0);
        EasyMock.replay(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.attemptBuildSettlement(0));
        assertEquals("Insufficient resources", exception.getMessage());
        EasyMock.verify(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_surplusResources_succeeds() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, playerMock);
        EasyMock.expect(playerMock.getSettlementCount()).andReturn(0);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            EasyMock.expect(playerMock.getResourceCount(r)).andReturn(2);
        }
        boardMock.buildSettlement(playerMock, 0);
        EasyMock.expectLastCall();
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER, Resource.WOOL, Resource.GRAIN)) {
            playerMock.updateResources(r, -1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }
        playerMock.increaseSettlementCount();
        EasyMock.expectLastCall();
        playerMock.updateVictoryPoints(1);
        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.SETUP);
        EasyMock.replay(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildSettlement(0);
        EasyMock.verify(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
    }

    @Test
    void attemptBuildSettlement_SetupPhase_ExpectSuccess_ExpectNoResourcesReduced(){
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        boardMock.buildSetupSettlement(blueStateMock, 0);
        EasyMock.expectLastCall();

        EasyMock.replay(boardMock, blueStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentGamePhase(GamePhase.SETUP_PHASE);
        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.attemptBuildSettlement(0);

        EasyMock.verify(boardMock, blueStateMock);
    }

    // --- BVA: resource amount boundaries for attemptBuildRoad ---

    @Test
    void attemptBuildRoad_zeroLumber_insufficientResources() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, playerMock);
        EasyMock.expect(playerMock.getResourceCount(Resource.BRICK)).andReturn(1);
        EasyMock.expect(playerMock.getResourceCount(Resource.LUMBER)).andReturn(0);
        EasyMock.replay(playerMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock, boardMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.attemptBuildRoad(0, 1));
        assertEquals("Insufficient resources", exception.getMessage());
        EasyMock.verify(playerMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock, boardMock);
    }

    @Test
    void attemptBuildRoad_surplusResources_succeeds() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.BLUE, playerMock);
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            EasyMock.expect(playerMock.getResourceCount(r)).andReturn(2);
        }
        boardMock.addRoad(playerMock, 0, 1);
        EasyMock.expectLastCall();
        for (Resource r : EnumSet.of(Resource.BRICK, Resource.LUMBER)) {
            playerMock.updateResources(r, -1);
            EasyMock.expectLastCall();
            decks.get(r).replenish();
            EasyMock.expectLastCall();
        }
        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.SETUP);
        EasyMock.replay(playerMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock, boardMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildRoad(0, 1);
        EasyMock.verify(playerMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock, boardMock);
    }

    @Test
    void attemptBuildRoad_RoadBuildingDevCardPhase_ExpectSuccess_ExpectNoResourcesReduced(){
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        boardMock.addRoad(blueStateMock, 0, 3);
        EasyMock.expectLastCall();
        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.SETUP);

        EasyMock.replay(boardMock, blueStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentGamePhase(GamePhase.ROAD_BUILDING_DEV_CARD);
        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.attemptBuildRoad(0, 3);

        EasyMock.verify(boardMock, blueStateMock);
    }

    @Test
    void attemptBuildRoad_SetupPhase_ExpectSuccess_ExpectNoResourcesReduced(){
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        boardMock.buildSetupSettlement(blueStateMock, 0);
        EasyMock.expectLastCall();
        boardMock.buildSetupRoad(blueStateMock, 0, 0, 3);
        EasyMock.expectLastCall();

        EasyMock.replay(boardMock, blueStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentGamePhase(GamePhase.SETUP_PHASE);
        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.attemptBuildSettlement(0);
        model.attemptBuildRoad(0, 3);

        EasyMock.verify(boardMock, blueStateMock);
    }


    // --- BVA: resource amount boundaries for attemptBuildCity ---

    @Test
    void attemptBuildCity_zeroOre_insufficientResources() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, playerMock);
        EasyMock.expect(playerMock.getResourceCount(Resource.ORE)).andReturn(0);
        EasyMock.replay(playerMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.attemptBuildCity(0));
        assertEquals("Insufficient resources", exception.getMessage());
        EasyMock.verify(playerMock);
    }

    @Test
    void attemptBuildCity_zeroGrain_insufficientResources() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.BLUE, playerMock);
        EasyMock.expect(playerMock.getResourceCount(Resource.ORE)).andReturn(3);
        EasyMock.expect(playerMock.getResourceCount(Resource.GRAIN)).andReturn(0);
        EasyMock.replay(playerMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.attemptBuildCity(0));
        assertEquals("Insufficient resources", exception.getMessage());
        EasyMock.verify(playerMock);
    }

    @Test
    void attemptBuildCity_surplusResources_succeeds() {
        Player playerMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.WHITE, playerMock);
        EasyMock.expect(playerMock.getResourceCount(Resource.ORE)).andReturn(4);
        EasyMock.expect(playerMock.getResourceCount(Resource.GRAIN)).andReturn(3);
        boardMock.buildCity(playerMock, 0);
        EasyMock.expectLastCall();
        playerMock.updateResources(Resource.ORE, -3);
        EasyMock.expectLastCall();
        oreDeckMock.replenish(3);
        EasyMock.expectLastCall();
        playerMock.updateResources(Resource.GRAIN, -2);
        EasyMock.expectLastCall();
        grainDeckMock.replenish(2);
        EasyMock.expectLastCall();
        playerMock.updateVictoryPoints(1);
        EasyMock.replay(playerMock, boardMock, oreDeckMock, grainDeckMock);
        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.attemptBuildCity(0);
        EasyMock.verify(playerMock, boardMock, oreDeckMock, grainDeckMock);
    }

    @Test
    void updateVictoryPoints_RedReceives1_ExpectSuccess() {
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock
        );;

        redStateMock.updateVictoryPoints(1);
        EasyMock.expectLastCall();

        EasyMock.replay(redStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.updateVictoryPoints(PlayerColor.RED, 1);

        EasyMock.verify(redStateMock);

    }

    @Test
    void updateVictoryPoints_OrangeReceives2_ExpectSuccess() {
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.ORANGE, orangeStateMock
        );;

        orangeStateMock.updateVictoryPoints(2);
        EasyMock.expectLastCall();

        EasyMock.replay(orangeStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.updateVictoryPoints(PlayerColor.ORANGE, 2);

        EasyMock.verify(orangeStateMock);
    }

    @Test
    void updateVictoryPoints_WhiteLoses2_ExpectSuccess() {
        Player whiteStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.WHITE, whiteStateMock
        );;

        whiteStateMock.updateVictoryPoints(-2);
        EasyMock.expectLastCall();

        EasyMock.replay(whiteStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.updateVictoryPoints(PlayerColor.WHITE, -2);

        EasyMock.verify(whiteStateMock);
    }

    @Test
    void updateVictoryPoints_BlueGains2_ExpectSuccess() {
        Player blueStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.BLUE, blueStateMock
        );;

        blueStateMock.updateVictoryPoints(2);
        EasyMock.expectLastCall();

        EasyMock.replay(blueStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.updateVictoryPoints(PlayerColor.BLUE, 2);

        EasyMock.verify(blueStateMock);
    }

    // checkCurrentPlayerHasTenOrMoreVictoryPoints()

    @Test
    void checkCurrentPlayer10OrMorePoints_RedHas0_ExpectSamePhase() {
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock
        );

        EasyMock.expect(redStateMock.getVictoryPoints()).andReturn(0);

        EasyMock.replay(redStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.checkCurrentPlayerHasTenOrMoreVictoryPoints();
        assertEquals(GamePhase.GENERAL_PLAY,model.getCurrentPhase());

        EasyMock.verify(redStateMock);
    }

    @Test
    void checkCurrentPlayer10OrMorePoints_WhiteHas9_ExpectSamePhase() {
        Player whiteStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.WHITE, whiteStateMock
        );

        EasyMock.expect(whiteStateMock.getVictoryPoints()).andReturn(0);

        EasyMock.replay(whiteStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.checkCurrentPlayerHasTenOrMoreVictoryPoints();
        assertEquals(GamePhase.GENERAL_PLAY,model.getCurrentPhase());

        EasyMock.verify(whiteStateMock);
    }

    @Test
    void checkCurrentPlayer10OrMorePoints_OrangeHas10_ExpectEndPhase() {
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.ORANGE, orangeStateMock
        );

        EasyMock.expect(orangeStateMock.getVictoryPoints()).andReturn(10);

        EasyMock.replay(orangeStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.checkCurrentPlayerHasTenOrMoreVictoryPoints();

        assertEquals(GamePhase.END_GAME, model.getCurrentPhase());

        EasyMock.verify(orangeStateMock);
    }

    @Test
    void checkCurrentPlayer10OrMorePoints_BlueHas11_ExpectEndPhase() {
        Player blueStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.BLUE, blueStateMock
        );

        EasyMock.expect(blueStateMock.getVictoryPoints()).andReturn(11);

        EasyMock.replay(blueStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.checkCurrentPlayerHasTenOrMoreVictoryPoints();

        assertEquals(GamePhase.END_GAME, model.getCurrentPhase());

        EasyMock.verify(blueStateMock);
    }

    // endTurn() tests

    @Test
    void endTurn_RedHasEnoughVictoryPoints_ExpectEndGame() {
        Player redStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player blueStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock,
                PlayerColor.ORANGE, orangeStateMock,
                PlayerColor.WHITE, whiteStateMock,
                PlayerColor.BLUE, blueStateMock
        );

        EasyMock.expect(redStateMock.getVictoryPoints()).andReturn(10);

        EasyMock.replay(redStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.endTurn();

        assertEquals(PlayerColor.RED, model.getCurrentPlayerColor());
        assertEquals(GamePhase.END_GAME, model.getCurrentPhase());
    }

    @Test
    void endTurn_OrangeHasEnoughVictoryPoints_ExpectEndGame() {
        Player redStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player blueStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(
                PlayerColor.RED, redStateMock,
                PlayerColor.ORANGE, orangeStateMock,
                PlayerColor.WHITE, whiteStateMock,
                PlayerColor.BLUE, blueStateMock
        );

        EasyMock.expect(orangeStateMock.getVictoryPoints()).andReturn(11);

        EasyMock.replay(orangeStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.endTurn();

        assertEquals(PlayerColor.ORANGE, model.getCurrentPlayerColor());
        assertEquals(GamePhase.END_GAME, model.getCurrentPhase());
    }

    @Test
    void endTurn_WhiteDoesNotHaveEnoughVictoryPoints_ExpectNextTurn() {
        Player redStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player blueStateMock = EasyMock.createMock(Player.class);
        List<Player> playerList = List.of(redStateMock, orangeStateMock, whiteStateMock, blueStateMock);
        BoardHandler boardStub = EasyMock.createNiceMock(BoardHandler.class);

        EasyMock.expect(redStateMock.getColor()).andReturn(PlayerColor.RED);
        EasyMock.expect(orangeStateMock.getColor()).andReturn(PlayerColor.ORANGE);
        EasyMock.expect(whiteStateMock.getColor()).andReturn(PlayerColor.WHITE);
        EasyMock.expect(blueStateMock.getColor()).andReturn(PlayerColor.BLUE);

        EasyMock.expect(whiteStateMock.getVictoryPoints()).andReturn(9);

        EasyMock.replay(whiteStateMock, blueStateMock, redStateMock, orangeStateMock);

        GameModel model = new GameModel(playerList, boardStub);

        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.setCurrentPlayerColor(PlayerColor.WHITE);
        model.setCurrentPlayerIndex(2);
        model.endTurn();

        assertEquals(PlayerColor.BLUE, model.getCurrentPlayerColor());
        assertEquals(GamePhase.BEFORE_ROLL, model.getCurrentPhase());

        EasyMock.verify(whiteStateMock);
    }

    @Test
    void endTurn_BlueDoesNotHaveEnoughVictoryPoints_ExpectNextTurn() {
        Player redStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player blueStateMock = EasyMock.createMock(Player.class);
        List<Player> playerList = List.of(redStateMock, orangeStateMock, whiteStateMock, blueStateMock);
        BoardHandler boardStub = EasyMock.createNiceMock(BoardHandler.class);

        EasyMock.expect(redStateMock.getColor()).andReturn(PlayerColor.RED);
        EasyMock.expect(orangeStateMock.getColor()).andReturn(PlayerColor.ORANGE);
        EasyMock.expect(whiteStateMock.getColor()).andReturn(PlayerColor.WHITE);
        EasyMock.expect(blueStateMock.getColor()).andReturn(PlayerColor.BLUE);

        EasyMock.expect(blueStateMock.getVictoryPoints()).andReturn(9);

        EasyMock.replay(whiteStateMock, blueStateMock, redStateMock, orangeStateMock);

        GameModel model = new GameModel(playerList, boardStub);

        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.setCurrentPlayerColor(PlayerColor.BLUE);
        model.setCurrentPlayerIndex(3);
        model.endTurn();

        assertEquals(PlayerColor.RED, model.getCurrentPlayerColor());
        assertEquals(GamePhase.BEFORE_ROLL, model.getCurrentPhase());

        EasyMock.verify(blueStateMock);
    }

    @Test
    void endTurn_RedDoesNotHaveEnoughVictoryPoints_ExpectNextTurn() {
        Player redStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player blueStateMock = EasyMock.createMock(Player.class);
        List<Player> playerList = List.of(redStateMock, orangeStateMock, whiteStateMock, blueStateMock);
        BoardHandler boardStub = EasyMock.createNiceMock(BoardHandler.class);

        EasyMock.expect(redStateMock.getColor()).andReturn(PlayerColor.RED);
        EasyMock.expect(orangeStateMock.getColor()).andReturn(PlayerColor.ORANGE);
        EasyMock.expect(whiteStateMock.getColor()).andReturn(PlayerColor.WHITE);
        EasyMock.expect(blueStateMock.getColor()).andReturn(PlayerColor.BLUE);

        EasyMock.expect(redStateMock.getVictoryPoints()).andReturn(9);

        EasyMock.replay(whiteStateMock, blueStateMock, redStateMock, orangeStateMock);

        GameModel model = new GameModel(playerList, boardStub);

        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentPlayerIndex(0);
        model.endTurn();

        assertEquals(PlayerColor.ORANGE, model.getCurrentPlayerColor());
        assertEquals(GamePhase.BEFORE_ROLL, model.getCurrentPhase());

        EasyMock.verify(redStateMock);
    }

    @Test
    void endTurn_OrangeDoesNotHaveEnoughVictoryPoints_ExpectNextTurn() {
        Player redStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player blueStateMock = EasyMock.createMock(Player.class);
        List<Player> playerList = List.of(redStateMock, orangeStateMock, whiteStateMock, blueStateMock);
        BoardHandler boardStub = EasyMock.createNiceMock(BoardHandler.class);

        EasyMock.expect(redStateMock.getColor()).andReturn(PlayerColor.RED);
        EasyMock.expect(orangeStateMock.getColor()).andReturn(PlayerColor.ORANGE);
        EasyMock.expect(whiteStateMock.getColor()).andReturn(PlayerColor.WHITE);
        EasyMock.expect(blueStateMock.getColor()).andReturn(PlayerColor.BLUE);

        EasyMock.expect(orangeStateMock.getVictoryPoints()).andReturn(9);

        EasyMock.replay(whiteStateMock, blueStateMock, redStateMock, orangeStateMock);

        GameModel model = new GameModel(playerList, boardStub);

        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentPlayerIndex(1);
        model.endTurn();

        assertEquals(PlayerColor.WHITE, model.getCurrentPlayerColor());
        assertEquals(GamePhase.BEFORE_ROLL, model.getCurrentPhase());

        EasyMock.verify(orangeStateMock);
    }
    @ParameterizedTest
    @EnumSource(value = GamePhase.class, names = {
            "BEFORE_ROLL",
            "RESOURCE_PRODUCTION",
            "MOVE_ROBBER",
            "MONOPOLY_DEV_CARD",
            "ROAD_BUILDING_DEV_CARD",
            "OFFERING_TRADE"})
    void endTurn_WrongPhase_ExpectError(GamePhase phase) {
        Player redStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player blueStateMock = EasyMock.createMock(Player.class);
        List<Player> playerList = List.of(redStateMock, orangeStateMock, whiteStateMock, blueStateMock);
        BoardHandler boardStub = EasyMock.createNiceMock(BoardHandler.class);

        EasyMock.expect(redStateMock.getColor()).andReturn(PlayerColor.RED);
        EasyMock.expect(orangeStateMock.getColor()).andReturn(PlayerColor.ORANGE);
        EasyMock.expect(whiteStateMock.getColor()).andReturn(PlayerColor.WHITE);
        EasyMock.expect(blueStateMock.getColor()).andReturn(PlayerColor.BLUE);

        GameModel model = new GameModel(playerList, boardStub);

        model.setCurrentGamePhase(phase);
        model.setCurrentPlayerColor(PlayerColor.ORANGE);
        model.setCurrentPlayerIndex(1);
        Exception exception = assertThrows(IllegalGamePhaseException.class,
            model::endTurn);

        assertEquals("Not proper phase for that action", exception.getMessage());
        assertEquals(PlayerColor.ORANGE, model.getCurrentPlayerColor());
        assertEquals(phase, model.getCurrentPhase());
    }

    // handleLongestRoad()

    @Test
    void handleLongestRoad_NoOneQualifies_ExpectPlayerSetup() {
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.SETUP);

        EasyMock.replay(boardMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        assertEquals(PlayerColor.SETUP, model.getCurrentLongestRoadPlayerColor());

        model.handleLongestRoad();

        assertEquals(PlayerColor.SETUP, model.getCurrentLongestRoadPlayerColor());

        EasyMock.verify(boardMock);
    }

    @Test
    void handleLongestRoad_RedQualifies_RedStillQualifies_ExpectPlayerRED() {
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.RED)
                )
        ).andReturn(PlayerColor.RED);

        EasyMock.replay(boardMock, redStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.setCurrentLongestRoadPlayerColor(PlayerColor.RED);

        model.handleLongestRoad();

        assertEquals(PlayerColor.RED, model.getCurrentLongestRoadPlayerColor());

        EasyMock.verify(boardMock, redStateMock);
    }

    @Test
    void handleLongestRoad_CurrentlySetup_BecomesWhite_ExpectPlayerWhite_WhiteGains2Points() {
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.SETUP)
                )
        ).andReturn(PlayerColor.WHITE);

        whiteStateMock.updateVictoryPoints(2);
        EasyMock.expectLastCall();
        EasyMock.replay(boardMock, whiteStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);

        model.handleLongestRoad();

        assertEquals(PlayerColor.WHITE, model.getCurrentLongestRoadPlayerColor());

        EasyMock.verify(boardMock, whiteStateMock);
    }

    @Test
    void handleLongestRoad_CurrentlyBlue_BecomesOrange_ExpectPlayerOrange_OrangeGains2Points_BlueLoses2Points() {
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.BLUE)
                )
        ).andReturn(PlayerColor.ORANGE);
        orangeStateMock.updateVictoryPoints(2);
        EasyMock.expectLastCall();
        blueStateMock.updateVictoryPoints(-2);
        EasyMock.expectLastCall();

        EasyMock.replay(boardMock, orangeStateMock, blueStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentLongestRoadPlayerColor(PlayerColor.BLUE);
        model.handleLongestRoad();

        assertEquals(PlayerColor.ORANGE, model.getCurrentLongestRoadPlayerColor());

        EasyMock.verify(boardMock, orangeStateMock, blueStateMock);
    }

    @Test
    void handleLongestRoad_CurrentlyOrange_BecomesBlue_ExpectPlayerBlue_BlueGains2Points_OrangeLoses2Points() {
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.ORANGE)
                )
        ).andReturn(PlayerColor.BLUE);
        blueStateMock.updateVictoryPoints(2);
        EasyMock.expectLastCall();
        orangeStateMock.updateVictoryPoints(-2);
        EasyMock.expectLastCall();

        EasyMock.replay(boardMock, orangeStateMock, blueStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentLongestRoadPlayerColor(PlayerColor.ORANGE);
        model.handleLongestRoad();

        assertEquals(PlayerColor.BLUE, model.getCurrentLongestRoadPlayerColor());

        EasyMock.verify(boardMock, orangeStateMock, blueStateMock);
    }

    @Test
    void handleLongestRoad_CurrentlyWhite_BecomesRed_ExpectPlayerRed_RedGains2Points_WhiteLoses2Points() {
        Player blueStateMock = EasyMock.createMock(Player.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        Player whiteStateMock = EasyMock.createMock(Player.class);
        Player orangeStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock.put(PlayerColor.BLUE, blueStateMock);
        ColorToPlayerObjMock.put(PlayerColor.RED, redStateMock);
        ColorToPlayerObjMock.put(PlayerColor.WHITE, whiteStateMock);
        ColorToPlayerObjMock.put(PlayerColor.ORANGE, orangeStateMock);

        EasyMock.expect(
                boardMock.calculateLongestRoad(
                        EasyMock.<List<Player>>anyObject(),
                        EasyMock.eq(PlayerColor.WHITE)
                )
        ).andReturn(PlayerColor.RED);
        redStateMock.updateVictoryPoints(2);
        EasyMock.expectLastCall();
        whiteStateMock.updateVictoryPoints(-2);
        EasyMock.expectLastCall();

        EasyMock.replay(boardMock, redStateMock, whiteStateMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentLongestRoadPlayerColor(PlayerColor.WHITE);
        model.handleLongestRoad();

        assertEquals(PlayerColor.RED, model.getCurrentLongestRoadPlayerColor());

        EasyMock.verify(boardMock, redStateMock, whiteStateMock);
    }

    // buyDevCard() tests

    // TC1: GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=1 (exact cost), deck=25 (full)
    //      -> card returned; player loses 1 each ORE/WOOL/GRAIN; ORE/WOOL/GRAIN decks each replenished by 1
    @Test
    void buyDevCard_ExactResources_FullDeck_ExpectCardReturnedAndResourcesDeducted() throws EmptyDeckException {
        DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
        DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

        EasyMock.expect(deckMock.drawCard(0)).andReturn(cardMock);
        redStateMock.updateResources(Resource.ORE, -1);
        redStateMock.updateResources(Resource.WOOL, -1);
        redStateMock.updateResources(Resource.GRAIN, -1);
        oreDeckMock.replenish();
        woolDeckMock.replenish();
        grainDeckMock.replenish();
        redStateMock.addDevelopmentCard(cardMock);

        EasyMock.replay(redStateMock, deckMock, cardMock, oreDeckMock, woolDeckMock, grainDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

        DevelopmentCard result = model.buyDevCard(deckMock);
        assertEquals(cardMock, result);

        EasyMock.verify(redStateMock, deckMock, cardMock, oreDeckMock, woolDeckMock, grainDeckMock);
    }

    // TC2: GENERAL_PLAY, ORE=3, WOOL=2, GRAIN=4 (surplus each), deck=25 (full)
    //      -> card returned; player loses 1 each ORE/WOOL/GRAIN; surplus does not prevent purchase
    @Test
    void buyDevCard_SurplusResources_FullDeck_ExpectCardReturnedAndResourcesDeducted() throws EmptyDeckException {
        DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
        DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

        EasyMock.expect(deckMock.drawCard(0)).andReturn(cardMock);
        redStateMock.updateResources(Resource.ORE, -1);
        redStateMock.updateResources(Resource.WOOL, -1);
        redStateMock.updateResources(Resource.GRAIN, -1);
        oreDeckMock.replenish();
        woolDeckMock.replenish();
        grainDeckMock.replenish();
        redStateMock.addDevelopmentCard(cardMock);

        EasyMock.replay(redStateMock, deckMock, cardMock, oreDeckMock, woolDeckMock, grainDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

        DevelopmentCard result = model.buyDevCard(deckMock);
        assertEquals(cardMock, result);

        EasyMock.verify(redStateMock, deckMock, cardMock, oreDeckMock, woolDeckMock, grainDeckMock);
    }

    // TC3: GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=1, deck=1 (last card)
    //      -> card returned; deck countRemaining = 0
    @Test
    void buyDevCard_ExactResources_LastCardInDeck_ExpectCardReturnedAndResourcesDeducted() throws EmptyDeckException {
        DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
        DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
        Player redStateMock = EasyMock.createMock(Player.class);
        ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

        EasyMock.expect(deckMock.drawCard(0)).andReturn(cardMock);
        redStateMock.updateResources(Resource.ORE, -1);
        redStateMock.updateResources(Resource.WOOL, -1);
        redStateMock.updateResources(Resource.GRAIN, -1);
        oreDeckMock.replenish();
        woolDeckMock.replenish();
        grainDeckMock.replenish();
        redStateMock.addDevelopmentCard(cardMock);

        EasyMock.replay(redStateMock, deckMock, cardMock, oreDeckMock, woolDeckMock, grainDeckMock);

        GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
                oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock);
        model.setCurrentPlayerColor(PlayerColor.RED);
        model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

        DevelopmentCard result = model.buyDevCard(deckMock);
        assertEquals(cardMock, result);

        EasyMock.verify(redStateMock, deckMock, cardMock, oreDeckMock, woolDeckMock, grainDeckMock);
    }
}
