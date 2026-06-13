#!/usr/bin/env bash
# Usage: ./scripts/commit-test.sh <TestName>
# Runs the full test suite. If green, stages changes under src/ and commits
# with message "<TestName> passes".

set -euo pipefail

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <TestName>" >&2
    echo "Example: $0 Construct_BrickAtLowerBoundary_ExpectValid" >&2
    exit 1
fi

TEST_NAME="$1"
REPO_ROOT="$(git rev-parse --show-toplevel)"
cd "$REPO_ROOT"

./gradlew test

git add src
git commit -m "${TEST_NAME} passes"
