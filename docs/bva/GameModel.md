
# GameModel BVA

`GameModel` coordinates the game state: it enforces phase rules, checks resource costs, delegates board
mutations to `BoardHandler`, and deducts resources from the current player.

---

### Constructor initial state: `GameModel(List<Player>, BoardHandler)`

|             | State of the System       | Expected output            | Implemented?       |
|-------------|---------------------------|----------------------------|--------------------|
| Test Case 1 | new model, one player     | getCurrentPhase() = BEFORE_ROLL | :white_check_mark: |

---

### Method under test: `getCurrentPlayerIndex()`

|             | State of the System          | Expected output | Implemented?       |
|-------------|------------------------------|-----------------|--------------------|
| Test Case 1 | fresh model after construction | 0             | :white_check_mark: |

---

### Method under test: `getCurrentRound()`

|             | State of the System          | Expected output | Implemented?       |
|-------------|------------------------------|-----------------|--------------------|
| Test Case 1 | fresh model after construction | 0             | :white_check_mark: |

---

### Method under test: `moveRobberAndSteal()`

|             | State of the System | Expected output      | Implemented?       |
|-------------|---------------------|----------------------|--------------------|
| Test Case 1 | any state           | no exception thrown  | :white_check_mark: |

---

### Method under test: `getTurnOrder()`

|             | State of the System                | Expected output                            | Implemented?       |
|-------------|------------------------------------|--------------------------------------------|---------------------|
| Test Case 1 | model with RED and BLUE players    | list contains both players (size = 2)      | :white_check_mark: |

---

### Method under test: `getOtherPlayers()`

Returns all players except the current player.

|             | State of the System                                         | Expected output                       | Implemented?       |
|-------------|-------------------------------------------------------------|---------------------------------------|--------------------|
| Test Case 1 | current player = RED; players = [RED, BLUE]                 | list contains only BLUE (size = 1)    | :white_check_mark: |

---

### Method under test: `performTurn(int roll)`

Transitions phase from `BEFORE_ROLL` to `GENERAL_PLAY` (non-7) or `MOVE_ROBBER` (7).
Throws `IllegalGamePhaseException` if called outside `BEFORE_ROLL`.
Wraps `EmptyDeckException` as `IllegalArgumentException` if a resource deck is empty during distribution.

Step 1:

- Input: roll (int)
- State: current game phase
- Output: phase transitions to GENERAL_PLAY (roll ≠ 7) or MOVE_ROBBER (roll = 7)
- Output: exception when current phase is not BEFORE_ROLL

Step 2:

- roll: Interval [2, 12]; special value 7
- Game phase: BEFORE_ROLL (valid); any other (invalid)

Step 3:

- roll: 2 (LOW), 7 (robber trigger), 12 (HIGH)
- phase: BEFORE_ROLL (valid); GENERAL_PLAY (already rolled — invalid)

|             | State of the System                                          | Expected output                                              | Implemented?       |
|-------------|--------------------------------------------------------------|--------------------------------------------------------------|--------------------|
| Test Case 2 | BEFORE_ROLL, roll = 2 (minimum)                              | phase transitions to GENERAL_PLAY                            | :white_check_mark: |
| Test Case 3 | BEFORE_ROLL, roll = 12 (maximum)                             | phase transitions to GENERAL_PLAY                            | :white_check_mark: |
| Test Case 4 | BEFORE_ROLL, roll = 7 (robber trigger)                       | phase transitions to MOVE_ROBBER                             | :white_check_mark: |
| Test Case 5 | GENERAL_PLAY (already rolled), roll = 6                      | IllegalGamePhaseException                                    | :white_check_mark: |
| Test Case 6 | BEFORE_ROLL, roll = 6, WOOL resource deck throws EmptyDeckException | IllegalArgumentException with same message as EmptyDeckException | :white_check_mark: |

---

### Method under test: `endTurn()`

