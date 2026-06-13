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

### Method under test: `addPlayerWithFullValidation(GameSetupModel model, String name, PlayerColor color)`

Step 1:

- Input: model
- Input: name
- Input: color (PlayerColor)
- Input: state of the model (used names, used colors)
- Output: PlayerAddResult
- Output: call to model.addPlayer(trimmedName, color) — on SUCCESS only

Step 2:

- model - GameSetupModel (object reference); cases: any non-null model
- name - String; cases: null, "" (empty), whitespace-only, non-empty after trim
- color - PlayerColor; cases: null, non-null
- State.usedNames - set of strings; cases: contains the trimmed name, does
  not contain the trimmed name
- State.usedColors - set of PlayerColor; cases: contains the color, does not
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
- Input color: null, PlayerColor.RED with model not containing RED, PlayerColor.RED
  with model already containing RED
- Check ordering: a name-format failure must short-circuit before
  name-uniqueness; a name-uniqueness failure must short-circuit before color
  presence; a color-presence failure must short-circuit before
  color-uniqueness
- Output: SUCCESS / NAME_EMPTY / NAME_TAKEN / COLOR_EMPTY / COLOR_TAKEN; on
  SUCCESS, addPlayer(trimmed, color) is invoked exactly once; on any
  non-SUCCESS, addPlayer is NOT invoked

|              | System under test                                         | Expected output                                    | Implemented?       |
|--------------|-----------------------------------------------------------|----------------------------------------------------|--------------------|
| Test Case 1  | empty model; name=null, color=RED                         | NAME_EMPTY; no model interaction                   | :white_check_mark: |
| Test Case 2  | empty model; name="", color=RED                           | NAME_EMPTY; no model interaction                   | :white_check_mark: |
| Test Case 3  | empty model; name=" ", color=RED                          | NAME_EMPTY; no model interaction                   | :white_check_mark: |
| Test Case 4  | empty model; name=" \t\n ", color=RED                     | NAME_EMPTY; no model interaction                   | :white_check_mark: |
| Test Case 5  | empty model; name="A", color=RED                          | SUCCESS; addPlayer("A", RED) invoked               | :white_check_mark: |
| Test Case 6  | empty model; name="  Alice  ", color=RED                  | SUCCESS; addPlayer("Alice", RED) invoked (trimmed) | :white_check_mark: |
| Test Case 7  | model.usedNames contains "Alice"; name="Alice", color=RED | NAME_TAKEN; no color check, no addPlayer           | :white_check_mark: |
| Test Case 8  | empty model; name="Alice", color=null                     | COLOR_EMPTY; no addPlayer                          | :white_check_mark: |
| Test Case 9  | model.usedColors contains RED; name="Alice", color=RED    | COLOR_TAKEN; no addPlayer                          | :white_check_mark: |
| Test Case 10 | empty model; name="Alice", color=RED                      | SUCCESS; addPlayer("Alice", RED) invoked           | :white_check_mark: |
| Test Case 11 | empty model; name="", color=null                          | NAME_EMPTY (color check not reached)               | :white_check_mark: |
| Test Case 12 | model.usedNames contains "Alice"; name="Alice", color=RED | NAME_TAKEN (color check not reached)               | :white_check_mark: |

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

---

### Method under test: `validatePlayerCount(GameSetupModel model)`

Valid player range is [3, 4]. BVA tests the two valid boundary values and the
adjacent-invalid values on each side.

Step 1:

- Input: model
- Output: boolean (true if count in [3, 4], false otherwise)

Step 2:

- count: Interval [3, 4]; values 2 (below) and 5 (above) are representative invalids

Step 3:

- count: 2 (LOW−1), 3 (LOW), 4 (HIGH), 5 (HIGH+1)

|              | System under test                                 | Expected output | Implemented?       |
|--------------|---------------------------------------------------|-----------------|--------------------|
| Test Case 14 | model.getPlayerCount() = 3 (lower boundary valid) | true            | :white_check_mark: |
| Test Case 15 | model.getPlayerCount() = 4 (upper boundary valid) | true            | :white_check_mark: |
| Test Case 16 | model.getPlayerCount() = 2 (below lower boundary) | false           | :white_check_mark: |
| Test Case 17 | model.getPlayerCount() = 5 (above upper boundary) | false           | :white_check_mark: |

---

### Method under test: `addPlayer(GameSetupModel model, String name, PlayerColor color)`

