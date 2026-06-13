### BVA for BoardHandler Class

'BoardHandler' is responsible for updating hexes and the board graph when players place a settlement, city, or a road. 
It will also calculate the longest road and handle the robber.

### Method under test: `buildSettlement(Player player, int nodeId)`

Step 1:
- Input: Player
- Input: nodeId
- Input: State of the board
- Output: State of the board
- Output: State of the hexes
- Output: Error

Step 2:
- Input - Player class
- Input - Interval
- Input - Cases
- Output - Cases
- Output - Cases
- Output - Exception

Step 3:
- Input: RED, BLUE, ORANGE, WHITE
- Input: 0, 53, -1, 54
- Input: Node is adjacent to 1, 2, or 3 hexes
- Output: Node now occupied, node not occupied, node still occupied by other player (was already occupied) 
- Output: Hexes have player in list of settlements, hex player list not updated - For integration testing, not unit testable
- Output: "Invalid NodeID - must be within [0, 53]."

|             | System under test             | Expected output                                                                                                    | Implemented?       |
|-------------|-------------------------------|--------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 1  | RED tries to claim node 0                                                     | Calls to add RED settlement to hex 0 and claimStoredNode, node level is settlement, owned by RED                   | :white_check_mark: |
| Test Case 2  | BLUE tries to claim node 53                                                   | Calls to add BLUE settlement to hex 18 and claimStoredNode, node level is settlement, owned by BLUE                | :white_check_mark: |
| Test Case 3  | ORANGE tries to claim node -1                                                 | "Invalid NodeID - must be within [0, 53].", playerClaimStoredNode and addPlayerSettlementToHex not called          | :white_check_mark: |
| Test Case 4  | WHITE tries to claim node 54                                                  | "Invalid NodeID - must be within [0, 53].", playerClaimStoredNode and addPlayerSettlementToHex not called          | :white_check_mark: |
| Test Case 5  | ORANGE tries to claim node 8                                                  | Calls to add ORANGE settlement to hexes 0, 1, and 4 and claimStoredNode, node level is settlement, owned by ORANGE | :white_check_mark: |
| Test Case 6  | BLUE tries to claim node 4                                                    | Calls to add BLUE settlement to hexes 0 and 1 and claimStoredNode, node level is settlement, owned by BLUE         | :white_check_mark: |
| Test Case 69 | Controller rejects claim: adjacent node already claimed (distance rule fired) | IllegalSettlementPlacementException with message "Can not claim node adjacent to node already claimed" propagates  | :white_check_mark: |



### Method under test: `buildCity(Player player, int nodeId)`

Step 1:
- Input: Player
- Input: nodeId
- Input: State of the board
- Output: State of the board
- Output: State of the hex
- Output: Error

Step 2:
- Input - Player class
- Input - Interval
- Input - Cases
- Output - Cases
- Output - Cases
- Output - Exception

Step 3:
- Input: RED, BLUE, ORANGE, WHITE
- Input: 0, 53, -1, 54
- Input: Node is adjacent to 1, 2, or 3 hexes
- Output: Node occupied, node not occupied, node still occupied by other player (was already occupied by other player) 
  - Note: Needed for cities, but not settlements, as claiming a node in BoardGraph already handles already owning a node, but isn't called here
- Output: Hex has player in list of cities, hex player list not updated - For integration testing, not unit testable
- Output: Player cities list updated or not (Other validation completed by Player BVA testing)
- Output: "Out of bounds nodeId", "Must upgrade a settlement to a city."

|              | System under test                                                       | Expected output                                                                                          | Implemented?       |
|--------------|-------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 7  | RED tries to build a city on node 0, which they had a settlement on     | Calls to remove RED settlement to hex 0 and add RED city to hex 0, node level is city, owned by RED      | :white_check_mark: |
| Test Case 8  | BLUE tries to build a city on node 53, which they had a settlement on   | Calls to remove BLUE settlement to hex 53 and add BLUE city to hex 53, node level is city, owned by BLUE | :white_check_mark: |
| Test Case 9  | ORANGE tries to build a city on node -1                                 | "Invalid NodeID - must be within [0, 53]."                                                               | :white_check_mark: |
| Test Case 10 | WHITE tries to build a city on node 54                                  | "Invalid NodeID - must be within [0, 53]."                                                               | :white_check_mark: |
| Test Case 11 | RED tries to build a city on node 6, which BLUE owns                    | "Node owned by other player, cannot build here.", no calls to hex, still owned by BLUE                   | :white_check_mark: |
| Test Case 12 | BLUE tries to build a city on node 36, which is unoccupied              | "Must upgrade a settlement to a city.", no calls to hex, still unowned and level 0                       | :white_check_mark: |
| Test Case 13 | ORANGE tries to build a city on node 20, which they had a settlement on | Calls to remove ORANGE settlement and add city to hexes 6 and 11, node level is city, owned by ORANGE    | :white_check_mark: |
| Test Case 14 | WHITE tries to build a city on node 24, which they had a settlement on  | Calls to remove WHITE settlement and add city to hexes 5, 9, and 10, node level is city, owned by RED    | :white_check_mark: |


