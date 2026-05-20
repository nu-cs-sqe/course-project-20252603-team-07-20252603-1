### BVA for Robber Class

The robber class represents the robber piece in the game of Catan. It is responsible for blocking resources from being awarded,
as well as stealing a resource from a player that resides on the hex it is currently being placed on.

### Method under test: `getRobberLocation()`

Step 1:
- Output: Robber location, as hexId

Step 2:
- HexId - Interval

Step 3:
- Output: IN_HEX_ID (0), MAX_HEX_ID (18), -1 (not feasible), 19 (not feasible)

|             | System under test   | Expected output | Implemented? |
|-------------|---------------------|-----------------|--------------|
| Test Case 1 | Robber is on hex 0  | 0               | x            |
| Test Case 2 | Robber is on hex 18 | 18              | x            |

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

|             | System under test     | Expected output                       | Implemented? |
|-------------|-----------------------|---------------------------------------|--------------|
| Test Case 3 | Move robber to hex 0  | Robber location is at 0               | x            |
| Test Case 4 | Move robber to hex 18 | Robber location is at 18              | x            |
| Test Case 5 | Move robber to hex -1 | "Cannot move Robber to invalid HexId" | x            |
| Test Case 6 | Move robber to hex 19 | "Cannot move Robber to invalid HexId" | x            |




