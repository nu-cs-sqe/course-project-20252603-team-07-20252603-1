### Method under test: `claimGraphEdge`

Inputs: Player Color (Cases), state of the edge (boolean, Cases)
Outputs: Boolean (success), state of the edge (boolean, Cases), or error (on not successful)

- **TC1: claimGraphEdge_NodeUnoccupied_ExpectTrue** (IMPLEMENTED)
    - **State of the system**: Player attempts to claim an unoccupied Edge
    - **Expected output**: True, edge now has input color, marks road has been built
- **TC2: claimGraphEdge_EdgeUnoccupied_ExpectError** (IMPLEMENTED)
    - **State of the system**: Player attempts to claim an occupied Edge
    - **Expected output**: Error "Edge already claimed", edge remains in previous state

### Method under test: `assertValidEdgeIDsOrdering(); in constructor`

#### Inputs:

- startingNodeID interval [0, 53]
- EndingNodeID interval [0, 53]

#### Outputs:

- True
- IllegalNodeOrderingInEdgeException -> "Starting nodeID must be lower than ending nodeID"

|             | State of the System | Expected output                    | Implemented?       |
|-------------|---------------------|------------------------------------|--------------------|
| Test Case 1 | 0, 3                | True, object created               | :white_check_mark: |
| Test Case 2 | 50, 53              | True, object created               | :white_check_mark: |
| Test Case 3 | 0, 0                | IllegalNodeOrderingInEdgeException | :white_check_mark: |
| Test Case 4 | 53, 52              | IllegalNodeOrderingInEdgeException | :white_check_mark: |