### Method under test: `addRoad(Player player, int nodeId1, int nodeId2)`

Step 1:
- Input: nodeId
- Input: State of the board
- Input: Player
- Output: State of the board
- Output: Error

Step 2:
- Input - Interval
- Input - Cases
- Input - Player class
- Output - Cases
- Output - Exception

Step 3:
- Input: 0, 53, -1, 54
- Input: 0, 53, -1, 54
- Input: Edge claimed, edge unclaimed - Handled by BoardGraphController
- Input: nodeId1 == nodeId2 (same start and end node) - Handled by BoardGraphController
- Input: Non-existent edge (e.g., reversed direction such as [3, 0]) - Handled by BoardGraphController
- Input: RED, BLUE, ORANGE, WHITE
- Output: Edge claimed, edge unclaimed - Handled by BoardGraphController
- Output: "Edge nodeId out of bounds. Must be within [0, 53]."
- Output: IllegalArgumentException (same start/end or non-existent edge, thrown by BoardGraphController)
- Output: IllegalEdgeClaim (already claimed edge, thrown by BoardGraphController)

|              | System under test                                                  | Expected output                                      | Implemented?       |
|--------------|--------------------------------------------------------------------|------------------------------------------------------|--------------------|
| Test Case 15 | RED claims edge [0,1]                                              | playerClaimStoredEdge is called                      | :white_check_mark: |
| Test Case 16 | ORANGE claims edge [52, 53]                                        | playerClaimStoredEdge is called                      | :white_check_mark: |
| Test Case 17 | WHITE tries to claim edge [-1, 0]                                  | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 18 | WHITE tries to claim edge [0, -1]                                  | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 19 | BLUE tries to claim edge [53, 54]                                  | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 20 | BLUE tries to claim edge [54, 53]                                  | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 65 | RED tries to claim edge [5, 5] (same start and end)                | IllegalArgumentException                             | :white_check_mark: |
| Test Case 66 | RED tries to claim edge [3, 0] (non-existent edge)                 | IllegalArgumentException                             | :white_check_mark: |
| Test Case 67 | RED claims edge [0, 1], then BLUE tries to claim edge [0, 1] again | IllegalEdgeClaim                                     | :white_check_mark: |
| Test Case 70 | Controller rejects road: edge already claimed                       | IllegalEdgeClaim with message "Edge already claimed" propagates | :white_check_mark: |


### Method under test: `awardResources(int rollNum)`

Step 1:
- Input: Die roll
- Input: Robber location
- Input: State of the Hex city/settlement lists (Already performed BVA on Hex class)
- Output: State of players

Step 2:
- Input - Interval
- Input - Cases
- Output - Interval

Step 3:
- Input: 2, 12, -1, 13 (not feasible, verified by DiceHandler)
- Input: Robber on hex or not
- Output: 0, 1, 19 (max number of a single resource possible), 20 (not feasible)

|              | System under test                              | Expected output                                                                          | Implemented?       |
|--------------|------------------------------------------------|------------------------------------------------------------------------------------------|--------------------|
| Test Case 21 | 2 is rolled, robber not on hex                 | awardSettlementResources() and awardCityResources() are called once on Hex 1             | :white_check_mark: |
| Test Case 22 | 12 is rolled, robber not on hex                | awardSettlementResources() and awardCityResources() are called once on Hex 3             | :white_check_mark: |
| Test Case 23 | 8 is rolled, robber not on hex                 | awardSettlementResources() and awardCityResources() are each called twice on Hex 11 & 12 | :white_check_mark: |
| Test Case 24 | 2 is rolled, but robber is on hex              | awardSettlementResources() and awardCityResources() are never called                     | :white_check_mark: |
| Test Case 25 | 8 is rolled, but robber is on one of the hexes | awardSettlementResources() and awardCityResources() are called once on Hex 11            | :white_check_mark: |

