### BVA for BoardHandler Class

'BoardHandler' is responsible for updating hexes and the board graph when players place a settlement, city, or a road. 
It will also calculate the longest road and handle the robber.


DON'T NEED TO VALIDATE PLAYER RESOURCES

### Method under test: `buildSettlement(Player player, int nodeId)`

Step 1:
- Input: Player
- Input: nodeId
- Input: State of the board
- Output: State of the board
- Output: State of the hexes
- Output: State of player
- Output: Error

Step 2:
- Input - Player class
- Input - Interval
- Input - Cases
- Output - Cases
- Output - Cases
- Output - Collection
- Output - Exception

Step 3:
- Input: RED, BLUE, ORANGE, WHITE
- Input: 0, 53, -1, 54
- Input: Node already occupied, node free (owned by setup)
- Output: Node now occupied, node not occupied, node still occupied by other player (was already occupied)
- Output: Hexes have player in list of settlements, hex player list not updated
- Output: Player settlements list updated or not (Other validation completed by Player BVA testing)
- Output: "Out of bounds nodeId", "Node already occupied"

|             | System under test                                              | Expected output                                                                           | Implemented? |
|-------------|----------------------------------------------------------------|-------------------------------------------------------------------------------------------|--------------|
| Test Case 1 | RED tries to claim node 0, which is free                       | RED now owns node 0, RED now in Hex city lists, RED now has 0 in list of settlements      | x            |
| Test Case 2 | BLUE tries to claim node 53, which is free                     | BLUE now owns node 53, BLUE now in Hex city lists, BLUE now has 53 in list of settlements | x            |
| Test Case 3 | ORANGE tries to claim node -1                                  | "Out of bounds nodeId", still unoccupied                                                  | x            |
| Test Case 4 | WHITE tries to claim node 54                                   | "Out of bounds nodeId", still unoccupied                                                  | x            |
| Test Case 5 | ORANGE tries to claim node 10, which is already owned by WHITE | "Node already occupied", still occupied by WHITE, Hex city lists not updated              | x            |


### Method under test: `buildCity(Player player, int nodeId)`

Step 1:
- Input: Player
- Input: nodeId
- Input: State of the board
- Output: State of the board
- Output: State of the hex
- Output: State of player
- Output: Error

Step 2:
- Input - Player class
- Input - Interval
- Input - Cases
- Output - Cases
- Output - Cases
- Output - Collection
- Output - Exception

Step 3:
- Input: RED, BLUE, ORANGE, WHITE
- Input: 0, 53, -1, 54
- Input: Node already occupied by someone else, node occupied by same color, node free (owned by setup)
- Output: Node occupied, node not occupied, node still occupied by other player (was already occupied by other player)
- Output: Hex has player in list of cities, hex player list not updated
- Output: Player cities list updated or not (Other validation completed by Player BVA testing)
- Output: "Out of bounds nodeId", "Node already occupied by other player", "Must build a settlement before building a city"

|              | System under test                                                     | Expected output                                                                           | Implemented? |
|--------------|-----------------------------------------------------------------------|-------------------------------------------------------------------------------------------|--------------|
| Test Case 6  | RED tries to build a city on node 0, which they had a settlement on   | RED has a city on node 0, RED now in Hex city lists, RED now has 0 in list of cities      | x            |
| Test Case 7  | BLUE tries to build a city on node 53, which they had a settlement on | BLUE has a city on node 53, BLUE now in Hex city lists, BLUE now has 53 in list of cities | x            |
| Test Case 8  | ORANGE tries to build a city on node -1                               | "Out of bounds nodeId"                                                                    | x            |
| Test Case 9  | WHITE tries to build a city on node 54                                | "Out of bounds nodeId"                                                                    | x            |
| Test Case 10 | RED tries to build a city on node 6, which BLUE owns                  | "Node already occupied by other player", still occupied by BLUE, Hex city lists unchanged | x            |
| Test Case 11 | BLUE tries to build a city on node 36, which is unoccupied            | "Must build a settlement before building a city", still unoccupied                        | x            |


### Method under test: `addRoad(Player player, int nodeId, int nodeId)`

Step 1:
- Input: nodeId
- Input: State of the board
- Input: Player
- Output: State of the board
- Output: State of the player
- Output: Error

Step 2:
- Input - Interval
- Input - Cases
- Input - Player class
- Output - Cases
- Output - Collection
- Output - Exception

Step 3:
- Input: 0, 53, -1, 54
- Input: 0, 53, -1, 54
- Input: Edge claimed, edge unclaimed
- Input: RED, BLUE, ORANGE, WHITE
- Output: Edge claimed, edge unclaimed
- Output: Player roads list updated or not (Other validation completed by Player BVA testing)
- Output: "Road already claimed", "Road not adjacent to player roads/buildings", "Not a valid edge"

|              | System under test                                                         | Expected output                                                                  | Implemented? |
|--------------|---------------------------------------------------------------------------|----------------------------------------------------------------------------------|--------------|
| Test Case 12 | RED claims edge [0,1]                                                     | RED owns graph edge [0,1], RED now has graph edge [0,1] in road list             | x            |
| Test Case 13 | ORANGE claims edge [52, 53]                                               | ORANGE owns graph edge [52, 53], ORANGE now has graph edge [52, 53] in road list | x            |
| Test Case 14 | BLUE tries to claim edge [10, 11], already owned by WHITE                 | "Road already claimed"                                                           | x            |
| Test Case 15 | WHITE tries to claim edge [10, 11], not adjacent to other roads/buildings | "Road not adjacent to player roads/buildings", edge still unclaimed              | x            |
| Test Case 16 | RED tries to claim edge [-1, 0]                                           | "Not a valid edge"                                                               | x            |
| Test Case 17 | ORANGE tries to claim edge [53, 54]                                       | "Not a valid edge"                                                               | x            |
| Test Case 18 | BLUE tries to claim edge [53, 54]                                         | "Not a valid edge"                                                               | x            |
| Test Case 19 | WHITE tries to claim edge [0, 16]                                         | "Not a valid edge"                                                               | x            |


