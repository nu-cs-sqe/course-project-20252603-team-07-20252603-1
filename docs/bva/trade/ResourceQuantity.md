# ResourceQuantity BVA

`ResourceQuantity` is a dataclass holding a `Resource` and an `int` quantity.
It is constructed only through its public constructor, which validates both
fields. `DESERT` is not tradeable and is rejected here.

---

### Method under test: `ResourceQuantity(Resource resource, int quantity)`

Step 1:
- Input: resource
- Input: quantity
- Output: constructed object
- Output: exception

Step 2:
- resource - Resource enum / cases (DESERT is not tradeable)
- quantity - integer interval [1, Integer.MAX_VALUE]. Note: `HIGH + 1` is
not representable as a Java `int`, so the upper-bound-exceeded BVA case is
not applicable.
- Exception - cases: invalid resource, out-of-range quantity

Step 3:
- Input: all five tradeable resources (BRICK, GRAIN, LUMBER, ORE, WOOL),
DESERT
- Input: lower boundary (1), upper boundary (Integer.MAX_VALUE), just below
lower boundary (0). Upper-boundary-exceeded is not representable in `int`.
- Output: valid object
- Output: "Resource must be tradeable." / "Quantity must be at least 1."

|             | System under test       | Expected output                                              | Implemented? |
|-------------|-------------------------|--------------------------------------------------------------|--------------|
| Test Case 1 | BRICK, 1                | valid object                                                 | :x:          |
| Test Case 2 | WOOL, Integer.MAX_VALUE | valid object                                                 | :x:          |
| Test Case 3 | GRAIN, 0                | IllegalArgumentException: "Quantity must be at least 1."     | :x:          |
| Test Case 4 | DESERT, 1               | IllegalArgumentException: "Resource must be tradeable."      | :x:          |
