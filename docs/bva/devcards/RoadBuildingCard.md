# RoadBuildingCard BVA

`RoadBuildingCard` represents a Road Building progress card in Catan. When
played, the player may immediately place 2 free roads on the board following
normal road-building rules. If the player has only 1 road piece remaining in
their supply, only 1 road is placed. Each road must be on an unoccupied edge
connected to the player's existing road/settlement/city network. Per Catan rules
each player has a maximum of 15 road pieces. The deck contains 2 Road Building
cards.

---

### Method under test: `play(Player player, Edge edge1, Edge edge2)`

Step 1:

- Input: player, edge1, edge2
- State: player's road count (roads already placed), edge1 occupancy, edge1 connectivity, edge2 occupancy, edge2 connectivity
- Output: up to 2 roads placed for the player
- Output: exception

Step 2:

- player: Pointer
- edge1: Pointer / Case (valid, occupied, not connected to network)
- edge2: Pointer / Case (valid, occupied, not connected to network, null if only 1 road remaining)
- road count (roads already placed): Interval [0, 15], Appending a Single Element (up to twice)
- Output (roads placed): Count [0, 2]
- Output (exception thrown): Boolean

Step 3:

- Input player (Pointer): null; valid Player
- Input edge1 (Pointer / Case): null; valid (unoccupied, connected to network); already occupied; not connected to network
- Input edge2 (Pointer / Case): null (only 1 road placed when 1 remaining); valid; already occupied; not connected to network (including edge1)
- State road count (Interval [0, 15]): −1 (CAN'T SET); 0 (LOW — 15 remaining, can place 2); 13 (last pair that fits — 2 remaining); 14 (only 1 remaining — can place 1); 15 (HIGH — no roads remaining); 16 (CAN'T SET)
- Output: roads appended to player's road list
- Output: "Player cannot be null." / "Edge cannot be null." / "Edge is already occupied." / "Road must connect to player's existing network." / "No roads remaining."


|              | System under test                                                                           | Expected output                                                             | Implemented? |
| ------------ | ------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------- | --------- |
| Test Case 1  | player = null                                                                               | IllegalArgumentException: "Player cannot be null."                          | :white_check_mark:        |
| Test Case 2  | edge1 = null, roads placed = 0                                                              | IllegalArgumentException: "Edge cannot be null."                            | :white_check_mark:        |
| Test Case 3  | edge1 valid, edge2 valid, roads placed = 0 (15 remaining)                                   | 2 roads placed; player road count increases by 2                            | :white_check_mark:        |
| Test Case 4  | edge1 valid, edge2 valid, roads placed = 13 (2 remaining — last pair that fits)             | 2 roads placed; player road count is 15                                     | :white_check_mark:       |
| Test Case 5  | edge1 valid, edge2 = null, roads placed = 14 (only 1 remaining)                             | 1 road placed; player road count is 15                                      | :white_check_mark:       |
| Test Case 6  | roads placed = 15 (no roads remaining)                                                      | IllegalStateException: "No roads remaining."                                | :white_check_mark:       |
| Test Case 7  | edge1 is already occupied, roads placed = 0                                                 | IllegalArgumentException: "Edge is already occupied."                       | :white_check_mark:       |
| Test Case 8  | edge1 not connected to player's network, roads placed = 0                                   | IllegalArgumentException: "Road must connect to player's existing network." | :white_check_mark:       |
| Test Case 9  | edge1 valid, edge2 is already occupied, roads placed = 0                                    | IllegalArgumentException: "Edge is already occupied."                       | :white_check_mark:       |
| Test Case 10 | edge1 valid, edge2 not connected to player's network (including edge1), roads placed = 0    | IllegalArgumentException: "Road must connect to player's existing network." | :white_check_mark:       |
