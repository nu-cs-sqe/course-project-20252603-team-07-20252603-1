package domain;

import java.util.ArrayList;
import java.util.List;

public class DevelopmentCardDeck {

    private final List<DevelopmentCard> cards;

    public DevelopmentCardDeck() {
        cards = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            cards.add(new DevelopmentCard());
        }
    }

    public int size() {
        return cards.size();
    }
}
