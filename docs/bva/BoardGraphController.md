
### Method under test: `playerClaimStoredNodeSetupPhase(PlayerColor color, int NodeID)`

#### Inputs:
- NodeID -> Integer -> Interval [0, 53]
- State of map -> NodeID exists or not
- State of map -> Adjacent node is claimed or not
- State of Node -> claimed or not
- PlayerColor Color -> Cases -> [Red, Blue, Orange, White]

#### Outputs:
- Change of state of color class -> will need to Mock
- True -> on success
- Error 1 -> "Node does not exist"
- Error 2 -> "Node already claimed"
- Error 3 -> "Can not claim node adjacent to node already claimed"


|             | State of the System                                                      | Expected output                                              | Implemented?       |
|-------------|--------------------------------------------------------------------------|--------------------------------------------------------------|--------------------|
| Test Case 1 | Red Claims ID 0, node exists, is not claimed, adjacent nodes not claimed | True                                                         | :white_check_mark: |
| Test Case 2 | Blue Claims ID 0, node does not exist                                    | Error "Node does not exist"                                  | :white_check_mark: |
| Test Case 3 | Orange Claims ID 53, node exists, is claimed                             | Error "Node already claimed"                                 | :white_check_mark: |
| Test Case 4 | White Claims ID 0, but adjacent node claimed                             | Error "Can not claim node adjacent to node already claimed"  | :white_check_mark: |


### Method under test: `playerClaimStoredEdgeSetupPhase(PlayerColor color, int nodeID, int startingNodeID, int endingNodeID)`

#### Inputs:
- PlayerColor -> RED, BLUE, ORANGE, WHITE
- nodeID -> interval [0, 53], ID of node just claimed
- Edge -> neighbors nodeID or not; (as part of setup phase, player must place edge immediately next to just placed settlement)
- Edge -> claimed or not;

#### Outputs:
- Boolean: True
  - Change of state of system -> edge claimed on by player
- Error -> "Edge must be adjacent to just placed settlement"
- Error -> "Edge already claimed"


|             | State of the System                                              | Expected output                                         | Implemented?       |
|-------------|------------------------------------------------------------------|---------------------------------------------------------|--------------------|
| Test Case 1 | Red Claims edge0to3, just claimed Node 0, edge unclaimed         | True                                                    | :white_check_mark: |
| Test Case 2 | Blue Claims edge0to3, just claimed Node 2, edge unclaimed        | Error "Edge must be adjacent to just placed settlement" | :white_check_mark: |
| Test Case 3 | Orange Claims edge50to53, just claimed node 50, edge claimed     | Error "Edge already claimed"                            | :white_check_mark: |
| Test Case 4 | White Claims ID edge50to53, just claimed node 53, edge unclaimed | True                                                    | :white_check_mark: |


## Still a WIP From Refactoring:
