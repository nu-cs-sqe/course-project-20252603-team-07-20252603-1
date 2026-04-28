package domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import domain.model.DevelopmentCard;

public class DevelopmentCardDeck {

    // index -1 is the top
    private Stack<DevelopmentCard> deck;
    private int cardsLeft;

    private int knightCount;
    private int victoryPointCount;
    private int roadBuilderCount;
    private int yearOfPlentyCount;
    private int monopolyCount;


    public DevelopmentCardDeck() {

        this.cardsLeft = 25; // basic counts for now -- maybe overload constructor later

        this.knightCount = 14;
        this.victoryPointCount = 5;
        this.roadBuilderCount = 2;
        this.yearOfPlentyCount = 2;
        this.monopolyCount = 2;

        // this is WRONG -- this says each one was DRAWN at round 0
            // a hacky fix would be to add an 'updateDrawnAt' method that allows us to reset it on drawn...
            // maybe not all class vars need to be instantiated at creation time?

        for (int i = 0; i < knightCount; i++) {
            DevelopmentCard knight = DevelopmentCard.createKnightDevelopmentCard(0);
            this.deck.push(knight);
        }

        for (int i = 0; i < victoryPointCount; i++) {
            DevelopmentCard victoryPoint = DevelopmentCard.createVictoryPointDevelopmentCard(0);
            this.deck.push(victoryPoint);
        }

        for (int i = 0; i < roadBuilderCount; i++) {
            DevelopmentCard roadBuilder = DevelopmentCard.createRoadBuilderDevelopmentCard(0);
            this.deck.push(roadBuilder);
        }

        for (int i = 0; i < yearOfPlentyCount; i++) {
            DevelopmentCard yearOfPlenty = DevelopmentCard.createYearOfPlentyDevelopmentCard(0);
            this.deck.push(yearOfPlenty);
        }

        for (int i = 0; i < monopolyCount; i++) {
            DevelopmentCard monopoly = DevelopmentCard.createMonopolyDevelopmentCard(0);
            this.deck.push(monopoly);
        }


        this.shuffle();

    }

    public void shuffle() { // private?? does someone else need to shuffle -- will they at gametime?
        Collections.shuffle(this.deck);
    }


    public DevelopmentCard drawCard() {

        // we may not need this -- i can't actually think whyt he deck needs to know how many of each are left
        DevelopmentCard drawn = this.deck.pop();
        this.cardsLeft--;
        DevelopmentCardType type = drawn.getType();

        switch (type) {
            case KNIGHT:
                this.knightCount--;
                break; // not actually necessary here who cares
            case VICTORY_POINT:
                this.victoryPointCount--;
                break;
            case ROAD_BUILDER:
                this.roadBuilderCount--;
                break;
            case YEAR_OF_PLENTY:
                this.yearOfPlentyCount--;
                break;
            case MONOPOLY:
                this.monopolyCount--;
                break;
        }


        return drawn;
    }

    public int countRemaining() {
        return cardsLeft;
    }

    
}
