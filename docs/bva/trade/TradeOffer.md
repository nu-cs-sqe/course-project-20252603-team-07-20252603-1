# TradeOffer BVA

`TradeOffer` is the immutable record posted by a player to the
`TradeManager`. It holds the offering `Player`, the `ResourceQuantity` being
given, and the `ResourceQuantity` being received. The constructor enforces
that the `giving` and `receiving` resources are different (you cannot trade
a resource for itself, regardless of quantities), so any `TradeOffer`
instance is valid by construction.

Each `ResourceQuantity` is itself already validated by its own constructor
(see `ResourceQuantity.md`), so this file only covers the additional
invariant enforced by `TradeOffer`.

---

### Method under test: `TradeOffer(Player offeringPlayer, ResourceQuantity giving, ResourceQuantity receiving)`

Step 1:
- Input: offeringPlayer
- Input: giving
- Input: receiving
- Output: constructed object
- Output: exception

Step 2:
- offeringPlayer - PlayerColor enum / cases
- giving - ResourceQuantity (object reference, assumed valid by its own
construction)
- receiving - ResourceQuantity (object reference); cases: same resource as
giving, different resource from giving
- Exception - cases: same resource on both sides

Step 3:
- Input: all four player colors
- Input: valid ResourceQuantity
- Input: valid ResourceQuantity (different resource), valid ResourceQuantity
(same resource as giving, same quantity), valid ResourceQuantity (same
resource as giving, different quantity)
- Output: valid object
- Output: "Cannot trade a resource for itself."

|             | System under test           | Expected output                                                       | Implemented? |
|-------------|-----------------------------|-----------------------------------------------------------------------|--------------|
| Test Case 1 | RED, (1 BRICK), (1 WOOL)    | valid object                                                          | :x:          |
| Test Case 2 | BLUE, (2 ORE), (1 GRAIN)    | valid object                                                          | :x:          |
| Test Case 3 | RED, (1 LUMBER), (1 LUMBER) | IllegalArgumentException: "Cannot trade a resource for itself."       | :x:          |
| Test Case 4 | WHITE, (2 WOOL), (3 WOOL)   | IllegalArgumentException: "Cannot trade a resource for itself."       | :x:          |
