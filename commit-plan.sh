#!/usr/bin/env bash
# Commit plan for feature/interactive-views
# Run from the repo root: ./commit-plan.sh
# Each commit is layered so every tree compiles on its own.
# NOTE: the untracked scripts/ directory is intentionally left out.

set -euo pipefail

# 1. Domain model — accessors + gap fixes
git add src/main/java/domain/model/GameModel.java \
        src/main/java/domain/model/board/BoardHandler.java \
        src/main/java/domain/model/board/BoardGraph.java \
        src/main/java/domain/model/board/BoardGraphController.java \
        src/main/java/domain/model/board/Port.java \
        src/test/java/domain/model/GameModelTests.java \
        src/test/java/domain/model/board/BoardHandlerTests.java

git commit -m "add board/port accessors, implement moveRobberAndSteal, setup phase entry/exit, and round counter"

# 2. Controller pass-throughs
git add src/main/java/ui/controller/GameLoopController.java \
        src/test/java/ui/controller/GameLoopControllerTest.java

git commit -m "add GameLoopController pass-throughs for build, robber, setup, and port queries"

# 3. Board rendering package (+ geometry tests)
git add src/main/java/ui/view/board/ src/test/java/ui/view/

git commit -m "add interactive hex board view with scaled rendering, port markers, and pick modes"

# 4. Dialogs + DevCardController wiring (Main also carries the 960px window width)
git add src/main/java/ui/view/DialogSupport.java \
        src/main/java/ui/view/DevCardDialog.java \
        src/main/java/ui/view/ResourcePickDialog.java \
        src/main/java/ui/view/VictimPickDialog.java \
        src/main/java/ui/view/TradeComposeDialog.java \
        src/main/java/ui/view/TradeRespondDialog.java \
        src/main/java/ui/view/PortTradeDialog.java \
        src/main/java/ui/ViewContext.java \
        src/main/java/ui/Main.java

git commit -m "add dev card, trade, port, and robber victim dialogs; wire DevCardController into ViewContext"

# 5. Game round overhaul — phase-driven view, setup placement, auto-roll,
#    hex summary board, i18n, CSS, and the BoardPlaceholderView deletion
git add src/main/java/ui/view/GameRoundView.java \
        src/main/java/ui/view/PlayerResourcesPanel.java \
        src/main/java/ui/view/RoundNavigator.java \
        src/main/java/ui/view/SetupSummaryView.java \
        src/main/java/ui/view/BoardPlaceholderView.java \
        src/main/java/ui/Navigator.java \
        src/main/resources/

git commit -m "phase-driven GameRoundView: setup placement, auto-roll, build/trade/devcard flows on hex board"

# Verify and push
git status
./gradlew build
git push -u origin feature/interactive-views
