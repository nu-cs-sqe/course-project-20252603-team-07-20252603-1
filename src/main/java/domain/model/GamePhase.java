package domain.model;

public enum GamePhase {
    RESOURCE_PRODUCTION, //  Beginning of turn
    MOVE_ROBBER, // Player rolls a 7, or plays a dev card, must immediately move robber
    GENERAL_PLAY,
    MONOPOLY_DEV_CARD,
    BUILDING_ROAD,
    BUILDING_SETTLEMENT,
    OFFERING_TRADE
}