Advances to the next player and resets phase to `BEFORE_ROLL`. Throws `IllegalGamePhaseException`
if called outside `GENERAL_PLAY`.

Step 1:

- State: current game phase, player turn order
- Output: phase resets to BEFORE_ROLL; next player becomes current
- Output: exception when current phase is not GENERAL_PLAY

Step 2:

- Game phase: GENERAL_PLAY (valid); BEFORE_ROLL (invalid); MOVE_ROBBER (invalid)

Step 3:

- phase: GENERAL_PLAY; BEFORE_ROLL (invalid); MOVE_ROBBER (invalid)

|             | State of the System                                            | Expected output                                          | Implemented?       |
|-------------|----------------------------------------------------------------|----------------------------------------------------------|--------------------|
| Test Case 6 | GENERAL_PLAY, two players (Alice → Bob)                        | phase = BEFORE_ROLL; current player advances to Bob      | :white_check_mark: |
| Test Case 7 | BEFORE_ROLL                                                    | IllegalGamePhaseException                                | :white_check_mark: |
| Test Case 8 | MOVE_ROBBER                                                    | IllegalGamePhaseException                                | :white_check_mark: |

---

### Method under test: `attemptBuildSettlement(nodeID)`

Cost: 1 brick + 1 grain + 1 lumber + 1 wool. Max 5 settlements per player.
Resource check iterates in enum ordinal order: BRICK → GRAIN → LUMBER → WOOL.

Step 1:

- Input: nodeID (board position)
- State: current game phase, settlement count, per-resource counts (brick, grain, lumber, wool)
- Output: settlement placed, resources deducted, decks replenished, settlement count incremented
- Output: exception

Step 2:

- nodeID: delegates validation to `BoardHandler`
- Game phase: Case {GENERAL_PLAY (allowed), any other (not allowed)}
- Settlement count: Interval [0, 5]; max = 5
- Per resource (brick, grain, lumber, wool): Interval [0, 19]; cost boundary = 1

Step 3:

- Game phase: GENERAL_PLAY; RESOURCE_PRODUCTION (representative invalid); BEFORE_ROLL
- Settlement count: 0 (LOW); 4 (HIGH−1); 5 (HIGH — exceeds max)
- Brick (first checked): 0 (below cost); 1 (at cost); 2 (surplus)
- Grain (second checked): 0 (below cost, brick already ≥ 1); 1 (at cost)
- Lumber (third checked): 0 (below cost, brick & grain already ≥ 1); 1 (at cost)
- Wool (fourth checked): 0 (below cost, brick/grain/lumber already ≥ 1); 1 (at cost)

|              | State of the System                                                           | Expected output                                   | Implemented?       |
|--------------|-------------------------------------------------------------------------------|---------------------------------------------------|--------------------|
| Test Case 1  | GENERAL_PLAY, count=0, all resources=1 (at cost boundary), board succeeds     | success                                           | :white_check_mark: |
| Test Case 2  | GENERAL_PLAY, count=0, all resources=1, board throws                          | IllegalSettlementPlacementException, no deduction | :white_check_mark: |
| Test Case 3  | GENERAL_PLAY, count=0, brick=0 (below cost boundary)                          | InsufficientResourcesException                    | :white_check_mark: |
| Test Case 4  | GENERAL_PLAY, count=5 (at max)                                                | IllegalSettlementPlacementException               | :white_check_mark: |
| Test Case 5  | GENERAL_PLAY, count=4 (one below max), all resources=1, board succeeds        | success                                           | :white_check_mark: |
| Test Case 6  | RESOURCE_PRODUCTION (invalid phase)                                           | IllegalGamePhaseException                         | :white_check_mark: |
| Test Case 7  | BEFORE_ROLL (invalid phase)                                                   | IllegalGamePhaseException                         | :white_check_mark: |
| Test Case 8  | GENERAL_PLAY, count=0, brick=1, grain=0 (second resource below cost boundary) | InsufficientResourcesException                    | :white_check_mark: |
| Test Case 9  | GENERAL_PLAY, count=0, brick=1, grain=1, lumber=0 (third resource below cost) | InsufficientResourcesException                    | :white_check_mark: |
| Test Case 10 | GENERAL_PLAY, count=0, brick=1, grain=1, lumber=1, wool=0 (fourth below cost) | InsufficientResourcesException                    | :white_check_mark: |
| Test Case 11 | GENERAL_PLAY, count=0, all resources=2 (surplus), board succeeds              | success (surplus does not prevent building)       | :white_check_mark: |
| Test Case 12 | SETUP_PHASE, board succeeds, correct SetupPhase method is called              | success (resources not reduced)                   | :white_check_mark: |

