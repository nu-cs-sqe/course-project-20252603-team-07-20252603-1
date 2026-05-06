# GameSetupController BVA

`GameSetupController` is the controller for the player-setup phase. The
methods documented here were introduced to move player-input validation out
of the view layer (`PlayerConfigView`).

Supporting types:

- `PlayerAddResult` — result enum returned from
  `addPlayerWithFullValidation`. Cases: `SUCCESS`, `NAME_EMPTY`,
  `NAME_TAKEN`, `COLOR_EMPTY`, `COLOR_TAKEN`.

The controller is currently stateless — the caller supplies the
`GameSetupModel` on every call. Side effects on the model are observed via
EasyMock expectations (`isNameAvailable`, `isColorAvailable`, `addPlayer`,
`clearPlayers`).

---

### Method under test: `addPlayerWithFullValidation(GameSetupModel model, String name, String color)`

Step 1:
- Input: model
- Input: name
- Input: color
- Input: state of the model (used names, used colors)
- Output: PlayerAddResult
- Output: call to model.addPlayer(trimmedName, color) — on SUCCESS only

Step 2:
- model - GameSetupModel (object reference); cases: any non-null model
- name - String; cases: null, "" (empty), whitespace-only, non-empty after
trim
- color - String; cases: null, non-null
- State.usedNames - set of strings; cases: contains the trimmed name, does
not contain the trimmed name
- State.usedColors - set of strings; cases: contains the color, does not
contain the color
- PlayerAddResult - enum; cases: SUCCESS, NAME_EMPTY, NAME_TAKEN,
COLOR_EMPTY, COLOR_TAKEN
- addPlayer side effect - cases: invoked exactly once with (trimmed name,
color), not invoked at all

Step 3:
- Input name: null, "" (lower invalid boundary, length 0), " "
(single-space whitespace), " \t\n " (mixed whitespace), "A" (lower valid
boundary, length 1 after trim), "Alice" (typical), "  Alice  "
(whitespace-padded — must trim before storage), "Alice" with model already
containing "Alice"
- Input color: null, "Red" with model not containing "Red", "Red" with
model already containing "Red"
- Check ordering: a name-format failure must short-circuit before
name-uniqueness; a name-uniqueness failure must short-circuit before color
presence; a color-presence failure must short-circuit before
color-uniqueness
- Output: SUCCESS / NAME_EMPTY / NAME_TAKEN / COLOR_EMPTY / COLOR_TAKEN; on
SUCCESS, addPlayer(trimmed, color) is invoked exactly once; on any
non-SUCCESS, addPlayer is NOT invoked

|              | System under test                                                              | Expected output                                            | Implemented?       |
|--------------|--------------------------------------------------------------------------------|------------------------------------------------------------|--------------------|
| Test Case 1  | empty model; name=null, color="Red"                                            | NAME_EMPTY; no model interaction                           | :white_check_mark: |
| Test Case 2  | empty model; name="", color="Red"                                              | NAME_EMPTY; no model interaction                           | :white_check_mark: |
| Test Case 3  | empty model; name=" ", color="Red"                                             | NAME_EMPTY; no model interaction                           | :white_check_mark: |
| Test Case 4  | empty model; name=" \t\n ", color="Red"                                        | NAME_EMPTY; no model interaction                           | :white_check_mark: |
| Test Case 5  | empty model; name="A", color="Red"                                             | SUCCESS; addPlayer("A", "Red") invoked                     | :white_check_mark: |
| Test Case 6  | empty model; name="  Alice  ", color="Red"                                     | SUCCESS; addPlayer("Alice", "Red") invoked (trimmed)       | :white_check_mark: |
| Test Case 7  | model.usedNames contains "Alice"; name="Alice", color="Red"                    | NAME_TAKEN; no color check, no addPlayer                   | :white_check_mark: |
| Test Case 8  | empty model; name="Alice", color=null                                          | COLOR_EMPTY; no addPlayer                                  | :white_check_mark: |
| Test Case 9  | model.usedColors contains "Red"; name="Alice", color="Red"                     | COLOR_TAKEN; no addPlayer                                  | :white_check_mark: |
| Test Case 10 | empty model; name="Alice", color="Red"                                         | SUCCESS; addPlayer("Alice", "Red") invoked                 | :white_check_mark: |
| Test Case 11 | empty model; name="", color=null                                               | NAME_EMPTY (color check not reached)                       | :white_check_mark: |
| Test Case 12 | model.usedNames contains "Alice"; name="Alice", color="Red"                    | NAME_TAKEN (color check not reached)                       | :white_check_mark: |

---

### Method under test: `clearPlayers(GameSetupModel model)`

Step 1:
- Input: model
- Output: call to model.clearPlayers()

Step 2:
- model - GameSetupModel (object reference); cases: any non-null model
- model.clearPlayers() side effect - cases: invoked exactly once

Step 3:
- Input: any model reference
- Output: model.clearPlayers() invoked exactly once

|              | System under test | Expected output              | Implemented?       |
|--------------|-------------------|------------------------------|--------------------|
| Test Case 13 | any model         | model.clearPlayers() invoked | :white_check_mark: |
