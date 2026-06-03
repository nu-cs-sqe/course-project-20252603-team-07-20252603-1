
// Methods Under Spencer's responsibility

### Method under test: `attemptBuildSettlement()`

#### Inputs:
- PlayerColor
- Cases
    - BoardHandler check succeed -> (if node is adjacent to already built node, if player owns adjacent road)
    - Player has enough resources or not
    - Player has 5 or less settlements, or more

#### Outputs:
- Board Updated
- PlayerState updated with less resources
- Resource Deck Replenished
- Settlement count increases on player
- Victory Point awarded
- Error -> InsufficientResourcesException, IllegalSettlementPlacementException, MaximumAmountOfSettlementsException

|             | State of the System                                                     | Expected output                                             | Implemented?       |
|-------------|-------------------------------------------------------------------------|-------------------------------------------------------------|--------------------|
| Test Case 1 | Red claimes node, BoardHandler succeeds, has enough resources           | Success                                                     | :white_check_mark: |
| Test Case 2 | White claims node, Boardhandler fails, has enough resources             | IllegalSettlementPlacementException , resources not reduced | :white_check_mark: |
| Test Case 3 | Orange claims node, BoardHandler succeeds, not enough resources         | InsufficientResourcesException, board not updated           | :white_check_mark: |
| Test Case 4 | Red claims node, BoardHandler succeeds, enough resources, 5 settlements | IllegalSettlementPlacementException, no updates             | :white_check_mark: |
| Test Case 5 | Blue claims node, BoardHandler succeeds, enough resurces, 4 settlements | Success                                                     | :white_check_mark: |
