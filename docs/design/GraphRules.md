Rules For the Graph

- Graph Nodes represent vertices on the hexes on which players can build settlements and cities.
- Graph Edges represent connections between nodes on which player can build roads

Identification
- Nodes are identified by integer nodeIDs from [0, 53]
- Edges are identified by two integer nodeIDs from [0, 53]
  - Within NodeIDs MUST be increasing order (i.e. 0 to 1 is allowed; 1 to 0 is not)
  - NodeIDs MUST not be the same

## BoardGraph Methods to be called:

### buildBoard()
### playerClaimStoredNode(PlayerColor color, int nodeID)
### playerClaimStoredEdge(PlayerColor color, int startingNodeID, int endingNodeID)
### 