---

### Method under test: `attemptBuildRoad(startNodeID, endNodeID)`

Cost: 1 brick + 1 lumber. Resource check order: BRICK → LUMBER.
Valid phases: GENERAL_PLAY and ROAD_BUILDING_DEV_CARD.

Step 1:

- Input: startNodeID, endNodeID (delegates to BoardHandler)
- State: current game phase, brick count, lumber count
- Output: road placed, resources deducted, decks replenished
- Output: exception

Step 2:

- Game phase: Case {GENERAL_PLAY or ROAD_BUILDING_DEV_CARD (allowed), others (not allowed)}
- Brick: Interval [0, 19]; cost boundary = 1
- Lumber: Interval [0, 19]; cost boundary = 1

Step 3:

- Game phase: GENERAL_PLAY; ROAD_BUILDING_DEV_CARD (alternate valid); BEFORE_ROLL (invalid); RESOURCE_PRODUCTION (invalid)
- Brick (first checked): 0 (below cost); 1 (at cost); 2 (surplus)
- Lumber (second checked): 0 (below cost, brick already ≥ 1); 1 (at cost)

|             | State of the System                                                   | Expected output                                                                       | Implemented?       |
|-------------|-----------------------------------------------------------------------|---------------------------------------------------------------------------------------|--------------------|
| Test Case 1 | GENERAL_PLAY, brick=1, lumber=1 (at cost boundary), board succeeds    | success                                                                               | :white_check_mark: |
| Test Case 2 | GENERAL_PLAY, brick=1, lumber=1, board throws                         | IllegalRoadPlacementException, no deduction                                           | :white_check_mark: |
| Test Case 3 | GENERAL_PLAY, brick=0 (below cost boundary)                           | InsufficientResourcesException                                                        | :white_check_mark: |
| Test Case 4 | BEFORE_ROLL (invalid phase)                                           | IllegalGamePhaseException                                                             | :white_check_mark: |
| Test Case 5 | RESOURCE_PRODUCTION (invalid phase)                                   | IllegalGamePhaseException                                                             | :white_check_mark: |
| Test Case 6 | GENERAL_PLAY, brick=1, lumber=0 (second resource below cost boundary) | InsufficientResourcesException                                                        | :white_check_mark: |
| Test Case 7 | GENERAL_PLAY, brick=2, lumber=2 (surplus), board succeeds             | success (surplus does not prevent building)                                           | :white_check_mark: |
| Test Case 8 | ROAD_BUILDING_DEV_CARD, brick=1, lumber=1, board succeeds             | success (alternate valid phase)                                                       | :white_check_mark: |
| Test Case 9 | SETUP_PHASE, board succeeds                                           | success, no resources reduced                                                         | :white_check_mark: |
| Test Case 8 | ROAD_BUILDING_DEV_CARD, board succeeds                                | Succeeds, no resources are reduced or checked, proper method from BoardHandler called | :white_check_mark: |

---

### Method under test: `attemptBuildCity(nodeID)`

Cost: 3 ore + 2 grain. Ore checked first.

Step 1:

- Input: nodeID (delegates to BoardHandler)
- State: current game phase, ore count, grain count
- Output: city placed, resources deducted, decks replenished
- Output: exception

