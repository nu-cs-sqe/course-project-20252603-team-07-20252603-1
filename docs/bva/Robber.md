### BVA for Robber Class

The robber class represents the robber piece in the game of Catan. It is responsible for blocking resources from being
awarded,
as well as stealing a resource from a player that resides on the hex it is currently being placed on.

### Method under test: `moveRobber(int HexId)`

Step 1:

- Input: HexId
- Output: Robber state
- Output: Invalid HexId

Step 2:

- HexId - Interval
- State - Robber location, interval
- Error - exception

Step 3:

- Input: MIN_HEX_ID (0), MAX_HEX_ID (18), -1, 19
- Output: IN_HEX_ID (0), MAX_HEX_ID (18), -1 (not feasible), 19 (not feasible)
- Output: "Cannot move Robber to invalid HexId"

|             | System under test     | Expected output                       | Implemented?       |
|-------------|-----------------------|---------------------------------------|--------------------|
| Test Case 1 | Move robber to hex 0  | Robber location is at 0               | :white_check_mark: |
| Test Case 2 | Move robber to hex 18 | Robber location is at 18              | :white_check_mark: |
| Test Case 3 | Move robber to hex -1 | "Cannot move Robber to invalid HexId" | :white_check_mark: |
| Test Case 4 | Move robber to hex 19 | "Cannot move Robber to invalid HexId" | :white_check_mark: |

### Method under test: `getRobberLocation()`

Step 1:

- Output: Robber location, as hexId

Step 2:

- HexId - Interval

Step 3:

- Output: IN_HEX_ID (0), MAX_HEX_ID (18), -1 (not feasible), 19 (not feasible)

|             | System under test     | Expected output | Implemented?               |
|-------------|-----------------------|-----------------|----------------------------|
| Test Case 5 | Robber is initialized | 9               | :white_check_mark:         |
| Test Case 6 | Robber is on hex 0    | 0               | Implemented in Test Case 1 |
| Test Case 7 | Robber is on hex 18   | 18              | Implemented in Test Case 2 |




