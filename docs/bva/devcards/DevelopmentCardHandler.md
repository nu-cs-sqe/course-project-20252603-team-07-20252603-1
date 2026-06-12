# DevelopmentCardHandler BVA

**MVC Layer: Model / Domain Service** (`domain.model.DevelopmentCardHandler`)

`DevelopmentCardHandler` is a domain-level service responsible for all
development card business logic: buying cards from the deck and executing
each card type's effect. This follows the same architectural pattern as
`DiceHandler`, `TradeManager`, and `Robber` in the existing codebase —
domain services that encapsulate game rules without coupling to the UI.

Since the controller (`DevCardController`) is intentionally thin — pure
delegation with no game-rule logic — all preconditions, validation, and
card effects live here.

**Design Principles:**
- **Single Responsibility**: Each public method handles exactly one card
  operation. The handler does not manage deck state (that is
  `DevelopmentCardDeck`'s job) or player identity (that is `Player`'s job).
- **Open/Closed**: Adding a new card type requires adding a new `play*` method
  (or, for further extensibility, a Strategy/Command pattern could be introduced
  where each `DevelopmentCardType` maps to a `CardEffectStrategy`).
- **Dependency Inversion**: The handler depends on abstractions (`Player`,
  `DevelopmentCardDeck`, `Robber`, `Edge`) rather than concrete UI classes.

---

## Common Card Preconditions (shared across all `play*` methods)

All `play*` methods enforce the following preconditions before executing card
effects. These are validated by the handler since the controller is thin.

| ID | Precondition | Violation | Expected result |
|----|-------------|-----------|-----------------|
| P1 | `card` is not null | `card == null` | `IllegalArgumentException`: "Development card cannot be null." |
| P2 | `card.getType()` matches the expected type for the method | Type mismatch | `IllegalArgumentException`: "Card is not a {expectedType} card." |
| P3 | `card.isPlayable(currentRound)` returns true | Card purchased this turn (non-VP only) | `IllegalStateException`: "Card cannot be played the same turn it was purchased." |
| P4 | Current player has not already played a development card this turn | Already played | `IllegalStateException`: "Already played a development card this turn." |

**Rules note (per official Catan rulebook):**
- P3 does **not** apply to Victory Point cards — VP cards may be revealed on
  the same turn they are purchased.
- P4 applies to Knight and Progress cards only. Multiple VP cards may be
  revealed in the same turn.
- Dev cards may be played **before or after** rolling dice (i.e., during
  `BEFORE_ROLL` or `GENERAL_PLAY` phases).

**Post-play effects (per official Catan rulebook):**
- **Knight cards**: remain face-up in front of the player (kept in a
  `playedKnights` count or list for Largest Army tracking).
