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
| Test Case 1 | RED tries to claim node 0     | Calls to add RED settlement to hex 0 and claimStoredNode, node level is settlement, owned by RED                   | :white_check_mark: |
| Test Case 2 | BLUE tries to claim node 53   | Calls to add BLUE settlement to hex 18 and claimStoredNode, node level is settlement, owned by BLUE                | :white_check_mark: |
| Test Case 3 | ORANGE tries to claim node -1 | "Invalid NodeID - must be within [0, 53].", playerClaimStoredNode and addPlayerSettlementToHex not called          | :white_check_mark: |
| Test Case 4 | WHITE tries to claim node 54  | "Invalid NodeID - must be within [0, 53].", playerClaimStoredNode and addPlayerSettlementToHex not called          | :white_check_mark: |
| Test Case 5 | ORANGE tries to claim node 8  | Calls to add ORANGE settlement to hexes 0, 1, and 4 and claimStoredNode, node level is settlement, owned by ORANGE | :white_check_mark: |
| Test Case 6 | BLUE tries to claim node 4    | Calls to add BLUE settlement to hexes 0 and 1 and claimStoredNode, node level is settlement, owned by BLUE         | :white_check_mark: |



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
- Input: RED, BLUE, ORANGE, WHITE
- Output: Edge claimed, edge unclaimed - Handled by BoardGraphController
- Output: "Edge nodeId out of bounds. Must be within [0, 53]."

|              | System under test                 | Expected output                                      | Implemented?       |
|--------------|-----------------------------------|------------------------------------------------------|--------------------|
| Test Case 15 | RED claims edge [0,1]             | playerClaimStoredEdge is called                      | :white_check_mark: |
| Test Case 16 | ORANGE claims edge [52, 53]       | playerClaimStoredEdge is called                      | :white_check_mark: |
| Test Case 17 | WHITE tries to claim edge [-1, 0] | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 18 | WHITE tries to claim edge [0, -1] | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 19 | BLUE tries to claim edge [53, 54] | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |
| Test Case 20 | BLUE tries to claim edge [54, 53] | "Edge nodeId out of bounds. Must be within [0, 53]." | :white_check_mark: |


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
- Input: 2, 12, -1, 13
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
- Output: Node now occupied, node not occupied, node still occupied by other player (was already occupied)
- Output: Hexes have player in list of settlements, hex player list not updated - For integration testing, not unit testable
- Output: "Invalid NodeID - must be within [0, 53]."

|              | System under test             | Expected output                                                                                                              | Implemented?       |
|--------------|-------------------------------|------------------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 41 | RED tries to claim node 0     | Calls to add RED settlement to hex 0 and claimStoredNodeSetupPhase, node level is settlement, owned by RED                   | :white_check_mark: |
| Test Case 42 | BLUE tries to claim node 53   | Calls to add BLUE settlement to hex 18 and claimStoredNodeSetupPhase, node level is settlement, owned by BLUE                | :white_check_mark: |
| Test Case 43 | ORANGE tries to claim node -1 | "Invalid NodeID - must be within [0, 53].", playerClaimStoredNodeSetupPhase and addPlayerSettlementToHex not called          | :white_check_mark: |
| Test Case 44 | WHITE tries to claim node 54  | "Invalid NodeID - must be within [0, 53].", playerClaimStoredNodeSetupPhase and addPlayerSettlementToHex not called          | :white_check_mark: |
| Test Case 45 | ORANGE tries to claim node 8  | Calls to add ORANGE settlement to hexes 0, 1, and 4 and claimStoredNodeSetupPhase, node level is settlement, owned by ORANGE | :white_check_mark: |
| Test Case 46 | BLUE tries to claim node 4    | Calls to add BLUE settlement to hexes 0 and 1 and claimStoredNodeSetupPhase, node level is settlement, owned by BLUE         | :white_check_mark: |



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
- Output: empty collection, contains just one element, contains more than one element, duplicate elements (not feasible), max possible size

|              | System under test                                                            | Expected output                | Implemented?       |
|--------------|------------------------------------------------------------------------------|--------------------------------|--------------------|
| Test Case 58 | RED has settlement on node 23                                                | Returns empty list             | :white_check_mark: |
| Test Case 59 | ORANGE has settlement on node 0                                              | Returns the one port on node 0 | x                  |
| Test Case 60 | WHITE has claimed 0, 5, 11, 15, 32, 38, and 46 (can maximally claim 7 nodes) | Returns 7 ports                | x                  |

