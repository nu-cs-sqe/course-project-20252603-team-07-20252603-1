
### Method under test: "setGraphNodeColor"
- **TC1: setColor_ExpectTrue** (NOT_IMPLEMENTED)
  - **State of the system**: Node exists
  - **Expected output**: True, node now has input color


### Method under test: `claimGraphNode`
- **TC1: claimGraphNode_NodeUnoccupied_ExpectTrue** (NOT_IMPLEMENTED)
  - **State of the system**: Player attempts to claim an unoccupied Node
  - **Expected output**: True, node now has input color

- **TC2: claimGraphNodeOccupied_ExpectError** (NOT_IMPLEMENTED)
  - **State of the system**: Player attempts to claim an occupied node
  - **Expected output**: Error, node still has same state as before

