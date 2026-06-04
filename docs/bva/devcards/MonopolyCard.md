# MonopolyCard BVA

`MonopolyCard` represents a Monopoly progress card in Catan. When played, the
player announces 1 type of resource. All other players must give the playing
player all of their resource cards of that type. If an opponent has none of the
named resource, they give nothing. The deck contains 2 Monopoly cards.

---

### Method under test: `play(Resource resource, List<Player> otherPlayers)`

Step 1:

- Input: resource, otherPlayers
- State: each other player's holdings of the named resource
- Output: all cards of the named resource transferred from other players to the playing player
- Output: exception

Step 2:

- resource: Pointer / Case (BRICK, GRAIN, LUMBER, ORE, WOOL; DESERT is invalid)
- otherPlayers: Pointer / Size of Collection
- per-player holdings of named resource: Count [0, 19]
- Output (resources transferred): Boolean
- Output (exception thrown): Boolean

Step 3:

- Input resource (Pointer / Case): null; BRICK; GRAIN; LUMBER; ORE; WOOL; DESERT (invalid)
- Input otherPlayers (Pointer / Size of Collection): null; empty list; one player; more than one player
- State per-player holdings of named resource (Count): 0 (has none — nothing transferred); 1 (has exactly one); >1 (has several)
- Output: playing player's resource count increases by the total taken from all opponents
- Output: "Resource cannot be null." / "Cannot monopolize DESERT." / "Other players list cannot be null."


|             | System under test                                                                  | Expected output                                              | Implemented? |
| ----------- | ---------------------------------------------------------------------------------- | ------------------------------------------------------------ | ------------ |
| Test Case 1 | resource = null                                                                    | IllegalArgumentException: "Resource cannot be null."         | :x:          |
| Test Case 2 | resource = DESERT                                                                  | IllegalArgumentException: "Cannot monopolize DESERT."        | :x:          |
| Test Case 3 | otherPlayers = null                                                                | IllegalArgumentException: "Other players list cannot be null." | :x:        |
| Test Case 4 | resource = BRICK, otherPlayers = [] (empty list)                                   | no resources transferred; player's resources unchanged       | :x:          |
| Test Case 5 | resource = ORE, 1 opponent has 5 ORE                                               | opponent loses 5 ORE; player gains 5 ORE                    | :x:          |
| Test Case 6 | resource = WOOL, 1 opponent has 0 WOOL                                             | no WOOL transferred; player's WOOL unchanged                 | :x:          |
| Test Case 7 | resource = GRAIN, 3 opponents have 2, 0, and 4 GRAIN respectively                 | opponents lose 2, 0, and 4 GRAIN; player gains 6 GRAIN      | :x:          |
| Test Case 8 | resource = LUMBER, 1 opponent has 1 LUMBER (minimum transferable amount)           | opponent loses 1 LUMBER; player gains 1 LUMBER              | :white_check_mark: |