Step 2:

- Game phase: Case {GENERAL_PLAY (allowed), others (not allowed)}
- Ore: Interval [0, 19]; cost boundary = 3
- Grain: Interval [0, 19]; cost boundary = 2

Step 3:

- Game phase: GENERAL_PLAY; ROAD_BUILDING_DEV_CARD (invalid); BEFORE_ROLL (invalid)
- Ore: 0 (zero); 2 (one below cost boundary); 3 (at cost boundary); 4 (surplus)
- Grain (only reached if ore ≥ 3): 0 (zero); 1 (one below cost boundary); 2 (at cost boundary); 3 (surplus)

|             | State of the System                                          | Expected output                                    | Implemented?       |
|-------------|--------------------------------------------------------------|----------------------------------------------------|--------------------|
| Test Case 1 | GENERAL_PLAY, ore=3 (at boundary), grain=2 (at boundary), board succeeds | success                               | :white_check_mark: |
| Test Case 2 | GENERAL_PLAY, ore=2 (one below boundary)                     | InsufficientResourcesException                     | :white_check_mark: |
| Test Case 3 | GENERAL_PLAY, ore=4, grain=1 (one below grain boundary)      | InsufficientResourcesException                     | :white_check_mark: |
| Test Case 4 | GENERAL_PLAY, ore=3, grain=2, board throws                   | IllegalCityPlacementException                      | :white_check_mark: |
| Test Case 5 | ROAD_BUILDING_DEV_CARD (invalid phase)                       | IllegalGamePhaseException                          | :white_check_mark: |
| Test Case 6 | BEFORE_ROLL (invalid phase)                                  | IllegalGamePhaseException                          | :white_check_mark: |
| Test Case 7 | GENERAL_PLAY, ore=0 (zero, well below boundary)              | InsufficientResourcesException                     | :white_check_mark: |
| Test Case 8 | GENERAL_PLAY, ore=3, grain=0 (zero, well below boundary)     | InsufficientResourcesException                     | :white_check_mark: |
| Test Case 9 | GENERAL_PLAY, ore=4 (surplus), grain=3 (surplus), board succeeds | success (surplus does not prevent building)    | :white_check_mark: |

### Method under test: `attemptPortTrade(Port port, Resource giving, Resource receiving)`

Step 1:

- Input: Port (player specific)
- Input: current game phase, deck size
- Output: trade executed, player resources updated, decks updated
- Output: exception

Step 2:

- Game phase:  Cases {GENERAL_PLAY (allowed), others (not allowed)} 
- Bank deck (receiving): Interval [0, 19]; boundary = 1 (need at least 1)

Step 3:

- Game phase: GENERAL_PLAY; BEFORE_ROLL (invalid);
- Bank deck: 0 (empty, below boundary); 1 (at boundary); 19 (max/surplus)

|             | State of the System                                      | Expected output                | Implemented?       |
|-------------|----------------------------------------------------------|--------------------------------|--------------------|
| Test Case 1 | GENERAL_PLAY, bank has 1 card (at boundary), valid trade | success                        | :white_check_mark: |
| Test Case 2 | GENERAL_PLAY, bank has 0 cards (empty)                   | InsufficientResourcesException | :white_check_mark: |
| Test Case 3 | GENERAL_PLAY, bank has 19 cards (max), valid trade       | success                        | :white_check_mark: |
| Test Case 4 | BEFORE_ROLL (invalid phase)                              | IllegalGamePhaseException      | x                  |

---

### Method  under test: `performTurn(int roll)` — resource distribution path

When roll ≠ 7, `performTurn` delegates to `distributeResources`, which calls `board.computeResourceDemand(roll)` and distributes resources per the following rules:
- **Multiple players competing for a resource**: if the bank deck has fewer cards than total demand, **no player receives that resource** (all-or-nothing).
- **Single player owed a resource**: the player receives however many cards the bank has, which may be a partial amount (including zero if the bank is empty).

