package domain.model;

import domain.model.development_cards.DevelopmentCard;
import domain.model.development_cards.DevelopmentCardDeck;
import domain.model.development_cards.DevelopmentCardType;
import domain.model.exceptions.EmptyDeckException;
import domain.model.exceptions.InsufficientResourcesException;
import domain.model.game_pieces.Robber;
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

    public void playKnightCard(Player player, DevelopmentCard card, int currentRound, Robber robber, int targetHexId, Player victim) {
        if (card == null) {
            throw new IllegalArgumentException("Development card cannot be null.");
        }
        if (card.getType() != DevelopmentCardType.KNIGHT) {
            throw new IllegalArgumentException("Card is not a Knight card.");
        }
    }

}
