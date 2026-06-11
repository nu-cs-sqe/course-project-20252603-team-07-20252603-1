# GameSetupModel BVA

The methods documented here were introduced alongside the controller-side
validation move (`isNameAvailable`, `clearPlayers`). The model is the source
of truth for setup-time player state (`players`, `usedNames`, `usedColors`,
`turnOrder`).

---

### Method under test: `isNameAvailable(String name)`

Step 1:
- Input: name
- Input: state of the model (usedNames set)
- Output: boolean
- Output: state of the model (unchanged — read-only, no side effects)

Step 2:
- name - String; cases: not present in usedNames, present in usedNames
(case-exact match), present in usedNames but with differing case, ""
(empty)
- State.usedNames - set of strings; cases: empty, contains one or more
names
- Output - boolean; cases: true, false
- State (post-call) - cases: unchanged across all inputs (read-only)

Step 3:
- Input: an unused name on an empty model; a name equal (case-sensitive) to
one already used; the same characters with differing case; "" on an empty
model (model does not reject blanks — that responsibility belongs to the
controller); a name distinct from one already used
- Output: true / false
- State: unchanged across all cases

|             | System under test                                | Expected output                                | Implemented?       |
|-------------|--------------------------------------------------|------------------------------------------------|--------------------|
| Test Case 1 | empty model; name="Alice"                        | true                                           | :white_check_mark: |
| Test Case 2 | model has player ("Alice", "Red"); name="Alice"  | false                                          | :white_check_mark: |
| Test Case 3 | model has player ("Alice", "Red"); name="alice"  | true (case-sensitive)                          | :white_check_mark: |
| Test Case 4 | empty model; name=""                             | true (model does not reject blanks)            | :white_check_mark: |
| Test Case 5 | model has player ("Alice", "Red"); name="Bob"    | true (other names unaffected)                  | :white_check_mark: |

---

### Method under test: `clearPlayers()`

Step 1:
- Input: state of the model (players, usedNames, usedColors, turnOrder)
- Output: state of the model (all four player-state fields emptied; board
and decks intentionally preserved)

Step 2:
- State.players - list of Player; cases: empty, populated
- State.usedNames - set of strings; cases: empty, populated
- State.usedColors - set of strings; cases: empty, populated
- State.turnOrder - list of Player; cases: empty, populated (after
determineTurnOrder has been called)
- State.board, State.resourceDeck, State.developmentCardDeck - cases:
untouched (preserved across the call)

Step 3:
- Input: model with one or more players added (and optionally a determined
turn order)
- Output: getPlayerCount() == 0; previously-used names and colors become
available again; turn order list is empty
- Cross-cutting: a previously-used (name, color) pair can be re-added
successfully after clear

|             | System under test                                                  | Expected output                                                                                | Implemented?       |
|-------------|--------------------------------------------------------------------|------------------------------------------------------------------------------------------------|--------------------|
| Test Case 6 | model has 2 players added and a determined turn order              | getPlayerCount() == 0; previously-used name and color available again; getTurnOrder() empty    | :white_check_mark: |
| Test Case 7 | model previously had ("Alice", "Red"), then cleared                | re-adding ("Alice", "Red") succeeds and is reflected in getPlayer(0)                           | :white_check_mark: |
