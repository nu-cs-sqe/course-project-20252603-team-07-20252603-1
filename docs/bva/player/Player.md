# Player BVA

`Player` holds a player's name, victory points, settlements, roads, and resources.
Its three public methods are `placeSettlement(vertex)`, `placeRoad(edge)`, and
`receiveResources(resources)`. Per standard Catan rules each player has 5 settlement
pieces and 15 road pieces. `DESERT` is not a receivable resource.

---

### Method under test: `placeSettlement(vertex)`

Step 1:

- Input: vertex
- State: settlement count, vertex occupancy, distance rule compliance (whether the attempt follows the distance rule)
- Output: settlement appended to player's settlements list
- Output: exception

Step 2:

- vertex: Pointer
- Non-null vertex: Case
- settlement count: Interval [0, 5], Appending a Single Element
- Output (settlement appended): Boolean
- Output (exception thrown): Boolean

Step 3:

- Input vertex (Pointer / Case): null; valid (unoccupied, distance rule OK); occupied; distance rule violated
- Input settlement count (Interval [0, 5]): −1 (CAN'T SET); 0 (LOW); 4 (last element that fits); 5 (HIGH — CAN'T ADD); 6 (CAN'T SET)
- Output: settlement appended to player's settlements list
- Output: "Vertex cannot be null." / "Vertex is already occupied." / "Settlement violates the distance rule." / "No settlements remaining."


|             | System under test                                                                   | Expected output                                                    | Implemented? |
| ----------- | ----------------------------------------------------------------------------------- | ------------------------------------------------------------------ | ------------ |
| Test Case 1 | vertex = null, settlements = 0                                                      | IllegalArgumentException: "Vertex cannot be null."                 | :x:          |
| Test Case 2 | vertex valid (unoccupied, no adjacent settlements), settlements = 0                 | settlement appended to player's settlements list                   | :x:          |
| Test Case 3 | vertex valid (unoccupied, no adjacent settlements), settlements = 4                 | settlement appended to player's settlements list                   | :x:          |
| Test Case 4 | vertex valid (unoccupied, no adjacent settlements), settlements = 5                 | IllegalStateException: "No settlements remaining."                 | :x:          |
| Test Case 5 | vertex already occupied, settlements = 0                                            | IllegalArgumentException: "Vertex is already occupied."            | :x:          |
| Test Case 6 | vertex adjacent to an existing settlement (distance rule violated), settlements = 0 | IllegalArgumentException: "Settlement violates the distance rule." | :x:          |


---

### Method under test: `placeRoad(edge)`

Step 1:

- Input: edge
- State: road count, edge occupancy, connectivity to player's network
- Output: road appended to player's roads list
- Output: exception

Step 2:

- edge: Pointer
- Non-null edge: Case
- road count: Interval [0, 15], Appending a Single Element
- Output (road appended): Boolean
- Output (exception thrown): Boolean

Step 3:

- Input edge (Pointer / Case): null; valid (unoccupied, connected to player's network); occupied; not connected to player's network
- Input road count (Interval [0, 15]): −1 (CAN'T SET); 0 (LOW); 14 (last element that fits); 15 (HIGH — CAN'T ADD); 16 (CAN'T SET)
- Output: road appended to player's roads list
- Output: "Edge cannot be null." / "Edge is already occupied." / "Road must connect to player's existing network." / "No roads remaining."


|              | System under test                                                  | Expected output                                                             | Implemented? |
| ------------ | ------------------------------------------------------------------ | --------------------------------------------------------------------------- | ------------ |
| Test Case 7  | edge = null, roads = 0                                             | IllegalArgumentException: "Edge cannot be null."                            | :x:          |
| Test Case 8  | edge valid (unoccupied, connected to player's network), roads = 0  | road appended to player's roads list                                        | :x:          |
| Test Case 9  | edge valid (unoccupied, connected to player's network), roads = 14 | road appended to player's roads list                                        | :x:          |
| Test Case 10 | edge valid (unoccupied, connected to player's network), roads = 15 | IllegalStateException: "No roads remaining."                                | :x:          |
| Test Case 11 | edge already occupied, roads = 0                                   | IllegalArgumentException: "Edge is already occupied."                       | :x:          |
| Test Case 12 | edge unoccupied but not connected to player's network, roads = 0   | IllegalArgumentException: "Road must connect to player's existing network." | :x:          |


---

### Method under test: `receiveResources(resources)`

Step 1:

- Input: resources
- State: player's existing resources map
- Output: each quantity merged into player's resources map
- Output: exception

Step 2:

- resources: Pointer
- Non-null resources: Size of Collection
- ResourceType per entry: Case
- quantity per entry: Interval [1, 19]
- Output (resources map updated): Boolean
- Output (exception thrown): Boolean

Step 3:

- Input resources (Pointer / Size of Collection): null; {} (empty); one entry; more than one entry
- Input ResourceType (Case): WOOD; BRICK; SHEEP; WHEAT; ORE; DESERT (invalid)
- Input quantity (Interval [1, 19]): 0 (LOW−ε = CAN'T SET); 1 (LOW); 19 (HIGH); 20 (HIGH+ε = CAN'T SET)
- Output: player's resources map updated
- Output: "Resources cannot be null." / "Resource quantity must be at least 1." / "Cannot receive DESERT as a resource."


|              | System under test                                     | Expected output                                                   | Implemented? |
| ------------ | ----------------------------------------------------- | ----------------------------------------------------------------- | ------------ |
| Test Case 13 | resources = null                                      | IllegalArgumentException: "Resources cannot be null."             | :x:          |
| Test Case 14 | resources = {} (empty map)                            | player's resources map unchanged                                  | :x:          |
| Test Case 15 | resources = {WOOD: 1} (quantity at lower boundary)    | player's WOOD count increases by 1                                | :x:          |
| Test Case 16 | resources = {BRICK: 19} (quantity at upper boundary)  | player's BRICK count increases by 19                              | :x:          |
| Test Case 17 | resources = {SHEEP: 0} (just below lower boundary)    | IllegalArgumentException: "Resource quantity must be at least 1." | :x:          |
| Test Case 18 | resources = {WOOD: 5, BRICK: 3} (more than one entry) | player's WOOD count increases by 5 and BRICK count increases by 3 | :x:          |
| Test Case 19 | resources = {DESERT: 1} (invalid resource type)       | IllegalArgumentException: "Cannot receive DESERT as a resource."  | :x:          |


