
### Method under test: `claimGraphNode`
- **TC1: claimGraphNode_NodeUnoccupied_ExpectTrue** (Implemented)
  - **State of the system**: Player attempts to claim an unoccupied Node
  - **Expected output**: True, node now has input color

- **TC2: claimGraphNodeOccupied_ExpectError** (NOT_IMPLEMENTED)
  - **State of the system**: Player attempts to claim an occupied node
  - **Expected output**: Error, node still has same state as before