### Method under test: `validateSettlementResources(Player player)`

Step 1:
- Input: Player
- Input: Player brick count
- Input: Player lumber count
- Input: Player grain count
- Input: Player wool count
- Output: Success/failure

Step 2:
- Input - Player class
- Input - Resources each Interval
- Output - Boolean

Step 3:
- Input: RED, BLUE, ORANGE, WHITE
- Input: 0, 1, 2, -1 (not feasible) for each resource
- Output: specific values

|              | System under test                             | Expected output | Implemented?       |
|--------------|-----------------------------------------------|-----------------|--------------------|
| Test Case 20 | RED has 1 brick, 1 lumber, 1 grain, 1 wool    | True            | x                  |
| Test Case 21 | BLUE has 0 brick, 1 lumber, 1 grain, 1 wool   | False           | x                  |
| Test Case 22 | ORANGE has 1 brick, 0 lumber, 1 grain, 1 wool | False           | x                  |
| Test Case 23 | WHITE has 0 brick, 1 lumber, 0 grain, 1 wool  | False           | x                  |
| Test Case 24 | RED has 0 brick, 1 lumber, 1 grain, 0 wool    | False           | x                  |
| Test Case 25 | WHITE has 2 brick, 2 lumber, 2 grain, 2 wool  | True            | x                  |


### Method under test: `validateCityResources(Player player)`

Step 1:
- Input: Player
- Input: Player rock count
- Input: Player grain count
- Output: Success/failure

Step 2:
- Input - Player class
- Input - Resources each Interval
- Output - Boolean

Step 3:
- Input: RED, BLUE, ORANGE, WHITE
- Input: 0, 3, 4, -1 (not feasible) for ore
- Input: 0, 2, 3, -1 (not feasible) for grain
- Output: specific values

|              | System under test         | Expected output | Implemented? |
|--------------|---------------------------|-----------------|--------------|
| Test Case 26 | BLUE has 3 ore, 2 grain   | True            | x            |
| Test Case 27 | ORANGE has 3 ore, 1 grain | False           | x            |
| Test Case 28 | RED has 2 ore, 2 grain    | False           | x            |
| Test Case 29 | WHITE has 0 ore, 0 grain  | False           | x            |
| Test Case 30 | ORANGE has 4 ore, 3 grain | True            | x            |


### Method under test: `validateRoadResources(Player player)`

Step 1:
- Input: Player
- Input: Player brick count
- Input: Player lumber count
- Output: Success/failure

Step 2:
- Input - Player class
- Input - Resources each Interval
- Output - Boolean

Step 3:
- Input: RED, BLUE, ORANGE, WHITE
- Input: 0, 1, 2, -1 (not feasible) for each resource
- Output: specific values

|              | System under test            | Expected output | Implemented?       |
|--------------|------------------------------|-----------------|--------------------|
| Test Case 31 | WHITE has 1 brick, 1 lumber  | True            | x                  |
| Test Case 32 | ORANGE has 0 brick, 1 lumber | False           | x                  |
| Test Case 33 | WHITE has 1 brick, 0 lumber  | False           | x                  |
| Test Case 34 | WHITE has 2 brick, 2 lumber  | True            | x                  |


### Method under test: `spendSettlementResources(Player player)`
Note: Assuming that spendSettlementResources is always called after validation

Step 1:
- Input: Player
- Output: State of player

Step 2:
- Input - Player class
- Output - Resource count 

Step 3:
- Input: specific values
- Output: specific values

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 1 |                   |                 | x                  |


### Method under test: `spendCityResources(Player player)`

Step 1:
- Input:
- Output:

Step 2:
- Input - Type
- Output - Type

Step 3:
- Input: specific values
- Output: specific values

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 1 |                   |                 | x                  |


### Method under test: `spendRoadResources(Player player)`

Step 1:
- Input:
- Output:

Step 2:
- Input - Type
- Output - Type

Step 3:
- Input: specific values
- Output: specific values

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 1 |                   |                 | x                  |


### Method under test: `awardResources(int rollNum)`

Step 1:
- Input:
- Output:

Step 2:
- Input - Type
- Output - Type

Step 3:
- Input: specific values
- Output: specific values

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 1 |                   |                 | x                  |


### Method under test: `moveRobber(int hexId)`

Step 1:
- Input:
- Output:

Step 2:
- Input - Type
- Output - Type

Step 3:
- Input: specific values
- Output: specific values

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 1 |                   |                 | x                  |


### Method under test: `getPlayersOnHex(int hexId)`

Step 1:
- Input:
- Output:

Step 2:
- Input - Type
- Output - Type

Step 3:
- Input: specific values
- Output: specific values

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 1 |                   |                 | x                  |


### Method under test: `buildSetupSettlement(Player player, int nodeId)`

Step 1:
- Input:
- Output:

Step 2:
- Input - Type
- Output - Type

Step 3:
- Input: specific values
- Output: specific values

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 1 |                   |                 | x                  |


### Method under test: `buildSetupRoad(int edgeId)`

Step 1:
- Input:
- Output:

Step 2:
- Input - Type
- Output - Type

Step 3:
- Input: specific values
- Output: specific values

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 1 |                   |                 | x                  |
