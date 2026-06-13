# ResourceDeck BVA

`ResourceDeck` represents a bank supply of one resource type. Each deck holds a maximum of 19 cards.
Methods under test: constructor, `draw()`, `drawMultiple(n)`, `replenish()`, `replenish(n)`,
`replenishAll()`.

---

### Constructor and type

|             | State of the System               | Expected output                          | Implemented?       |
|-------------|-----------------------------------|------------------------------------------|--------------------|
| Test Case 1 | new ResourceDeck(Resource.LUMBER) | getType() returns LUMBER                 | :white_check_mark: |
| Test Case 11 | new ResourceDeck(Resource.DESERT) | IllegalArgumentException thrown          | :x: |
| Test Case 12 | new ResourceDeck(Resource.LUMBER) | getTotalCards() returns 19               | :x: |

---

### Static initializer coverage: `ResourceType` and `Resources` enums

These tests exist solely to trigger the static initializer blocks that JaCoCo tracks.

|              | State of the System                  | Expected output                          | Implemented?       |
|--------------|--------------------------------------|------------------------------------------|--------------------|
| Test Case 13 | reference ResourceType.WOOD          | enum loads; values().length == 6         | :x: |
| Test Case 14 | reference Resources.values()         | empty array returned; length == 0        | :x: |

---

### Method under test: `draw()`

Step 1:

- Input: none
- State: cards remaining in deck [0, 19]
- Output: one card removed and returned, or EmptyDeckException

Step 2:

- remaining: Interval [0, 19]; critical boundary = 0

Step 3:

- remaining = 19 (full deck, LOW+): card returned
- remaining = 0 (empty, boundary): EmptyDeckException with resource name in message

|             | State of the System                                  | Expected output                                     | Implemented?       |
|-------------|------------------------------------------------------|-----------------------------------------------------|--------------------|
| Test Case 2 | deck full (19 cards remaining)                       | returns correct Resource, one card consumed         | :white_check_mark: |
| Test Case 3 | deck empty (0 remaining)                             | EmptyDeckException; message contains resource name  | :white_check_mark: |

---

### Method under test: `drawMultiple(n)`

Step 1:

- Input: n (requested count)
- State: cards remaining
- Output: number of cards actually drawn (min of n and remaining)

Step 2:

- n: Interval [0, 19+]; boundary = remaining count
- remaining: Interval [0, 19]

Step 3:

- n < remaining (normal): returns n
- n > remaining (capped): returns remaining
- n > 0, remaining = 0 (empty deck): returns 0

|             | State of the System                                  | Expected output                      | Implemented?       |
|-------------|------------------------------------------------------|--------------------------------------|--------------------|
| Test Case 4 | draw 5 from full deck (5 < 19)                       | returns 5                            | :white_check_mark: |
| Test Case 5 | draw 15, then draw 10 (4 remain, 10 > 4)             | second draw returns 4                | :white_check_mark: |
| Test Case 6 | draw all 19, then drawMultiple(5) (0 remain)         | returns 0                            | :white_check_mark: |

---

### Method under test: `replenish()` (add one card)

Step 1:

- State: cards remaining
- Output: remaining increases by 1, capped at 19

Step 2:

- remaining before replenish: Interval [0, 19]; boundary = 0 (empty) and 19 (full)

Step 3:

- remaining = 0 (empty, LOW): replenish returns deck to 1; subsequent draw succeeds

|             | State of the System                                  | Expected output                           | Implemented?       |
|-------------|------------------------------------------------------|-------------------------------------------|--------------------|
| Test Case 7 | draw all 19, then replenish()                        | deck has 1 card; draw() succeeds          | :white_check_mark: |

---

### Method under test: `replenish(n)` (add n cards)

Step 1:

- Input: n
- State: cards remaining
- Output: remaining increases by n, capped at 19

Step 2:

- n: Interval [1, 19]; remaining + n may exceed 19 (capped)
- remaining + n ≤ 19: exact replenish
- remaining + n > 19: capped at 19

Step 3:

- n = 5, remaining = 0: exact replenish to 5
- n = 10, remaining = 14 (would be 24, caps at 19): drawMultiple(20) returns 19

|             | State of the System                                        | Expected output                           | Implemented?       |
|-------------|-------------------------------------------------------------|-------------------------------------------|--------------------|
| Test Case 8 | draw all 19, replenish(5)                                  | can draw exactly 5                        | :white_check_mark: |
| Test Case 9 | draw 5 (14 remain), replenish(10) (would exceed 19)        | drawMultiple(20) returns 19 (capped)      | :white_check_mark: |

---

### Method under test: `replenishAll()`

Step 1:

- State: any remaining count
- Output: remaining reset to 19

Step 2:

- remaining before: any value in [0, 19]

Step 3:

- remaining = 9 (arbitrary partial): replenishAll → 19 cards available

|              | State of the System                           | Expected output                | Implemented?       |
|--------------|-----------------------------------------------|--------------------------------|--------------------|
| Test Case 10 | draw 10, then replenishAll()                  | can draw all 19                | :white_check_mark: |
