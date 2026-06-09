# DevelopmentCard BVA

**MVC Layer: Model** (`domain.model.development_cards.DevelopmentCard`)

`DevelopmentCard` is a data model representing a single development card in Catan.
Each card has a `DevelopmentCardType` (KNIGHT, VICTORY_POINT, ROAD_BUILDER,
YEAR_OF_PLENTY, MONOPOLY) and tracks the round it was drawn via `roundDrawnAt`.
Per standard Catan rules, non-Victory Point cards cannot be played on the same
turn they were purchased; `isPlayable(currentRound)` enforces this constraint.
The class follows the **Single Responsibility Principle** — it stores card identity
and playability state only. All card _effects_ are delegated to `DevelopmentCardHandler`.

---

### Method under test: `DevelopmentCard(DevelopmentCardType type, int currentRoundNumber)` (constructor)

Step 1:

- Input: type, currentRoundNumber
- Output: initialized DevelopmentCard with the given type and round
- Output: exception

Step 2:

- type: Case (KNIGHT, VICTORY_POINT, ROAD_BUILDER, YEAR_OF_PLENTY, MONOPOLY)
- currentRoundNumber: Interval [0, ∞)
- Output: DevelopmentCard instance

Step 3:

- Input type (Case): KNIGHT; VICTORY_POINT; ROAD_BUILDER; YEAR_OF_PLENTY; MONOPOLY; null (CAN'T SET — enum)
- Input currentRoundNumber (Interval [0, ∞)): 0 (LOW); 1 (LOW + ε); large value (e.g. 100)


|             | System under test                                        | Expected output                                     | Implemented?                    |
| ----------- | -------------------------------------------------------- | --------------------------------------------------- |---------------------------------|
| Test Case 1 | new DevelopmentCard(KNIGHT, 0)                           | card.getType() == KNIGHT, roundDrawnAt == 0         | :white_check_mark:              |
| Test Case 2 | new DevelopmentCard(VICTORY_POINT, 5)                    | card.getType() == VICTORY_POINT, roundDrawnAt == 5  | :white_check_mark:              |
| Test Case 3 | new DevelopmentCard(ROAD_BUILDER, 1)                     | card.getType() == ROAD_BUILDER, roundDrawnAt == 1   | :white_check_mark:              |
| Test Case 4 | new DevelopmentCard(YEAR_OF_PLENTY, 10)                  | card.getType() == YEAR_OF_PLENTY, roundDrawnAt == 10| :white_check_mark:                              |
| Test Case 5 | new DevelopmentCard(MONOPOLY, 3)                         | card.getType() == MONOPOLY, roundDrawnAt == 3       | :white_check_mark:                              |


---

### Method under test: `getType()`

Step 1:

- State: card type
- Output: DevelopmentCardType

Step 2:

- Output: Case (KNIGHT, VICTORY_POINT, ROAD_BUILDER, YEAR_OF_PLENTY, MONOPOLY)

Step 3:

- Each Case value is tested via the constructor tests above (Test Cases 1–5).

| | System under test | Expected output | Implemented?        |
| ----------- | ----------------------------------------------- | --------------- |---------------------|
| Test Case 6 | getType() on KNIGHT card | KNIGHT | :white_check_mark: (covered by TC1) |
| Test Case 7 | getType() on VICTORY_POINT card | VICTORY_POINT | :white_check_mark: (covered by TC2) |
| Test Case 8 | getType() on ROAD_BUILDER card | ROAD_BUILDER | :white_check_mark: (covered by TC3) |
| Test Case 9 | getType() on YEAR_OF_PLENTY card | YEAR_OF_PLENTY | :white_check_mark: (covered by TC4) |
| Test Case 10 | getType() on MONOPOLY card | MONOPOLY | :white_check_mark: (covered by TC5) |


---

### Method under test: `isPlayable(int currentRoundNumber)`

Per official Catan rules: non-Victory Point cards **cannot** be played on the
same turn they were purchased. Victory Point cards **can** be played (revealed)
on the same turn they are purchased. Therefore `isPlayable` must use strict
`>` for non-VP cards, while VP cards always return `true`.

Step 1:

- Input: currentRoundNumber
- State: roundDrawnAt (set at draw time), card type
- Output: boolean

Step 2:

- currentRoundNumber: Interval [0, ∞)
- roundDrawnAt: Interval [0, ∞)
- card type: Case (VICTORY_POINT vs all others)
- Pair of Intervals: currentRoundNumber vs roundDrawnAt
- Output: Boolean

Step 3:

- Card type = VICTORY_POINT: always returns true regardless of round comparison
- Card type ≠ VICTORY_POINT, Pair (currentRoundNumber vs roundDrawnAt): currentRoundNumber < roundDrawnAt (not playable); currentRoundNumber == roundDrawnAt (NOT playable — same turn purchased); currentRoundNumber > roundDrawnAt (playable)


|              | System under test                                                                  | Expected output | Implemented? |
| ------------ | ---------------------------------------------------------------------------------- | --------------- | ------------ |
| Test Case 11 | isPlayable(4), KNIGHT card drawn at round 5 (currentRound < roundDrawnAt)          | false           | :x:          |
| Test Case 12 | isPlayable(5), KNIGHT card drawn at round 5 (currentRound == roundDrawnAt)         | false           | :x:          |
| Test Case 13 | isPlayable(6), KNIGHT card drawn at round 5 (currentRound > roundDrawnAt)          | true            | :x:          |
| Test Case 14 | isPlayable(0), MONOPOLY card drawn at round 0 (both at LOW boundary)               | false           | :x:          |
| Test Case 15 | isPlayable(1), ROAD_BUILDER card drawn at round 0 (LOW + ε vs LOW)                 | true            | :x:          |
| Test Case 16 | isPlayable(5), VICTORY_POINT card drawn at round 5 (same turn — VP exception)      | true            | :x:          |
| Test Case 17 | isPlayable(0), VICTORY_POINT card drawn at round 0 (LOW boundary — VP exception)   | true            | :x:          |
