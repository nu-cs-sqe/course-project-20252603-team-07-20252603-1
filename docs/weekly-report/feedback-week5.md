# Week 5 Project Feedback by PM/TA

**Dedicated PM/TA**: Vandan Agrawal (vandanagrawal@u.northwestern.edu)

## How to Read This Feedback
> [!NOTE]
> **Purpose.** This feedback focuses on your team's progress and collaboration. It is meant as guidance, not judgement.

> [!IMPORTANT]
> **Scope.** For the BVA and TDD items, the PM/TA evaluates only the `main` branch. Ongoing work in feature branches will be evaluated after it is merged. If you'd like early feedback on work in progress, please reach out to your PM/TA directly.

> [!TIP]
> **Mistakes are expected :).** As the instructor mentioned in class, early mistakes are part of the learning process. As long as your team addresses the issues after you get the feedback, your grade will not suffer from them.

### Evaluation basis (for this pass)
This review uses both the local repository snapshot (`main` contents only) and public GitHub REST API data (repo metadata, branches, open/closed PRs, and public PR/issue comments). Some protected GitHub endpoints (for detailed branch protection policy) still require authentication and remain only partially verifiable.

## Checklist
Status:
- ✅: All done/Good job!
- ⚠️: Attention needed
- ❌: Significant issue found
- ➖: No basis to evaluate

### Past Feedback
| # | Item                                                                                                 | Status | Reviewer Notes | Source Instructions or Resources |
|---|------------------------------------------------------------------------------------------------------|:------:|----------------|----------------------------------|
| 0 | The team has closed and merged the past Feedback PR(s), indicating that they have read the feedbacl. | ✅ | Verified via GitHub API: PR `#26` ("Feedback week4") is closed and merged (`merged_at` present). |                                  |

### Software Process Quality
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1 | Each active feature branch has an open draft PR against main.                                                                                                | ❌        | Multiple non-`main` branches exist, but only one open PR is currently draft (`graph-wip` -> PR `#24`). Several open PRs are not draft (`#35`, `#34`, `#33`, `#29`, `#28`), and some branches do not have an open PR mapped in current open-PR list. | Week 4 Wednesday Lecture (Lecture 08)                                             |
| 2 | The team has a “definition of done” (BVA) fully documented for the part of the system that is done. (needed for Letter Grade D)                              | ⚠️        | Improvement from week 4: `docs/bva/Hex.md` is detailed and includes 42 test cases mapped to methods. However, only `Hex` appears documented; other implemented domain classes (`Player`, enums) do not yet have corresponding BVA artifacts. | Project grading rubrics                                                           |
| 3 | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code. (needed for Letter Grade C)                                       | ⚠️        | On `main`, there is clear test coverage for `Hex` (`src/test/java/domain/HexTests.java`, 42 tests) aligned with documented BVA. This is good evidence of TDD-like practice for that class. Still partial for “all non-UI code”: other domain code has little/no corresponding test depth yet. | Project grading rubrics                                                           |

### Planning & Progress Evaluation
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 4 | The team documents every week’s planning and progress evaluation professionally. (needed for Letter Grade B)                                                 | ✅        | `docs/weekly-reports/week5report.md` contains dated scope, concrete progress percentages per owner, PR links, and next goals. This is a clear improvement from previous weeks and meets the expected weekly planning/progress baseline. | Week 4 Wednesday Lecture (Lecture 08), Project grading rubrics                    |

### Progress & Collaboration
| # | Item                                                                                                                                                                                   |  Status   | Reviewer Notes      | Source Instructions or Resources                 |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|--------------------------------------------------|
| 5 | Overall development progress (recall the recommended order is: Game Setup Phase -> One turn of the game -> Multiple turns -> One win condition -> Other win conditions (if applicable) | ⚠️        | The team is progressing in the recommended order: game-setup-related classes (`Hex`, `Player`, `Resource`, `PlayerColor`) and substantial `Hex` testing are now on `main`. However, there is still limited merged gameplay flow beyond setup (turn loop / win condition not yet visible on `main`). | Canvas assignment Project: Week 4 and 5 Guidance |
| 6 | Collaboration: Quality of discussion in PR reviews and work item comments on the board.                                                                                                | ⚠️        | Public PR comments show substantive technical discussion (BVA edge cases, testability concerns, naming/style nits), which is a good sign for review culture. Board-comment quality is still not fully verified from API data used here. |                                                  |

### The following items are not checked by the reviewer as they were checked in the previous weeks
But if your team wants the reviewer to check any of these for any reasons, please contact them or the instructor via either email or tagging them in the feedback PR.

| #   | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1   | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval. (needed for Letter Grade C) | ➖        | Not checked this week since it was already reviewed in week 4. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 2   | Continuous Integration (CI) is fully set up from the beginning. (needed for Letter Grade B)                                                                  | ➖        | Not checked this week since it was already reviewed in week 4. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 3   | The team uses the project management board steadily and frequently, and the description of each task is detailed. (needed for Letter Grade B)                | ➖ | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.1 | Every functionality-related work item on the management board includes a user story, and optionally one or more use cases.                                   | ➖        | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.2 | The design is documented somewhere, either in the work item description, or in a separate design document.                                                   | ➖        | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.3 | Task assignments are documented clearly in the management board.                                                                                             | ➖        | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |

## Additional Comments
- Strong improvement from week 4 to week 5 on `main`: concrete domain implementation and substantial automated testing are now visible.
- `docs/bva/Hex.md` and `HexTests` are well-aligned and show careful boundary/case thinking for one class.
- Remaining priority: add design documentation beyond placeholder text, and continue extending BVA + tests across newly merged non-UI classes/features as they land.

