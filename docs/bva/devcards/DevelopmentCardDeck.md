# DevelopmentCardDeck BVA

`DevelopmentCardDeck` manages the shared deck of 25 development cards used in a
game of Catan. Per standard Catan rules the deck contains 14 Knight cards,
2 Road Building cards, 2 Year of Plenty cards, 2 Monopoly cards, and 5 Victory
Point cards (25 total). The deck must be shuffled before play begins. Cards are
drawn from the top and never returned to the deck.

---

### Method under test: `DevelopmentCardDeck()` (constructor)

Step 1:

- Output: initialized deck containing 25 development cards with the correct composition

Step 2:

- deck size: Count, fixed at 25
- card composition: Cases (Knight ×14, Road Building ×2, Year of Plenty ×2, Monopoly ×2, Victory Point ×5)

Step 3:

- Output deck size: 25; 24 (not feasible); 26 (not feasible)
- Output composition: exactly 14 Knight, exactly 2 Road Building, exactly 2 Year of Plenty, exactly 2 Monopoly, exactly 5 Victory Point


|             | System under test         | Expected output                              | Implemented? |
| ----------- | ------------------------- | -------------------------------------------- | ---------- |
| Test Case 1 | new DevelopmentCardDeck() | deck size is 25                              | :white_check_mark: |
| Test Case 2 | new DevelopmentCardDeck() | deck contains exactly 14 Knight cards        | :white_check_mark:         |
| Test Case 3 | new DevelopmentCardDeck() | deck contains exactly 2 Road Building cards  | :white_check_mark:         |
| Test Case 4 | new DevelopmentCardDeck() | deck contains exactly 2 Year of Plenty cards | :white_check_mark:        |
| Test Case 5 | new DevelopmentCardDeck() | deck contains exactly 2 Monopoly cards       | :white_check_mark:        |
| Test Case 6 | new DevelopmentCardDeck() | deck contains exactly 5 Victory Point cards  | :white_check_mark:        |


---

### Method under test: `drawCard()`

Step 1:

- State: deck (collection of cards)
- Output: DevelopmentCard removed from top of deck
- Output: exception

Step 2:

- deck size: Interval [0, 25], removing a single element
- Output (card drawn): DevelopmentCard (Pointer)
- Output (exception thrown): Boolean

Step 3:

- State deck size (Interval [0, 25]): −1 (CAN'T SET); 0 (empty — CAN'T DRAW); 1 (last card that can be drawn); 25 (full deck); 26 (CAN'T SET)
- Output: card returned, deck size decremented by 1
- Output: "The development card deck is empty."


|             | System under test                          | Expected output                                              | Implemented? |
| ----------- | ------------------------------------------ | ------------------------------------------------------------ | ------------ |
| Test Case 7 | drawCard() from full deck (size 25)        | card returned; deck size is 24                               | :white_check_mark:          |
| Test Case 8 | drawCard() from deck with 1 card remaining | card returned; deck size is 0                                | :white_check_mark:          |
| Test Case 9 | drawCard() from empty deck (size 0)        | IllegalStateException: "The development card deck is empty." | :white_check_mark:          |


---

### Method under test: `shuffle()`

Step 1:

- State: deck (collection of cards)
- Output: deck card order is randomized

Step 2:

- deck size: Size of Collection [0, 25]

Step 3:

- State deck size (Size of Collection): 25 (full deck — order randomized); 1 (single card — trivially unchanged); 0 (empty — no-op)


|              | System under test             | Expected output                              | Implemented? |
| ------------ | ----------------------------- | -------------------------------------------- | ------------ |
| Test Case 10 | shuffle() on full deck (25)   | card order is randomized; deck size still 25 | :white_check_mark:          |
| Test Case 11 | shuffle() on deck with 1 card | deck unchanged; deck size still 1            | :white_check_mark:          |
| Test Case 12 | shuffle() on empty deck (0)   | deck remains empty; no error                 | :white_check_mark:          |


---

### Method under test: `isEmpty()`

Step 1:

- State: deck size
- Output: boolean

Step 2:

- deck size: Interval [0, 25]
- Output: Boolean

Step 3:

- State deck size: 0 (empty → true); 1 (boundary, not empty → false); 25 (full → false)


|              | System under test                         | Expected output | Implemented? |
| ------------ | ----------------------------------------- | --------------- | ------------ |
| Test Case 13 | isEmpty() on new deck (size 25)           | false           | :white_check_mark:          |
| Test Case 14 | isEmpty() after drawing all 25 cards      | true            | :white_check_mark:          |
| Test Case 15 | isEmpty() after drawing 24 cards (size 1) | false           | :white_check_mark:          |


---

### Method under test: `size()`

Step 1:

- State: deck
- Output: int (number of remaining cards)

Step 2:

- deck size: Interval [0, 25]
- Output: Interval [0, 25]

Step 3:

- State deck size (Interval [0, 25]): 0 (LOW); 1 (LOW + ε); 25 (HIGH)


|              | System under test                 | Expected output | Implemented? |
| ------------ | --------------------------------- | --------------- | ------------ |
| Test Case 16 | size() on new deck                | 25              | :white_check_mark:          |
| Test Case 17 | size() after drawing 1 card       | 24              | :white_check_mark:          |
| Test Case 18 | size() after drawing all 25 cards | 0               | :white_check_mark:          |


