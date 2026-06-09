package domain.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public void playMonopolyCard(Player player, DevelopmentCard card, int currentRound, Resource resource, List<Player> otherPlayers) {
        if (card == null) {
            throw new IllegalArgumentException("Development card cannot be null.");
        }
        if (card.getType() != DevelopmentCardType.MONOPOLY) {
            throw new IllegalArgumentException("Card is not a Monopoly card.");
        }
        if (!card.isPlayable(currentRound)) {
            throw new IllegalStateException("Card cannot be played the same turn it was purchased.");
        }
        if (player.hasPlayedDevCardThisTurn()) {
            throw new IllegalStateException("Already played a development card this turn.");
        }
        if (resource == null) {
            throw new IllegalArgumentException("Resource cannot be null.");
        }
    }

    public void playKnightCard(Player player, DevelopmentCard card, int currentRound, Robber robber, int targetHexId, Player victim) {
        if (card == null) {
            throw new IllegalArgumentException("Development card cannot be null.");
        }
        if (card.getType() != DevelopmentCardType.KNIGHT) {
            throw new IllegalArgumentException("Card is not a Knight card.");
        }
        if (!card.isPlayable(currentRound)) {
            throw new IllegalStateException("Card cannot be played the same turn it was purchased.");
        }
        if (player.hasPlayedDevCardThisTurn()) {
            throw new IllegalStateException("Already played a development card this turn.");
        }
        if (robber == null) {
            throw new IllegalArgumentException("Robber cannot be null.");
        }
        if (targetHexId == robber.getRobberLocation()) {
            throw new IllegalArgumentException("Must move robber to a different hex.");
        }
        if (victim != null && !victim.isAdjacentToHex(targetHexId)) {
            throw new IllegalArgumentException("Victim must be adjacent to the robber's new hex.");
        }

        robber.moveRobber(targetHexId);

        if (victim != null && victim.getTotalResourceCount() > 0) {
            List<Resource> available = new ArrayList<>();
            for (Map.Entry<Resource, Integer> entry : victim.getResources().entrySet()) {
                if (entry.getValue() > 0) available.add(entry.getKey());
            }
            Resource stolen = available.get(new Random().nextInt(available.size()));
            victim.updateResources(stolen, -1);
            player.updateResources(stolen, 1);
        }

        player.incrementKnightCount();
        player.removeDevelopmentCard(card);
        player.setHasPlayedDevCardThisTurn(true);
    }

}
