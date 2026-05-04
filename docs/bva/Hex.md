### Method under test: `addPlayerSettlementToHex(String playerName)`

Step 1:
- Input: playerColor
- Input: state of the list
- Input: values of the list
- Input: size of totalBuildingsOnHex
- Output: state of the list
- Output: exception

Step 2:
- playerName - Enums/Cases
- State - collection
- Values - Enums/Cases
- Exception - too large
- totalBuildingsOnHex - interval

Step 3:
- Input: All four possibilities, an impossible case
- Input: empty collection, contains just one element, contains more than one element, duplicate elements,
- max possible size
- Input: All four possibilities, an impossible case
- Input: 0, 3, -1 (not feasible), 4 (not feasible)
- Output: empty collection, contains just one element, contains more than one element, duplicate elements,
- max possible size
- Output: "Already three settlements on hex.", "Adding invalid player name to Hex."


|             | System under test                       | Expected output                              | Implemented?       |
|-------------|-----------------------------------------|----------------------------------------------|--------------------|
| Test Case 1 | RED into empty list                     | list with size 1, red player                 | :white_check_mark: |
| Test Case 2 | add BLUE, ORANGE to list                | list with size 2, blue, orange               | :white_check_mark: |
| Test Case 3 | add WHITE to ORANGE ORANGE              | list with size 3, white, orange              | :white_check_mark: |
| Test Case 4 | add to list with 3 elements             | error - "Already three buildings on hex."    | :white_check_mark: |
| Test Case 5 | add NULL to a list                      | error - "Adding invalid player name to Hex." | :white_check_mark: |
| Test Case 6 | add three cities, try to add settlement | error - "Already three buildings on hex."    | :white_check_mark: |


### Method under test: `removePlayerSettlementFromHex(String playerName)`

Step 1:
- Input: playerColor
- Input: state of the list
- Input: values of the list
- Input: size of totalBuildingsOnHex
- Output: state of the list
- Output: exception

Step 2:
- playerName - PlayerColor enum / cases
- State - collection
- Values - PlayerColor enum / cases
- Exception - too large
- totalBuildingsOnHex - interval

Step 3:
- Input: All four possibilities, an impossible case
- Input: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Input: All four possibilities, an impossible case
- Input: size of totalBuildingsOnHex is 0, max, -1 (not feasible), 4 (not feasible)
- Output: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size -> not possible
- Output: "Player does not have a building on hex."


|              | System under test                            | Expected output                                     | Implemented?       |
|--------------|----------------------------------------------|-----------------------------------------------------|--------------------|
| Test Case 7  | empty list, remove WHITE                     | error - "Player does not have a settlement on hex." | :white_check_mark: |
| Test Case 8  | remove BLUE from BLUE                        | list with size 0                                    | :white_check_mark: |
| Test Case 9  | remove RED from RED RED                      | list with size 1, red                               | :white_check_mark: |
| Test Case 10 | remove ORANGE from ORANGE WHITE RED          | list with size 2, white, red, no orange             | :white_check_mark: |
| Test Case 11 | remove WHITE from list with 3 WHITEs         | list with size 2, contains duplicates, white        | :white_check_mark: |
| Test Case 12 | remove NULL to a list                        | error - "Player does not have a settlement on hex." | :white_check_mark: |
| Test Case 13 | remove settlement from hex with three cities | error - "Player does not have a settlement on hex." | :white_check_mark: |

### Method under test: `addPlayerCityToHex(String playerName)`
Step 1:
- Input: playerNColor
- Input: state of the list
- Input: values of the list
- Input: size of totalBuildingsOnHex
- Output: state of the list
- Output: exception

Step 2:
- playerName - enum / cases
- State - collection
- Values - enum / cases
- Exception - too large
- totalBuildingsOnHex - interval

Step 3:
- Input: all possible inputs, impossible input
- Input: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Input: all possible inputs, impossible input
- Input: size of totalBuildingsOnHex is 0, max, -1 (not feasible), 4 (not feasible)
- Output: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Output: "Already three buildings on hex.", "Adding invalid player name to Hex."


|              | System under test                                 | Expected output                              | Implemented?       |
|--------------|---------------------------------------------------|----------------------------------------------|--------------------|
| Test Case 14 | RED into empty list                               | list with size 1, red                        | :white_check_mark: |
| Test Case 15 | add BLUE, ORANGE to list                          | list with size 2, blue, orange               | :white_check_mark: |
| Test Case 16 | add WHITE to ORANGE ORANGE                        | list with size 3, white, orange              | :white_check_mark: |
| Test Case 17 | add to list with 3 elements                       | error - "Already three buildings on hex."    | :white_check_mark: |
| Test Case 18 | add NULL to a list                                | error - "Adding invalid player name to Hex." | :white_check_mark: |
| Test Case 19 | add three settlements, remove one, add two cities | error - "Already three buildings on hex."    | :white_check_mark: |


### Method under test: `awardSettlementResources()`
Step 1:
- Input: state of the list
- Input: resource
- Input: values of the list
- Output: state of the list

Step 2:
- State - collection
- Resource - cases / enums
- Values - cases / enums

Step 3:
- Input: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Input: all possible inputs, impossible inputs - not feasible
- Output: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size


|              | System under test                   | Expected output                      | Implemented?       |
|--------------|-------------------------------------|--------------------------------------|--------------------|
| Test Case 20 | empty list                          | no update                            | :white_check_mark: |
| Test Case 21 | list RED, brick                     | call to update player resources once | :white_check_mark: |
| Test Case 22 | list ORANGE, WHITE, grain           | two calls to update                  | :white_check_mark: |
| Test Case 23 | list with BLUE, BLUE, WHITE, lumber | three calls to update                | :white_check_mark: |
| Test Case 24 | list with RED, RED, RED, ore        | three calls to update                | :white_check_mark: |
| Test Case 25 | list with RED, WHITE, BLUE, wool    | three calls to update                | :white_check_mark: |
| Test Case 26 | list with ORANGE, desert            | no update                            | :white_check_mark: |

### Method under test: `awardCityResources()`
Step 1:
- Input: state of the list
- Input: resource
- Input: values of the list
- Output: state of the list

Step 2:
- State - collection
- Resource - cases / enums
- Values - cases / enums

Step 3:
- Input: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size
- Input: all possible inputs, impossible inputs - not feasible
- Output: empty collection, contains just one element, contains more than one element, duplicate elements, max possible size


|              | System under test                   | Expected output                      | Implemented?       |
|--------------|-------------------------------------|--------------------------------------|--------------------|
| Test Case 27 | empty list                          | no update                            | :white_check_mark: |
| Test Case 28 | list RED, brick                     | call to update player resources once | :white_check_mark: |
| Test Case 29 | list ORANGE, WHITE, grain           | two calls to update                  | :white_check_mark: |
| Test Case 30 | list with BLUE, BLUE, WHITE, lumber | three calls to update                | :white_check_mark: |
| Test Case 31 | list with RED, RED, RED, ore        | three calls to update                | :white_check_mark: |
| Test Case 32 | list with RED, WHITE, BLUE, wool    | three calls to update                | :white_check_mark: |
| Test Case 33 | list with ORANGE, BLUE on desert    | no update                            | :white_check_mark: |
