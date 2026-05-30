#!/bin/bash

echo "=========================================="
echo "  Running ALL Tests (Clean Build)"
echo "=========================================="
echo ""

# Run tests quietly
./gradlew clean test --console=plain 2>&1 | grep -E "(BUILD|tests? (completed|failed))" | grep -v "^Download"

TEST_EXIT_CODE=$?

# Color codes
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
BOLD='\033[1m'
NC='\033[0m'

# Check if test results exist
TEST_RESULTS_DIR="build/test-results/test"
if [ ! -d "$TEST_RESULTS_DIR" ]; then
    echo -e "${RED}✗ No test results found${NC}"
    exit 1
fi

echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}${BOLD}  TEST RESULTS${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""

# Counters
TOTAL=0
PASSED=0
FAILED=0

# Process each test result XML file
for xml_file in "$TEST_RESULTS_DIR"/*.xml; do
    [ -f "$xml_file" ] || continue

    # Get class name from testsuite (get first name attribute only)
    CLASS=$(grep '<testsuite' "$xml_file" | sed -n 's/.*<testsuite name="\([^"]*\)".*/\1/p')

    # Process each test case
    while IFS= read -r testline; do
        TOTAL=$((TOTAL + 1))

        # Extract test name (get first name attribute from testcase tag)
        TEST=$(echo "$testline" | sed -n 's/.*<testcase name="\([^"]*\)".*/\1/p')

        # Check if it's a self-closing tag (passed) or has children (might have failed)
        if echo "$testline" | grep -q '/>'; then
            # Passed test
            PASSED=$((PASSED + 1))
            echo -e "${GREEN}✓${NC} ${CLASS}.${TEST}"
        else
            # Check for failure in the full testcase block
            TESTCASE_BLOCK=$(sed -n "/<testcase.*name=\"${TEST}\"/,/<\/testcase>/p" "$xml_file")

            if echo "$TESTCASE_BLOCK" | grep -q '<failure'; then
                # Failed test
                FAILED=$((FAILED + 1))

                # Extract failure details
                FAILURE_TYPE=$(echo "$TESTCASE_BLOCK" | grep '<failure' | sed -n 's/.*type="\([^"]*\)".*/\1/p')
                FAILURE_MSG=$(echo "$TESTCASE_BLOCK" | grep '<failure' | sed -n 's/.*message="\([^"]*\)".*/\1/p')

                # Extract first stack trace line with line number
                STACK_LINE=$(echo "$TESTCASE_BLOCK" | grep -o 'at [^(]*([^)]*:[0-9]*)' | head -1)

                echo -e "${RED}✗${NC} ${CLASS}.${TEST}"
                echo -e "   ${YELLOW}Type:${NC}     ${FAILURE_TYPE}"
                [ -n "$FAILURE_MSG" ] && echo -e "   ${YELLOW}Message:${NC}  ${FAILURE_MSG}"
                [ -n "$STACK_LINE" ] && echo -e "   ${YELLOW}At:${NC}       ${STACK_LINE}"
                echo ""
            else
                # Passed test (has children but no failure)
                PASSED=$((PASSED + 1))
                echo -e "${GREEN}✓${NC} ${CLASS}.${TEST}"
            fi
        fi
    done < <(grep '<testcase' "$xml_file")
done

# Summary
echo ""
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo -e "${BLUE}${BOLD}  SUMMARY${NC}"
echo -e "${BLUE}━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━${NC}"
echo ""
printf "%-15s %s\n" "Total Tests:" "$TOTAL"
printf "%-15s ${GREEN}%s${NC}\n" "Passed:" "$PASSED"
printf "%-15s ${RED}%s${NC}\n" "Failed:" "$FAILED"
echo ""

# Final status
if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}${BOLD}✓ All tests passed!${NC}"
    echo ""
    exit 0
else
    echo -e "${RED}${BOLD}✗ ${FAILED} test(s) failed${NC}"
    echo ""
    echo "View detailed report:"
    echo "  file://$(pwd)/build/reports/tests/test/index.html"
    echo ""
    exit 1
fi
