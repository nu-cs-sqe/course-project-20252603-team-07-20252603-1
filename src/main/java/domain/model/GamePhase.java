package domain.model;

public enum GamePhase {
    BEFORE_ROLL,
    RESOURCE_PRODUCTION, //  Beginning of turn
    MOVE_ROBBER, // Player rolls a 7, or plays a dev card, must immediately move robber
    GENERAL_PLAY,
    MONOPOLY_DEV_CARD,
    ROAD_BUILDING_DEV_CARD,
    OFFERING_TRADE
}
