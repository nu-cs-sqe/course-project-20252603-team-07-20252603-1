
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

## Blank Template:

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

### Method under test: `addGraphNodeConnection(int nodeID, GraphEdge connectingEdge)`

#### inputs:
- nodeID -> cases -> nodeID exists in map, or it does not (also covers cases for state of map)
- connectingEdge -> cases -> new edge, or duplicate edge
#### outputs
- boolean -> success on new edge
- error 1 -> edge already exists with that node "Node already has edge"
- error 2 -> node does not exist "Node does not exist"
- state of map -> new edge added


|             | State of the System                     | Expected output                 | Implemented?       |
|-------------|-----------------------------------------|---------------------------------|--------------------|
| Test Case 1 | Node exists, not duplicate edge         | True, map updates               | :white_check_mark: |
| Test Case 2 | Node exists, duplicate edge             | Error, "Node already has edge"  | :x:                |
| Test Case 3 | Node does not exist, not duplicate edge | Error 2,  "Node does not exist" | :x:                |



## Blank Template:

### Method under test: `abc()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |                     |                 | :x: or :white_check_mark: |
| Test Case 2  |    