### Method under test: `moveRobber(int hexId)`

Step 1:
- Input: hexId
- Input: Location of the robber
- Output: Location of the robber
- Output: Error

Step 2:
- Input - Interval
- Output - Interval
- Output - Exception

Step 3:
- Input: 0, 18, -1, 19
- Output: 0, 18, -1 (not feasible), 19 (not feasible)
- Output: "Cannot move Robber to invalid Hex ID", "Must move robber to new location"

|              | System under test                | Expected output                        | Implemented?       |
|--------------|----------------------------------|----------------------------------------|--------------------|
| Test Case 26 | Move robber from hex 0 to hex 18 | Robber is now on hex 18                | :white_check_mark: |
| Test Case 27 | Move robber from hex 18 to hex 0 | Robber is now on hex 0                 | :white_check_mark: |
| Test Case 28 | Move robber from hex 0 to hex -1 | "Cannot move Robber to invalid Hex ID" | :white_check_mark: |       
| Test Case 29 | Move robber from hex 0 to hex 19 | "Cannot move Robber to invalid Hex ID" | :white_check_mark: |
| Test Case 30 | Move robber from hex 9 to hex 9  | "Must move robber to new location"     | :white_check_mark: |


### Method under test: `getPlayersOnHex(int hexId)`

Step 1:
- Input: hexId
- Input: State of the Hex - Settlements
- Input: State of the Hex - Cities
- Output: List of Players
- Output: Error

Step 2:
- Input - Interval
- Input - Collection
- Input - Collection
- Output - Collection
- Output - Exception

Step 3:
- Input: 0, 18, -1, 19 - Will be validated by moveRobber
- Input: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Input: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Output: empty collection, contains just one element, contains more than one element, duplicate elements (not feasible), max possible size
- Output: "Invalid Hex ID, must be within [0,18]"

|              | System under test                   | Expected output                         | Implemented?       |
|--------------|-------------------------------------|-----------------------------------------|--------------------|
| Test Case 31 | BLUE settlement on Hex 0, no cities | BLUE                                    | :white_check_mark: |
| Test Case 32 | RED city on Hex 18, no settlements  | RED                                     | :white_check_mark: |
| Test Case 33 | WHITE, ORANGE settlements, RED city | WHITE, ORANGE, RED                      | :white_check_mark: |
| Test Case 34 | WHITE has two settlments, RED city  | WHITE, RED                              | :white_check_mark: |
| Test Case 35 | No settlements, BLUE has two cities | BLUE                                    | :white_check_mark: |
| Test Case 36 | ORANGE has three settlements        | ORANGE                                  | :white_check_mark: |
| Test Case 37 | RED has three cities                | RED                                     | :white_check_mark: |
| Test Case 38 | No settlements or cities            | Empty set                               | :white_check_mark: |
| Test Case 39 | Calls getPlayers with -1            | "Invalid Hex ID, must be within [0,18]" | :white_check_mark: |
| Test Case 40 | Calls getPlayers with 19            | "Invalid Hex ID, must be within [0,18]" | :white_check_mark: |


### Method under test: `buildSetupSettlement(Player player, int nodeId)`

Step 1:
- Input: Player
- Input: nodeId
- Input: State of the board
- Output: State of the board
- Output: State of the hexes
- Output: Error

Step 2:
- Input - Player class
- Input - Interval
- Input - Cases
- Output - Cases
- Output - Cases
- Output - Exception

Step 3:
- Input: RED, BLUE, ORANGE, WHITE
- Input: 0, 53, -1, 54
- Input: Node is adjacent to 1, 2, or 3 hexes
- Input: Node adjacent to an already-claimed node (adjacency constraint enforced by BoardGraphController)
- Output: Node now occupied, node not occupied, node still occupied by other player (was already occupied)
- Output: Hexes have player in list of settlements, hex player list not updated - For integration testing, not unit testable
- Output: "Invalid NodeID - must be within [0, 53]."
- Output: AdjacentNodeAlreadyClaimed (thrown by BoardGraphController when placing next to an owned node)

