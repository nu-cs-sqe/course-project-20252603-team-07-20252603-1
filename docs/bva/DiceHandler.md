### BVA for DiceHandler Class

'DiceHandler' is responsible for rolling two dice, and returning a value [2-12].

### Method under test: `rollTwoDice()`

Step 1:

- Input: Die
- Input: Dice rolls
- Output: number 2-12

Step 2:

- Die - implemented class
- Roll - interval

Step 3:

- Input: Die class, already unit tested, can assume works as intended
- Input: Die roll of 1, 6, 0 (not feasible), 7 (not feasible)
- Output: 2, 12, 1 (not feasible), 13 (not feasible)

|             | System under test                                            | Expected output | Implemented?       |
|-------------|--------------------------------------------------------------|-----------------|--------------------|
| Test Case 1 | Initialize new DiceHandler, Die each return 1                | Die roll is 2   | :white_check_mark: |
| Test Case 2 | Initialize new DiceHandler, Die each return 6                | Die roll is 12  | :white_check_mark: |
| Test Case 3 | Initialize new DiceHandler, Die 1 returns 1, Die 2 returns 6 | Die roll is 7   | :white_check_mark: |
| Test Case 4 | Initialize new DiceHandler, Die 1 returns 6, Die 2 returns 1 | Die roll is 7   | :white_check_mark: |