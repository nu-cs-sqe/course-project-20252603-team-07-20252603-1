
### Method under test: `claimGraphEdge`

Inputs: Player Color (Cases), state of the edge (boolean, Cases)
Outputs: Boolean (success), state of the edge (boolean, Cases), or error (on not successful)

- **TC1: claimGraphEdge_NodeUnoccupied_ExpectTrue** (IMPLEMENTED)
  - **State of the system**: Player attempts to claim an unoccupied Edge
  - **Expected output**: True, edge now has input color, marks road has been built
- **TC2: claimGraphEdge_EdgeUnoccupied_ExpectError** (IMPLEMENTED)
  - **State of the system**: Player attempts to claim an occupied Edge
  - **Expected output**: Error "Edge already claimed", edge remains in previous state


