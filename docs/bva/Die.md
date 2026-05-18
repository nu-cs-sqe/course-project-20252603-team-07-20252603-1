### BVA for Die Class

'Die' is the class responsible for generating a random number
from a range of values, imitating the use of a die.

Note: Catan always uses two dice numbered from 1 through 6, so for our purposes we will hardcode those values. We know that these requirements will not change.

### Method under test: `rollOneDie()`

Step 1:
- Input: randomizer
- Output: number 1-6

Step 2:
- Randomizer - Java's random number generator
- Roll - interval

Step 3:
- Input: Java's random number generator is a trusted library, no variation/validation needed
- Output: 1, 6, 0 (not feasible), 7 (not feasible)


|             | System under test                    | Expected output | Implemented?       |
|-------------|--------------------------------------|-----------------|--------------------|
| Test Case 1 | Initialize new die, random returns 0 | Die roll is 1   | :white_check_mark: |
| Test Case 2 | Initialize new die, random returns 5 | Die roll is 6   | x                  |