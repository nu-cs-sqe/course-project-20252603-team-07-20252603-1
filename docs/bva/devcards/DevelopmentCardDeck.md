# DevelopmentCardDeck BVA

**MVC Layer: Model** (`domain.model.development_cards.DevelopmentCardDeck`)

`DevelopmentCardDeck` manages the shared deck of 25 development cards used in a
game of Catan. Per standard Catan rules the deck contains 14 Knight cards,
2 Road Building cards, 2 Year of Plenty cards, 2 Monopoly cards, and 5 Victory
Point cards (25 total). The deck is shuffled at construction. Cards are drawn
from the top and never returned. This class follows the **Single Responsibility
Principle** — it manages only the collection lifecycle (creation, shuffling,
drawing). It does not handle card effects or player interactions.

---

### Method under test: `DevelopmentCardDeck()` (constructor)

Step 1:

- Output: initialized deck containing 25 development cards with the correct composition

Step 2:

- deck size: Count, fixed at 25
- card composition: Cases (KNIGHT ×14, ROAD_BUILDER ×2, YEAR_OF_PLENTY ×2, MONOPOLY ×2, VICTORY_POINT ×5)

Step 3:

- Output deck size: 25; 24 (not feasible); 26 (not feasible)
- Output composition: exactly 14 KNIGHT, exactly 2 ROAD_BUILDER, exactly 2 YEAR_OF_PLENTY, exactly 2 MONOPOLY, exactly 5 VICTORY_POINT


|             | System under test          | Expected output                                    | Implemented? |
| ----------- | -------------------------- | -------------------------------------------------- | ------------ |
| Test Case 1 | new DevelopmentCardDeck()  | deck countRemaining() is 25                        | :white_check_mark: |
| Test Case 2 | new DevelopmentCardDeck()  | deck contains exactly 14 KNIGHT cards              | :white_check_mark: |
| Test Case 3 | new DevelopmentCardDeck()  | deck contains exactly 2 ROAD_BUILDER cards         | :white_check_mark: |
| Test Case 4 | new DevelopmentCardDeck()  | deck contains exactly 2 YEAR_OF_PLENTY cards       | :white_check_mark: |
| Test Case 5 | new DevelopmentCardDeck()  | deck contains exactly 2 MONOPOLY cards             | :white_check_mark: |
| Test Case 6 | new DevelopmentCardDeck()  | deck contains exactly 5 VICTORY_POINT cards        | :white_check_mark: |


---

### Method under test: `drawCard(int currentRound)`

Per official Catan rules, cards cannot be played the same turn they are
purchased (except VP cards). To enforce this, `drawCard` accepts the
current round number and stamps the drawn card's `roundDrawnAt` field
before returning it. Cards in the deck are stored without a meaningful
round value; the true round is assigned only at draw time.

Step 1:

- Input: currentRound
- State: deck (collection of cards)
- Output: DevelopmentCard removed from top of deck, stamped with currentRound
- Output: exception (EmptyDeckException)

Step 2:

- currentRound: Interval [0, ∞)
- deck size: Interval [0, 25], Deleting Elements (removing a single element)
- Output (card drawn): Pointer (non-null DevelopmentCard with roundDrawnAt == currentRound)
- Output (exception thrown): Boolean

Step 3:

- State deck size (Interval [0, 25]): −1 (CAN'T SET); 0 (empty — cannot draw); 1 (last card that can be drawn); 25 (full deck); 26 (CAN'T SET)
- Output: card returned with roundDrawnAt == currentRound, countRemaining() decremented by 1
- Output: EmptyDeckException "Cannot draw new DevelopmentCard, no cards remain."


|              | System under test                                        | Expected output                                                                          | Implemented? |
| ------------ | -------------------------------------------------------- | ---------------------------------------------------------------------------------------- | ------------ |
| Test Case 7  | drawCard(3) from full deck (size 25)                     | card returned (non-null with valid type, roundDrawnAt == 3); countRemaining() is 24      | :white_check_mark: |
| Test Case 8  | drawCard(7) from deck with 1 card remaining              | card returned (roundDrawnAt == 7); countRemaining() is 0                                 | :white_check_mark: |
| Test Case 9  | drawCard(1) from empty deck (size 0)                     | EmptyDeckException: "Cannot draw new DevelopmentCard, no cards remain."                  | :white_check_mark: |


---

### Method under test: `shuffle()`

Step 1:

- State: deck (collection of cards)
- Output: deck card order is randomized

Step 2:

- deck size: Size of Collection [0, 25]

Step 3:

- State deck size (Size of Collection): 25 (full deck — order randomized); 1 (single card — trivially unchanged); 0 (empty — no-op)


|              | System under test              | Expected output                              | Implemented? |
| ------------ | ------------------------------ | -------------------------------------------- | ------------ |
| Test Case 10 | shuffle() on full deck (25)    | card order is randomized; countRemaining() still 25 | :white_check_mark: |
| Test Case 11 | shuffle() on deck with 1 card  | deck unchanged; countRemaining() still 1     | :white_check_mark: |
| Test Case 12 | shuffle() on empty deck (0)    | deck remains empty; no error                 | :x:          |


---

### Method under test: `countRemaining()`

Step 1:

- State: deck
- Output: int (number of remaining cards)

Step 2:

- deck size: Interval [0, 25]
- Output: Interval [0, 25]

Step 3:

- State deck size (Interval [0, 25]): 0 (LOW); 1 (LOW + ε); 25 (HIGH)


|              | System under test                      | Expected output | Implemented? |
| ------------ | -------------------------------------- | --------------- | ------------ |
| Test Case 13 | countRemaining() on new deck           | 25              | :white_check_mark: (covered by TC1) |
| Test Case 14 | countRemaining() after drawing 1 card  | 24              | :x:          |
| Test Case 15 | countRemaining() after drawing all 25  | 0               | :x:          |
