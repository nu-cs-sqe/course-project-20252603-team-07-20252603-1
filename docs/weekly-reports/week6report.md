# Week 6 Report (05/4/2026 - 5/10/2026)

## Planning
Still making progress on game setup, with views and models being created to have something to actually look at.

## Work
1. [done] Ben: Hex Class https://github.com/nu-cs-sqe/course-project-20252603-team-07-20252603-1/pull/23
- Reviewed Connor's and Kevin's PRs, gave feedback for both
- Updated Hex class based off of professor feedback
2. [90% done] Spencer: Continued work on graph class https://github.com/nu-cs-sqe/course-project-20252603-team-07-20252603-1/pull/24
- Worked on claiming nodes and edges
- Allow building of actual graph based on board layout
3. [done] Connor: Completed Player BVA based on PlantUML diagram + implemented associated tests.
- Added spotbug + checkstyle to project
4. [done] Kevin: Initialized the first round of views based on the initialization steps, branched out from Theo's PR. 
5. [done] Theo: Game Round Mechanics https://github.com/nu-cs-sqe/course-project-20252603-team-07-20252603-1/pull/41
- Built turn management system test-first
- GameModel tracks turn order with automatic player wraparound
- Added DiceRoller abstraction for deterministic testing
- PlayerState now holds resource inventory separate from Player identity
- performTurn() orchestrates dice rolling and resource distribution
- All tests passing, zero regression 


## Goals
1. Ben: Begin work on dice, settlement, city, and road classes
2. Spencer: Finish graph model, work on controller and view for the graph
3. Connor: 
4. Kevin: Review PRs and start creating views based on the work of merged PRs
5. Theo: Connect turn mechanics to board state (hex production, settlements) 
