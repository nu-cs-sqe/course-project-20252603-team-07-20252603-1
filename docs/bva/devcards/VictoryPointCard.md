# VictoryPointCard BVA

`VictoryPointCard` represents a Victory Point development card in Catan (e.g.,
Library, Market, Chapel, Great Hall, University). Each card is worth 1 victory
point. Per Catan rules, Victory Point cards are kept hidden in the player's hand
and may only be revealed when the player declares victory with 10 or more
points on their turn. Unlike other development cards, Victory Point cards may be
revealed on the same turn they are purchased. The deck contains 5 Victory Point
cards.

---

### Method under test: `getVictoryPoints()`

Step 1:

- Output: int (victory points awarded by this card)

Step 2:

- Output: fixed value, Interval [1, 1]

Step 3:

- Output: 1 (always); 0 (not feasible); 2 (not feasible)


|             | System under test                        | Expected output | Implemented? |
| ----------- | ---------------------------------------- | --------------- | ------------ |
| Test Case 1 | getVictoryPoints() on a VictoryPointCard | 1               | :x:          |
