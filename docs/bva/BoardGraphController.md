
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
- Error 1 -> "Node already claimed"
- Error 2 -> "Can not claim node adjacent to node already claimed"


|             | State of the System                                                      | Expected output                                              | Implemented?       |
|-------------|--------------------------------------------------------------------------|--------------------------------------------------------------|--------------------|
| Test Case 1 | Red Claims ID 0, node exists, is not claimed, adjacent nodes not claimed | True                                                         | :white_check_mark: |
| Test Case 2 | Blue Claims ID 0, node does not exist                                    | Error "Node does not exist"                                  | :white_check_mark: |
| Test Case 3 | Orange Claims ID 53, node exists, is claimed                             | Error "Node already claimed"                                 | :white_check_mark: |
| Test Case 4 | White Claims ID 0, but adjacent node claimed                             | Error "Can not claim node adjacent to node already claimed"  | :white_check_mark: |


## Still a WIP From Refactoring:
### Method under test: `playerClaimStoredEdge(PlayerColor color, int startingNodeID, int endingNodeID)`

#### Inputs:
- Node IDs -> interval [0, 53]
- Player color -> Red, Blue, White, Orange
- States of Graph -. Cases
  - Edge between node exists or not
  - Edge is claimed, or not
  - Edge is adjacent to player owned node, or not
    - I.e. player owns starting node, or ending node
  - Edge is adjacent to player owned edge, or not

#### Outputs:
- Bool -> success or not
- Edge Color is changed -> need to Mock to make sure call is made on Edge
- Error 1 -> "Edge does not exist"
- Error 2 -> "Edge already claimed"
- Error 3 -> "To claim an edge, player must own an adjacent node or edge"


|             | State of the System                                     | Expected output | Implemented?       |
|-------------|---------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Red Claims edge0to1, owns node 0, edge unclaimed        | True            | :white_check_mark: |
| Test Case 2 | Blue Claims edge0to1, owns node 1, edge unclaimed       | True            | :white_check_mark: |
| Test Case 3 | Orange Claims edge52to53, edge does not exist           | Error 1         | :white_check_mark: |
| Test Case 4 | White Claims edge52to53, edge already claimed           | Error 2         | :white_check_mark: |
| Test Case 5 | Red Claims edge52to53, owns no adjacencies              | Error 3         | :white_check_mark: |
| Test Case 6 | Red Claims edge0to1, owns adjacent edge, edge unclaimed | True            | :white_check_mark: |