|              | System under test                                            | Expected output                                                                                                              | Implemented?       |
|--------------|--------------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 41 | RED tries to claim node 0                                    | Calls to add RED settlement to hex 0 and claimStoredNodeSetupPhase, node level is settlement, owned by RED                   | :white_check_mark: |
| Test Case 42 | BLUE tries to claim node 53                                  | Calls to add BLUE settlement to hex 18 and claimStoredNodeSetupPhase, node level is settlement, owned by BLUE                | :white_check_mark: |
| Test Case 43 | ORANGE tries to claim node -1                                | "Invalid NodeID - must be within [0, 53].", playerClaimStoredNodeSetupPhase and addPlayerSettlementToHex not called          | :white_check_mark: |
| Test Case 44 | WHITE tries to claim node 54                                 | "Invalid NodeID - must be within [0, 53].", playerClaimStoredNodeSetupPhase and addPlayerSettlementToHex not called          | :white_check_mark: |
| Test Case 45 | ORANGE tries to claim node 8                                 | Calls to add ORANGE settlement to hexes 0, 1, and 4 and claimStoredNodeSetupPhase, node level is settlement, owned by ORANGE | :white_check_mark: |
| Test Case 46 | BLUE tries to claim node 4                                   | Calls to add BLUE settlement to hexes 0 and 1 and claimStoredNodeSetupPhase, node level is settlement, owned by BLUE         | :white_check_mark: |
| Test Case 68 | RED claims node 7, then BLUE tries to claim adjacent node 12 | AdjacentNodeAlreadyClaimed                                                                                                   | :white_check_mark: |



### Method under test: `buildSetupRoad(Player player, int nodeID, int startingNodeID, int endingNodeID)`

Step 1:
- Input: nodeId
- Input: State of the board
- Input: Player
- Output: State of the board
- Output: Error

Step 2:
- Input - Interval
- Input - Cases
- Input - Player class
- Output - Cases
- Output - Exception

Step 3:
- Input: 0, 53, -1, 54
- Input: 0, 53, -1, 54
- Input: 0, 53, -1, 54 (out of bounds not feasible for previous node claimed)
- Input: Edge claimed, edge unclaimed - Handled by BoardGraphController
- Input: RED, BLUE, ORANGE, WHITE
- Output: Edge claimed, edge unclaimed - Handled by BoardGraphController

|              | System under test                             | Expected output                                      | Implemented?       |
|--------------|-----------------------------------------------|------------------------------------------------------|--------------------|
| Test Case 47 | RED claims edge [0,1] after claiming 0        | playerClaimSetupStoredEdge is called                 | :white_check_mark: |
| Test Case 48 | ORANGE claims edge [52, 53] after claiming 53 | playerClaimSetupStoredEdge is called                 | :white_check_mark: |
| Test Case 49 | WHITE tries to claim edge [-1, 0]             | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 50 | WHITE tries to claim edge [0, -1]             | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 51 | BLUE tries to claim edge [53, 54]             | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 52 | BLUE tries to claim edge [54, 53]             | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |

### Method under test: `calculateLongestRoad(List<Players> players, PlayerColor previousWinner)`

Step 1:
- Input: Active players
- Input: Previous winner
- Input: state of the board
- Output: Player

Step 2:
- Input - Collection - Not used here, passed down to BoardGraph
- Input - PlayerColor - Not used here, passed down to BoardGraph
- Input - Interval - cases (verified by LongestRoadCalculator)
- Output - Player

Step 3:
- Output: RED, ORANGE, WHITE, BLUE, SETUP

|              | System under test         | Expected output                                | Implemented?       |
|--------------|---------------------------|------------------------------------------------|--------------------|
| Test Case 53 | RED holds longest road    | calculateLongestRoad is called, returns RED    | :white_check_mark: |
| Test Case 54 | ORANGE holds longest road | calculateLongestRoad is called, returns ORANGE | :white_check_mark: |
| Test Case 55 | WHITE holds longest road  | calculateLongestRoad is called, returns WHITE  | :white_check_mark: |
| Test Case 56 | BLUE holds longest road   | calculateLongestRoad is called, returns BLUE   | :white_check_mark: |
| Test Case 57 | SETUP holds longest road  | calculateLongestRoad is called, returns SETUP  | :white_check_mark: |


### Method under test: `computeResourceDemand(int rollNum)`

Iterates all 19 hexes; a hex contributes when: `hex.rollNum == rollNum` AND `hex.id != robberLocation` AND `hex.resource != DESERT`. Settlements contribute 1 per player; cities contribute 2 per player. Players on multiple contributing hexes with the same resource have their amounts summed.

Step 1:

- Input: rollNum (the die value to match against hexes)
- Input: Robber location (blocks a matching hex entirely)
- Input: Settlement and city player lists on each hex
- Output: `Map<Resource, Map<Player, Integer>>` — demand per resource per player

Step 2:

- rollNum: Interval [2, 12]; matches or does not match each hex's rollNum
- Robber: Cases {on a matching hex (blocks it), not on any matching hex}
- Hex resource: Cases {DESERT (skipped), non-DESERT (included)}
- Settlement list per hex: Collection {empty, one player, multiple players}
- City list per hex: Collection {empty, one player}
- Same player on multiple matching hexes with same resource: amounts are summed

Step 3:

- rollNum: 2 (min valid), 12 (max valid); value that matches multiple hexes (e.g. 8 matches hex 11 ORE and hex 12 LUMBER)
- Robber: on the only matching hex; on one of two matching hexes; not on any matching hex
- Settlement: none on hex; 1 player; same player on 2 matching hexes same resource
- City: 1 player on hex (produces 2 instead of 1)
- No hexes match rollNum → empty result

|              | State of the System                                                                        | Expected output                                          | Implemented?       |
|--------------|--------------------------------------------------------------------------------------------|----------------------------------------------------------|--------------------|
| Test Case 58 | Roll 2, hex 1 (WOOL, rollNum=2) has RED settlement; robber elsewhere                       | `{WOOL: {RED: 1}}`                                       | :white_check_mark: |
| Test Case 59 | Roll 2, hex 1 (WOOL, rollNum=2) has RED settlement; robber on hex 1                        | `{}` (robber blocks)                                     | :white_check_mark: |
| Test Case 60 | Roll 8, hex 11 (ORE) has RED settlement; hex 12 (LUMBER) has BLUE settlement; no robber    | `{ORE: {RED: 1}, LUMBER: {BLUE: 1}}`                     | :white_check_mark: |
| Test Case 61 | Roll 2, hex 1 (WOOL) has RED city; robber elsewhere                                        | `{WOOL: {RED: 2}}` (city = 2x)                           | :white_check_mark: |
| Test Case 62 | Roll 8, hex 11 (ORE) has RED settlement; hex 12 (ORE) has RED settlement; no robber        | `{ORE: {RED: 2}}` (same player, same resource, summed)   | :white_check_mark: |
| Test Case 63 | Roll 6; all hexes configured with rollNum ≠ 6 (all hexes return 0)                         | `{}` (no hexes match)                                    | :white_check_mark: |
| Test Case 64 | Roll 8, hex 11 (ORE) RED settlement; hex 12 (LUMBER) BLUE settlement; robber on hex 12     | `{ORE: {RED: 1}}` (only unblocked hex contributes)       | :white_check_mark: |
### Method under test: `getAvailablePorts(Player player)`

Step 1:
- Input: Player
- Input: Ports
- Input: State of the board
- Output: List of available ports

Step 2:
- Input - Cases
- Input - Collection
- Input - Cases
- Output - Collection

Step 3:
- Input: RED, ORANGE, BLUE, WHITE 
- Input: Collection - Will always be list of 9 created ports
- Input: Player owns nodes next to number of ports
- Output: empty collection, contains just one element, contains more than one element, duplicate elements (not feasible), max possible size (7)

|              | System under test                                                            | Expected output                | Implemented?       |
|--------------|------------------------------------------------------------------------------|--------------------------------|--------------------|
| Test Case 58 | RED has settlement on node 23                                                | Returns empty list             | :white_check_mark: |
| Test Case 59 | ORANGE has settlement on node 0                                              | Returns the one port on node 0 | :white_check_mark: |
| Test Case 60 | WHITE has claimed 0, 5, 11, 15, 32, 38, and 46 (can maximally claim 7 nodes) | Returns 7 ports                | :white_check_mark: |

---

### Method under test: `getHexOrder()`

Returns a list of resource name strings for all 19 hexes, in order.

|              | System under test                         | Expected output                                         | Implemented?       |
|--------------|-------------------------------------------|---------------------------------------------------------|--------------------|
| Test Case 61 | board with 19 hexes, each returning LUMBER | list of 19 strings; first element is "LUMBER"          | :white_check_mark: |

---

### Method under test: `getHexCount()`

Returns the number of hexes on the board.

|              | System under test   | Expected output | Implemented?       |
|--------------|---------------------|-----------------|--------------------|
| Test Case 62 | board with 19 hexes | 19              | :white_check_mark: |

