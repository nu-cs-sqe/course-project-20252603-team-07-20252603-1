package domain;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentCardDeck {
  //TODO: Make this a dictionary mapping from card type to number
  private static final int NUM_KNIGHT_CARDS = 14;
  private static final int NUM_OTHER_CARDS = 11;

  private final List<DevelopmentCard> cards;

  public DevelopmentCardDeck() {
    cards = new ArrayList<>();
    //TODO: Make this add cards based on a constant dict mapping from card type to number
    for (int i = 0; i < NUM_KNIGHT_CARDS; i++) {
      cards.add(new KnightCard());
    }
    for (int i = 0; i < NUM_OTHER_CARDS; i++) {
      cards.add(new DevelopmentCard(DevelopmentCardType.OTHER) {});
    }
  }

  public List<DevelopmentCard> getCards() {
    return List.copyOf(cards);
  }

  public int size() {
    return cards.size();
  }
}
