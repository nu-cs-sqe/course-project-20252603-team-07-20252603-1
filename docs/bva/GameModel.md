
# GameModel BVA

`GameModel` coordinates the game state: it enforces phase rules, checks resource costs, delegates board
mutations to `BoardHandler`, and deducts resources from the current player.

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

|              | State of the System                                                             | Expected output                                       | Implemented?       |
|--------------|---------------------------------------------------------------------------------|-------------------------------------------------------|--------------------|
| Test Case 1  | GENERAL_PLAY, count=0, all resources=1 (at cost boundary), board succeeds       | success                                               | :white_check_mark: |
| Test Case 2  | GENERAL_PLAY, count=0, all resources=1, board throws                            | IllegalSettlementPlacementException, no deduction     | :white_check_mark: |
| Test Case 3  | GENERAL_PLAY, count=0, brick=0 (below cost boundary)                           | InsufficientResourcesException                        | :white_check_mark: |
| Test Case 4  | GENERAL_PLAY, count=5 (at max)                                                  | IllegalSettlementPlacementException                   | :white_check_mark: |
| Test Case 5  | GENERAL_PLAY, count=4 (one below max), all resources=1, board succeeds          | success                                               | :white_check_mark: |
| Test Case 6  | RESOURCE_PRODUCTION (invalid phase)                                             | IllegalGamePhaseException                             | :white_check_mark: |
| Test Case 7  | BEFORE_ROLL (invalid phase)                                                     | IllegalGamePhaseException                             | :white_check_mark: |
| Test Case 8  | GENERAL_PLAY, count=0, brick=1, grain=0 (second resource below cost boundary)  | InsufficientResourcesException                        | :white_check_mark: |
| Test Case 9  | GENERAL_PLAY, count=0, brick=1, grain=1, lumber=0 (third resource below cost)  | InsufficientResourcesException                        | :white_check_mark: |
| Test Case 10 | GENERAL_PLAY, count=0, brick=1, grain=1, lumber=1, wool=0 (fourth below cost)  | InsufficientResourcesException                        | :white_check_mark: |
| Test Case 11 | GENERAL_PLAY, count=0, all resources=2 (surplus), board succeeds                | success (surplus does not prevent building)           | :white_check_mark: |

---

### Method under test: `attemptBuildRoad(startNodeID, endNodeID)`

Cost: 1 brick + 1 lumber. Resource check order: BRICK → LUMBER.

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

- Game phase: GENERAL_PLAY; BEFORE_ROLL (invalid)
- Brick (first checked): 0 (below cost); 1 (at cost); 2 (surplus)
- Lumber (second checked): 0 (below cost, brick already ≥ 1); 1 (at cost)

|             | State of the System                                                   | Expected output                              | Implemented?       |
|-------------|-----------------------------------------------------------------------|----------------------------------------------|--------------------|
| Test Case 1 | GENERAL_PLAY, brick=1, lumber=1 (at cost boundary), board succeeds    | success                                      | :white_check_mark: |
| Test Case 2 | GENERAL_PLAY, brick=1, lumber=1, board throws                         | IllegalRoadPlacementException, no deduction  | :white_check_mark: |
| Test Case 3 | GENERAL_PLAY, brick=0 (below cost boundary)                           | InsufficientResourcesException               | :white_check_mark: |
| Test Case 4 | BEFORE_ROLL (invalid phase)                                           | IllegalGamePhaseException                    | :white_check_mark: |
| Test Case 5 | RESOURCE_PRODUCTION (invalid phase)                                   | IllegalGamePhaseException                    | :white_check_mark: |
| Test Case 6 | GENERAL_PLAY, brick=1, lumber=0 (second resource below cost boundary) | InsufficientResourcesException               | :white_check_mark: |
| Test Case 7 | GENERAL_PLAY, brick=2, lumber=2 (surplus), board succeeds             | success (surplus does not prevent building)  | :white_check_mark: |

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

---

### Method under test: `performTurn(int roll)` — resource distribution path

When roll ≠ 7, `performTurn` delegates to `distributeResources`, which calls `board.computeResourceDemand(roll)` and distributes resources per the standard Catan bank rule: if the bank deck for a resource has fewer cards than the total demanded, **no player receives that resource**. Each resource is evaluated independently.

Step 1:

- Input: roll (die value)
- Input: Demand map returned by `board.computeResourceDemand(roll)` (Map<Resource, Map<Player, Integer>>)
- Input: Bank deck sizes per resource (Interval [0, 19])
- Output: Player resource counts updated; decks drawn; game phase transitions to GENERAL_PLAY
- Output: No change (bank insufficient, or demand map empty)

Step 2:

- roll: Cases {7 (robber, no distribution), non-7 (distribution path)}
- Demand map: Cases {empty (no active hexes), non-empty}
- Per resource: total demand vs. deck size — Interval comparison
  - deck.total < total demand → no distribution (bank rule)
  - deck.total == total demand → exactly enough, distribute
  - deck.total > total demand → more than enough, distribute
- Single-player demand amount: 1 (settlement) or 2 (city)
- Multiple resources in demand: each evaluated independently

Step 3:

- roll: non-7 value (e.g. 6) for distribution path; 7 for robber path
- Demand map: empty; one resource one player; one resource two players; two resources
- Deck size at boundary: 0 (empty, below any demand); 1 (at demand for one player); 1 vs. demand of 2 (insufficient for two players); 2 (exactly covers two players of 1 each)
- Demand amount: 1 (settlement); 2 (city)
- Two resources, one covered, one not: only covered resource distributes

|              | State of the System                                                                              | Expected output                                                               | Implemented?       |
|--------------|--------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|--------------------|
| Test Case 10 | Board returns `{WOOL: {red: 1}}`; wool deck has 5 cards                                         | `drawMultiple(1)` called; red receives 1 WOOL; phase → GENERAL_PLAY           | :white_check_mark: |
| Test Case 11 | Board returns `{WOOL: {red: 1}}`; wool deck has 0 cards (empty)                                 | No draw, no player update; phase → GENERAL_PLAY                               | :white_check_mark: |
| Test Case 12 | Board returns `{WOOL: {red: 1, blue: 1}}`; wool deck has 1 card (less than total demand of 2)   | No draw, neither player receives WOOL (bank rule)                             | :white_check_mark: |
| Test Case 13 | Board returns `{WOOL: {red: 1, blue: 1}}`; wool deck has exactly 2 cards                        | `drawMultiple(2)` called; both players receive 1 WOOL each                    | :white_check_mark: |
| Test Case 14 | Board returns `{ORE: {red: 2}}`; ore deck has 10 cards (city demand = 2)                        | `drawMultiple(2)` called; red receives 2 ORE                                  | :white_check_mark: |
| Test Case 15 | Board returns `{}`; all decks idle                                                               | No deck interactions, no player updates; phase → GENERAL_PLAY                 | :white_check_mark: |
| Test Case 16 | Board returns `{WOOL: {red: 1}, ORE: {red: 1, blue: 1}}`; wool deck ok, ore deck has 1 card     | WOOL distributed (drawMultiple(1), red +1); ORE skipped (bank insufficient)   | :white_check_mark: |

