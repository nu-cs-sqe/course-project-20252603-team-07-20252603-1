
### Method under test: `claimGraphNode`
- **TC1: claimGraphNode_NodeUnoccupied_ExpectTrue** (Implemented)
  - **State of the system**: Player attempts to claim an unoccupied Node
  - **Expected output**: True, node now has input color

- **TC2: claimGraphNodeOccupied_ExpectError** (IMPLEMENTED)
  - **State of the system**: Player attempts to claim an occupied node
  - **Expected output**: Error "Node Already Claimed", node still has same state as before

### Method under test: `assertValidNodeID(); in constructor`

#### Inputs:
- NodeID -> Integer -> Interval [0, 53]
#### Outputs:
- True
- IllegalNodeIDException

|             | State of the System | Expected output      | Implemented?       |
|-------------|---------------------|----------------------|--------------------|
| Test Case 1 | 0                   | True, object created | :white_check_mark: |
| Test Case 2 | 53                  | True, object created | :white_check_mark: |
| Test Case 3 | -1                  | Error                | :x:                |
| Test Case 4 | 54                  | Error                | :x:                |