- **Progress cards** (Road Building, Year of Plenty, Monopoly): removed from
  the game after play (discarded from the player's hand, not returned to deck).
- **Victory Point cards**: remain hidden in hand until the player declares
  victory at 10+ points; they are never "played" in the discard sense.

---

### Method under test: `buyDevelopmentCard(Player buyer, DevelopmentCardDeck deck, int currentRound)`

Step 1:

- Input: buyer, deck, currentRound
- State: buyer's resource holdings (must have 1 ORE, 1 WOOL, 1 GRAIN), deck size
- Output: DevelopmentCard drawn from deck and added to buyer's hand
- Output: exception

Step 2:

- buyer: Pointer
- deck: Pointer
- deck size: Interval [0, 25]
- buyer resources: Pairs of Counts (ORE count, WOOL count, GRAIN count each ≥ 1)
- currentRound: Interval [0, ∞)
- Output (card drawn): Pointer (DevelopmentCard)
- Output (exception thrown): Boolean

Step 3:

- Input buyer (Pointer): null (CAN'T SET by controller guard); valid Player
- Input deck (Pointer): null (CAN'T SET by controller guard); valid DevelopmentCardDeck
- State deck size (Interval [0, 25]): 0 (empty — cannot buy); 1 (last card); 25 (full)
- State buyer ORE count (Count): 0 (insufficient); 1 (exactly enough); >1 (surplus)
- State buyer WOOL count (Count): 0 (insufficient); 1 (exactly enough); >1 (surplus)
- State buyer GRAIN count (Count): 0 (insufficient); 1 (exactly enough); >1 (surplus)
- Output: DevelopmentCard returned, buyer resources decremented (−1 ORE, −1 WOOL, −1 GRAIN), card added to buyer's hand, deck countRemaining() decremented by 1
- Output: "Not enough resources to buy a development card." / EmptyDeckException


|             | System under test                                                                       | Expected output                                                                                | Implemented? |
| ----------- | --------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------- | ------------ |
| Test Case 1 | buyer has 1 ORE, 1 WOOL, 1 GRAIN (exact cost); deck full (25)                          | card returned; buyer loses 1 ORE, 1 WOOL, 1 GRAIN; card added to hand; deck countRemaining() is 24 | :white_check_mark: |
| Test Case 2 | buyer has 3 ORE, 2 WOOL, 4 GRAIN (surplus); deck full (25)                             | card returned; buyer's resources each decremented by 1; card added to hand                     | :white_check_mark: |
| Test Case 3 | buyer has 0 ORE, 1 WOOL, 1 GRAIN (insufficient ORE)                                    | InsufficientResourcesException: "Not enough resources to buy a development card."              | :white_check_mark: |
| Test Case 4 | buyer has 1 ORE, 0 WOOL, 1 GRAIN (insufficient WOOL)                                   | InsufficientResourcesException: "Not enough resources to buy a development card."              | :white_check_mark: |
| Test Case 5 | buyer has 1 ORE, 1 WOOL, 0 GRAIN (insufficient GRAIN)                                  | InsufficientResourcesException: "Not enough resources to buy a development card."              | :white_check_mark: |
| Test Case 6 | buyer has 1 ORE, 1 WOOL, 1 GRAIN; deck empty (0)                                       | EmptyDeckException: "Cannot draw new DevelopmentCard, no cards remain."                        | :white_check_mark: |
| Test Case 7 | buyer has 1 ORE, 1 WOOL, 1 GRAIN; deck has 1 card remaining (last card)                | card returned; deck countRemaining() is 0                                                      | :white_check_mark: |


---

### Method under test: `playKnightCard(Player player, DevelopmentCard card, int currentRound, Robber robber, int targetHexId, Player victim)`

Per official Catan rules: the player must move the robber to a **different**
terrain hex and may steal 1 random resource card from an opponent who has a
settlement or city **adjacent to the robber's new hex**. If the victim has no
resources, nothing is stolen. The knight card remains face-up in front of the
player and counts toward Largest Army. The played card is removed from the
player's hand.

Step 1:

- Input: player, card, currentRound, robber, targetHexId, victim
- State: robber's current hex location, victim's resource count, victim adjacency, player's played knight count, player's hasPlayedDevCardThisTurn flag
- Output: robber moved to targetHexId
- Output: 1 random resource transferred from victim to player (if applicable)
- Output: player's played knight count incremented
- Output: card removed from player's hand
- Output: exception

Step 2:

- card: Pointer / Case (must be KNIGHT) — see Common Preconditions P1–P4
- robber: Pointer
- targetHexId: Interval [0, 18]
- victim: Pointer (null allowed — no adjacent opponent)
- robber current location vs targetHexId: Pair of Intervals (must differ)
- victim adjacency: Boolean (must be adjacent to targetHexId if non-null)
- victim's resource count: Count [0, ∞)
- Output (robber moved): Boolean
- Output (resource stolen): Boolean
- Output (exception thrown): Boolean

Step 3:

- card (Common Preconditions): null (P1); wrong type (P2); not playable / same turn (P3); already played dev card (P4)
- Input robber (Pointer): null; valid Robber instance
- Input targetHexId (Interval [0, 18]): 0 (LOW); 18 (HIGH); same as current (invalid)
- Input targetHexId vs current location (Pair of Intervals): same hex (invalid); different hex (valid)
- Input victim (Pointer): null (no one adjacent); valid adjacent opponent; non-adjacent player (invalid)
- State victim resource count (Count): 0 (nothing to steal); 1 (steal the only card); >1 (steal 1 of many)
- Output: robber location updated / exceptions as listed


|              | System under test                                                                    | Expected output                                                             | Implemented? |
| ------------ | ------------------------------------------------------------------------------------ | --------------------------------------------------------------------------- | ------------ |
| Test Case 8  | card = null                                                                          | IllegalArgumentException: "Development card cannot be null."                | :white_check_mark: |
| Test Case 9  | card type = MONOPOLY (not KNIGHT)                                                    | IllegalArgumentException: "Card is not a Knight card."                      | :white_check_mark: |
| Test Case 10 | card drawn this round (not playable, same turn)                                      | IllegalStateException: "Card cannot be played the same turn it was purchased." | :white_check_mark: |
| Test Case 11 | player already played a dev card this turn                                           | IllegalStateException: "Already played a development card this turn."       | :white_check_mark: |
| Test Case 12 | robber = null                                                                        | IllegalArgumentException: "Robber cannot be null."                          | :white_check_mark: |
| Test Case 13 | targetHexId = 5 (valid, different from current), victim adjacent with 3 resources    | robber moves to hex 5; 1 random resource transferred from victim to player; knight count incremented; card removed from hand | :white_check_mark: |
| Test Case 14 | targetHexId = 0 (LOW boundary), victim adjacent with resources                       | robber moves to hex 0; 1 random resource transferred                       | :white_check_mark: |
| Test Case 15 | targetHexId = 18 (HIGH boundary), victim adjacent with resources                     | robber moves to hex 18; 1 random resource transferred                      | :white_check_mark: |
| Test Case 16 | targetHexId = robber's current hex (same hex)                                        | IllegalArgumentException: "Must move robber to a different hex."            | :white_check_mark: |
| Test Case 17 | targetHexId valid, victim = null (no adjacent opponent)                               | robber moves; no resource stolen; knight count incremented                  | :white_check_mark: |
| Test Case 18 | targetHexId valid, victim adjacent with 0 resource cards                              | robber moves; no resource stolen; knight count incremented                  | :white_check_mark: |
| Test Case 19 | targetHexId valid, victim adjacent with exactly 1 resource card                       | robber moves; that 1 resource transferred; knight count incremented         | :white_check_mark: |
| Test Case 20 | targetHexId valid, victim not adjacent to targetHexId                                 | IllegalArgumentException: "Victim must be adjacent to the robber's new hex." | :white_check_mark: |


---

### Method under test: `playMonopolyCard(Player player, DevelopmentCard card, int currentRound, Resource resource, List<Player> otherPlayers)`

Per official Catan rules: the player announces 1 resource type. All other
players must surrender **all** cards of that type to the player. The progress
card is removed from the game (discarded from hand) after play.

Step 1:

- Input: player, card, currentRound, resource, otherPlayers
- State: each other player's holdings of the named resource
- Output: all cards of the named resource transferred from other players to player
- Output: card removed from player's hand (discarded)
- Output: exception

Step 2:

- card: Pointer / Case (must be MONOPOLY) — see Common Preconditions P1–P4
- resource: Pointer / Case (BRICK, GRAIN, LUMBER, ORE, WOOL; DESERT is invalid)
- otherPlayers: Pointer / Size of Collection
- per-player holdings of named resource: Count [0, 19]
- Output (resources transferred): Boolean
- Output (exception thrown): Boolean

Step 3:

- card (Common Preconditions): null (P1); wrong type (P2); not playable / same turn (P3); already played (P4)
- Input resource (Pointer / Case): null; BRICK; GRAIN; LUMBER; ORE; WOOL; DESERT (invalid)
- Input otherPlayers (Pointer / Size of Collection): null; empty list; one player; more than one player
- State per-player holdings (Count): 0 (has none); 1 (has exactly one); >1 (has several)
- Output: player's resource count increases by total taken; card removed from hand
- Output: "Resource cannot be null." / "Cannot monopolize DESERT." / "Other players list cannot be null."


|              | System under test                                                                  | Expected output                                                  | Implemented? |
| ------------ | ---------------------------------------------------------------------------------- | ---------------------------------------------------------------- | ------------ |
| Test Case 21 | card = null                                                                        | IllegalArgumentException: "Development card cannot be null."     | :white_check_mark: |
| Test Case 22 | card type = KNIGHT (not MONOPOLY)                                                  | IllegalArgumentException: "Card is not a Monopoly card."         | :white_check_mark: |
| Test Case 23 | card drawn this round (not playable)                                               | IllegalStateException: "Card cannot be played the same turn it was purchased." | :white_check_mark: |
| Test Case 24 | player already played a dev card this turn                                         | IllegalStateException: "Already played a development card this turn." | :white_check_mark: |
| Test Case 25 | resource = null                                                                    | IllegalArgumentException: "Resource cannot be null."             | :white_check_mark: |
| Test Case 26 | resource = DESERT                                                                  | IllegalArgumentException: "Cannot monopolize DESERT."            | :white_check_mark: |
| Test Case 27 | otherPlayers = null                                                                | IllegalArgumentException: "Other players list cannot be null."   | :white_check_mark: |
| Test Case 28 | resource = BRICK, otherPlayers = [] (empty list)                                   | no resources transferred; card removed from hand                 | :white_check_mark: |
| Test Case 29 | resource = ORE, 1 opponent has 5 ORE                                               | opponent loses 5 ORE; player gains 5 ORE; card removed from hand | :white_check_mark: |
| Test Case 30 | resource = WOOL, 1 opponent has 0 WOOL                                             | no WOOL transferred; card removed from hand                      | :white_check_mark: |
| Test Case 31 | resource = GRAIN, 3 opponents have 2, 0, and 4 GRAIN respectively                 | opponents lose 2, 0, 4; player gains 6 GRAIN; card removed       | :white_check_mark: |
| Test Case 32 | resource = LUMBER, 1 opponent has 1 LUMBER (minimum transferable)                  | opponent loses 1 LUMBER; player gains 1 LUMBER; card removed     | :white_check_mark: |


---

### Method under test: `playRoadBuildingCard(Player player, DevelopmentCard card, int currentRound, Edge edge1, Edge edge2)`

Per official Catan rules: the player may immediately place 2 free roads on
the board following normal road-building rules. If only 1 road piece remains,
edge2 may be null and only 1 road is placed. Each player has a maximum of 15
road pieces. The progress card is removed from the game after play.

Step 1:

- Input: player, card, currentRound, edge1, edge2
- State: player's road count (roads already placed), edge1 occupancy, edge1 connectivity, edge2 occupancy, edge2 connectivity
- Output: up to 2 roads placed for the player
- Output: card removed from player's hand (discarded)
- Output: exception

Step 2:

- card: Pointer / Case (must be ROAD_BUILDER) — see Common Preconditions P1–P4
- edge1: Pointer / Case (valid, occupied, not connected to network)
- edge2: Pointer / Case (valid, occupied, not connected to network, null if only 1 road remaining)
- road count: Interval [0, 15], Appending a Single Element (up to twice)
- Output (roads placed): Count [0, 2]
- Output (exception thrown): Boolean

Step 3:

- card (Common Preconditions): null (P1); wrong type (P2); not playable / same turn (P3); already played (P4)
- Input edge1 (Pointer / Case): null; valid (unoccupied, connected); already occupied; not connected to network
- Input edge2 (Pointer / Case): null (only 1 road placed); valid; already occupied; not connected (including via edge1)
- State road count (Interval [0, 15]): 0 (LOW — 15 remaining); 13 (last pair that fits); 14 (only 1 remaining); 15 (HIGH — no roads remaining)
- Output: roads appended to player's road list; card removed from hand
- Output: "Edge cannot be null." / "Edge is already occupied." / "Road must connect to player's existing network." / "No roads remaining."


|              | System under test                                                                          | Expected output                                                             | Implemented? |
| ------------ | ------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------- | ------------ |
| Test Case 33 | card = null                                                                                | IllegalArgumentException: "Development card cannot be null."                | :white_check_mark: |
| Test Case 34 | card type = VICTORY_POINT (not ROAD_BUILDER)                                               | IllegalArgumentException: "Card is not a Road Builder card."                | :white_check_mark: |
| Test Case 35 | card drawn this round (not playable)                                                       | IllegalStateException: "Card cannot be played the same turn it was purchased." | :white_check_mark: |
| Test Case 36 | player already played a dev card this turn                                                 | IllegalStateException: "Already played a development card this turn."       | :white_check_mark: |
| Test Case 37 | edge1 = null, roads placed = 0                                                             | IllegalArgumentException: "Edge cannot be null."                            | :white_check_mark: |
| Test Case 38 | edge1 valid, edge2 valid, roads placed = 0 (15 remaining)                                  | 2 roads placed; player road count increases by 2; card removed from hand    | :white_check_mark: |
| Test Case 39 | edge1 valid, edge2 valid, roads placed = 13 (2 remaining — last pair that fits)            | 2 roads placed; player road count is 15; card removed                       | :white_check_mark: |
| Test Case 40 | edge1 valid, edge2 = null, roads placed = 14 (only 1 remaining)                            | 1 road placed; player road count is 15; card removed                        | :white_check_mark: |
| Test Case 41 | roads placed = 15 (no roads remaining)                                                     | IllegalStateException: "No roads remaining."                                | :white_check_mark: |
| Test Case 42 | edge1 is already occupied, roads placed = 0                                                | IllegalArgumentException: "Edge is already occupied."                       | :white_check_mark: |
| Test Case 43 | edge1 not connected to player's network, roads placed = 0                                  | IllegalArgumentException: "Road must connect to player's existing network." | :white_check_mark: |
| Test Case 44 | edge1 valid, edge2 is already occupied, roads placed = 0                                   | IllegalArgumentException: "Edge is already occupied."                       | :white_check_mark: |
| Test Case 45 | edge1 valid, edge2 not connected to player's network (including edge1), roads placed = 0   | IllegalArgumentException: "Road must connect to player's existing network." | :white_check_mark: |


---

### Method under test: `playYearOfPlentyCard(Player player, DevelopmentCard card, int currentRound, Resource resource1, Resource resource2)`

Per official Catan rules: the player immediately takes any 2 resource cards
from the bank supply. The two may be the same or different types. The progress
card is removed from the game after play.

Step 1:

- Input: player, card, currentRound, resource1, resource2
- Output: player receives resource1 and resource2 from the bank
- Output: card removed from player's hand (discarded)
- Output: exception

Step 2:

- card: Pointer / Case (must be YEAR_OF_PLENTY) — see Common Preconditions P1–P4
- resource1: Pointer / Case (BRICK, GRAIN, LUMBER, ORE, WOOL; DESERT is invalid)
- resource2: Pointer / Case (same set)
- Output (resources received): Boolean
- Output (exception thrown): Boolean

Step 3:

- card (Common Preconditions): null (P1); wrong type (P2); not playable / same turn (P3); already played (P4)
- Input resource1 (Pointer / Case): null; BRICK; GRAIN; LUMBER; ORE; WOOL; DESERT (invalid)
- Input resource2 (Pointer / Case): null; BRICK; GRAIN; LUMBER; ORE; WOOL; DESERT (invalid)
- Combined resource1 and resource2: same type (valid); different types (valid)
- Output: player's resource counts increase accordingly; card removed from hand
- Output: "Resource cannot be null." / "Cannot take DESERT as a resource."


|              | System under test                                                                         | Expected output                                                              | Implemented? |
| ------------ | ----------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------- | ------------ |
| Test Case 46 | card = null                                                                               | IllegalArgumentException: "Development card cannot be null."                 | :white_check_mark: |
| Test Case 47 | card type = KNIGHT (not YEAR_OF_PLENTY)                                                   | IllegalArgumentException: "Card is not a Year of Plenty card."               | :white_check_mark: |
| Test Case 48 | card drawn this round (not playable)                                                      | IllegalStateException: "Card cannot be played the same turn it was purchased." | :white_check_mark: |
| Test Case 49 | player already played a dev card this turn                                                | IllegalStateException: "Already played a development card this turn."        | :white_check_mark: |
| Test Case 50 | resource1 = null, resource2 = BRICK                                                      | IllegalArgumentException: "Resource cannot be null."                         | :white_check_mark: |
| Test Case 51 | resource1 = BRICK, resource2 = null                                                      | IllegalArgumentException: "Resource cannot be null."                         | :white_check_mark: |
| Test Case 52 | resource1 = DESERT, resource2 = ORE                                                      | IllegalArgumentException: "Cannot take DESERT as a resource."                | :white_check_mark: |
| Test Case 53 | resource1 = LUMBER, resource2 = DESERT                                                   | IllegalArgumentException: "Cannot take DESERT as a resource."                | :white_check_mark: |
| Test Case 54 | resource1 = ORE, resource2 = ORE (same type)                                             | player gains 2 ORE; card removed from hand                                  | :white_check_mark: |
| Test Case 55 | resource1 = BRICK, resource2 = WOOL (different types)                                    | player gains 1 BRICK and 1 WOOL; card removed from hand                     | :white_check_mark: |


---

### Method under test: `countVictoryPointCards(List<DevelopmentCard> hand)`

Per official Catan rules: each Victory Point card (Library, Market, Chapel,
Great Hall, University) is worth 1 victory point. VP cards are kept hidden in
the player's hand and only revealed when the player declares victory at 10+
points. They are never "played" in the discard sense — they simply count
toward the point total.

Step 1:

- Input: hand (player's development card hand)
- Output: int (count of VICTORY_POINT cards in hand)

Step 2:

- hand: Pointer / Size of Collection
- Output: Count [0, 5]

Step 3:

- Input hand (Pointer / Size of Collection): null (CAN'T SET by caller); empty list; 1 VP card; >1 VP card; mixed VP and non-VP cards; no VP cards among non-VP cards
- Output (Count): 0 (no VP cards); 1; 5 (maximum VP cards in deck)


|              | System under test                                                    | Expected output | Implemented? |
| ------------ | -------------------------------------------------------------------- | --------------- | ------------ |
| Test Case 56 | hand is empty                                                        | 0               | :white_check_mark: |
| Test Case 57 | hand contains 1 VICTORY_POINT card only                              | 1               | :white_check_mark: |
| Test Case 58 | hand contains 3 KNIGHT and 2 VICTORY_POINT cards                     | 2               | :white_check_mark: |
| Test Case 59 | hand contains 5 VICTORY_POINT cards (maximum)                        | 5               | :white_check_mark: |
| Test Case 60 | hand contains 3 KNIGHT, 1 MONOPOLY, 0 VICTORY_POINT cards           | 0               | :white_check_mark: |
