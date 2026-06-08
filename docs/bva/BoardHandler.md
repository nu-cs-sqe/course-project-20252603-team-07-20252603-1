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

|              | System under test                                                         | Expected output                 | Implemented?       |
|--------------|---------------------------------------------------------------------------|---------------------------------|--------------------|
| Test Case 15 | RED claims edge [0,1]                                                     | playerClaimStoredEdge is called | :white_check_mark: |
| Test Case 16 | ORANGE claims edge [52, 53]                                               | playerClaimStoredEdge is called | x                  |
| Test Case 17 | RED tries to claim edge [-1, 0]                                           | "Not a valid edge"              | x                  |
| Test Case 18 | BLUE tries to claim edge [53, 54]                                         | "Not a valid edge"              | x                  |


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

|              | System under test                 | Expected output                                                           | Implemented? |
|--------------|-----------------------------------|---------------------------------------------------------------------------|--------------|
| Test Case 20 | 2 is rolled, robber not on hex    | awardSettlementResources() and awardCityResources() are called once       | x            |
| Test Case 21 | 12 is rolled, robber not on hex   | awardSettlementResources() and awardCityResources() are called once       | x            |
| Test Case 22 | 8 is rolled, robber not on hex    | awardSettlementResources() and awardCityResources() are each called twice | x            |
| Test Case 23 | 8 is rolled, but robber IS on hex | awardSettlementResources() and awardCityResources() are never called      | x            |


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
- Output: "Cannot move Robber to invalid Hex ID"

|              | System under test                 | Expected output                        | Implemented? |
|--------------|-----------------------------------|----------------------------------------|--------------|
| Test Case 24 | Move robber from hex 0 to hex 18  | Robber is now on hex 18                | x            |
| Test Case 25 | Move robber from hex 18 to hex 0  | Robber is now on hex 0                 | x            |
| Test Case 26 | Move robber from hex 0 to hex -1  | "Cannot move Robber to invalid Hex ID" | x            |       
| Test Case 27 | Move robber from hex 0 to hex 19  | "Cannot move Robber to invalid Hex ID" | x            |


### Method under test: `getPlayersOnHex(int hexId)`

Step 1:
- Input: State of the Hex - Settlements
- Input: State of the Hex - Cities
- Output: List of Players

Step 2:
- Input - Collection
- Input - Collection
- Output - Collection

Step 3:
- Input: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Input: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Output: empty collection (not feasible), contains just one element, contains more than one element, duplicate elements (not feasible), max possible size

|              | System under test                   | Expected output    | Implemented? |
|--------------|-------------------------------------|--------------------|--------------|
| Test Case 28 | BLUE settlement on Hex, no cities   | BLUE               | x            |
| Test Case 29 | RED city on Hex, no settlements     | RED                | x            |
| Test Case 30 | WHITE, ORANGE settlements, RED city | WHITE, ORANGE, RED | x            |
| Test Case 31 | WHITE has two settlments, RED city  | WHITE, RED         | x            |
| Test Case 32 | No settlements, BLUE has two cities | BLUE               | x            |
| Test Case 33 | ORANGE has three settlements        | ORANGE             | x            |
| Test Case 34 | RED has three cities                | RED                | x            |
| Test Case 35 | No settlements or cities            | SETUP player       | x            |


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
- Input: Node already occupied, node free (owned by setup)
- Output: Node now occupied, node not occupied, node still occupied by other player (was already occupied)
- Output: Hexes have player in list of settlements, hex player list not updated
- Output: "Out of bounds nodeId", "Node already occupied"

|              | System under test                                              | Expected output                                                              | Implemented? |
|--------------|----------------------------------------------------------------|------------------------------------------------------------------------------|--------------|
| Test Case 36 | RED tries to claim node 0, which is free                       | RED now owns node 0, RED now in Hex city lists                               | x            |
| Test Case 37 | BLUE tries to claim node 53, which is free                     | BLUE now owns node 53, BLUE now in Hex city lists                            | x            |
| Test Case 38 | ORANGE tries to claim node -1                                  | "Out of bounds nodeId", still unoccupied                                     | x            |
| Test Case 39 | WHITE tries to claim node 54                                   | "Out of bounds nodeId", still unoccupied                                     | x            |
| Test Case 40 | ORANGE tries to claim node 10, which is already owned by WHITE | "Node already occupied", still occupied by WHITE, Hex city lists not updated | x            |



### Method under test: `buildSetupRoad(int edgeId)`

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
- Input: Edge claimed, edge unclaimed
- Input: RED, BLUE, ORANGE, WHITE
- Output: Edge claimed, edge unclaimed
- Output: "Road already claimed", "Road not adjacent to player roads/buildings", "Not a valid edge"

|              | System under test                                                         | Expected output                                                     | Implemented? |
|--------------|---------------------------------------------------------------------------|---------------------------------------------------------------------|--------------|
| Test Case 41 | RED claims edge [0,1]                                                     | RED owns graph edge [0,1]                                           | x            |
| Test Case 42 | ORANGE claims edge [52, 53]                                               | ORANGE owns graph edge [52, 53]                                     | x            |
| Test Case 43 | BLUE tries to claim edge [10, 11], already owned by WHITE                 | "Road already claimed"                                              | x            |
| Test Case 44 | WHITE tries to claim edge [10, 11], not adjacent to other roads/buildings | "Road not adjacent to player roads/buildings", edge still unclaimed | x            |
| Test Case 45 | RED tries to claim edge [-1, 0]                                           | "Not a valid edge"                                                  | x            |
| Test Case 46 | ORANGE tries to claim edge [53, 54]                                       | "Not a valid edge"                                                  | x            |
| Test Case 47 | BLUE tries to claim edge [53, 54]                                         | "Not a valid edge"                                                  | x            |
| Test Case 48 | WHITE tries to claim edge [0, 16]                                         | "Not a valid edge"                                                  | x            |
