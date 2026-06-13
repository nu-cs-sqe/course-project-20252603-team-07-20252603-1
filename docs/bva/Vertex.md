# Vertex BVA

`Vertex` is a package-private board node that tracks occupancy and distance-rule violations for settlement placement.

---

### Method under test: `Vertex()` constructor

|             | System under test | Expected output           | Implemented?       |
|-------------|-------------------|---------------------------|--------------------|
| Test Case 1 | new Vertex()      | non-null instance created | :white_check_mark: |

---

### Method under test: `isOccupied()`

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 2 | fresh Vertex      | false           | :white_check_mark: |

---

### Method under test: `hasAdjacentSettlementViolatingDistanceRule()`

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 3 | fresh Vertex      | false           | :white_check_mark: |
