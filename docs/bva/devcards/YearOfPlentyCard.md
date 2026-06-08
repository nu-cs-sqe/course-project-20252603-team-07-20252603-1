# YearOfPlentyCard BVA

`YearOfPlentyCard` represents a Year of Plenty progress card in Catan. When
played, the player immediately takes any 2 resource cards from the bank supply.
The two resources may be of the same type or different types, and can be used to
build in the same turn. The deck contains 2 Year of Plenty cards.

---

### Method under test: `play(Player player, Resource resource1, Resource resource2)`

Step 1:

- Input: player, resource1, resource2
- Output: player receives resource1 and resource2 from the bank
- Output: exception

Step 2:

- player: Pointer
- resource1: Pointer / Case (BRICK, GRAIN, LUMBER, ORE, WOOL; DESERT is invalid)
- resource2: Pointer / Case (same set as resource1)
- Output (resources received): Boolean
- Output (exception thrown): Boolean

Step 3:

- Input player (Pointer): null; valid Player
- Input resource1 (Pointer / Case): null; BRICK; GRAIN; LUMBER; ORE; WOOL; DESERT (invalid)
- Input resource2 (Pointer / Case): null; BRICK; GRAIN; LUMBER; ORE; WOOL; DESERT (invalid)
- Combined resource1 and resource2: same type (valid); different types (valid)
- Output: player's resource counts increase accordingly
- Output: "Player cannot be null." / "Resource cannot be null." / "Cannot take DESERT as a resource."


|             | System under test                                     | Expected output                                           | Implemented?       |
| ----------- | ----------------------------------------------------- | --------------------------------------------------------- |--------------------|
| Test Case 1 | player = null                                         | IllegalArgumentException: "Player cannot be null."        | :white_check_mark: |
| Test Case 2 | resource1 = null, resource2 = BRICK                   | IllegalArgumentException: "Resource cannot be null."      | :white_check_mark: |
| Test Case 3 | resource1 = BRICK, resource2 = null                   | IllegalArgumentException: "Resource cannot be null."      | :x:                |
| Test Case 4 | resource1 = DESERT, resource2 = ORE                   | IllegalArgumentException: "Cannot take DESERT as a resource." | :x:                |
| Test Case 5 | resource1 = LUMBER, resource2 = DESERT                | IllegalArgumentException: "Cannot take DESERT as a resource." | :x:                |
| Test Case 6 | resource1 = ORE, resource2 = ORE (same type)          | player gains 2 ORE                                       | :x:                |
| Test Case 7 | resource1 = BRICK, resource2 = WOOL (different types) | player gains 1 BRICK and 1 WOOL                          | :x:                |
