package domain.model;

/** Represents the phases of a Catan game turn. */
public enum GamePhase {
  SETUP_PHASE,
  BEFORE_ROLL,
  RESOURCE_PRODUCTION,
  MOVE_ROBBER,
  GENERAL_PLAY,
  MONOPOLY_DEV_CARD,
  ROAD_BUILDING_DEV_CARD,
  OFFERING_TRADE,
  END_GAME
}
