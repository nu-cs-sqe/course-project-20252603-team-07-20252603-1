
### Method under test: `addGraphNodeObject(int NodeID)`

#### Inputs:

- NodeID -> Integer -> Interval [0, 53] -> (checking ID is responsibility of separate GraphNode class)
- Internal Map of NodeIDs to Object -> Collection
  - Duplicates impossible, NodeIDs are unique

#### Outputs:

- Change of state of Internal Map
  - Update -> upon doing BVA for addGraphConnection(), needs to update BOTH maps
- Boolean to represent success
  - Upon failure -> insertion of a duplicate, error with message "Node already exists"

|             | State of the System                                        | Expected output                                  | Implemented?       |
|-------------|------------------------------------------------------------|--------------------------------------------------|--------------------|
| Test Case 1 | Empty collection, adding NodeID 0                          | Updated Map, True                                | :white_check_mark: |
| Test Case 2 | Collection with one element, adding NodeID 53              | Updated Map, True                                | :white_check_mark: |
| Test Case 3 | Collection with multiple elements, adding NodeID 53        | Updated Map, True                                | :white_check_mark: |
| Test Case 4 | Collection with multiple elements, adding duplicate node 0 | Map Stays the same, Error "Node already exists"  | :white_check_mark: |

### Method under test: `getGraphNodeByID(int NodeID)`

#### Inputs:
- State of map -> NodeID exists or not
- Collection, one element, multiple elements, empty collection

#### Outputs:
- GraphNode Obj if it exists
- If not, error "Node does not exist"


|             | State of the System                                 | Expected output | Implemented?       |
|-------------|-----------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Get ID 0, empty collection                          | Error           | :white_check_mark: |
| Test Case 2 | Get ID 0, one element, ID 0 exists                  | GraphNode Obj   | :white_check_mark: |
| Test Case 3 | Get ID 53, multiple  elements, ID 53 does not exist | Error           | :white_check_mark: |

### Method under test: `checkPlayerOwnsGraphNodeObject(PlayerColor color, int NodeID)`

#### Inputs:
- Player color -> RED, ORANGE, WHITE, BLUE
- State of map
  - NodeID exists or not
  - Player color owns it, or not

#### Outputs:
- Boolean
- Error -> "Node does not exist"


|             | State of the System                           | Expected output             | Implemented?       |
|-------------|-----------------------------------------------|-----------------------------|--------------------|
| Test Case 1 | Red checks ID 0, it exists, Red owns it       | True                        | :white_check_mark: |
| Test Case 2 | Orange checks ID 53, it exists, White Owns it | False                       | :white_check_mark: |
| Test Case 3 | Blue checks 53, it does not exist             | Error "Node does not exist" | :white_check_mark: |
| Test Case 4 | White checks 0, it exists, Blue owns it       | False                       | :white_check_mark: |

### Method under test: `claimGraphNodeObject(PlayerColor color, int NodeID)`

#### Inputs:
- Player color -> RED, ORANGE, WHITE, BLUE
- State of map 
  - NodeID exists or not
  - NodeIS claimed
- Collection, one element, multiple elements, empty collection

#### Outputs:
- Calls claimGraphNode() on Node Object -> use mocking to verify
- Error 1 -> "Node does not exist"
- Error 2 -> "Node already claimed"


|             | State of the System                                     | Expected output | Implemented?       |
|-------------|---------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Red claims ID 0, it exists, node updated                | True            | :white_check_mark: |
| Test Case 2 | Orange claims ID 0, multiple elements, ID 0 exists      | True            | :white_check_mark: |
| Test Case 3 | Blue claims 53, multiple elements, ID 53 does not exist | Error 1         | :white_check_mark: |
| Test Case 4 | White claims 53, it is already claimed                  | Error 2         | :white_check_mark: |

### Method under test: `claimGraphEdgeObject(PlayerColor color, int startingNodeID, int endingNodeID)`

#### Inputs:
- Player color -> Red, Blue, White, Orange
- States of Graph -> Edge exists, edge does not exist, edge is unclaimed/claimed
  - WhiteBox: edge will be in set -> collection -> empty, one item, multiple

#### Outputs:
- Bool -> success or not
- Edge Color is changed -> need to Mock to make sure call is made on Edge
- Error 1 -> "Edge does not exist"
- Error 2 -> "Edge already claimed"


|             | State of the System                                              | Expected output | Implemented?       |
|-------------|------------------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Red Claims edge0to1, edge unclaimed, single item set             | True            | :white_check_mark: |
| Test Case 2 | Blue Claims edge0to1, edge unclaimed, multiple item set          | True            | :white_check_mark: |
| Test Case 3 | Orange Claims edge52to53, edge does not exist, empty set         | Error 1         | :white_check_mark: |
| Test Case 4 | White Claims edge50to53, edge already claimed, multiple item set | Error 2         | :white_check_mark: |


### Method under test: `addGraphNodeConnection(int nodeID, GraphEdge connectingEdge)`

