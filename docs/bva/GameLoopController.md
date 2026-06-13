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

|             | State of the System                                   | Expected output                                          | Implemented?       |
|-------------|-------------------------------------------------------|----------------------------------------------------------|--------------------|
| Test Case 4 | roller.rollTwoDice() returns 8                        | model.performTurn(8) called; method returns 8            | :white_check_mark: |


### Method under test: `offerTrade(model, offer)`

Delegates directly to `model.offerTrade(offer)`.

|             | State of the System | Expected output            | Implemented?       |
|-------------|---------------------|----------------------------|--------------------|
| Test Case 5 | In trade phase      | model.offerTrade is called | :white_check_mark: |

### Method under test: `acceptTrade(model, offer, acceptingPlayer)`

Delegates directly to `model.acceptTrade(offer, acceptingPlayer)`.

|             | State of the System | Expected output             | Implemented?       |
|-------------|---------------------|-----------------------------|--------------------|
| Test Case 6 | In trade phase      | model.acceptTrade is called | :white_check_mark: |

### Method under test: `clearOffers(model)`

Delegates directly to `model.clearOffers()`.

|             | State of the System | Expected output             | Implemented?       |
|-------------|---------------------|-----------------------------|--------------------|
| Test Case 5 | In trade phase      | model.clearOffers is called | :white_check_mark: |

### Method under test: `attemptPortTrade(model, port, giving, receiving)`

Delegates directly to `model.attemptPortTrade(port, giving, receiving)`.

|             | State of the System   | Expected output                  | Implemented?       |
|-------------|-----------------------|----------------------------------|--------------------|
| Test Case 5 | In general play phase | model.attemptPortTrade is called | :white_check_mark: |


---

### Method under test: `buyDevCard(GameModel model, DevelopmentCardDeck deck, DevelopmentCardHandler handler)`

Calls `model.getCurrentPlayer()` and `model.getCurrentRound()`, then delegates to
`handler.buyDevelopmentCard(currentPlayer, deck, currentRound)` and returns the result.
All precondition enforcement (resource checks, deck-empty check) lives in the handler;
this controller only extracts the player/round from the model and relays whatever the
handler returns or throws.

Step 1:

- Input: model, deck, handler
- State: current player's resource holdings (ORE, WOOL, GRAIN); deck size
- Output: DevelopmentCard returned from handler; or exception relayed

Step 2:

- model: Pointer (mock)
- deck: Pointer (mock)
- handler: Pointer (mock)
- Output: delegation result or relayed exception

Step 3:

- Verify controller calls model.getCurrentPlayer() and model.getCurrentRound() and passes them to handler
- Verify controller returns the DevelopmentCard that handler returns
- Verify controller relays InsufficientResourcesException when handler throws it (buyer lacks ORE, WOOL, or GRAIN)
- Verify controller relays EmptyDeckException when handler throws it (deck is empty)


|             | System under test                                                                                   | Expected output                                                                                                   | Implemented? |
| ----------- | --------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------- | ------------ |
| Test Case 5 | buyDevCard(model, deck, handler); handler returns a DevelopmentCard                                 | controller returns the same DevelopmentCard; verify handler called with currentPlayer, deck, and currentRound     | :white_check_mark: |
| Test Case 6 | buyDevCard(model, deck, handler); handler throws InsufficientResourcesException (buyer lacks resources) | controller relays InsufficientResourcesException to caller                                                    | :white_check_mark: |
| Test Case 7 | buyDevCard(model, deck, handler); handler throws EmptyDeckException (deck is empty)                 | controller relays EmptyDeckException to caller                                                                    | :white_check_mark: |

---

### Method under test: `playDevCard(GameModel model, DevelopmentCard card)`

Thin delegation only. Delegates to `model.playDevCard(card)` and relays whatever
the model returns or throws. No game-rule logic lives here.

BVA focuses on correct delegation and transparent exception relay.

Step 1:

- Input: model, card
- State: delegated to model
- Output: void on success; exception relayed unchanged

Step 2:

- model: Pointer (mock)
- card: Pointer (mock)
- Output: delegation verified or relayed exception

Step 3:

- Verify controller calls `model.playDevCard(card)`
- Verify controller relays `IllegalGamePhaseException` when model throws it (wrong phase)
- Verify controller relays `IllegalArgumentException` when model throws it (null card)

|              | System under test                                                                    | Expected output                                                  | Implemented? |
|--------------|--------------------------------------------------------------------------------------|------------------------------------------------------------------|--------------|
| Test Case 8  | playDevCard(model, card); model completes normally                                   | model.playDevCard(card) called once; no exception                | :white_check_mark: |
| Test Case 9  | playDevCard(model, card); model throws IllegalGamePhaseException (wrong phase)       | IllegalGamePhaseException relayed to caller                      | :white_check_mark: |
| Test Case 10 | playDevCard(model, card); model throws IllegalArgumentException (null card)          | IllegalArgumentException relayed to caller                       | :white_check_mark: |

---

### Method under test: `getResourceCount(GameModel model, PlayerColor color, Resource resource)`

Delegates to `model.getArbitraryPlayer(color).getResourceCount(resource)`.

|              | System under test                                              | Expected output                            | Implemented?       |
|--------------|----------------------------------------------------------------|--------------------------------------------|--------------------|
| Test Case 11 | model.getArbitraryPlayer(RED) returns mockPlayer; mockPlayer.getResourceCount(ORE) returns 5 | controller returns 5 | :x: |
