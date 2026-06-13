# Player BVA

`Player` holds a player's name, victory points, settlements, roads, and resources.
Its three public methods are `placeSettlement()`, `placeRoad()`, and
`receiveResources(resources)`. Per standard Catan rules each player has 5 settlement
pieces and 15 road pieces. `DESERT` is not a receivable resource.

Board-level validations (vertex occupancy, distance rule, edge connectivity) are
the responsibility of `BoardHandler`, not `Player`.

---

### Method under test: `placeSettlement()`

Step 1:

- State: settlement count
- Output: settlement appended to player's settlements list
- Output: exception

Step 2:

- settlement count: Interval [0, 5], Appending a Single Element
- Output (settlement appended): Boolean
- Output (exception thrown): Boolean

Step 3:

- Input settlement count (Interval [0, 5]): −1 (CAN'T SET); 0 (LOW); 4 (last element that fits); 5 (HIGH — CAN'T ADD); 6 (CAN'T SET)
- Output: settlement appended to player's settlements list
- Output: "No settlements remaining."


|             | System under test | Expected output                                    | Implemented? |
| ----------- | ----------------- | -------------------------------------------------- | ------------ |
| Test Case 1 | settlements = 0   | settlement appended to player's settlements list   | :white_check_mark: |
| Test Case 2 | settlements = 4   | settlement appended to player's settlements list   | :white_check_mark: |
| Test Case 3 | settlements = 5   | IllegalStateException: "No settlements remaining." | :white_check_mark: |


---

### Method under test: `placeRoad()`

Step 1:

- State: road count
- Output: road appended to player's roads list
- Output: exception

Step 2:

- road count: Interval [0, 15], Appending a Single Element
- Output (road appended): Boolean
- Output (exception thrown): Boolean

Step 3:

- Input road count (Interval [0, 15]): −1 (CAN'T SET); 0 (LOW); 14 (last element that fits); 15 (HIGH — CAN'T ADD); 16 (CAN'T SET)
- Output: road appended to player's roads list
- Output: "No roads remaining."


|             | System under test | Expected output                              | Implemented? |
| ----------- | ----------------- | -------------------------------------------- | ------------ |
| Test Case 4 | roads = 0         | road appended to player's roads list         | :white_check_mark: |
| Test Case 5 | roads = 14        | road appended to player's roads list         | :white_check_mark: |
| Test Case 6 | roads = 15        | IllegalStateException: "No roads remaining." | :white_check_mark: |


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
- Input ResourceType (Case): LUMBER; BRICK; WOOL; ORE; GRAIN; DESERT (invalid)
- Input quantity (Interval [1, 19]): 0 (LOW−ε = CAN'T SET); 1 (LOW); 19 (HIGH); 20 (HIGH+ε = CAN'T SET)
- Output: player's resources map updated
- Output: "Resources cannot be null." / "Resource quantity must be at least 1." / "Cannot receive DESERT as a resource."


|              | System under test                                        | Expected output                                                   | Implemented? |
| ------------ | -------------------------------------------------------- | ----------------------------------------------------------------- | ------------ |
| Test Case 7  | resources = null                                         | IllegalArgumentException: "Resources cannot be null."             | :white_check_mark: |
| Test Case 8  | resources = {} (empty map)                               | player's resources map unchanged                                  | :white_check_mark: |
| Test Case 9  | resources = {LUMBER: 1} (quantity at lower boundary)    | player's LUMBER count increases by 1                              | :white_check_mark: |
| Test Case 10 | resources = {BRICK: 19} (quantity at upper boundary)    | player's BRICK count increases by 19                              | :white_check_mark: |
| Test Case 11 | resources = {WOOL: 0} (just below lower boundary)       | IllegalArgumentException: "Resource quantity must be at least 1." | :white_check_mark: |
| Test Case 12 | resources = {LUMBER: 5, BRICK: 3} (more than one entry) | player's LUMBER count increases by 5 and BRICK count increases by 3 | :white_check_mark: |
| Test Case 13 | resources = {DESERT: 1} (invalid resource type)         | IllegalArgumentException: "Cannot receive DESERT as a resource."  | :white_check_mark: |


---

### Method under test: `updateVictoryPoints(int amount)`

Inputs:
- Amount -> integer -> Cases
  - 1 -> VP devcard, player builds a settlement, player builds a city
  - 2 -> longest road, largest army
  - -2 -> losing longest road, losing largest army
- State of Player -> How many VPs they have -> [0, 10]

Output
- Updated player state -> amount of VPs -> [0, 10]
  - Since players can only lose VPs in the scenario once they've gained them (i.e. losing longest road) it is not feasible that a player's points would ever be negative


|             | System under test             | Expected output      | Implemented?       |
|-------------|-------------------------------|----------------------|--------------------|
| Test Case 1 | 0 starting points, receives 1 | player has 1 point   | :white_check_mark: |
| Test Case 2 | 0 starting points, receives 2 | player has 2 points  | :white_check_mark: |
| Test Case 3 | 8 starting points, receives 2 | player has 10 points | :white_check_mark: |
| Test Case 4 | 10 starting points,loses 2    | player has 8 points  | :white_check_mark: |
| Test Case 5 | 11 starting points, loses 2   | player has 9 points  | :white_check_mark: |
| Test Case 6 | 9 starting points, receives 2 | player has 11 points | :white_check_mark: |
| Test Case 7 | 2 starting points, loses 2    | player has 0 points  | :white_check_mark: |


---

### Method under test: `increaseSettlementCount()` / `getSettlementCount()`

|              | System under test                             | Expected output                | Implemented?       |
|--------------|-----------------------------------------------|--------------------------------|--------------------|
| Test Case 14 | fresh player; call increaseSettlementCount()  | getSettlementCount() returns 1 | :white_check_mark: |
| Test Case 15 | fresh player; no call                         | getSettlementCount() returns 0 | :white_check_mark: |

---

### Method under test: `incrementKnightCount()` / `getKnightCount()`

|              | System under test                         | Expected output             | Implemented?       |
|--------------|-------------------------------------------|-----------------------------|---------------------|
| Test Case 16 | fresh player; call incrementKnightCount() | getKnightCount() returns 1  | :white_check_mark: |
| Test Case 17 | fresh player; no call                     | getKnightCount() returns 0  | :white_check_mark: |

---

### Method under test: `addDevelopmentCard()` / `removeDevelopmentCard()` / `getDevelopmentCards()`

|              | System under test                                  | Expected output                | Implemented?       |
|--------------|----------------------------------------------------|--------------------------------|--------------------|
| Test Case 18 | fresh player; addDevelopmentCard(mockCard)         | getDevelopmentCards() size = 1 | :white_check_mark: |
| Test Case 19 | player has 1 card; removeDevelopmentCard(mockCard) | getDevelopmentCards() size = 0 | :white_check_mark: |
| Test Case 20 | fresh player; no cards added                       | getDevelopmentCards() size = 0 | :white_check_mark: |

---

### Method under test: `setHasPlayedDevCardThisTurn(boolean)` / `hasPlayedDevCardThisTurn()`

|              | System under test                               | Expected output                    | Implemented?       |
|--------------|-------------------------------------------------|------------------------------------|--------------------|
| Test Case 21 | fresh player; setHasPlayedDevCardThisTurn(true) | hasPlayedDevCardThisTurn() = true  | :white_check_mark: |
| Test Case 22 | fresh player; no call                           | hasPlayedDevCardThisTurn() = false | :x: |

---

### Method under test: `isAdjacentToHex(int hexId)`

Stub implementation — always returns false regardless of hexId.

|              | System under test       | Expected output | Implemented?       |
|--------------|-------------------------|-----------------|--------------------|
| Test Case 23 | fresh player; hexId = 5 | false           | :x: |