#### inputs:
- nodeID -> cases -> nodeID exists in map, or it does not (also covers cases for state of map)
- connectingEdge -> cases -> new edge, or duplicate edge
#### outputs
- boolean -> success on new edge
- error 1 -> edge already exists with that node "Node already has specified edge"
- error 2 -> node does not exist "Node does not exist"
- state of map -> new edge added


|             | State of the System                               | Expected output                 | Implemented?       |
|-------------|---------------------------------------------------|---------------------------------|--------------------|
| Test Case 1 | Node exists, not duplicate edge                   | True, map updates               | :white_check_mark: |
| Test Case 2 | Node exists, duplicate edge                       | Error, "Node already has edge"  | :white_check_mark: |
| Test Case 2 | Node exists, duplicate edges, into separate nodes | True, map updates               | :white_check_mark: |
| Test Case 4 | Node does not exist, not duplicate edge           | Error 2,  "Node does not exist" | :white_check_mark: |

### Method under test: `getConnectingEdgesByID(int NodeID)`

#### Inputs:
- State of map -> NodeID exists or not
  - Collection, one element, multiple elements, empty collection
- State of respective set (i.e the set of Edges for NodeID 0)
  - Collection -> empty, one element, multiple elements

#### Outputs:
- Set of Edges -> collection -> empty, one element, multiple elements
- If not, error "Node does not exist"


|             | State of the System                                              | Expected output      | Implemented?       |
|-------------|------------------------------------------------------------------|----------------------|--------------------|
| Test Case 1 | Get ID 0, no nodes exist                                         | Error                | :white_check_mark: |
| Test Case 2 | Get ID 0, Only one Node exists, ID 0 has empty set of edges      | Empty set            | :white_check_mark: |
| Test Case 3 | Get ID 53, multiple Nodes Exist, ID 53 has set of one edge       | One element set      | :white_check_mark: |
| Test Case 4 | Get ID 53, multiple Nodes Exist, ID 53 has set of multiple edges | Multiple Element set | :white_check_mark: |


### Method under test: `getCorrectEdgeFromSet(Set<GraphEdge> connectingEdges, int startingNodeID, int endingNodeID)`

#### Inputs:
- set of Graph Edges -> collection -> empty, one element, multiple elements
- startingNodeID + endingNodeID -> both are integers, but technically they collectively serve as an "ID" of the node
  - Cases -> Edge with "ID" exist, edge without "ID" exists

#### Outputs:
- GraphEdge Object (if it exists)
- Error "Edge does not exist"


|             | State of the System                       | Expected output | Implemented?       |
|-------------|-------------------------------------------|-----------------|--------------------|
| Test Case 1 | Set Empty                                 | Error           | :white_check_mark: |
| Test Case 2 | One element Set, edge exists              | Correct Object  | :white_check_mark: |
| Test Case 3 | Multiple element set, edge exists         | Correct Object  | :white_check_mark: |
| Test Case 4 | Multiple element set, edge does not exist | Error           | :white_check_mark: |

### Method under test: `checkNodeOccupied(int nodeID)`

Returns true if the node at nodeID is occupied by any player.

|             | State of the System                               | Expected output | Implemented?       |
|-------------|---------------------------------------------------|-----------------|--------------------|
| Test Case 1 | node exists; node is not occupied                 | false           | :white_check_mark: |
| Test Case 2 | node exists; node is occupied                     | true            | :white_check_mark: |

---

### Method under test: `dfs(int nodeId, PlayerColor color, Set<GraphEdge> visited)`

Recursive depth-first search that computes the longest connected road for `color`, skipping enemy-owned edges and stopping at enemy-occupied intermediate nodes.

|             | State of the System                                                                          | Expected output                               | Implemented?       |
|-------------|----------------------------------------------------------------------------------------------|-----------------------------------------------|--------------------|
| Test Case 1 | intermediate node has own (friendly) settlement                                              | road continues through own settlement         | :white_check_mark: |
| Test Case 2 | intermediate node has enemy settlement                                                       | road is blocked; search stops at that node    | :x: |
| Test Case 3 | node has an unvisited edge owned by an enemy color                                           | enemy edge not traversed                      | :x: |

---

### Method under test: `edgeCheckPlayerOwnsNeighboringEdge(PlayerColor color, int startingNodeID, int endingNodeID)`

#### Inputs:
- PlayerColor -> RED, WHITE, BLUE, ORANGE
- System State -> Cases
  - Player owns no adjacent edges
  - Player owns edge connecting to starting Node
  - Player owns edge connecting to ending node

#### Outputs:
- Boolean

|             | State of the System                                                     | Expected output | Implemented?       |
|-------------|-------------------------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Red, checking edge [0, 1], Red owns edge connecting to node 0           | True            | :white_check_mark: |
| Test Case 2 | White, checking edge [0, 1], White owns edge conencting to node 1       | True            | :white_check_mark: |
| Test Case 3 | Blue, checking edge [52, 53], does not own any connecting edges         | False           | :white_check_mark: |
| Test Case 4 | Orange, checking edge [52, 53], owns edges connecting to both 52 and 53 | True            | :white_check_mark: |


### Method under test: `edgeCheckPlayerOwnsNeighboringNode(PlayerColor color, int startingNodeID, int endingNodeID)`