Each resource is evaluated independently.

Step 1:

- Input: roll (die value)
- Input: Demand map returned by `board.computeResourceDemand(roll)` (Map<Resource, Map<Player, Integer>>)
- Input: Bank deck sizes per resource (Interval [0, 19])
- Output: Player resource counts updated; decks drawn; game phase transitions to GENERAL_PLAY
- Output: No change (bank insufficient for multi-player resource, or demand map empty, or bank empty for single player)

Step 2:

- roll: Cases {7 (robber, no distribution), non-7 (distribution path)}
- Demand map: Cases {empty (no active hexes), non-empty}
- Per resource, number of players: Cases {single player, multiple players}
  - Single player: deck drawn up to demand; player receives `drawn` amount (partial if bank short, nothing if bank empty)
  - Multiple players: deck.total < total demand → no distribution; deck.total ≥ total demand → each player draws their amount
- Single-player demand amount: 1 (settlement) or 2 (city)
- Multiple resources in demand: each evaluated independently

Step 3:

- roll: non-7 value (e.g. 6) for distribution path; 7 for robber path
- Demand map: empty; one resource one player; one resource two players; two resources
- Single player, deck sizes: 0 (empty → player gets 0); partial (bank < owed → player gets partial); full (bank ≥ owed)
- Multiple players, deck sizes: 1 vs. demand of 2 (insufficient → no one gets any); 2 (exactly covers two players of 1 each)
- Demand amount: 1 (settlement); 2 (city)
- Two resources, one covered, one not: only covered resource distributes

|              | State of the System                                                                              | Expected output                                                                     | Implemented?       |
|--------------|--------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------|-------------------|
| Test Case 10 | Board returns `{WOOL: {red: 1}}`; wool deck has 5 cards (single player, bank sufficient)        | `drawMultiple(1)` called; red receives 1 WOOL; phase → GENERAL_PLAY                 | :white_check_mark: |
| Test Case 11 | Board returns `{WOOL: {red: 1}}`; wool deck has 0 cards (single player, bank empty)             | `drawMultiple(1)` called → returns 0; no player update; phase → GENERAL_PLAY        | :white_check_mark: |
| Test Case 12 | Board returns `{WOOL: {red: 1, blue: 1}}`; wool deck has 1 card (multi-player, bank short)      | No draw, neither player receives WOOL (all-or-nothing rule)                          | :white_check_mark: |
| Test Case 13 | Board returns `{WOOL: {red: 1, blue: 1}}`; wool deck has exactly 2 cards (multi-player, exact)  | `drawMultiple(1)` called twice; both players receive 1 WOOL each                     | :white_check_mark: |
| Test Case 14 | Board returns `{ORE: {red: 2}}`; ore deck has 10 cards (single player, city demand = 2)         | `drawMultiple(2)` called; red receives 2 ORE                                         | :white_check_mark: |
| Test Case 15 | Board returns `{}`; all decks idle                                                               | No deck interactions, no player updates; phase → GENERAL_PLAY                        | :white_check_mark: |
| Test Case 16 | Board returns `{WOOL: {red: 1}, ORE: {red: 1, blue: 1}}`; wool deck ok, ore deck has 1 card     | WOOL: `drawMultiple(1)`, red +1; ORE: skipped (multi-player, bank insufficient)      | :white_check_mark: |
| Test Case 17 | Board returns `{WOOL: {red: 3}}`; wool deck has 2 cards (single player, bank partially short)   | `drawMultiple(3)` called → returns 2; red receives 2 WOOL (partial); phase → GENERAL_PLAY | :white_check_mark: |

Ends the current player's turn

