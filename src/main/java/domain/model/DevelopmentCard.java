package domain.model;


public class DevelopmentCard {

    private DevelopmentCardType type;
    private int roundDrawnAt;


    // any sort of control here would have to be in a controller to affect the game anyway
        // i mean for it to activate its BOON

    // i suppose this assumes cards are only created in the round they're drawn -- this may be intentional
    public DevelopmentCard(DevelopmentCardType type, int currentRoundNumber) {
        this.type = type;
        this.roundDrawnAt = currentRoundNumber;
    }

    // whatever \/

    public static DevelopmentCard createKnightDevelopmentCard(int currentRoundNumber) {

        return new DevelopmentCard(DevelopmentCardType.KNIGHT, currentRoundNumber);

    }

    public static DevelopmentCard createVictoryPointDevelopmentCard(int currentRoundNumber) {

        return new DevelopmentCard(DevelopmentCardType.VICTORY_POINT, currentRoundNumber);

    }

    public static DevelopmentCard createRoadBuilderDevelopmentCard(int currentRoundNumber) {

        return new DevelopmentCard(DevelopmentCardType.ROAD_BUILDER, currentRoundNumber);

    }

    public static DevelopmentCard createYearOfPlentyDevelopmentCard(int currentRoundNumber) {

        return new DevelopmentCard(DevelopmentCardType.YEAR_OF_PLENTY, currentRoundNumber);

    }

    public static DevelopmentCard createMonopolyDevelopmentCard(int currentRoundNumber) {

        return new DevelopmentCard(DevelopmentCardType.MONOPOLY, currentRoundNumber);

    }

    public boolean isPlayable(int currentRoundNumber) {
        return currentRoundNumber >= this.roundDrawnAt;
    }

    public DevelopmentCardType getType() {
        return this.type;
    }
    
}