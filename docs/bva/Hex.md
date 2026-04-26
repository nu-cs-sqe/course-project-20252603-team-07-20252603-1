### Method under test: `addPlayerSettlementToHex(String playerName)`

Step 1:
Input: playerColor
Input: state of the list
Input: values of the list
Output: state of the list
Output: exception

Step 2:
playerName - Enums/Cases
State - collection
Values - Enums/Cases
Exception - too large

Step 3:
Input: All four possibilities, an impossible case
Input: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size
Input: All four possibilities, an impossible case
Output: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size
Output: "Already three settlements on hex."


|             | System under test           | Expected output  | Implemented?       |
|-------------|-----------------------------|------------------|--------------------|
| Test Case 1 | RED into empty list         | list with size 1 | :white_check_mark: |
| Test Case 2 | add BLUE, ORANGE to list    | list with size 2 | :white_check_mark: |
| Test Case 3 | add WHITE to ORANGE ORANGE  | list with size 3 | :white_check_mark: |
| Test Case 4 | add to list with 3 elements | error            | :white_check_mark: |
| Test Case 5 | add NULL to a list          | error            | x                  |


### Method under test: `removePlayerSettlementFromHex(String playerName)`

Step 1:
Input: playerColor
Input: state of the list
Input: values of the list
Output: state of the list
Output: exception

Step 2:
playerName - PlayerColor enum / cases
State - collection
Values - PlayerColor enum / cases
Exception - too large

Step 3:
Input: All four possibilities, an impossible case
Input: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size
Input: All four possibilities, an impossible case
Output: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size -> not possible
Output: "Player does not have a building on hex."


|              | System under test                    | Expected output                       | Implemented?       |
|--------------|--------------------------------------|---------------------------------------|--------------------|
| Test Case 6  | empty list, remove WHITE             | error                                 | :white_check_mark: |
| Test Case 7  | remove BLUE from BLUE                | list with size 0                      | :white_check_mark: |
| Test Case 8  | remove RED from RED RED              | list with size 1                      | :white_check_mark: |
| Test Case 9  | remove ORANGE from ORANGE WHITE RED  | list with size 2                      | :white_check_mark: |
| Test Case 10 | remove WHITE from list with 3 WHITEs | list with size 2, contains duplicates | :white_check_mark: |
| Test Case 11 | add NULL to a list                   | error                                 | x                  |

### Method under test: `addPlayerCityToHex(String playerName)`
Step 1:
Input: playerName String
Input: state of the list
Input: values of the list
Output: state of the list
Output: exception

Step 2:
playerName - String
State - collection
Values - Strings
Exception - too large

Step 3:
Input: empty string
Input: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size
Input: empty strings
Output: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size
Output: "Already three settlements on hex."


|              | System under test                        | Expected output  | Implemented? |
|--------------|------------------------------------------|------------------|--------------|
| Test Case 12 | empty string into empty list             | list with size 1 | x            |
| Test Case 13 | add list with 1 element, an empty string | list with size 2 | x            |
| Test Case 14 | add to list with duplicates              | list with size 3 | x            |
| Test Case 15 | add to list with 3 elements              | error            | x            |

### Method under test: `awardSettlementResources()`
Step 1:
Input: state of the list
Input: values of the list
Output: state of the list

Step 2:
State - collection
Values - Strings

Step 3:
Input: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size
Input: empty strings
Output: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size


|              | System under test                     | Expected output                      | Implemented? |
|--------------|---------------------------------------|--------------------------------------|--------------|
| Test Case 16 | empty list                            | no update                            | x            |
| Test Case 17 | list with 1 element, an empty string  | call to update player resources once | x            |
| Test Case 18 | list with 2 elements                  | two calls to update                  | x            |
| Test Case 19 | list with 3 elements, with duplicates | three calls to update                | x            |

### Method under test: `awardCityResources()`
Step 1:
Input: state of the list
Input: values of the list
Output: state of the list

Step 2:
State - collection
Values - Strings

Step 3:
Input: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size
Input: empty strings
Output: empty collection, contains just one element, contains more than one element, duplicate elements,
max possible size


|              | System under test                     | Expected output                      | Implemented? |
|--------------|---------------------------------------|--------------------------------------|--------------|
| Test Case 20 | empty list                            | no update                            | x            |
| Test Case 21 | list with 1 element, an empty string  | call to update player resources once | x            |
| Test Case 22 | list with 2 elements                  | two calls to update                  | x            |
| Test Case 23 | list with 3 elements, with duplicates | three calls to update                | x            |