#### Inputs:
- PlayerColor -> RED, WHITE, BLUE, ORANGE
- System State -> Cases
  - Player owns startingNode
  - Player owns endingNode
  - Player owns both
  - Player owns neither

#### Outputs:
- Boolean

|             | State of the System                                             | Expected output | Implemented?       |
|-------------|-----------------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Red, checking edge [0, 1], Red owns node 0                      | True            | :white_check_mark: |
| Test Case 2 | White, checking edge [0, 1], White owns node 1                  | True            | :white_check_mark: |
| Test Case 3 | Blue, checking edge [52, 53], does not own any connecting nodes | False           | :white_check_mark: |
| Test Case 4 | Orange, checking edge [52, 53], owns both nodes                 | True            | :white_check_mark: |

### Method under test: `nodeCheckPlayerOwnsNeighboringEdge(PlayerColor color, int nodeID)`
#### Inputs:
- PlayerColor -> RED, WHITE, BLUE, ORANGE
- System State/Graph Topology -> Cases
  - Player owns no adjacent edge
  - Player owns adjacent edge

#### Outputs:
- Boolean

|             | State of the System                                         | Expected output | Implemented?       |
|-------------|-------------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Red, checking node 0, Red owns edge connecting to node 0    | True            | :white_check_mark: |
| Test Case 2 | White, checking node 0, White owns edge conneting to node 0 | True            | :white_check_mark: |
| Test Case 3 | Blue, checking node 53, does not own any connecting edges   | False           | :white_check_mark: |
| Test Case 4 | Orange, checking node 53, does not own any connecting edges | False           | :white_check_mark: |

### Method under test: `checkIfAdjacentNodesNotClaimed(int nodeID)`

#### Inputs:
- state of system -> cases
  - No adjacent nodes are claimed
  - One adjacent node is claimed
    - With Whitebox analysis
      - One adjacent node is claimed, and is the "endingNode" in the edge connected with nodeID
      - One adjacent node is claimed, and is the "startingNode" in the edge connected with nodeID

#### Outputs:
- Boolean

|             | State of the System                                           | Expected output | Implemented?       |
|-------------|---------------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Node 0, no adjacent nodes are claimed                         | True            | :white_check_mark: |
| Test Case 2 | Node 0, adjacent node 3 is claimed ("endingNode" of edge)     | False           | :white_check_mark: |
| Test Case 3 | Node 53, adjacent node 50 is claimed ("startingNode" of edge) | False           | :white_check_mark: |
| Test Case 4 | Node 49, adjacemt nodes 45 and 53 are claimed                 | False           | :white_check_mark: |


### Method under test: `buildGameGraph()`

#### Inputs:
- None -> this function builds the graph for our specified rule Set

#### Outputs:
- Updated Maps -> store all nodes and graph edges

|             | State of the System | Expected output   | Implemented?       |
|-------------|---------------------|-------------------|--------------------|
| Test Case 1 | Empty Maps          | Setup Board Graph | :white_check_mark: |

### Method under test: `calculateLongestRoad(List<Players> players, PlayerColor previousWinner)`

#### Inputs:

- Players -> List<Player> -> Collection of players to check
- PreviousWinner -> PlayerColor -> the player who currently holds longest road 
  - PlayerColor.SETUP if nobody holds it yet
- Internal graph edge state -> each edge has an owning PlayerColor
- Road length -> Integer -> [0, 15] max possible roads in Catan

#### Outputs:

- PlayerColor of the player with the longest road
  - Returns PlayerColor.SETUP if no player has 5+ roads 
  - Returns the max owning player's color if they have 5+ roads 
  - In case of tie -> returns previous owner of longest road

|             | State of the System                                                                            | Expected output    | Implemented?       |
|-------------|------------------------------------------------------------------------------------------------|--------------------|--------------------|
| Test Case 1 | No player has any roads, no previous winner                                                    | PlayerColor.SETUP  | :white_check_mark: |
| Test Case 2 | RED has exactly 4 roads, no previous winner                                                    | PlayerColor.SETUP  | :white_check_mark: |
| Test Case 3 | ORANGE has exactly 5 roads, no previous winner                                                 | PlayerColor.ORANGE | :white_check_mark: |
| Test Case 4 | WHITE and BLUE both have exactly 5 roads, WHITE is previous winner                             | PlayerColor.WHITE  | :white_check_mark: |
| Test Case 5 | RED and BLUE both have exactly 5 roads, BLUE is previous winner                                | PlayerColor.BLUE   | :white_check_mark: |
| Test Case 6 | RED is previous winner with 5 roads, BLUE builds to 6                                          | PlayerColor.BLUE   | :white_check_mark: |
| Test Case 7 | BLUE has 6 roads but branching, RED was previous winner                                        | PlayerColor.RED    | :white_check_mark: |
| Test Case 8 | WHITE is the previous winner, 8 total roads, but 5 continous, ORANGE has 8 total, 6 continuous | PlayerColor.ORANGE | :white_check_mark: |
| Test Case 9 | BLUE has longest road at 5, gets blocked to have 4, no other players at road of length 5       | PlayerColor.SETUP  | :white_check_mark: |