Inputs:
- State of Game, 
  - if checkCurrentPlayerHasTenOrMoreVictoryPoints() passes and GamePhase is in END_GAME, phase stays in end_game and currentPlayerColor Stays the same
  - else, gamePhase switches to BEFORE_ROLL, currentPlayerColor changes
  - Collection of players RED -> ORANGE -> WHITE -> BLUE
  - GamePhase needs to start in GENERAL_PLAY

Outputs:
- GamePhase -> END_GAME or BEFORE_ROLL
- currentPlayerColor -> stays the same, changes (RED, ORANGE, WHITE, BLUE)
- IllegalGamePhaseException

|             | State of the System  | Expected output           | Implemented?       |
|-------------|----------------------|---------------------------|--------------------|
| Test Case 1 | RED, check passes    | RED, END_GAME phase       | :white_check_mark: |
| Test Case 2 | ORANGE, check passes | ORANGE, END_GAME phase    | :white_check_mark: |
| Test Case 3 | WHITE, check fails   | BLUE, BEFORE_ROLL phase   | :white_check_mark: |
| Test Case 4 | BLUE, check fails    | RED, BEFORE_ROLL phase    | :white_check_mark: |
| Test Case 5 | RED, check fails     | ORANGE, BEFORE_ROLL phase | :white_check_mark: |
| Test Case 6 | ORANGE, check fails  | WHITE, BEFORE_ROLL phase  | :white_check_mark: |
| Test Case 7 | Incorrect game phase | IllegalGamePhaseException | :white_check_mark: |

### Method under test: `buyDevCard(DevelopmentCardDeck deck)`

Cost: 1 ORE + 1 WOOL + 1 GRAIN. Valid phase: GENERAL_PLAY only.
Resource check order (matching existing GameModel convention): ORE → WOOL → GRAIN.
The deck is drawn **after** resources are validated; resources are deducted only on a successful draw.
The ORE, WOOL, and GRAIN resource decks are replenished by 1 each on success.
Returns the drawn `DevelopmentCard`.

Step 1:

- Input: deck (DevelopmentCardDeck)
- State: current game phase, current player's ORE/WOOL/GRAIN counts, deck size
- Output: DevelopmentCard drawn; player's ORE/WOOL/GRAIN each decremented by 1; ORE/WOOL/GRAIN resource decks each replenished by 1; card added to player's hand
- Output: exception

Step 2:

- Game phase: Case {GENERAL_PLAY (allowed), others (not allowed)}
- Player ORE: Interval [0, 19]; cost boundary = 1
- Player WOOL: Interval [0, 19]; cost boundary = 1
- Player GRAIN: Interval [0, 19]; cost boundary = 1
- Deck size: Interval [0, 25]; boundary values: 0 (empty), 1 (last card), 25 (full)

Step 3:

- Game phase: GENERAL_PLAY; BEFORE_ROLL (invalid); MOVE_ROBBER (invalid)
- ORE (first checked): 0 (below cost); 1 (at cost boundary); 3 (surplus)
- WOOL (second checked): 0 (below cost, ORE already ≥ 1); 1 (at cost boundary)
- GRAIN (third checked): 0 (below cost, ORE/WOOL already ≥ 1); 1 (at cost boundary)
- Deck size: 0 (empty — EmptyDeckException after resource check passes); 1 (last card); 25 (full)

