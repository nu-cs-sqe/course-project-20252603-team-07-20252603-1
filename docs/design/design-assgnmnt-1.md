User Story 1:
As a player of Catan, I want the gameboard to be properly set up automatically, with number tiles and biome tiles distributed appropriately and evenly, so that I can begin playing without manually organizing the board. I want the deck of Development Cards to be shuffled for me so that I do not have to manually organize the cards. 
I want to be able to trigger a roll of the die to be assigned my rank in the order

Acceptance Criteria
✅ The game must not start unless there are 3 to 4 players.
✅ The game board after activating the game is set up in a valid game state* with all 19 game tiles
* i.e. no two resource type tiles or powerful number tiles are too close together
This can be accomplished by using the out-of-the-box layout
The game must not start if there are two players with identical names. 
✅ Each player must be granted an opportunity to roll the die at least once, with winning ties being settled by re-roll
✅ the deck of development cards must be initialized in a randomized order
✅ Upon completion of the dice rolls, the order of play must be dictated by highest-to-lowest roll, with winning ties being resolved randomly
✅Users must be initialized with 0 victory points




User Story 2:
As a player of Catan, I want to be able to place my first two settlements and roads as the first turn of play.
I want each other player to perform the same action in succession.
Acceptance Criteria
✅ The order of play must begin in the determined order from the roll of the dice.
✅ The player is able to place only one settlement and one road when it is their turn. 
✅ After each player has taken one turn, the order should then reverse so that the last player is the first to place their second settlement and road.
This must be the only time the order gets reversed throughout the game
✅ The player is allowed to place their settlements at any of the intersections of tiles, given no settlement is within 1 position away
✅Each road must be placed next to the settlement. 
✅ Players receive the materials of the second settlement that they place. 