Delegates directly to `model.addPlayer(name, color)`.

|              | System under test  | Expected output                      | Implemented?       |
|--------------|--------------------|--------------------------------------|--------------------|
| Test Case 18 | any name and color | model.addPlayer(name, color) invoked | :white_check_mark: |

---

### Method under test: `addPlayerWithColorValidation(GameSetupModel model, String name, PlayerColor color)`

Checks `model.isColorAvailable(color)` before adding. Returns true on success, false if color taken.

|              | System under test                   | Expected output                            | Implemented?       |
|--------------|-------------------------------------|--------------------------------------------|--------------------|
| Test Case 19 | model.isColorAvailable(RED) = true  | model.addPlayer invoked; returns true      | :white_check_mark: |
| Test Case 20 | model.isColorAvailable(RED) = false | model.addPlayer not invoked; returns false | :white_check_mark: |

---

### Method under test: `getPlayerName(GameSetupModel model, int index)`

Delegates to `model.getPlayer(index).getName()`.

|              | System under test                            | Expected output | Implemented?       |
|--------------|----------------------------------------------|-----------------|--------------------|
| Test Case 21 | model.getPlayer(0).getName() returns "Alice" | returns "Alice" | :white_check_mark: |

---

### Method under test: `getPlayerCount(GameSetupModel model)`

Delegates to `model.getPlayerCount()`.

|              | System under test                | Expected output | Implemented?       |
|--------------|----------------------------------|-----------------|--------------------|
| Test Case 22 | model.getPlayerCount() returns 4 | returns 4       | :white_check_mark: |

---

### Method under test: `getBoardHexCount(BoardHandler board)`

Delegates to `board.getHexCount()`.

|              | System under test              | Expected output | Implemented?       |
|--------------|--------------------------------|-----------------|--------------------|
| Test Case 23 | board.getHexCount() returns 19 | returns 19      | :white_check_mark: |

---

### Method under test: `getHexOrder(BoardHandler board)`

Delegates to `board.getHexOrder()`.

|              | System under test                            | Expected output       | Implemented?       |
|--------------|----------------------------------------------|-----------------------|--------------------|
| Test Case 24 | board.getHexOrder() returns a non-empty list | returns the same list | :white_check_mark: |

---

### Method under test: `getResourceDeck(GameSetupModel model)`

Delegates to `model.getResourceDeck()`.

|              | System under test                             | Expected output       | Implemented?       |
|--------------|-----------------------------------------------|-----------------------|--------------------|
| Test Case 25 | model.getResourceDeck() returns a deck object | returns the same deck | :white_check_mark: |

---

### Method under test: `initializeResourceDeck(GameSetupModel model)`

Calls `model.setResourceDeck(new ResourceDeck())`.

|              | System under test | Expected output                         | Implemented?       |
|--------------|-------------------|-----------------------------------------|--------------------|
| Test Case 26 | any model         | model.setResourceDeck(...) invoked once | :white_check_mark: |

---

### Method under test: `getDevelopmentCardDeck(GameSetupModel model)`

Delegates to `model.getDevelopmentCardDeck()`.

|              | System under test                                    | Expected output       | Implemented?       |
|--------------|------------------------------------------------------|-----------------------|--------------------|
| Test Case 27 | model.getDevelopmentCardDeck() returns a deck object | returns the same deck | :white_check_mark: |

---

### Method under test: `initializeDevelopmentCardDeck(GameSetupModel model)`

Calls `model.setDevelopmentCardDeck(new DevelopmentCardDeck())`.

|              | System under test | Expected output                                | Implemented?       |
|--------------|-------------------|------------------------------------------------|--------------------|
| Test Case 28 | any model         | model.setDevelopmentCardDeck(...) invoked once | :white_check_mark: |

---

### Method under test: `determineTurnOrder(GameSetupModel model)`

Delegates to `model.determineTurnOrder()`.

|              | System under test | Expected output                         | Implemented?       |
|--------------|-------------------|-----------------------------------------|--------------------|
| Test Case 29 | any model         | model.determineTurnOrder() invoked once | :white_check_mark: |

---

### Method under test: `getTurnOrder(GameSetupModel model)`

Delegates to `model.getTurnOrder()`.

|              | System under test                             | Expected output       | Implemented?       |
|--------------|-----------------------------------------------|-----------------------|--------------------|
| Test Case 30 | model.getTurnOrder() returns a non-empty list | returns the same list | :white_check_mark: |
