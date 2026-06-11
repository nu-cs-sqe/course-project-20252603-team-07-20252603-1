# Player Resource BVA

`Player` tracks per-resource counts for the five normal Catan resources (brick, grain, lumber, ore,
wool). DESERT is not a valid resource for a player to hold. Methods under test:
`updateResources(resource, delta)`, `getResourceCount(resource)`, `getTotalResourceCount()`,
`receiveResources(map)`.

---

### Method under test: `updateResources(resource, delta)`

Step 1:

- Input: resource type, integer delta (positive to add, negative to subtract)
- State: current count for the given resource
- Output: count updated
- Output: exception

Step 2:

- resource: Case {null, DESERT (invalid), BRICK/GRAIN/LUMBER/ORE/WOOL (valid)}
- delta: Interval (any int); adding boundary = 1; bank max = 19; subtract-to-zero boundary = current count
- current count when subtracting: Interval [0, 19]; critical boundary = delta magnitude

Step 3:

- resource: null; DESERT; valid (BRICK representative)
- delta (add): 0 (neutral — not a normal use case); 1 (min positive, LOW); 19 (bank max, HIGH)
- delta (subtract): −1 from count=1 (subtract to exactly zero); −1 from count=0 (subtract below zero); −2 from count=1 (delta exceeds count)
- atomicity: failed subtract must not change any resource count

|              | State of the System                                           | Expected output                                     | Implemented?       |
|--------------|---------------------------------------------------------------|-----------------------------------------------------|--------------------|
| Test Case 1  | resource=null                                                 | IllegalArgumentException                            | :white_check_mark: |
| Test Case 2  | resource=DESERT                                               | IllegalArgumentException                            | :white_check_mark: |
| Test Case 3  | resource=BRICK, delta=+1 (min positive delta), count starts 0 | count becomes 1                                    | :white_check_mark: |
| Test Case 4  | resource=WOOL, delta=+19 (bank max), count starts 0           | count becomes 19                                   | :white_check_mark: |
| Test Case 5  | resource=BRICK, delta=+1 then delta=−1 (subtract to zero)     | count becomes 0                                    | :white_check_mark: |
| Test Case 6  | resource=BRICK, delta=−1, count=0 (subtract below zero)       | InsufficientResourcesException                     | :white_check_mark: |
| Test Case 7  | resource=BRICK, delta=−2, count=1 (delta exceeds count)       | InsufficientResourcesException; count stays 1      | :white_check_mark: |
| Test Case 8  | resource=ORE, delta=+2 then +3 (accumulation)                 | count becomes 5                                    | :white_check_mark: |
| Test Case 9  | brick=+3, lumber=+5 (two resources, independent tracking)     | brick=3, lumber=5, wool=0                          | :white_check_mark: |
| Test Case 10 | brick=2, wool=3; subtract −5 from brick (fails); both unchanged | InsufficientResourcesException; brick=2, wool=3  | :white_check_mark: |

---

### Method under test: `getResourceCount(resource)`

Step 1:

- Input: resource type
- State: player's resource map
- Output: current count for that resource, or exception

Step 2:

- resource: Case {null, DESERT (invalid), valid resource}
- count: Interval [0, 19]

Step 3:

- resource: null; DESERT; valid resource on new player (count=0)

|             | State of the System                                 | Expected output          | Implemented?       |
|-------------|-----------------------------------------------------|--------------------------|--------------------|
| Test Case 11 | resource=null                                      | IllegalArgumentException | :white_check_mark: |
| Test Case 12 | resource=DESERT                                    | IllegalArgumentException | :white_check_mark: |
| Test Case 13 | new player, each of 5 valid resources queried      | 0 for each resource      | :white_check_mark: |

---

### Method under test: `getTotalResourceCount()`

Step 1:

- Input: none
- State: counts across all 5 valid resources
- Output: sum of all counts

Step 2:

- Total: Interval [0, 95] (5 resources × bank max 19); boundary points 0 and n > 0

Step 3:

- New player (all counts = 0): total = 0 (LOW)
- Multiple resources set: total = sum

|             | State of the System                                      | Expected output | Implemented?       |
|-------------|----------------------------------------------------------|-----------------|--------------------|
| Test Case 14 | new player, all resources = 0                           | 0               | :white_check_mark: |
| Test Case 15 | brick=2, lumber=3, wool=1, grain=4, ore=2               | 12              | :white_check_mark: |

---

### Method under test: `receiveResources(map)`

Step 1:

- Input: Map<Resource, Integer>
- State: player's existing resource counts
- Output: counts updated for each entry in the map

Step 2:

- map: Pointer; size of collection [0, 5]; per-entry resource type: Case; per-entry quantity: Interval [1, ...]

Step 3:

- map: one entry; more than one entry

|             | State of the System                                        | Expected output                                       | Implemented?       |
|-------------|-------------------------------------------------------------|------------------------------------------------------|--------------------|
| Test Case 16 | map = {BRICK: 1, WOOL: 2}                                 | brick count = 1, wool count = 2                      | :white_check_mark: |
