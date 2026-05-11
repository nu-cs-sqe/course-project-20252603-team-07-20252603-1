package domain.model;

import domain.model.resources.ResourceDeck;
import domain.model.resources.ResourceCard;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GameModel {

    private final List<PlayerState> playerStates;
    private int currentPlayerIndex;

    public GameModel(List<Player> players) {
        this.playerStates = new ArrayList<>();
        for (Player player : players) {
            this.playerStates.add(new PlayerState(player));
        }
        this.currentPlayerIndex = 0;
    }

    public List<Player> getTurnOrder() {
        return playerStates.stream()
                .map(PlayerState::getPlayer)
                .collect(Collectors.toList());
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return playerStates.get(currentPlayerIndex).getPlayer();
    }

    public void advanceToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % playerStates.size();
    }

    public PlayerState getPlayerState(int index) {
        return playerStates.get(index);
    }

    public void performTurn(DiceRoller diceRoller, ResourceDeck resourceDeck) {
        // Roll dice
        int roll = diceRoller.roll();

        // Give current player one resource (minimal stub - ignoring dice result for now)
        try {
            ResourceCard card = resourceDeck.draw();
            playerStates.get(currentPlayerIndex).addResource(card);
        } catch (Exception e) {
            // Gracefully handle empty deck - no resource distributed
        }
    }
}
