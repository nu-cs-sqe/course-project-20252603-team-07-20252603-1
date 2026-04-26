
### Method under test: `claimGraphEdge`

Inputs: Player Color, state of the edge
Outputs: Boolean (success or not), state of the edge
- 
- **TC1: claimGraphEdge_NodeUnoccupied_ExpectTrue** (NOT IMPLEMENTED)
  - **State of the system**: Player attempts to claim an unoccupied Edge
  - **Expected output**: True, edge now has input color, marks road has been built
- **TC1: claimGraphEdge_EdgeUnoccupied_ExpectError** (NOT IMPLEMENTED)
  - **State of the system**: Player attempts to claim an occupied Edge
  - **Expected output**: Error "Edge already claimed", edge remains in previous state


