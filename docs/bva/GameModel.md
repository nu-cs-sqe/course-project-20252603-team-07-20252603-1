
# GameModel BVA

`GameModel` coordinates the game state: it enforces phase rules, checks resource costs, delegates board
mutations to `BoardHandler`, and deducts resources from the current player.

---

### Constructor initial state: `GameModel(List<Player>, BoardHandler)`

|             | State of the System       | Expected output            | Implemented?       |
|-------------|---------------------------|----------------------------|--------------------|
| Test Case 1 | new model, one player     | getCurrentPhase() = BEFORE_ROLL | :white_check_mark: |

---

### Method under test: `performTurn(int roll)`

Transitions phase from `BEFORE_ROLL` to `GENERAL_PLAY` (non-7) or `MOVE_ROBBER` (7).
Throws `IllegalGamePhaseException` if called outside `BEFORE_ROLL`.

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

|             | State of the System                             | Expected output                              | Implemented?       |
|-------------|-------------------------------------------------|----------------------------------------------|--------------------|
| Test Case 2 | BEFORE_ROLL, roll = 2 (minimum)                 | phase transitions to GENERAL_PLAY            | :white_check_mark: |
| Test Case 3 | BEFORE_ROLL, roll = 12 (maximum)                | phase transitions to GENERAL_PLAY            | :white_check_mark: |
| Test Case 4 | BEFORE_ROLL, roll = 7 (robber trigger)          | phase transitions to MOVE_ROBBER             | :white_check_mark: |
| Test Case 5 | GENERAL_PLAY (already rolled), roll = 6         | IllegalGamePhaseException                    | :white_check_mark: |

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

|             | State of the System                                             | Expected output                                          | Implemented?       |
|-------------|-----------------------------------------------------------------|----------------------------------------------------------|--------------------|
| Test Case 6 | GENERAL_PLAY, two players (Alice → Bob)                        | phase = BEFORE_ROLL; current player advances to Bob      | :white_check_mark: |
| Test Case 7 | BEFORE_ROLL                                                     | IllegalGamePhaseException                                | :white_check_mark: |
| Test Case 8 | MOVE_ROBBER                                                     | IllegalGamePhaseException                                | :white_check_mark: |

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

|             | State of the System                                                   | Expected output                              | Implemented?       |
|-------------|-----------------------------------------------------------------------|----------------------------------------------|--------------------|
| Test Case 1 | GENERAL_PLAY, brick=1, lumber=1 (at cost boundary), board succeeds    | success                                      | :white_check_mark: |
| Test Case 2 | GENERAL_PLAY, brick=1, lumber=1, board throws                         | IllegalRoadPlacementException, no deduction  | :white_check_mark: |
| Test Case 3 | GENERAL_PLAY, brick=0 (below cost boundary)                           | InsufficientResourcesException               | :white_check_mark: |
| Test Case 4 | BEFORE_ROLL (invalid phase)                                           | IllegalGamePhaseException                    | :white_check_mark: |
| Test Case 5 | RESOURCE_PRODUCTION (invalid phase)                                   | IllegalGamePhaseException                    | :white_check_mark: |
| Test Case 6 | GENERAL_PLAY, brick=1, lumber=0 (second resource below cost boundary) | InsufficientResourcesException               | :white_check_mark: |
| Test Case 7 | GENERAL_PLAY, brick=2, lumber=2 (surplus), board succeeds             | success (surplus does not prevent building)  | :white_check_mark: |
| Test Case 8 | ROAD_BUILDING_DEV_CARD, brick=1, lumber=1, board succeeds             | success (alternate valid phase)              | :white_check_mark: |

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
