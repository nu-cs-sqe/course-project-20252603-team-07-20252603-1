# GameLoopController BVA

`GameLoopController` is a thin controller that delegates all game-loop actions to `GameModel` and
`DiceHandler`. It holds no state of its own. All tests use EasyMock to verify the exact delegation
calls. BVA is not the primary concern here — the tests verify correct delegation rather than
boundary conditions on inputs.

---

### Method under test: `getCurrentPlayer(model)`

Delegates directly to `model.getCurrentPlayer()`.

|             | State of the System                            | Expected output                            | Implemented?       |
|-------------|------------------------------------------------|--------------------------------------------|--------------------|
| Test Case 1 | model.getCurrentPlayer() returns a Player      | controller returns the same Player object  | :white_check_mark: |

---

### Method under test: `getCurrentPlayerIndex(model)`

Delegates directly to `model.getCurrentPlayerIndex()`.

|             | State of the System                              | Expected output                       | Implemented?       |
|-------------|--------------------------------------------------|---------------------------------------|--------------------|
| Test Case 2 | model.getCurrentPlayerIndex() returns 2          | controller returns 2                  | :white_check_mark: |

---

### Method under test: `endTurn(model)`

Delegates directly to `model.endTurn()`.

|             | State of the System                    | Expected output                | Implemented?       |
|-------------|----------------------------------------|--------------------------------|--------------------|
| Test Case 3 | model.endTurn() completes normally     | model.endTurn() called once    | :white_check_mark: |

---

### Method under test: `rollDiceAndDistribute(model, roller)`

Calls `roller.rollTwoDice()`, passes the result to `model.performTurn(roll)`, returns the roll value.

Step 1:

- Input: GameModel, DiceHandler
- State: dice roll result from roller
- Output: model.performTurn called with roll; roll value returned

Step 2:

- roll: Interval [2, 12] (two dice)

Step 3:

- roll = 8 (representative mid-range value): performTurn(8) called; 8 returned

|             | State of the System                                     | Expected output                                          | Implemented?       |
|-------------|----------------------------------------------------------|----------------------------------------------------------|--------------------|
| Test Case 4 | roller.rollTwoDice() returns 8                          | model.performTurn(8) called; method returns 8            | :white_check_mark: |
