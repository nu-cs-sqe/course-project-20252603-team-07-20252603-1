package domain.model;

import domain.model.board.BoardHandler;
import domain.model.developmentcards.DevelopmentCard;
import domain.model.developmentcards.DevelopmentCardDeck;
import domain.model.developmentcards.DevelopmentCardType;
import domain.model.board.Port;
import domain.model.board.PortTradeRequest;
import domain.model.exceptions.*;
import domain.model.player.Player;
import domain.model.player.PlayerColor;
import domain.model.player.TradeManager;
import domain.model.player.TradeOffer;
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
  private TradeManager tradeManagerMock;
  private Random randomMock;


  @BeforeEach
  void setUp() {
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
    tradeManagerMock = EasyMock.createMock(TradeManager.class);
    randomMock = EasyMock.createMock(Random.class);
  }

  @Test
  void attemptBuildSettlement_test01_BoardHandlerSucceeds_EnoughResources_UnderMaxCount_ExpectSuccess() {
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.attemptBuildSettlement(0);

    EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);
  }

  @Test
  void attemptBuildSettlement_test02_BoardHandlerFails_EnoughResources_UnderMaxCount_ExpectError() {
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.WHITE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(IllegalSettlementPlacementException.class,
            () -> model.attemptBuildSettlement(0));

    assertEquals("Can not place a settlement at this node", exception.getMessage());

    EasyMock.verify(whiteStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);
  }

  @Test
  void attemptBuildSettlement_test03_BoardHandlerSucceeds_NotEnoughResources_UnderMaxCount_ExpectError() {
    Player orangeStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.ORANGE, orangeStateMock
    );

    EasyMock.expect(orangeStateMock.getSettlementCount()).andReturn(0);

    EasyMock.expect(orangeStateMock.getResourceCount(Resource.BRICK)).andReturn(0);

    EasyMock.replay(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.ORANGE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(InsufficientResourcesException.class,
            () -> model.attemptBuildSettlement(0));

    assertEquals("Insufficient resources", exception.getMessage());

    EasyMock.verify(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);
  }

  @Test
  void attemptBuildSettlement_test04_BoardHandlerSucceeds_EnoughResources_AtMaxCount_ExpectError() {
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.RED, redStateMock
    );

    EasyMock.expect(redStateMock.getSettlementCount()).andReturn(5);

    EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(IllegalSettlementPlacementException.class,
            () -> model.attemptBuildSettlement(0));

    assertEquals("Can not have more than 5 settlements", exception.getMessage());

    EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);
  }

  @Test
  void attemptBuildSettlement_test05_BoardHandlerSucceeds_EnoughResources_UnderMaxCount_ExpectSuccess() {
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.BLUE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.attemptBuildSettlement(0);

    EasyMock.verify(blueStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);
  }

  @Test
  void attemptBuildSettlement_test06_IncorrectPhase_ExpectError() {
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.RED, redStateMock
    );

    EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.RESOURCE_PRODUCTION);
    Exception exception = assertThrows(IllegalGamePhaseException.class,
            () -> model.attemptBuildSettlement(0));

    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            woolDeckMock);
  }

  @Test
  void attemptBuildRoad_test01_BoardHandlerSucceeds_EnoughResources_ExpectSuccess() {
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);


    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.attemptBuildRoad(0, 1);

    EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, boardMock);

  }

  @Test
  void attemptBuildRoad_test02_BoardHandlerFails_ExpectError() {
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);


    model.setCurrentPlayerColor(PlayerColor.WHITE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(IllegalRoadPlacementException.class,
            () -> model.attemptBuildRoad(0, 1));

    assertEquals("Can not place road at this edge", exception.getMessage());

    EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, boardMock);

  }

  @Test
  void attemptBuildRoad_test03_BoardHandlerSucceeds_NotEnoughResources_ExpectError() {
    Player orangeStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.ORANGE, orangeStateMock
    );

    EasyMock.expect(orangeStateMock.getResourceCount(Resource.BRICK)).andReturn(0);

    EasyMock.replay(orangeStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, boardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.ORANGE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(InsufficientResourcesException.class,
            () -> model.attemptBuildRoad(0, 1));

    assertEquals("Insufficient resources", exception.getMessage());

    EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, boardMock);

  }

  @Test
  void attemptBuildRoad_test04_IncorrectGamePhase_ExpectError() {
    Player blueStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.BLUE, blueStateMock
    );

    EasyMock.replay(blueStateMock, lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, boardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.BLUE);
    model.setCurrentGamePhase(GamePhase.RESOURCE_PRODUCTION);
    Exception exception = assertThrows(IllegalGamePhaseException.class,
            () -> model.attemptBuildRoad(0, 1));

    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, boardMock);

  }

  @Test
    // BVA TC8 — ROAD_BUILDING_DEV_CARD is an alternate valid phase for road building
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.ROAD_BUILDING_DEV_CARD);
    model.attemptBuildRoad(0, 1);

    EasyMock.verify(playerMock, boardMock);
  }

  @Test
  void attemptBuildCity_test01_EnoughResources_BoardSucceeds_ExpectSuccess() {
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.attemptBuildCity(0);

    EasyMock.verify(redStateMock, boardMock, oreDeckMock, grainDeckMock);
  }

  @Test
  void attemptBuildCity_test02_NotEnoughOre_ExpectError() {
    Player whiteStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.WHITE, whiteStateMock
    );

    EasyMock.expect(whiteStateMock.getResourceCount(Resource.ORE)).andReturn(2);

    EasyMock.replay(whiteStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.WHITE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(InsufficientResourcesException.class,
            () -> model.attemptBuildCity(0));

    assertEquals("Insufficient resources", exception.getMessage());

    EasyMock.verify(whiteStateMock);
  }

  @Test
  void attemptBuildCity_test03_NotEnoughGrain_ExpectError() {
    Player whiteStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.WHITE, whiteStateMock
    );

    EasyMock.expect(whiteStateMock.getResourceCount(Resource.ORE)).andReturn(4);
    EasyMock.expect(whiteStateMock.getResourceCount(Resource.GRAIN)).andReturn(1);

    EasyMock.replay(whiteStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.WHITE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(InsufficientResourcesException.class,
            () -> model.attemptBuildCity(0));

    assertEquals("Insufficient resources", exception.getMessage());

    EasyMock.verify(whiteStateMock);
  }

  @Test
  void attemptBuildCity_test04_EnoughResources_BoardFails_ExpectError() {
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.ORANGE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(IllegalCityPlacementException.class,
            () -> model.attemptBuildCity(0));

    assertEquals("Can not place city at specified node", exception.getMessage());

    EasyMock.verify(orangeStateMock, boardMock);
  }

  @Test
  void attemptBuildCity_test05_IllegalPhase_ExpectError() {
    Player blueStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.ORANGE, blueStateMock
    );
    ;

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.ORANGE);
    model.setCurrentGamePhase(GamePhase.ROAD_BUILDING_DEV_CARD);
    Exception exception = assertThrows(IllegalGamePhaseException.class,
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
    EasyMock.expect(board.computeResourceDemand(2)).andReturn(new HashMap<>());
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
    EasyMock.expect(board.computeResourceDemand(12)).andReturn(new HashMap<>());
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
    EasyMock.expect(board.computeResourceDemand(6)).andReturn(new HashMap<>());
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.endTurn());
    assertEquals("Not proper phase for that action", exception.getMessage());
  }

  @Test
  void endTurn_fromMoveRobber_expectError() {
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.attemptBuildSettlement(0);
    EasyMock.verify(playerMock, boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, woolDeckMock);
  }

  @Test
  void attemptBuildSettlement_SetupPhase_ExpectSuccess_ExpectNoResourcesReduced() {
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
    blueStateMock.updateVictoryPoints(1);
    EasyMock.expectLastCall();

    EasyMock.replay(boardMock, blueStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.BLUE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.attemptBuildRoad(0, 1);
    EasyMock.verify(playerMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock, boardMock);
  }

  @Test
  void attemptBuildRoad_RoadBuildingDevCardPhase_ExpectSuccess_ExpectNoResourcesReduced() {
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentGamePhase(GamePhase.ROAD_BUILDING_DEV_CARD);
    model.setCurrentPlayerColor(PlayerColor.BLUE);
    model.attemptBuildRoad(0, 3);

    EasyMock.verify(boardMock, blueStateMock);
  }

  @Test
  void attemptBuildRoad_SetupPhase_ExpectSuccess_ExpectNoResourcesReduced() {
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
    blueStateMock.updateVictoryPoints(1);
    EasyMock.expectLastCall();
    boardMock.buildSetupRoad(blueStateMock, 0, 0, 3);
    EasyMock.expectLastCall();

    EasyMock.replay(boardMock, blueStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
    );
    ;

    redStateMock.updateVictoryPoints(1);
    EasyMock.expectLastCall();

    EasyMock.replay(redStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.updateVictoryPoints(PlayerColor.RED, 1);

    EasyMock.verify(redStateMock);

  }

  @Test
  void updateVictoryPoints_OrangeReceives2_ExpectSuccess() {
    Player orangeStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.ORANGE, orangeStateMock
    );
    ;

    orangeStateMock.updateVictoryPoints(2);
    EasyMock.expectLastCall();

    EasyMock.replay(orangeStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.updateVictoryPoints(PlayerColor.ORANGE, 2);

    EasyMock.verify(orangeStateMock);
  }

  @Test
  void updateVictoryPoints_WhiteLoses2_ExpectSuccess() {
    Player whiteStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.WHITE, whiteStateMock
    );
    ;

    whiteStateMock.updateVictoryPoints(-2);
    EasyMock.expectLastCall();

    EasyMock.replay(whiteStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.updateVictoryPoints(PlayerColor.WHITE, -2);

    EasyMock.verify(whiteStateMock);
  }

  @Test
  void updateVictoryPoints_BlueGains2_ExpectSuccess() {
    Player blueStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(
            PlayerColor.BLUE, blueStateMock
    );
    ;

    blueStateMock.updateVictoryPoints(2);
    EasyMock.expectLastCall();

    EasyMock.replay(blueStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.checkCurrentPlayerHasTenOrMoreVictoryPoints();
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.WHITE);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.checkCurrentPlayerHasTenOrMoreVictoryPoints();
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentLongestRoadPlayerColor(PlayerColor.WHITE);
    model.handleLongestRoad();

    assertEquals(PlayerColor.RED, model.getCurrentLongestRoadPlayerColor());

    EasyMock.verify(boardMock, redStateMock, whiteStateMock);
  }

  // buyDevCard() tests

  // --- distributeResources (via performTurn) ---

  @Test
  void performTurn_bankHasEnough_playerReceivesResource() throws EmptyDeckException {
    Player redMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redMock);
    EasyMock.expect(boardMock.computeResourceDemand(6))
            .andReturn(Map.of(Resource.WOOL, new HashMap<>(Map.of(redMock, 1))));
    EasyMock.expect(woolDeckMock.drawMultiple(1)).andReturn(1);
    redMock.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();
    EasyMock.replay(redMock, boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.performTurn(6);
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(redMock, boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
  }

  @Test
  void performTurn_bankEmpty_playerReceivesNothing() throws EmptyDeckException {
    Player redMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redMock);
    EasyMock.expect(boardMock.computeResourceDemand(6))
            .andReturn(Map.of(Resource.WOOL, new HashMap<>(Map.of(redMock, 1))));
    EasyMock.expect(woolDeckMock.drawMultiple(1)).andReturn(0);
    EasyMock.replay(redMock, boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.performTurn(6);
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(redMock, boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
  }

  @Test
  void performTurn_bankLessThanTotalDemand_nobodyReceivesResource() {
    Player redMock = EasyMock.createMock(Player.class);
    Player blueMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redMock, PlayerColor.BLUE, blueMock);
    Map<Player, Integer> playerAmounts = new HashMap<>();
    playerAmounts.put(redMock, 1);
    playerAmounts.put(blueMock, 1);
    EasyMock.expect(boardMock.computeResourceDemand(6))
            .andReturn(Map.of(Resource.WOOL, playerAmounts));
    EasyMock.expect(woolDeckMock.getTotalCards()).andReturn(1);
    EasyMock.replay(redMock, blueMock, boardMock, woolDeckMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock);
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.performTurn(6);
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(redMock, blueMock, boardMock, woolDeckMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock);
  }

  @Test
  void performTurn_bankExactlyEnough_allPlayersReceive() throws EmptyDeckException {
    Player redMock = EasyMock.createMock(Player.class);
    Player blueMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redMock, PlayerColor.BLUE, blueMock);
    Map<Player, Integer> playerAmounts = new HashMap<>();
    playerAmounts.put(redMock, 1);
    playerAmounts.put(blueMock, 1);
    EasyMock.expect(boardMock.computeResourceDemand(6))
            .andReturn(Map.of(Resource.WOOL, playerAmounts));
    EasyMock.expect(woolDeckMock.getTotalCards()).andReturn(2);
    EasyMock.expect(woolDeckMock.drawMultiple(1)).andReturn(1);
    EasyMock.expect(woolDeckMock.drawMultiple(1)).andReturn(1);
    redMock.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();
    blueMock.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();
    EasyMock.replay(redMock, blueMock, boardMock, woolDeckMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock);
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.performTurn(6);
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(redMock, blueMock, boardMock, woolDeckMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock);
  }

  @Test
  void performTurn_cityPlayer_receivesTwo() throws EmptyDeckException {
    Player redMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redMock);
    EasyMock.expect(boardMock.computeResourceDemand(8))
            .andReturn(Map.of(Resource.ORE, new HashMap<>(Map.of(redMock, 2))));
    EasyMock.expect(oreDeckMock.drawMultiple(2)).andReturn(2);
    redMock.updateResources(Resource.ORE, 2);
    EasyMock.expectLastCall();
    EasyMock.replay(redMock, boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.performTurn(8);
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(redMock, boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
  }

  @Test
  void performTurn_singlePlayerBankShort_receivesPartial() throws EmptyDeckException {
    Player redMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redMock);
    EasyMock.expect(boardMock.computeResourceDemand(6))
            .andReturn(Map.of(Resource.WOOL, new HashMap<>(Map.of(redMock, 3))));
    EasyMock.expect(woolDeckMock.drawMultiple(3)).andReturn(2);
    redMock.updateResources(Resource.WOOL, 2);
    EasyMock.expectLastCall();
    EasyMock.replay(redMock, boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.performTurn(6);
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(redMock, boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
  }

  @Test
  void performTurn_emptyDemandMap_noInteractionsWithDecksOrPlayers() {
    ColorToPlayerObjMock = new HashMap<>();
    EasyMock.expect(boardMock.computeResourceDemand(6)).andReturn(new HashMap<>());
    EasyMock.replay(boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.performTurn(6);
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(boardMock, woolDeckMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock);
  }

  @Test
  void performTurn_oneResourceCovered_otherNot_onlyCoveredDistributed() throws EmptyDeckException {
    Player redMock = EasyMock.createMock(Player.class);
    Player blueMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redMock, PlayerColor.BLUE, blueMock);
    Map<Player, Integer> oreAmounts = new HashMap<>();
    oreAmounts.put(redMock, 1);
    oreAmounts.put(blueMock, 1);
    Map<Resource, Map<Player, Integer>> demand = new HashMap<>();
    demand.put(Resource.WOOL, new HashMap<>(Map.of(redMock, 1)));
    demand.put(Resource.ORE, oreAmounts);
    EasyMock.expect(boardMock.computeResourceDemand(6)).andReturn(demand);
    EasyMock.expect(woolDeckMock.drawMultiple(1)).andReturn(1);
    redMock.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();
    EasyMock.expect(oreDeckMock.getTotalCards()).andReturn(1);
    EasyMock.replay(redMock, blueMock, boardMock, woolDeckMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock);
    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.performTurn(6);
    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(redMock, blueMock, boardMock, woolDeckMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock);

  }

  // TC1: GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=1 (exact cost), deck=25 (full)
  //      -> card returned; player loses 1 each ORE/WOOL/GRAIN; ORE/WOOL/GRAIN decks each replenished by 1
  @Test
  void buyDevCard_ExactResources_FullDeck_ExpectCardReturnedAndResourcesDeducted() throws EmptyDeckException {
    DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
    DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);
    EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.WOOL)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.GRAIN)).andReturn(1);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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

    EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(3);
    EasyMock.expect(redStateMock.getResourceCount(Resource.WOOL)).andReturn(2);
    EasyMock.expect(redStateMock.getResourceCount(Resource.GRAIN)).andReturn(4);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
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

    EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.WOOL)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.GRAIN)).andReturn(1);
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
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    DevelopmentCard result = model.buyDevCard(deckMock);
    assertEquals(cardMock, result);

    EasyMock.verify(redStateMock, deckMock, cardMock, oreDeckMock, woolDeckMock, grainDeckMock);
  }

  // TC4: GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=1, deck=0 (empty)
  //      -> EmptyDeckException; player resources NOT deducted
  @Test
  void buyDevCard_EmptyDeck_ExpectEmptyDeckExceptionAndNoResourceDeduction() throws EmptyDeckException {
    DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.WOOL)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.GRAIN)).andReturn(1);
    EasyMock.expect(deckMock.drawCard(0)).andThrow(new EmptyDeckException("Cannot draw new DevelopmentCard, no cards remain."));

    EasyMock.replay(redStateMock, deckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    Exception exception = assertThrows(EmptyDeckException.class, () -> model.buyDevCard(deckMock));
    assertEquals("Cannot draw new DevelopmentCard, no cards remain.", exception.getMessage());

    EasyMock.verify(redStateMock, deckMock);
  }

  // TC5: GENERAL_PLAY, ORE=0 (below cost boundary)
  //      -> InsufficientResourcesException
  @Test
  void buyDevCard_InsufficientOre_ExpectInsufficientResourcesException() throws EmptyDeckException {
    DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(0);

    EasyMock.replay(redStateMock, deckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.buyDevCard(deckMock));
    assertEquals("Insufficient resources", exception.getMessage());

    EasyMock.verify(redStateMock, deckMock);
  }

  // TC6: GENERAL_PLAY, ORE=1, WOOL=0 (below cost boundary, ORE already >= 1)
  //      -> InsufficientResourcesException
  @Test
  void buyDevCard_InsufficientWool_ExpectInsufficientResourcesException() throws EmptyDeckException {
    DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.WOOL)).andReturn(0);

    EasyMock.replay(redStateMock, deckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.buyDevCard(deckMock));
    assertEquals("Insufficient resources", exception.getMessage());

    EasyMock.verify(redStateMock, deckMock);
  }

  // TC7: GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=0 (below cost boundary, ORE/WOOL already >= 1)
  //      -> InsufficientResourcesException
  @Test
  void buyDevCard_InsufficientGrain_ExpectInsufficientResourcesException() throws EmptyDeckException {
    DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.expect(redStateMock.getResourceCount(Resource.ORE)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.WOOL)).andReturn(1);
    EasyMock.expect(redStateMock.getResourceCount(Resource.GRAIN)).andReturn(0);

    EasyMock.replay(redStateMock, deckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    Exception exception = assertThrows(InsufficientResourcesException.class, () -> model.buyDevCard(deckMock));
    assertEquals("Insufficient resources", exception.getMessage());

    EasyMock.verify(redStateMock, deckMock);
  }

  // TC8: BEFORE_ROLL (invalid phase)
  //      -> IllegalGamePhaseException
  @Test
  void buyDevCard_BeforeRollPhase_ExpectIllegalGamePhaseException() throws EmptyDeckException {
    DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.replay(redStateMock, deckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);

    Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.buyDevCard(deckMock));
    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(redStateMock, deckMock);
  }

  // TC9: MOVE_ROBBER (invalid phase)
  //      -> IllegalGamePhaseException
  @Test
  void buyDevCard_MoveRobberPhase_ExpectIllegalGamePhaseException() throws EmptyDeckException {
    DevelopmentCardDeck deckMock = EasyMock.createMock(DevelopmentCardDeck.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.replay(redStateMock, deckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);

    Exception exception = assertThrows(IllegalGamePhaseException.class, () -> model.buyDevCard(deckMock));
    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(redStateMock, deckMock);
  }

  // playDevCard() tests

  // TC1: card = null
  //      -> IllegalArgumentException: "Development card cannot be null."
  @Test
  void playDevCard_NullCard_ExpectIllegalArgumentException() {
    EasyMock.replay(boardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> model.playDevCard(null));
    assertEquals("Development card cannot be null.", exception.getMessage());

    EasyMock.verify(boardMock);
  }

  // TC2: MOVE_ROBBER (invalid phase), valid card
  //      -> IllegalGamePhaseException: "Not proper phase for that action"
  @Test
  void playDevCard_InvalidPhase_ExpectIllegalGamePhaseException() {
    DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
    EasyMock.replay(boardMock, cardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);

    Exception exception = assertThrows(IllegalGamePhaseException.class,
            () -> model.playDevCard(cardMock));
    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(boardMock, cardMock);
  }

  // TC3: GENERAL_PLAY, card type = KNIGHT
  //      -> phase transitions to MOVE_ROBBER
  @Test
  void playDevCard_GeneralPlayKnightCard_ExpectPhaseMovesToMoveRobber() {
    DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
    EasyMock.expect(cardMock.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.replay(boardMock, cardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    model.playDevCard(cardMock);

    assertEquals(GamePhase.MOVE_ROBBER, model.getCurrentPhase());
    EasyMock.verify(boardMock, cardMock);
  }

  // TC4: GENERAL_PLAY, card type = ROAD_BUILDER
  //      -> phase transitions to ROAD_BUILDING_DEV_CARD
  @Test
  void playDevCard_GeneralPlayRoadBuilderCard_ExpectPhaseMovesToRoadBuildingDevCard() {
    DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
    EasyMock.expect(cardMock.getType()).andReturn(DevelopmentCardType.ROAD_BUILDER);
    EasyMock.replay(boardMock, cardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    model.playDevCard(cardMock);

    assertEquals(GamePhase.ROAD_BUILDING_DEV_CARD, model.getCurrentPhase());
    EasyMock.verify(boardMock, cardMock);
  }

  // TC5: GENERAL_PLAY, card type = MONOPOLY
  //      -> phase transitions to MONOPOLY_DEV_CARD
  @Test
  void playDevCard_GeneralPlayMonopolyCard_ExpectPhaseMovesToMonopolyDevCard() {
    DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
    EasyMock.expect(cardMock.getType()).andReturn(DevelopmentCardType.MONOPOLY);
    EasyMock.replay(boardMock, cardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    model.playDevCard(cardMock);

    assertEquals(GamePhase.MONOPOLY_DEV_CARD, model.getCurrentPhase());
    EasyMock.verify(boardMock, cardMock);
  }

  // TC6: GENERAL_PLAY, card type = YEAR_OF_PLENTY
  //      -> phase unchanged (remains GENERAL_PLAY)
  @Test
  void playDevCard_GeneralPlayYearOfPlentyCard_ExpectPhaseUnchanged() {
    DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
    EasyMock.expect(cardMock.getType()).andReturn(DevelopmentCardType.YEAR_OF_PLENTY);
    EasyMock.replay(boardMock, cardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    model.playDevCard(cardMock);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(boardMock, cardMock);
  }

  // TC7: GENERAL_PLAY, card type = VICTORY_POINT
  //      -> phase unchanged (remains GENERAL_PLAY)
  @Test
  void playDevCard_GeneralPlayVictoryPointCard_ExpectPhaseUnchanged() {
    DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
    EasyMock.expect(cardMock.getType()).andReturn(DevelopmentCardType.VICTORY_POINT);
    EasyMock.replay(boardMock, cardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    model.playDevCard(cardMock);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());
    EasyMock.verify(boardMock, cardMock);
  }

  // TC8: BEFORE_ROLL, card type = KNIGHT
  //      -> phase transitions to MOVE_ROBBER
  @Test
  void playDevCard_BeforeRollKnightCard_ExpectPhaseMovesToMoveRobber() {
    DevelopmentCard cardMock = EasyMock.createMock(DevelopmentCard.class);
    EasyMock.expect(cardMock.getType()).andReturn(DevelopmentCardType.KNIGHT);
    EasyMock.replay(boardMock, cardMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);

    model.playDevCard(cardMock);

    assertEquals(GamePhase.MOVE_ROBBER, model.getCurrentPhase());
    EasyMock.verify(boardMock, cardMock);
  }

  // Attempt Port Trade Tests

  // Test Case 1
  @Test
  void attemptPortTrade_test01_GeneralPlay_BankHasOneCard_ValidTrade_ExpectSuccess() throws EmptyDeckException {
    Player redStateMock = EasyMock.createMock(Player.class);
    Port portMock = EasyMock.createMock(Port.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    portMock.executePortTrade(
            EasyMock.eq(redStateMock),
            EasyMock.eq(boardMock),
            EasyMock.anyObject(PortTradeRequest.class));
    EasyMock.expectLastCall();

    EasyMock.replay(redStateMock, boardMock, portMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.attemptPortTrade(portMock, Resource.WOOL, Resource.ORE);

    EasyMock.verify(redStateMock, boardMock, portMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 2
  @Test
  void attemptPortTrade_test02_GeneralPlay_BankHasZeroCards_ExpectIllegalStateException() throws EmptyDeckException {
    Player redStateMock = EasyMock.createMock(Player.class);
    Port portMock = EasyMock.createMock(Port.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    portMock.executePortTrade(
            EasyMock.eq(redStateMock),
            EasyMock.eq(boardMock),
            EasyMock.anyObject(PortTradeRequest.class));
    EasyMock.expectLastCall().andThrow(
            new EmptyDeckException("Bank has insufficient resources for this trade."));

    EasyMock.replay(redStateMock, boardMock, portMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(IllegalStateException.class,
            () -> model.attemptPortTrade(portMock, Resource.WOOL, Resource.ORE));

    assertEquals("Bank has insufficient resources for this trade.", exception.getMessage());

    EasyMock.verify(redStateMock, boardMock, portMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 3
  @Test
  void attemptPortTrade_test03_GeneralPlay_BankHasNineteenCards_ValidTrade_ExpectSuccess() throws EmptyDeckException {
    Player redStateMock = EasyMock.createMock(Player.class);
    Port portMock = EasyMock.createMock(Port.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    portMock.executePortTrade(
            EasyMock.eq(redStateMock),
            EasyMock.eq(boardMock),
            EasyMock.anyObject(PortTradeRequest.class));
    EasyMock.expectLastCall();

    EasyMock.replay(redStateMock, boardMock, portMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.attemptPortTrade(portMock, Resource.WOOL, Resource.ORE);

    EasyMock.verify(redStateMock, boardMock, portMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 4
  @Test
  void attemptPortTrade_test04_BeforeRoll_ExpectIllegalGamePhaseException() throws EmptyDeckException {
    Player redStateMock = EasyMock.createMock(Player.class);
    Port portMock = EasyMock.createMock(Port.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.replay(redStateMock, boardMock, portMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    Exception exception = assertThrows(IllegalGamePhaseException.class,
            () -> model.attemptPortTrade(portMock, Resource.WOOL, Resource.ORE));

    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(redStateMock, boardMock, portMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Player Trade Requests
  // Test Case 1
  @Test
  void offerTrade_test01_GeneralPlay_ValidOffer_ExpectSuccess() {
    Player redStateMock = EasyMock.createMock(Player.class);
    TradeManager tradeManagerMock = EasyMock.createMock(TradeManager.class);
    TradeOffer offerMock = EasyMock.createMock(TradeOffer.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    tradeManagerMock.offerTrade(offerMock);
    EasyMock.expectLastCall();

    EasyMock.replay(redStateMock, boardMock, tradeManagerMock, offerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.offerTrade(offerMock);

    assertEquals(GamePhase.OFFERING_TRADE, model.getCurrentPhase());

    EasyMock.verify(redStateMock, boardMock, tradeManagerMock, offerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 2
  @Test
  void offerTrade_test02_BeforeRoll_ExpectIllegalGamePhaseException() {
    Player redStateMock = EasyMock.createMock(Player.class);
    TradeManager tradeManagerMock = EasyMock.createMock(TradeManager.class);
    TradeOffer offerMock = EasyMock.createMock(TradeOffer.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.replay(redStateMock, boardMock, tradeManagerMock, offerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    Exception exception = assertThrows(IllegalGamePhaseException.class,
            () -> model.offerTrade(offerMock));

    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(redStateMock, boardMock, tradeManagerMock, offerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // acceptOffer() Tests
  // Test Case 1
  @Test
  void acceptTrade_test01_OfferingTrade_ValidTrade_ExpectSuccess() {
    Player redStateMock = EasyMock.createMock(Player.class);
    Player blueStateMock = EasyMock.createMock(Player.class);
    TradeManager tradeManagerMock = EasyMock.createMock(TradeManager.class);
    TradeOffer offerMock = EasyMock.createMock(TradeOffer.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock, PlayerColor.BLUE, blueStateMock);

    tradeManagerMock.acceptTrade(offerMock, blueStateMock);
    EasyMock.expectLastCall();

    EasyMock.replay(redStateMock, blueStateMock, boardMock, tradeManagerMock, offerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.OFFERING_TRADE);
    model.acceptTrade(offerMock, blueStateMock);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

    EasyMock.verify(redStateMock, blueStateMock, boardMock, tradeManagerMock, offerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 2
  @Test
  void acceptTrade_test02_GeneralPlay_ExpectIllegalGamePhaseException() {
    Player redStateMock = EasyMock.createMock(Player.class);
    Player blueStateMock = EasyMock.createMock(Player.class);
    TradeManager tradeManagerMock = EasyMock.createMock(TradeManager.class);
    TradeOffer offerMock = EasyMock.createMock(TradeOffer.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock, PlayerColor.BLUE, blueStateMock);

    EasyMock.replay(redStateMock, blueStateMock, boardMock, tradeManagerMock, offerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(IllegalGamePhaseException.class,
            () -> model.acceptTrade(offerMock, blueStateMock));

    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(redStateMock, blueStateMock, boardMock, tradeManagerMock, offerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // clearOffers Tests
  // Test Case 1
  @Test
  void clearOffers_test01_OfferingTrade_ExpectSuccess() {
    Player redStateMock = EasyMock.createMock(Player.class);
    TradeManager tradeManagerMock = EasyMock.createMock(TradeManager.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    tradeManagerMock.clearOffers();
    EasyMock.expectLastCall();

    EasyMock.replay(redStateMock, boardMock, tradeManagerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.OFFERING_TRADE);
    model.clearOffers();

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

    EasyMock.verify(redStateMock, boardMock, tradeManagerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 2
  @Test
  void clearOffers_test02_GeneralPlay_ExpectIllegalGamePhaseException() {
    Player redStateMock = EasyMock.createMock(Player.class);
    TradeManager tradeManagerMock = EasyMock.createMock(TradeManager.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.replay(redStateMock, boardMock, tradeManagerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    Exception exception = assertThrows(IllegalGamePhaseException.class,
            () -> model.clearOffers());

    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(redStateMock, boardMock, tradeManagerMock,
            lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // enterSetupPhase() / completeSetupPhase() tests

  @Test
  void enterSetupPhase_test01_FromBeforeRoll_EntersSetupPhase() {
    List<Player> players = List.of(
            new Player("A", PlayerColor.RED),
            new Player("B", PlayerColor.BLUE),
            new Player("C", PlayerColor.WHITE));
    GameModel model = new GameModel(players, boardMock);

    model.enterSetupPhase();
    assertEquals(GamePhase.SETUP_PHASE, model.getCurrentPhase());
  }

  @Test
  void enterSetupPhase_test02_WrongPhase_ExpectIllegalGamePhaseException() {
    List<Player> players = List.of(
            new Player("A", PlayerColor.RED),
            new Player("B", PlayerColor.BLUE),
            new Player("C", PlayerColor.WHITE));
    GameModel model = new GameModel(players, boardMock);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    assertThrows(IllegalGamePhaseException.class, () -> model.enterSetupPhase());
  }

  @Test
  void completeSetupPhase_test01_ResetsToFirstPlayerAndBeforeRoll() {
    List<Player> players = List.of(
            new Player("A", PlayerColor.RED),
            new Player("B", PlayerColor.BLUE),
            new Player("C", PlayerColor.WHITE));
    GameModel model = new GameModel(players, boardMock);
    model.enterSetupPhase();
    model.setCurrentPlayerIndex(2);
    model.setCurrentPlayerColor(PlayerColor.WHITE);

    model.completeSetupPhase();

    assertEquals(GamePhase.BEFORE_ROLL, model.getCurrentPhase());
    assertEquals(0, model.getCurrentPlayerIndex());
    assertEquals(PlayerColor.RED, model.getCurrentPlayerColor());
  }

  @Test
  void completeSetupPhase_test02_WrongPhase_ExpectIllegalGamePhaseException() {
    List<Player> players = List.of(
            new Player("A", PlayerColor.RED),
            new Player("B", PlayerColor.BLUE),
            new Player("C", PlayerColor.WHITE));
    GameModel model = new GameModel(players, boardMock);

    assertThrows(IllegalGamePhaseException.class, () -> model.completeSetupPhase());
  }

  // endTurn() round-increment tests

  @Test
  void endTurn_test_RoundIncrementsWhenTurnOrderWraps() {
    List<Player> players = List.of(
            new Player("A", PlayerColor.RED),
            new Player("B", PlayerColor.BLUE),
            new Player("C", PlayerColor.WHITE));
    GameModel model = new GameModel(players, boardMock);

    assertEquals(0, model.getCurrentRound());
    for (int turn = 0; turn < players.size(); turn++) {
      model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
      model.endTurn();
    }
    assertEquals(1, model.getCurrentRound());
  }

  @Test
  void endTurn_test_RoundUnchangedMidRound() {
    List<Player> players = List.of(
            new Player("A", PlayerColor.RED),
            new Player("B", PlayerColor.BLUE),
            new Player("C", PlayerColor.WHITE));
    GameModel model = new GameModel(players, boardMock);

    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);
    model.endTurn();
    assertEquals(0, model.getCurrentRound());
  }

  // moveRobberAndSteal(int targetHexID, PlayerColor victimColor) Test Cases
  // Test Case 1
  @Test
  void moveRobberAndSteal_test01_GeneralPlay_ExpectIllegalGamePhaseException() {
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.GENERAL_PLAY);

    Exception exception = assertThrows(IllegalGamePhaseException.class,
            () -> model.moveRobberAndSteal(18, PlayerColor.SETUP));

    assertEquals("Not proper phase for that action", exception.getMessage());

    EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 2
  @Test
  void moveRobberAndSteal_test02_MoveRobber0To18_NullVictim_ExpectRobberMovedNoSteal() {
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    boardMock.moveRobber(18);
    EasyMock.expectLastCall();

    EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);
    model.moveRobberAndSteal(18, PlayerColor.SETUP);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

    EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 3
  @Test
  void moveRobberAndSteal_test03_MoveRobber18To0_VictimHasNoResources_ExpectRobberMovedNoSteal() {
    Player blueStateMock = EasyMock.createMock(Player.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.BLUE, blueStateMock, PlayerColor.RED, redStateMock);

    boardMock.moveRobber(0);
    EasyMock.expectLastCall();
    EasyMock.expect(boardMock.getPlayersOnHex(0)).andReturn(Set.of(redStateMock));
    EasyMock.expect(redStateMock.getResources()).andReturn(Map.of());

    EasyMock.replay(blueStateMock, redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.BLUE);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);
    model.moveRobberAndSteal(0, PlayerColor.RED);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

    EasyMock.verify(blueStateMock, redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 4
  @Test
  void moveRobberAndSteal_test04_MoveRobber18To0_VictimHasOneResource_ExpectResourceStolen() {
    Player whiteStateMock = EasyMock.createMock(Player.class);
    Player blueStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.WHITE, whiteStateMock, PlayerColor.BLUE, blueStateMock);

    boardMock.moveRobber(0);
    EasyMock.expectLastCall();
    EasyMock.expect(boardMock.getPlayersOnHex(0)).andReturn(Set.of(blueStateMock));
    EasyMock.expect(blueStateMock.getResources()).andReturn(Map.of(Resource.WOOL, 1));
    blueStateMock.updateResources(Resource.WOOL, -1);
    EasyMock.expectLastCall();
    whiteStateMock.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(whiteStateMock, blueStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.WHITE);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);
    model.moveRobberAndSteal(0, PlayerColor.BLUE);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

    EasyMock.verify(whiteStateMock, blueStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 5
  @Test
  void moveRobberAndSteal_test05_MoveRobber18To0_VictimHasMultipleResources_ExpectRandomResourceStolen() {
    Player orangeStateMock = EasyMock.createMock(Player.class);
    Player whiteStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.ORANGE, orangeStateMock, PlayerColor.WHITE, whiteStateMock);

    boardMock.moveRobber(0);
    EasyMock.expectLastCall();
    EasyMock.expect(boardMock.getPlayersOnHex(0)).andReturn(Set.of(whiteStateMock));
    EasyMock.expect(whiteStateMock.getResources()).andReturn(
            Map.of(Resource.WOOL, 2, Resource.ORE, 1));
    EasyMock.expect(randomMock.nextInt(2)).andReturn(0);
    whiteStateMock.updateResources(EasyMock.anyObject(Resource.class), EasyMock.eq(-1));
    EasyMock.expectLastCall();
    orangeStateMock.updateResources(EasyMock.anyObject(Resource.class), EasyMock.eq(1));
    EasyMock.expectLastCall();

    EasyMock.replay(orangeStateMock, whiteStateMock, boardMock, randomMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.ORANGE);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);
    model.moveRobberAndSteal(0, PlayerColor.WHITE);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

    EasyMock.verify(orangeStateMock, whiteStateMock, boardMock, randomMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 6
  @Test
  void moveRobberAndSteal_test06_MoveRobberToNegativeOne_ExpectIllegalArgumentException() {
    Player orangeStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.ORANGE, orangeStateMock);

    boardMock.moveRobber(-1);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Cannot move Robber to invalid Hex ID"));

    EasyMock.replay(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, new Random());

    model.setCurrentPlayerColor(PlayerColor.ORANGE);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> model.moveRobberAndSteal(-1, PlayerColor.SETUP));

    assertEquals("Cannot move Robber to invalid Hex ID", exception.getMessage());

    EasyMock.verify(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 7
  @Test
  void moveRobberAndSteal_test07_MoveRobberTo19_ExpectIllegalArgumentException() {
    Player orangeStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.ORANGE, orangeStateMock);

    boardMock.moveRobber(19);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Cannot move Robber to invalid Hex ID"));

    EasyMock.replay(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, new Random());

    model.setCurrentPlayerColor(PlayerColor.ORANGE);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> model.moveRobberAndSteal(19, PlayerColor.SETUP));

    assertEquals("Cannot move Robber to invalid Hex ID", exception.getMessage());

    EasyMock.verify(orangeStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 8
  @Test
  void moveRobberAndSteal_test08_VictimOnDifferentHex_ExpectIllegalArgumentException() {
    Player redStateMock = EasyMock.createMock(Player.class);
    Player orangeStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock, PlayerColor.ORANGE, orangeStateMock);

    boardMock.moveRobber(18);
    EasyMock.expectLastCall();
    EasyMock.expect(boardMock.getPlayersOnHex(18)).andReturn(Set.of());

    EasyMock.replay(redStateMock, orangeStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, new Random());

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> model.moveRobberAndSteal(18, PlayerColor.ORANGE));

    assertEquals("Victim is not on the target hex.", exception.getMessage());

    EasyMock.verify(redStateMock, orangeStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 9
  @Test
  void moveRobberAndSteal_test09_MoveRobberToSameHex_ExpectIllegalArgumentException() {
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    boardMock.moveRobber(0);
    EasyMock.expectLastCall().andThrow(new IllegalArgumentException("Must move robber to new location"));

    EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, new Random());

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);

    Exception exception = assertThrows(IllegalArgumentException.class,
            () -> model.moveRobberAndSteal(0, PlayerColor.SETUP));

    assertEquals("Must move robber to new location", exception.getMessage());

    EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 10
  @Test
  void moveRobberAndSteal_test10_NullVictimColor_ExpectRobberMovedNoSteal() {
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    boardMock.moveRobber(18);
    EasyMock.expectLastCall();

    EasyMock.replay(redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.RED);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);
    model.moveRobberAndSteal(18, null);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

    EasyMock.verify(redStateMock, boardMock, lumberDeckMock, brickDeckMock,
            grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // Test Case 11
  @Test
  void moveRobberAndSteal_test11_VictimHasZeroValueResourceEntry_ExpectOnlyPositiveResourceStolen() {
    Player blueStateMock = EasyMock.createMock(Player.class);
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.BLUE, blueStateMock, PlayerColor.RED, redStateMock);

    boardMock.moveRobber(0);
    EasyMock.expectLastCall();
    EasyMock.expect(boardMock.getPlayersOnHex(0)).andReturn(Set.of(redStateMock));
    EasyMock.expect(redStateMock.getResources()).andReturn(Map.of(Resource.WOOL, 1, Resource.ORE, 0));
    EasyMock.expect(randomMock.nextInt(1)).andReturn(0);
    redStateMock.updateResources(Resource.WOOL, -1);
    EasyMock.expectLastCall();
    blueStateMock.updateResources(Resource.WOOL, 1);
    EasyMock.expectLastCall();

    EasyMock.replay(blueStateMock, redStateMock, boardMock, randomMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    model.setCurrentPlayerColor(PlayerColor.BLUE);
    model.setCurrentGamePhase(GamePhase.MOVE_ROBBER);
    model.moveRobberAndSteal(0, PlayerColor.RED);

    assertEquals(GamePhase.GENERAL_PLAY, model.getCurrentPhase());

    EasyMock.verify(blueStateMock, redStateMock, boardMock, randomMock, lumberDeckMock,
            brickDeckMock, grainDeckMock, oreDeckMock, woolDeckMock);
  }

  // ← REDUCES CXTY
  @Test
  void getCurrentPlayerIndex_AfterConstruction_ExpectZero() {
    EasyMock.replay(boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock,
            woolDeckMock, tradeManagerMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    assertEquals(0, model.getCurrentPlayerIndex());

    EasyMock.verify(boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock,
            woolDeckMock, tradeManagerMock);
  }

  // ← REDUCES CXTY
  @Test
  void getCurrentRound_AfterConstruction_ExpectZero() {
    EasyMock.replay(boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock,
            woolDeckMock, tradeManagerMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);

    assertEquals(0, model.getCurrentRound());

    EasyMock.verify(boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock,
            woolDeckMock, tradeManagerMock);
  }

  // ← REDUCES CXTY
  @Test
  void getTurnOrder_TwoPlayers_ExpectBothPlayersReturned() {
    Player redMock = EasyMock.createMock(Player.class);
    Player blueMock = EasyMock.createMock(Player.class);
    EasyMock.expect(redMock.getColor()).andReturn(PlayerColor.RED);
    EasyMock.expect(blueMock.getColor()).andReturn(PlayerColor.BLUE);
    EasyMock.replay(redMock, blueMock, boardMock);

    GameModel model = new GameModel(List.of(redMock, blueMock), boardMock);
    List<Player> order = model.getTurnOrder();

    assertEquals(2, order.size());
    assertTrue(order.contains(redMock));
    assertTrue(order.contains(blueMock));
    EasyMock.verify(redMock, blueMock, boardMock);
  }

  // ← REDUCES CXTY
  @Test
  void getOtherPlayers_TwoPlayers_ExpectOnlyOtherPlayerReturned() {
    Player redMock = EasyMock.createMock(Player.class);
    Player blueMock = EasyMock.createMock(Player.class);
    EasyMock.expect(redMock.getColor()).andReturn(PlayerColor.RED);
    EasyMock.expect(blueMock.getColor()).andReturn(PlayerColor.BLUE);
    EasyMock.replay(redMock, blueMock, boardMock);

    GameModel model = new GameModel(List.of(redMock, blueMock), boardMock);
    List<Player> others = model.getOtherPlayers();

    assertEquals(1, others.size());
    assertFalse(others.contains(redMock));
    assertTrue(others.contains(blueMock));
    EasyMock.verify(redMock, blueMock, boardMock);
  }

  // ← REDUCES CXTY
  @Test
  void performTurn_DrawThrowsException_ExpectIllegalArgumentExceptionWithSameMessage() throws EmptyDeckException {
    Player redStateMock = EasyMock.createMock(Player.class);
    ColorToPlayerObjMock = Map.of(PlayerColor.RED, redStateMock);

    EasyMock.expect(boardMock.computeResourceDemand(6))
            .andReturn(Map.of(Resource.WOOL, new HashMap<>(Map.of(redStateMock, 1))));
    EasyMock.expect(woolDeckMock.drawMultiple(1)).andThrow(new EmptyDeckException("No WOOL cards remain."));
    EasyMock.replay(boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock,
            woolDeckMock, tradeManagerMock, redStateMock);

    GameModel model = new GameModel(lumberDeckMock, brickDeckMock, grainDeckMock,
            oreDeckMock, woolDeckMock, ColorToPlayerObjMock, boardMock, tradeManagerMock, randomMock);
    model.setCurrentGamePhase(GamePhase.BEFORE_ROLL);
    model.setCurrentPlayerColor(PlayerColor.RED);

    Exception exception = assertThrows(IllegalArgumentException.class, () -> model.performTurn(6));
    assertEquals("No WOOL cards remain.", exception.getMessage());

    EasyMock.verify(boardMock, lumberDeckMock, brickDeckMock, grainDeckMock, oreDeckMock,
            woolDeckMock, tradeManagerMock, redStateMock);
  }
}
