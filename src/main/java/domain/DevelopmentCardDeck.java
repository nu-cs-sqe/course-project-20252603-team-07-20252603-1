package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DevelopmentCardDeck {
  private static final int INDEX_OF_FRONT = 0;
  private static final Map<DevelopmentCardType, Integer> CARD_COUNTS = Map.of(
      DevelopmentCardType.KNIGHT, 14,
      DevelopmentCardType.ROAD_BUILDING, 2,
      DevelopmentCardType.YEAR_OF_PLENTY, 2,
      DevelopmentCardType.MONOPOLY, 2,
      DevelopmentCardType.VICTORY_POINT, 5
  );

  private final List<DevelopmentCard> cards;

  public DevelopmentCardDeck() {
    cards = new ArrayList<>();
    for (Map.Entry<DevelopmentCardType, Integer> entry : CARD_COUNTS.entrySet()) {
      for (int i = 0; i < entry.getValue(); i++) {
        cards.add(entry.getKey().createCard());
      }
    }
  }

  public List<DevelopmentCard> getCards() {
    return List.copyOf(cards);
  }

  public DevelopmentCard drawCard() {
    if (cards.isEmpty()) {
      throw new IllegalStateException("The development card deck is empty.");
    }
    return cards.remove(INDEX_OF_FRONT);
  }

  public int size() {
    return cards.size();
  }
}