|             | State of the System                                                                              | Expected output                                                                                          | Implemented? |
|-------------|--------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=1 (exact cost), deck=25 (full)                               | card returned; player loses 1 each ORE/WOOL/GRAIN; ORE/WOOL/GRAIN decks each replenished by 1; deck countRemaining = 24 | :white_check_mark: |
| Test Case 2 | GENERAL_PLAY, ORE=3, WOOL=2, GRAIN=4 (surplus each), deck=25                                    | card returned; player loses 1 each ORE/WOOL/GRAIN; surplus does not prevent purchase                    | :white_check_mark: |
| Test Case 3 | GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=1, deck=1 (last card)                                        | card returned; deck countRemaining = 0                                                                   | :white_check_mark: |
| Test Case 4 | GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=1, deck=0 (empty)                                            | EmptyDeckException; player resources NOT deducted                                                        | :white_check_mark: |
| Test Case 5 | GENERAL_PLAY, ORE=0 (below cost boundary)                                                        | InsufficientResourcesException                                                                           | :white_check_mark: |
| Test Case 6 | GENERAL_PLAY, ORE=1, WOOL=0 (below cost boundary, ORE already ≥ 1)                              | InsufficientResourcesException                                                                           | :white_check_mark: |
| Test Case 7 | GENERAL_PLAY, ORE=1, WOOL=1, GRAIN=0 (below cost boundary, ORE/WOOL already ≥ 1)               | InsufficientResourcesException                                                                           | :white_check_mark: |
| Test Case 8 | BEFORE_ROLL                                                                                      | IllegalGamePhaseException                                                                                | :white_check_mark: |
| Test Case 9 | MOVE_ROBBER                                                                                      | IllegalGamePhaseException                                                                                | :white_check_mark: |

---

### Method under test: `playDevCard(DevelopmentCard card)`

Validates that the current phase allows playing a development card and transitions
the game phase based on card type. This method is purely a game-state coordinator —
it does not execute card effects. Card effects (robber movement, road placement,
resource transfer) are triggered separately via the resulting phase.

Valid phases: `BEFORE_ROLL` and `GENERAL_PLAY` (dev cards may be played before or
after rolling dice per official Catan rules).

Phase transitions:
- `KNIGHT` → `MOVE_ROBBER`
- `ROAD_BUILDER` → `ROAD_BUILDING_DEV_CARD`
- `MONOPOLY` → `MONOPOLY_DEV_CARD`
- `YEAR_OF_PLENTY` → phase unchanged
- `VICTORY_POINT` → phase unchanged

Step 1:

- Input: card (DevelopmentCard)
- State: current game phase, card type
- Output: phase transitions per card type above
- Output: exception

Step 2:

- card: Pointer; null (invalid)
- card type: Case {KNIGHT, ROAD_BUILDER, MONOPOLY, YEAR_OF_PLENTY, VICTORY_POINT}
- Game phase: Case {BEFORE_ROLL or GENERAL_PLAY (valid), all others (invalid)}

Step 3:

- card: null (invalid); valid card
- card type: KNIGHT; ROAD_BUILDER; MONOPOLY; YEAR_OF_PLENTY; VICTORY_POINT
- Game phase: GENERAL_PLAY (valid); BEFORE_ROLL (valid); MOVE_ROBBER (invalid representative)

|             | State of the System                         | Expected output                                                      | Implemented? |
|-------------|---------------------------------------------|----------------------------------------------------------------------|--------------|
| Test Case 1 | card = null                                 | IllegalArgumentException: "Development card cannot be null."         | :white_check_mark: |
| Test Case 2 | MOVE_ROBBER (invalid phase), valid card     | IllegalGamePhaseException: "Not proper phase for that action"        | :white_check_mark: |
| Test Case 3 | GENERAL_PLAY, card type = KNIGHT            | phase → MOVE_ROBBER                                                  | :white_check_mark: |
| Test Case 4 | GENERAL_PLAY, card type = ROAD_BUILDER      | phase → ROAD_BUILDING_DEV_CARD                                       | :white_check_mark: |
| Test Case 5 | GENERAL_PLAY, card type = MONOPOLY          | phase → MONOPOLY_DEV_CARD                                            | :white_check_mark: |
| Test Case 6 | GENERAL_PLAY, card type = YEAR_OF_PLENTY    | phase unchanged (GENERAL_PLAY)                                       | :white_check_mark: |
| Test Case 7 | GENERAL_PLAY, card type = VICTORY_POINT     | phase unchanged (GENERAL_PLAY)                                       | :white_check_mark: |
| Test Case 8 | BEFORE_ROLL, card type = KNIGHT             | phase → MOVE_ROBBER                                                  | :white_check_mark: |

