
### Method under test: `addGraphNodeObject(int NodeID)`

#### Inputs:

- NodeID -> Integer -> Interval [0, 53] -> -1 and 54 don't make sense, there is a set amount of nodes
- Internal Map of NodeIDs to Object -> Collection
  - Duplicates impossible, NodeIDs are unique

#### Outputs:

- Change of state of Internal Map
- Boolean to represent success

|             | State of the System                                 | Expected output   | Implemented?       |
|-------------|-----------------------------------------------------|-------------------|--------------------|
| Test Case 1 | Empty collection, adding NodeID 0                   | Updated Map, True | :white_check_mark: |
| Test Case 2 | Collection with one element, adding NodeID 53       | Updated Map, True | :white_check_mark: |
| Test Case 3 | Collection with multiple elements, adding NodeID 53 | Updated Map, True | :white_check_mark: |

## Blank Template:

### Method under test: `abc()`

|              | State of the System | Expected output | Implemented?              |
|--------------|---------------------|-----------------|---------------------------|
| Test Case 1  |                     |                 | :x: or :white_check_mark: |
| Test Case 2  |    

