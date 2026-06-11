### Method under test: `playerCanUsePort(BoardHandler board, Player player)`

Step 1:
- Input: State of the board
- Input: Player
- Output: True/False

Step 2:
- State - Cases (claimed or not)
- Player - Cases
- True/False - Boolean

Step 3:
- Input: Player doesn't own any of the ports, player owns port 1, player owns port 2, player owns both ports (not feasible)
- Input: RED, WHITE, ORANGE, BLUE (all the same functionality)
- Output: True, False

|             | System under test                     | Expected output | Implemented?       |
|-------------|---------------------------------------|-----------------|--------------------|
| Test Case 1 | Player owns neither of the port nodes | false           | :white_check_mark: | 
| Test Case 2 | Player owns port node 1               | true            | :white_check_mark: |
| Test Case 3 | Player owns port node 2               | true            | :white_check_mark: |


### Method under test: `executePortTrade(Player player, BoardHandler board, Resource givingResource, Resource receivingResource)`

Step 1:
- Input: Player
- Input: BoardHandler
- Input: givingResource
- Input: receivingResource
- Input: Trade ratio, either 2 or 3
- Input: Port resource
- Input: Player resources
- Input: ResourceDeck amount
- Output: Player resources updated
- Output: Error

Step 2:
- Player - Player class
- BoardHandler - Cases
- givingResource - Cases
- receivingResource - Cases
- Trade ratio - Cases
- Port resource - Cases
- Player resources - Interval
- Resource Amount - ResourceDeck
- Player resources - Interval
- Error - Exception

Step 3:
- Input: RED, WHITE, ORANGE, BLUE (all the same, will just use RED for simplicity)
- Input: playerCanUsePort - true or false
- Input: BRICK, LUMBER, GRAIN, ORE, WOOL
- Input: BRICK, LUMBER, GRAIN, ORE, WOOL
- Input: BRICK, LUMBER, GRAIN, ORE, WOOL, ANY
- Input: tradeRatio - 2 (specific port), 3 (ANY port)
- Input: ResourceDeck (validation handled by ResourceDeck class)
- Input: player resource count - 1, 2, 3, 19
- Output: player resources updated correctly
- Output: bank resources updated correctly
- Output: Exceptions


|              | System under test                                                                 | Expected output                                                                                                                                                                                 | Implemented?       |
|--------------|-----------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------|
| Test Case 4  | RED at ANY port, gives 3 WOOL, receives 1 ORE, bank has 19 ORE, player has 3 WOOL | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(WOOL) returns 3, player.updateResources(WOOL, -3), bank.draw() returns ORE, player.updateResources(ORE, 1)         | :white_check_mark: |
| Test Case 5  | RED at ANY port, gives 3 LUMBER, receives 1 GRAIN, player has 3 LUMBER            | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(LUMBER) returns 3, player.updateResources(LUMBER, -3), bank.draw() returns GRAIN, player.updateResources(GRAIN, 1) | :white_check_mark: |
| Test Case 6  | RED at WOOL port, gives 2 WOOL, receives 1 ORE, player has 2 WOOL                 | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(WOOL) returns 2, player.updateResources(WOOL, -2), bank.draw() returns ORE, player.updateResources(ORE, 1)         | :white_check_mark: |
| Test Case 7  | RED at ORE port, gives 2 ORE, receives 1 BRICK, player has 2 ORE                  | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(ORE) returns 2, player.updateResources(ORE, -2), bank.draw() returns BRICK, player.updateResources(BRICK, 1)       | :white_check_mark: |
| Test Case 8  | RED at LUMBER port, gives 2 LUMBER, receives 1 GRAIN, player has 2 LUMBER         | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(LUMBER) returns 2, player.updateResources(LUMBER, -2), bank.draw() returns GRAIN, player.updateResources(GRAIN, 1) | :white_check_mark: |
| Test Case 9  | RED at GRAIN port, gives 2 GRAIN, receives 1 WOOL, player has 2 GRAIN             | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(GRAIN) returns 2, player.updateResources(GRAIN, -2), bank.draw() returns WOOL, player.updateResources(WOOL, 1)     | x                  |
| Test Case 10 | RED at BRICK port, gives 2 BRICK, receives 1 LUMBER, player has 2 BRICK           | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(BRICK) returns 2, player.updateResources(BRICK, -2), bank.draw() returns LUMBER, player.updateResources(LUMBER, 1) | x                  |
| Test Case 11 | RED at WOOL port, tries to give 2 ORE                                             | board.checkPlayerOwnsNode(RED, nodeId) returns true, no further calls, exception thrown                                                                                                         | x                  |
| Test Case 12 | RED tries to trade WOOL for WOOL at ANY port                                      | board.checkPlayerOwnsNode(RED, nodeId) returns true, no further calls, exception thrown                                                                                                         | x                  |
| Test Case 13 | RED at WOOL port, has 1 WOOL, tries to give 2                                     | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(WOOL) returns 1, no further calls, exception thrown                                                                | x                  |
| Test Case 14 | RED does not own adjacent node                                                    | board.checkPlayerOwnsNode(RED, nodeId) returns false for all nodes, no further calls, exception thrown                                                                                          | x                  |
| Test Case 15 | RED at ANY port, gives 3 WOOL, bank has 0 ORE                                     | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(WOOL) returns 3, player.updateResources(WOOL, -3), bank.draw() throws EmptyDeckException                           | x                  |
| Test Case 16 | RED at ANY port, gives 3 WOOL, bank has 1 ORE                                     | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(WOOL) returns 3, player.updateResources(WOOL, -3), bank.draw() returns ORE, player.updateResources(ORE, 1)         | x                  |
| Test Case 17 | RED at ANY port, gives 3 WOOL, player has 19 WOOL, gets 1 ORE                     | board.checkPlayerOwnsNode(RED, nodeId) returns true, player.getResourceCount(WOOL) returns 19, player.updateResources(WOOL, -3), bank.draw() returns ORE, player.updateResources(ORE, 1)        | x                  |
| Test Case 18 | RED at WOOL port, gives 2 WOOL, receives 1 WOOL                                   | board.checkPlayerOwnsNode(RED, nodeId) returns true, no further calls, exception thrown                                                                                                         | x                  |