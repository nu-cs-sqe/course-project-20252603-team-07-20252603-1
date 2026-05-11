# Week 6 Report (05/4/2026 - 5/10/2026)

## Planning
Still making progress on game setup, with views and models being created to have something to actually look at.

## Work
1. [done] Ben: Hex Class https://github.com/nu-cs-sqe/course-project-20252603-team-07-20252603-1/pull/23
- Reviewed Connor's and Kevin's PRs, gave feedback for both
- Updated Hex class based off of professor feedback
2. [??% done] Spencer: 
3. [??% done] Connor: 
4. [??% done] Kevin: 
5. [done] Theo: Game Round Mechanics https://github.com/nu-cs-sqe/course-project-20252603-team-07-20252603-1/pull/41
- Built entire turn management system test-first (8 TDD cycles, 15 commits)
- GameModel tracks turn order with automatic player wraparound
- Added DiceRoller abstraction for deterministic testing
- PlayerState now holds resource inventory separate from Player identity
- performTurn() orchestrates dice rolling and resource distribution
- All tests passing, zero regression 


## Goals
1. Ben: Begin work on dice, settlement, city, and road classes
2. Spencer: 
3. Connor: 
4. Kevin: 
5. Theo: Connect turn mechanics to board state (hex production, settlements) 
