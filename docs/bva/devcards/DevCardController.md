# DevCardController BVA

**MVC Layer: Controller** (`ui.controller.DevCardController`)

`DevCardController` is a thin controller in the MVC architecture that mediates
between the View layer and the domain model/handler for development card
operations. Following the project's established controller pattern (see
`GameLoopController`, `GameSetupController`), this controller is **stateless** —
it receives the `GameModel` as a parameter and delegates all business logic to
the model and `DevelopmentCardHandler`.

**Design Principles:**
- **Single Responsibility**: The controller's only job is to translate user
  actions into domain calls and return results to the view. It does not contain
  game rules or precondition validation — those live in `DevelopmentCardHandler`.
- **Separation of Concerns**: All validation, preconditions (P1–P4), and game
  logic live in `DevelopmentCardHandler`; the controller only delegates and
  relays exceptions.
- **Testability**: Because the controller is stateless and delegates to
  injected/passed dependencies, each method can be tested with mock objects
  (following the `GameLoopControllerTest` pattern using EasyMock).

**Phase note (per official Catan rules):** Development cards may be played at
any time during a player's turn — before rolling dice (`BEFORE_ROLL`) or after
(`GENERAL_PLAY`). The controller does not enforce phase restrictions on `play*`
methods; phase validation (if needed) is the responsibility of the game loop.

---

### Method under test: `buyDevelopmentCard(GameModel model, DevelopmentCardDeck deck)`

This method delegates to `DevelopmentCardHandler.buyDevelopmentCard()` using
the current player and current round from the model.

Step 1:

- Input: model, deck
- Output: DevelopmentCard (result from handler)
- Output: exception (relayed from handler)

Step 2:

- model: Pointer (mock)
- deck: Pointer (mock)
- Output: delegation result from DevelopmentCardHandler

Step 3:

- Verify controller calls model.getCurrentPlayer() and model.getCurrentRound() and passes results to handler
- Verify controller relays the DevelopmentCard returned by handler
- Verify controller relays IllegalStateException from handler when resources insufficient
- Verify controller relays EmptyDeckException from handler when deck empty


|             | System under test                                                                         | Expected output                                                                                          | Implemented? |
| ----------- | ----------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------- | ------------ |
| Test Case 1 | buyDevelopmentCard(model, deck); handler returns a card                                   | controller returns the same DevelopmentCard; verify handler was called with current player, deck, and round | :white_check_mark: |
| Test Case 2 | buyDevelopmentCard(model, deck); handler throws InsufficientResourcesException (insufficient resources) | controller relays InsufficientResourcesException to caller                                         | :white_check_mark: |
| Test Case 3 | buyDevelopmentCard(model, deck); handler throws EmptyDeckException (deck empty)            | controller relays EmptyDeckException to caller                                                           | :white_check_mark: |


---

### Method under test: `playKnightCard(GameModel model, DevelopmentCard card, Robber robber, int targetHexId, Player victim)`

This method delegates to `DevelopmentCardHandler.playKnightCard()` using the
current player and current round from the model.

Step 1:

- Input: model, card, robber, targetHexId, victim
- Output: void (side effects in model via handler)
- Output: exception (relayed from handler)

Step 2:

- model: Pointer (mock)
- card: Pointer (mock or invalid)
- robber: Pointer (mock or null)
- targetHexId: Interval [0, 18]
- victim: Pointer (mock or null)
- Output: delegation verified via mock expectations

Step 3:

- Verify controller extracts current player and current round from model and delegates to handler
- Verify controller relays success (handler completes normally)
- Verify controller relays exceptions from handler (precondition violations, invalid hex, etc.)


|             | System under test                                                                    | Expected output                                                                                   | Implemented? |
| ----------- | ------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------- | ------------ |
| Test Case 4 | playKnightCard(model, card, robber, 5, victim); handler succeeds                    | verify handler called with current player, card, currentRound, robber, 5, victim; no exception    | :white_check_mark: |
| Test Case 5 | playKnightCard(model, card, null, 5, victim); handler throws IllegalArgumentException | controller relays IllegalArgumentException: "Robber cannot be null."                             | :white_check_mark: |
| Test Case 6 | playKnightCard(model, card, robber, 5, null); handler succeeds (no victim)           | verify handler called; no resource stolen; no exception                                           | :white_check_mark: |
| Test Case 7 | playKnightCard(model, null, robber, 5, victim); handler throws IllegalArgumentException | controller relays IllegalArgumentException: "Development card cannot be null."                  | :white_check_mark: |


---

### Method under test: `playMonopolyCard(GameModel model, DevelopmentCard card, Resource resource)`

This method delegates to `DevelopmentCardHandler.playMonopolyCard()` using the
current player, current round, and other players from the model.

Step 1:

- Input: model, card, resource
- Output: void (side effects in model via handler)
- Output: exception (relayed from handler)

Step 2:

- model: Pointer (mock)
- card: Pointer (mock or invalid)
- resource: Pointer / Case
- Output: delegation verified via mock expectations

