### Method under test: `playerClaimStoredNodeSetupPhase(PlayerColor color, int NodeID)`

#### Inputs:

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

|             | State of the System                                                      | Expected output                                             | Implemented?       |
|-------------|--------------------------------------------------------------------------|-------------------------------------------------------------|--------------------|
| Test Case 1 | Red Claims ID 0, node exists, is not claimed, adjacent nodes not claimed | True                                                        | :white_check_mark: |
| Test Case 2 | Blue Claims ID 0, node does not exist                                    | Error "Node does not exist"                                 | :white_check_mark: |
| Test Case 3 | Orange Claims ID 53, node exists, is claimed                             | Error "Node already claimed"                                | :white_check_mark: |
| Test Case 4 | White Claims ID 0, but adjacent node claimed                             | Error "Can not claim node adjacent to node already claimed" | :white_check_mark: |

### Method under test:
`playerClaimStoredEdgeSetupPhase(PlayerColor color, int nodeID, int startingNodeID, int endingNodeID)`

#### Inputs:

- PlayerColor -> RED, BLUE, ORANGE, WHITE
- State of nodeID -> does player actually own it?
- nodeID -> ID of node just claimed
- Edge -> neighbors nodeID or not; (as part of setup phase, player must place edge immediately next to just placed
  settlement)
- Edge -> claimed or not;

#### Outputs:

- Boolean: True
    - Change of state of system -> edge claimed on by player
- Error -> "Edge must be adjacent to just placed settlement"
- Error -> "Edge already claimed"
- Error -> "During setup phase, player must own node next to edge they want to claim"

|             | State of the System                                              | Expected output                                                                  | Implemented?       |
|-------------|------------------------------------------------------------------|----------------------------------------------------------------------------------|--------------------|
| Test Case 1 | Red Claims edge0to3, just claimed Node 0, edge unclaimed         | True                                                                             | :white_check_mark: |
| Test Case 2 | Blue Claims edge0to3, just claimed Node 2, edge unclaimed        | Error "Edge must be adjacent to just placed settlement"                          | :white_check_mark: |
| Test Case 3 | Orange Claims edge50to53, just claimed node 50, edge claimed     | Error "Edge already claimed"                                                     | :white_check_mark: |
| Test Case 4 | White Claims ID edge50to53, just claimed node 53, edge unclaimed | True                                                                             | :white_check_mark: |
| Test Case 5 | White Claims edge adjacent to node 0, which they do not own      | Error "During setup phase, player must own node next to edge they want to claim" | :white_check_mark: |

### Method under test: `playerClaimStoredNode(PlayerColor color, int NodeID)`

#### Inputs:

- State of graph -> Adjacent node is claimed or not; player owns adjacent rode or not
- State of Node -> claimed or not
- PlayerColor Color -> Cases -> [Red, Blue, Orange, White]

#### Outputs:

- Change of state of color class -> will need to Mock
- True -> on success
- Error class: IllegalSettlementPlacementException
- Error 1 -> "Must own an adjacent road to claim node"
- Error 2 -> "Node already claimed"
- Error 3 -> "Can not claim node adjacent to node already claimed"

|             | State of the System                                                                       | Expected output                                             | Implemented?       |
|-------------|-------------------------------------------------------------------------------------------|-------------------------------------------------------------|--------------------|
| Test Case 1 | Red Claims ID 0, node is not claimed, adjacent nodes unclaimed, player owns adjacent road | True                                                        | :white_check_mark: |
| Test Case 2 | Blue Claims ID 53, node is not claimed, adjacent nodes claimed, player owns adjacent road | Error "Can not claim node adjacent to node already claimed" | :white_check_mark: |
| Test Case 3 | Orange Claims ID 53, node exists, is claimed                                              | Error "Node already claimed"                                | :white_check_mark: |
| Test Case 4 | White Claims ID 0, player does not own adjacent road                                      | Error "Must own an adjacent road to claim node"             | :white_check_mark: |

### Method under test: `calculateLongestRoad(List<Player> players, PlayerColor previousWinner)`

Thin delegation to `boardGraph.calculateLongestRoad(players, previousWinner)`.

|             | State of the System                                    | Expected output        | Implemented?       |
|-------------|--------------------------------------------------------|------------------------|--------------------|
| Test Case 1 | boardGraph.calculateLongestRoad([], SETUP) returns RED | controller returns RED | :white_check_mark: |

---

### Method under test: `playerClaimStoredEdge(PlayerColor color, int startingNodeID, int endingNodeID)`

#### Inputs:

- PlayerColor -> RED, BLUE, ORANGE, WHITE
- Edge -> Cases: claimed or not; player owns adjacent road or not (technically, the rules say an adjacent structure, but
  by the rules of building, after the setup phase, if they are building a road next to a settlement, then that
  settlement would at least have one road attached to it; so we can just check adjacent edges)

#### Outputs:

- Change of state of system -> edge claimed on by player
- Exception Type: IllegalEdgeClaim
- Error -> "Edge must be adjacent to an owned structure"
- Error -> "Edge already claimed"

|             | State of the System                                             | Expected output                                        | Implemented?       |
|-------------|-----------------------------------------------------------------|--------------------------------------------------------|--------------------|
| Test Case 1 | Red Claims edge0to3, edge unclaimed, owns adjacent road         | Success                                                | :white_check_mark: |
| Test Case 2 | Blue Claims edge0to3, edge claimed                              | Error "Edge already claimed"                           | :white_check_mark: |
| Test Case 3 | Orange Claims edge50to53,  edge unclaimed, owns adjacent        | Success                                                | :white_check_mark: |
| Test Case 4 | White Claims ID edge50to53, edge unclaimed, owns no adjacencies | Error -> "Edge must be adjacent to an owned structure" | :white_check_mark: |

