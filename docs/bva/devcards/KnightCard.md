# KnightCard BVA

`KnightCard` represents a Knight development card in Catan. When played, the
player must immediately move the robber to a different terrain hex and may then
steal 1 random resource card from an opponent who has a settlement or city
adjacent to the robber's new hex. Per standard Catan rules, if the chosen victim
has no resource cards, nothing is stolen. Knight cards remain face-up in front of
the player after being played and contribute toward the Largest Army special card
(first player to play 3 knights receives it). The deck contains 14 Knight cards.

---

### Method under test: `play(Robber robber, int targetHexId, Player victim)`

Step 1:

- Input: robber, targetHexId, victim
- State: robber's current hex location, victim's resource count
- Output: robber moved to targetHexId
- Output: 1 random resource transferred from victim to playing player (if applicable)
- Output: exception

Step 2:

- robber: Pointer
- targetHexId: Interval [0, 18]
- victim: Pointer (null allowed — no adjacent opponent)
- robber current location vs targetHexId: Pair of Intervals (must differ)
- victim's resource count: Count [0, ∞)
- Output (robber moved): Boolean
- Output (resource stolen): Boolean
- Output (exception thrown): Boolean

Step 3:

- Input robber (Pointer): null; valid Robber instance
- Input targetHexId (Interval [0, 18]): −1 (CAN'T SET); 0 (LOW); 18 (HIGH); 19 (CAN'T SET)
- Input targetHexId vs current location (Pair of Intervals): same hex (invalid — must move); different hex (valid)
- Input victim (Pointer): null (no one adjacent to steal from); valid opponent
- State victim resource count (Count): 0 (nothing to steal); 1 (steal the only card); >1 (steal 1 of many)
- Output: robber location updated / "Robber cannot be null." / "Must move robber to a different hex." / "Cannot move Robber to invalid HexId"


|             | System under test                                                                  | Expected output                                                  | Implemented? |
| ----------- | ---------------------------------------------------------------------------------- | ---------------------------------------------------------------- | ------------ |
| Test Case 1 | robber = null                                                                      | IllegalArgumentException: "Robber cannot be null."               | :white_check_mark: |
| Test Case 2 | targetHexId = 5 (valid, different from current), victim has 3 resources            | robber moves to hex 5; 1 random resource transferred from victim | :white_check_mark: |
| Test Case 3 | targetHexId = 0 (LOW boundary), victim has resources                               | robber moves to hex 0; 1 random resource transferred from victim | :white_check_mark: |
| Test Case 4 | targetHexId = 18 (HIGH boundary), victim has resources                             | robber moves to hex 18; 1 random resource transferred from victim| :white_check_mark: |
| Test Case 5 | targetHexId = −1 (below LOW boundary)                                              | IllegalArgumentException: "Cannot move Robber to invalid HexId"  | :white_check_mark: |
| Test Case 6 | targetHexId = 19 (above HIGH boundary)                                             | IllegalArgumentException: "Cannot move Robber to invalid HexId"  | :white_check_mark: |
| Test Case 7 | targetHexId = robber's current hex (same hex)                                      | IllegalArgumentException: "Must move robber to a different hex." | :white_check_mark: |
| Test Case 8 | targetHexId valid, victim = null (no adjacent opponent)                             | robber moves; no resource stolen                                 | :white_check_mark: |
| Test Case 9 | targetHexId valid, victim has 0 resource cards                                     | robber moves; no resource stolen                                 | :white_check_mark: |
