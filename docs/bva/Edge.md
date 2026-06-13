# Edge BVA

`Edge` is a package-private board edge that tracks occupancy and connectivity to a player's road network.

---

### Method under test: `Edge()` constructor

|             | System under test | Expected output           | Implemented?       |
|-------------|-------------------|---------------------------|--------------------|
| Test Case 1 | new Edge()        | non-null instance created | :white_check_mark: |

---

### Method under test: `isOccupied()`

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 2 | fresh Edge        | false           | :white_check_mark: |

---

### Method under test: `isConnectedToPlayerNetwork()`

|             | System under test | Expected output | Implemented?       |
|-------------|-------------------|-----------------|--------------------|
| Test Case 3 | fresh Edge        | false           | :x: |
