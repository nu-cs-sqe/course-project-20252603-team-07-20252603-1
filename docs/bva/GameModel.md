
// Methods Under Spencer's responsibility

### Method under test: `attemptBuildSettlement()`

#### Inputs:
- PlayerColor
- Cases
    - BoardHandler check succeed -> (if node is adjacent to already built node, if player owns adjacent road)
    - Player has enough resources or not
    - Player has 5 or less settlements, or more
    - Game is in proper phase or not

#### Outputs:
- Board Updated
- PlayerState updated with less resources
- Resource Deck Replenished
- Settlement count increases on player
- Victory Point awarded
- Error -> InsufficientResourcesException, IllegalSettlementPlacementException, IllegalGamePhaseException

|             | State of the System                                                     | Expected output                                             | Implemented?       |
|-------------|-------------------------------------------------------------------------|-------------------------------------------------------------|--------------------|
| Test Case 1 | Red claimes node, BoardHandler succeeds, has enough resources           | Success                                                     | :white_check_mark: |
| Test Case 2 | White claims node, Boardhandler fails, has enough resources             | IllegalSettlementPlacementException , resources not reduced | :white_check_mark: |
| Test Case 3 | Orange claims node, BoardHandler succeeds, not enough resources         | InsufficientResourcesException, board not updated           | :white_check_mark: |
| Test Case 4 | Red claims node, BoardHandler succeeds, enough resources, 5 settlements | IllegalSettlementPlacementException, no updates             | :white_check_mark: |
| Test Case 5 | Blue claims node, BoardHandler succeeds, enough resurces, 4 settlements | Success                                                     | :white_check_mark: |
| Test Case 6 | Incorrect Game Phase                                                    | IllegalGamePhaseException                                   | :white_check_mark: |


### Method under test: `attemptBuildRoad()`

#### Inputs:
- PlayerColor
- Cases
  - BoardHandler check succeed
  - Player has enough resources or not

#### Outputs:
- Board Updated
- PlayerState updated with less resources
- Resource Deck Replenished
- Victory Point awarded
- Error -> InsufficientResourcesException, IllegalRoadPlacementException, IllegalGamePhaseException

|             | State of the System                                             | Expected output                | Implemented?       |
|-------------|-----------------------------------------------------------------|--------------------------------|--------------------|
| Test Case 1 | Red claimes edge, BoardHandler succeeds, has enough resources   | Success                        | :white_check_mark: |
| Test Case 2 | White claims edge, Boardhandler fails, has enough resources     | IllegalRoadPlacementException  | :white_check_mark: |
| Test Case 3 | Orange claims edge, BoardHandler succeeds, not enough resources | InsufficientResourcesException | :white_check_mark: |
| Test Case 4 | Blue attempts, Improper Game Phase                              | IllegalGamePhaseException      | :white_check_mark: |

### Method under test: `attemptBuildCity()`


#### Inputs:
- PlayerColor
- Cases
  - BoardHandler check succeed
  - Player has enough resources or not

#### Outputs:
- Board Updated
- PlayerState updated with less resources
- Resource Deck Replenished
- Victory Point awarded
- Error -> InsufficientResourcesException, IllegalCityPlacementException, IllegalGamePhaseException

|             | State of the System                                          | Expected output                   | Implemented?       |
|-------------|--------------------------------------------------------------|-----------------------------------|--------------------|
| Test Case 1 | Red builds city, BoardHandler succeeds, has enough resources | Success                           | :white_check_mark: |
| Test Case 2 | White builds city, not enough resources (ore)                | InsufficientResourcesException    | :white_check_mark: |
| Test Case 3 | White builds city, not enough resources (grain)              | InsufficientResourcesException    | :x:                |
| Test Case 4 | Orange builds, BoardHandler fails                            | IllegalCityPlacementException     | :x:                |
| Test Case 5 | Blue attempts, Improper Game Phase                           | IllegalGamePhaseException         | :x:                |

