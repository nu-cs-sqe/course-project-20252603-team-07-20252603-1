package domain.model;

import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.exceptions.EmptyDeckException;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.player.Player;
import domain.model.resources.Resource;

public class DevelopmentCardHandler {

    public DevelopmentCard buyDevelopmentCard(Player buyer, DevelopmentCardDeck deck, int currentRound) throws EmptyDeckException {
        if (buyer.getResourceCount(Resource.ORE) < 1 ||
            buyer.getResourceCount(Resource.WOOL) < 1 ||
            buyer.getResourceCount(Resource.GRAIN) < 1) {
            throw new InsufficientResourcesException("Not enough resources to buy a development card.");
        }

        DevelopmentCard card = deck.drawCard(currentRound);

        buyer.updateResources(Resource.ORE, -1);
        buyer.updateResources(Resource.WOOL, -1);
        buyer.updateResources(Resource.GRAIN, -1);
        buyer.addDevelopmentCard(card);

        return card;
    }

}
