
### Method under test: `addGraphNodeObject(int NodeID)`

#### Inputs:

- NodeID -> Integer -> Interval [0, 53] -> -1 and 54 don't make sense, there is a set amount of nodes
- Internal Map of NodeIDs to Object -> Collection
  - Duplicates impossible, NodeIDs are unique

#### Outputs:

- Change of state of Internal Map
  - Update -> upon doing BVA for addGraphConnection(), needs to update BOTH maps
- Boolean to represent success
  - Upon failure -> insertion of a duplicate, error with message "Node already exists"

|             | State of the System                                        | Expected output            | Implemented?       |
|-------------|------------------------------------------------------------|----------------------------|--------------------|
| Test Case 1 | Empty collection, adding NodeID 0                          | Updated Map, True          | :white_check_mark: |
| Test Case 2 | Collection with one element, adding NodeID 53              | Updated Map, True          | :white_check_mark: |
| Test Case 3 | Collection with multiple elements, adding NodeID 53        | Updated Map, True          | :white_check_mark: |
| Test Case 4 | Collection with multiple elements, adding duplicate node 0 | Map Stays the same, Error  | :white_check_mark: |

### Method under test: `getGraphNodeByID(int NodeID)`

#### Inputs:
- NodeID -> Integer -> Interval [0, 53]
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

### Method under test: `claimGraphNodeObject(PlayerColor color, int NodeID)`

#### Inputs:
- Player color -> RED, ORANGE, WHITE, BLUE
- NodeID -> Integer -> Interval [0, 53]
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
| Test Case 3 | Blue claims 53, multiple elements, ID 53 does not exist | Error 1         | :x:                |
| Test Case 4 | White claims 53, it is already claimed                  | Error 2         | :x:                |

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
- NodeID -> Integer -> Interval [0, 53]
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

### Method under test: `checkPlayerOwnsNeighboringEdge(PlayerColor color, int startingNodeID, int endingNodeID)`

#### Inputs:
- PlayerColor -> RED, WHITE, BLUE, ORANGE
- starting node, ending node -> interval [0, 52] for startingNode, [1, 53] for ending node
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


### Method under test: `checkPlayerOwnsNeighboringNode(PlayerColor color, int startingNodeID, int endingNodeID)`

#### Inputs:
- PlayerColor -> RED, WHITE, BLUE, ORANGE
- starting node, ending node -> interval [0, 52] for startingNode, [1, 53] for ending node
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

### Method under test: `checkIfAdjacentNodesNotClaimed(int nodeID)`

#### Inputs:
- nodeID -> interval [0, 53]
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