---

### Method under test: `handleLongestRoad()`

Handles checking and redistributing points based on longest road, to be called in building settlements and roads (things which can change longest road)

Inputs:
- currentLongestRoadColor -> RED, WHITE, ORANGE, BLUE
- Board state

Outputs:
- Cases -> BoardGraph result
  - PlayerColor.SETUP -> no one meets conditions for longest road
  - Same PlayerColor -> no change -> no victory points awarded
  - New PlayerColor -> change -> Victory Points Redistributed

|             | State of the System            | Expected output                                                       | Implemented?       |
|-------------|--------------------------------|-----------------------------------------------------------------------|--------------------|
| Test Case 1 | Currently no one, still no one | PlayerColor.setup                                                     | :white_check_mark: |
| Test Case 2 | Currently RED, stays RED       | PlayerColor.Red                                                       | :white_check_mark: |
| Test Case 3 | Currently SETUP, Becomes WHITE | PlayerColor.White, victory points awarded to WHITE                    | :white_check_mark: |
| Test Case 4 | Currently BLUE, Becomes ORANGE | PlayerColor.Orange, victory points awarded to Orange, taken from blue | :white_check_mark: |
| Test Case 5 | Currently ORANGE, becomes BLUE | PlayerColor.Blue, victory points awarded to BLUE, taken from ORANGE   | :white_check_mark: |
| Test Case 6 | Currently WHITE, becomes RED   | PlayerColor.Red, victory points awarded to RED, taken from white      | :white_check_mark: |


### Method under test: `offerTrade(TradeOffer offer)`

Step 1:

- Input: offer - validated by trademanager
- Input: Game phase
- Output: offer added
- Output: now OFFERING_TRADE phase
- Output: Exception

Step 2:

- Game phase: Now GENERAL_PLAY (allowed), others (not allowed)

Step 3:

- Game phase: GENERAL_PLAY; BEFORE_ROLL (invalid);


|             | State of the System         | Expected output                  | Implemented?       |
|-------------|-----------------------------|----------------------------------|--------------------|
| Test Case 1 | GENERAL_PLAY, valid offer   | phase → OFFERING_TRADE, success  | :white_check_mark: |
| Test Case 2 | BEFORE_ROLL (invalid phase) | IllegalGamePhaseException        | :white_check_mark: |


### Method under test: `acceptTrade(TradeOffer offer, Player acceptingPlayer)`

Step 1:

- Input: offer - validated by trademanager
- Input: Player
- Input: Game phase
- Output: trade executed
- Output: now GENERAL_PLAY phase
- Output: Exception

Step 2:

- Game phase: Now OFFERING_TRADE (allowed), others (not allowed)

Step 3:

- Game phase: OFFERING_TRADE; GENERAL_PLAY (invalid);


|             | State of the System                              | Expected output             | Implemented?       |
|-------------|--------------------------------------------------|-----------------------------|--------------------|
| Test Case 1 | OFFERING_TRADE, valid offer and accepting player | GENERAL_PLAY phase, success | :white_check_mark: |
| Test Case 2 | GENERAL_PLAY (invalid phase)                     | IllegalGamePhaseException   | :white_check_mark: |


### Method under test: `clearOffers()`

Step 1:

- Input: Game phase
- Output: Offers cleared
- Output: Exception

Step 2:

- Game phase: Now OFFERING_TRADE (allowed), others (not allowed)

Step 3:

- Game phase: GENERAL_PLAY; GENERAL_PLAY (invalid);


|             | State of the System            | Expected output           | Implemented?       |
|-------------|--------------------------------|---------------------------|--------------------|
| Test Case 1 | Currently no one, still no one | PlayerColor.setup         | :white_check_mark: |
| Test Case 2 | GENERAL_PLAY (invalid phase)   | IllegalGamePhaseException | :white_check_mark: |
