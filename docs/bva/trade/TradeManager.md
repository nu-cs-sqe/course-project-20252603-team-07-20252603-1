# TradeManager BVA

`TradeManager` is a game-level coordinator that maintains the list of active
player-to-player trade offers. It does not handle counteroffers and does not
handle bank/port trades.

Supporting types (each documented in its own BVA file):

- `ResourceQuantity` — validated dataclass `(Resource, int)`. See
  `ResourceQuantity.md`.
- `TradeOffer` — validated record `(Player offeringPlayer,
  ResourceQuantity giving, ResourceQuantity receiving)`. See `TradeOffer.md`.
  Because `TradeOffer` is validated at construction, `TradeManager` may
  assume any `TradeOffer` it receives is well-formed (distinct resources).

`TradeOffer` instances are passed to `TradeManager` and returned from
`listTrades()` by reference, so `acceptTrade(...)` matches by identity.

---

### Method under test: `offerTrade(TradeOffer offer)`

Step 1:
- Input: offer
- Input: state of the trade list
- Output: state of the trade list

Step 2:
- offer - TradeOffer (object reference)
- State - collection

Step 3:
- Input: non-null TradeOffer
- Input: empty collection, contains just one element, contains more than one
element, duplicate elements
- Output: empty collection, contains just one element, contains more than one
element, duplicate elements

|             | System under test                                                              | Expected output                | Implemented? |
|-------------|--------------------------------------------------------------------------------|--------------------------------|--------------|
| Test Case 1 | RED's offer of (1 BRICK) for (1 WOOL) into empty list                          | list with size 1               | :x:          |
| Test Case 2 | BLUE's offer of (2 ORE) for (1 GRAIN) into list of size 1                      | list with size 2               | :x:          |
| Test Case 3 | RED posts the same offer twice (duplicate offering player and quantities)      | list with size 2, duplicate ok | :x:          |

---

### Method under test: `clearOffers()`

Step 1:
- Input: state of the trade list
- Output: state of the trade list

Step 2:
- State - collection for both

Step 3:
- Input: empty collection, contains just one element, contains more than one
element, duplicate elements
- Output: empty collection

|             | System under test                                       | Expected output | Implemented? |
|-------------|---------------------------------------------------------|-----------------|--------------|
| Test Case 4 | clear an empty list                                     | empty list      | :x:          |
| Test Case 5 | clear a list with one offer                             | empty list      | :x:          |
| Test Case 6 | clear a list with three offers from different players   | empty list      | :x:          |
| Test Case 7 | clear a list containing duplicate offers                | empty list      | :x:          |

---

### Method under test: `listTrades()`

Step 1:
- Input: state of the trade list
- Output: list of trade offers
- Output: state of the trade list (unchanged — read-only, no side effects)

Step 2:
- State - collection

Step 3:
- Input: empty collection, contains just one element, contains more than one
element, duplicate elements
- Output: returned list - empty collection, contains just one element,
contains more than one element, duplicate elements
- Output: trade list state - unchanged across all cases

|              | System under test                                | Expected output                                                       | Implemented? |
|--------------|--------------------------------------------------|-----------------------------------------------------------------------|--------------|
| Test Case 8  | list trades from empty TradeManager              | empty list returned; trade list unchanged                             | :x:          |
| Test Case 9  | list trades after one offer                      | list with size 1, contains that offer; trade list unchanged           | :x:          |
| Test Case 10 | list trades after three offers                   | list with size 3, in insertion order; trade list unchanged            | :x:          |
| Test Case 11 | list trades when two duplicate offers were made  | list with size 2, contains duplicates; trade list unchanged           | :x:          |

---

### Method under test: `acceptTrade(TradeOffer offer, Player acceptingPlayer)`

Step 1:
- Input: offer (passed by reference)
- Input: acceptingPlayer
- Input: state of the trade list
- Input: offering player's resource holdings
- Input: accepting player's resource holdings
- Output: state of the trade list
- Output: calls to `updateResources` on both players
- Output: exception

Step 2:
- offer - TradeOffer (object reference); cases: present in the trade list,
not present in the trade list (already accepted / never added)
- acceptingPlayer - PlayerColor enum / cases; identity cases: same as the
offering player, different from the offering player
- State - collection
- Resource holdings - cases: sufficient, insufficient (on either side)
- Exception - cases: self-accept, missing offer, insufficient resources

Step 3:
- Input: offer present in list, offer not present (already accepted / never
added)
- Input: all four player colors, same player as the offerer
- Input: empty collection, contains just one element, contains more than one
element, duplicate elements
- Input: offering player has enough / not enough of the giving resource
- Input: accepting player has enough / not enough of the receiving resource
- Output: list shrinks by 1 (the accepted offer is removed); duplicates remain
unaffected
- Output: offerer loses `giving`, gains `receiving`; acceptor loses `receiving`,
gains `giving`
- Output: "Trade not found." / "A player cannot accept their own trade." /
"Offering player has insufficient resources." / "Accepting player has
insufficient resources."

|              | System under test                                                                                                       | Expected output                                                                                                            | Implemented? |
|--------------|-------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------|--------------|
| Test Case 12 | BLUE accepts RED's offer of (1 BRICK) for (1 WOOL); both have resources; list has only this offer                       | list with size 0; RED -1 BRICK +1 WOOL; BLUE +1 BRICK -1 WOOL                                                              | :x:          |
| Test Case 13 | WHITE accepts ORANGE's offer of (2 ORE) for (1 GRAIN); both have resources; list has three offers                       | list with size 2; ORANGE -2 ORE +1 GRAIN; WHITE +2 ORE -1 GRAIN; only this offer removed                                   | :x:          |
| Test Case 14 | BLUE accepts one of two duplicate offers from RED; both have resources                                                  | list with size 1; only one duplicate removed; resources updated once                                                       | :x:          |
| Test Case 15 | RED accepts RED's own offer                                                                                             | IllegalArgumentException: "A player cannot accept their own trade."; list unchanged; no resource updates                   | :x:          |
| Test Case 16 | BLUE accepts a TradeOffer reference that is not in the list (already accepted earlier)                                  | IllegalArgumentException: "Trade not found."; list unchanged                                                               | :x:          |
| Test Case 17 | BLUE accepts RED's offer of (5 BRICK) for (1 WOOL) but RED has 0 BRICK                                                  | IllegalStateException: "Offering player has insufficient resources."; list unchanged; no resource updates                  | :x:          |
| Test Case 18 | BLUE accepts RED's offer of (1 BRICK) for (5 WOOL) but BLUE has 0 WOOL                                                  | IllegalStateException: "Accepting player has insufficient resources."; list unchanged; no resource updates                 | :x:          |