Step 3:

- Verify controller extracts current player, current round, and other players from model
- Verify controller delegates card and resource to handler
- Verify controller relays exceptions


|              | System under test                                                                 | Expected output                                                                    | Implemented? |
| ------------ | --------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------- | ------------ |
| Test Case 8  | playMonopolyCard(model, card, BRICK); handler succeeds                            | verify handler called with current player, card, currentRound, BRICK, and other players; no exception | :white_check_mark: |
| Test Case 9  | playMonopolyCard(model, card, null); handler throws IllegalArgumentException      | controller relays IllegalArgumentException: "Resource cannot be null."             | :white_check_mark: |
| Test Case 10 | playMonopolyCard(model, null, BRICK); handler throws IllegalArgumentException     | controller relays IllegalArgumentException: "Development card cannot be null."     | :white_check_mark: |


---

### Method under test: `playRoadBuildingCard(GameModel model, DevelopmentCard card, Edge edge1, Edge edge2)`

This method delegates to `DevelopmentCardHandler.playRoadBuildingCard()` using
the current player and current round from the model.

Step 1:

- Input: model, card, edge1, edge2
- Output: void (side effects in model via handler)
- Output: exception (relayed from handler)

Step 2:

- model: Pointer (mock)
- card: Pointer (mock or invalid)
- edge1: Pointer (mock or null)
- edge2: Pointer (mock or null)
- Output: delegation verified via mock expectations

Step 3:

- Verify controller delegates to handler with current player, card, currentRound, edge1, edge2
- Verify controller relays success and exceptions


|              | System under test                                                               | Expected output                                                              | Implemented? |
| ------------ | ------------------------------------------------------------------------------- | ---------------------------------------------------------------------------- | ------------ |
| Test Case 11 | playRoadBuildingCard(model, card, edge1, edge2); handler succeeds               | verify handler called with current player, card, currentRound, edge1, edge2; no exception | :white_check_mark: |
| Test Case 12 | playRoadBuildingCard(model, card, null, edge2); handler throws IllegalArgumentException | controller relays IllegalArgumentException: "Edge cannot be null."          | :white_check_mark: |
| Test Case 13 | playRoadBuildingCard(model, card, edge1, null); handler succeeds (1 road remaining) | verify handler called; 1 road placed                                        | :white_check_mark: |
| Test Case 14 | playRoadBuildingCard(model, null, edge1, edge2); handler throws IllegalArgumentException | controller relays IllegalArgumentException: "Development card cannot be null." | :white_check_mark: |


---

### Method under test: `playYearOfPlentyCard(GameModel model, DevelopmentCard card, Resource resource1, Resource resource2)`

This method delegates to `DevelopmentCardHandler.playYearOfPlentyCard()` using
the current player and current round from the model.

Step 1:

- Input: model, card, resource1, resource2
- Output: void (side effects in model via handler)
- Output: exception (relayed from handler)

Step 2:

- model: Pointer (mock)
- card: Pointer (mock or invalid)
- resource1: Pointer / Case
- resource2: Pointer / Case
- Output: delegation verified via mock expectations

Step 3:

- Verify controller delegates to handler with current player, card, currentRound, resource1, resource2
- Verify controller relays success and exceptions


|              | System under test                                                                         | Expected output                                                                     | Implemented? |
| ------------ | ----------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------- | ------------ |
| Test Case 15 | playYearOfPlentyCard(model, card, ORE, ORE); handler succeeds                             | verify handler called with current player, card, currentRound, ORE, ORE; no exception | :white_check_mark: |
| Test Case 16 | playYearOfPlentyCard(model, card, null, BRICK); handler throws IllegalArgumentException   | controller relays IllegalArgumentException: "Resource cannot be null."              | :white_check_mark: |
| Test Case 17 | playYearOfPlentyCard(model, null, ORE, ORE); handler throws IllegalArgumentException      | controller relays IllegalArgumentException: "Development card cannot be null."      | :white_check_mark: |


---

### Method under test: `getVictoryPointCount(GameModel model)`

This method delegates to `DevelopmentCardHandler.countVictoryPointCards()` using
the current player's hand from the model.

Step 1:

- Input: model
- Output: int (VP card count from handler)

Step 2:

- model: Pointer (mock)
- Output: Count [0, 5]

Step 3:

- Verify controller extracts current player's hand from model and delegates to handler
- Verify controller returns the int result from handler


|              | System under test                                              | Expected output                                                          | Implemented? |
| ------------ | -------------------------------------------------------------- | ------------------------------------------------------------------------ | ------------ |
| Test Case 18 | getVictoryPointCount(model); handler returns 0                 | controller returns 0; verify handler called with current player's hand   | :x:          |
| Test Case 19 | getVictoryPointCount(model); handler returns 3                 | controller returns 3; verify handler called with current player's hand   | :x:          |
| Test Case 20 | getVictoryPointCount(model); handler returns 5 (maximum)       | controller returns 5; verify handler called                              | :x:          |
