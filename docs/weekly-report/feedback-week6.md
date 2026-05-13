# Week 6 Project Feedback by PM/TA

**Dedicated PM/TA**: Vandan Agrawal (vandanagrawal@u.northwestern.edu)

## How to Read This Feedback
> [!NOTE]
> **Purpose.** This feedback focuses on your team's progress and collaboration. It is meant as guidance, not judgement.

> [!IMPORTANT]
> **Scope.** For items tied to implementation quality (for example static analysis), the PM/TA evaluates what is on the `main` branch unless otherwise noted. Ongoing work in feature branches will be evaluated after it is merged. If you'd like early feedback on work in progress, please reach out to your PM/TA directly.

> [!TIP]
> **Mistakes are expected :).** As the instructor mentioned in class, early mistakes are part of the learning process. As long as your team addresses the issues after you get the feedback, your grade will not suffer from them.

### Evaluation basis (for this pass)
This review uses the local workspace where it matches `main`, plus public GitHub REST API data and raw `main` files on GitHub (for example `build.gradle.kts`, `docs/weekly-reports/week6report.md`, and the repository tree). Branch protection policy details remain partially behind authenticated endpoints.

## Checklist
Status:
- ✅: All done/Good job!
- ⚠️: Attention needed
- ❌: Significant issue found
- ➖: No basis to evaluate

### Past Feedback
| # | Item                                                                                                 | Status | Reviewer Notes | Source Instructions or Resources |
|---|------------------------------------------------------------------------------------------------------|:------:|----------------|----------------------------------|
| 0 | The team has closed and merged the past Feedback PR(s), indicating that they have read the feedback. | ❌ | PR `#36` ("Week 5 feedback") is still **open** (not merged). Week 4 feedback PR `#26` was merged earlier; the most recent feedback loop should be closed out the same way once the team has read it. |                                  |

### Software Process Quality
| # | Item                                                                                                                                         | Status | Reviewer Notes | Source Instructions or Resources                                                                              |
|---|----------------------------------------------------------------------------------------------------------------------------------------------|:------:|----------------|---------------------------------------------------------------------------------------------------------------|
| 1 | Checkstyle: Checkstyle is set up or there is a to-do item planned in the Project Management board for this task. (needed for Letter Grade B) | ✅ | Gradle applies the `checkstyle` plugin, `checkstyle { configFile = file("config/checkstyle/google_checks.xml") }`, and `config/checkstyle/google_checks.xml` is present (merged via PR `#35`). This satisfies the “set up in repo” path; board-only to-do was not needed. | Week 6 Monday lecture (Lecture 11); the build script and config file in the repository for Lab: Code Coverage |
| 2 | SpotBugs: SpotBugs is set up or there is a to-do item planned in the Project Management board for this task. (needed for Letter Grade B)     | ✅ | Gradle applies `com.github.spotbugs` (version `6.0.25`) with SpotBugs configuration and HTML report wiring (same PR `#35`). CI runs `./gradlew build` on push/PR to `main`, which exercises the standard Gradle lifecycle for these plugins. | Week 6 Monday lecture (Lecture 11); the build script and config file in the repository for Lab: Code Coverage |

### Planning & Progress Evaluation
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 4 | The team documents every week’s planning and progress evaluation professionally. (needed for Letter Grade B)                                                 | ✅        | `docs/weekly-reports/week6report.md` on `main` (merged via PR `#38`) has a clear date range, planning summary, per-member work with PR links, and forward-looking goals. | Week 4 Wednesday Lecture (Lecture 08), Project grading rubrics                    |

### Progress & Collaboration
| # | Item                                                                                                                                                                                   |  Status   | Reviewer Notes      | Source Instructions or Resources                 |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|--------------------------------------------------|
| 5 | Overall development progress (recall the recommended order is: Game Setup Phase -> One turn of the game -> Multiple turns -> One win condition -> Other win conditions (if applicable) | ⚠️        | Week 6 report describes meaningful progress (views, graph work, Player BVA/tests, turn mechanics in PR `#41`). On `main` at review time, merged code still centers on setup/domain (`Hex`, `Player`, enums) plus tooling; **PR `#41` (turn / `GameModel` work) remains open**, so “one turn on `main`” is not fully landed yet. Align merge cadence with the course’s recommended ordering. | Canvas assignment Project: Week 4 and 5 Guidance |

### The following items are not checked by the reviewer as they were checked in the previous weeks
But if your team wants the reviewer to check any of these for any reasons, please contact them or the instructor via either email or tagging them in the feedback PR.

| #   | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1   | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval. (needed for Letter Grade C) | ➖        | Not checked this week since it was already reviewed in week 4. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 2   | Continuous Integration (CI) is fully set up from the beginning. (needed for Letter Grade B)                                                                  | ➖        | Not checked this week since it was already reviewed in week 4. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 3   | The team uses the project management board steadily and frequently, and the description of each task is detailed. (needed for Letter Grade B)                | ➖        | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.1 | Every functionality-related work item on the management board includes a user story, and optionally one or more use cases.                                   | ➖        | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.2 | The design is documented somewhere, either in the work item description, or in a separate design document.                                                   | ➖        | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.3 | Task assignments are documented clearly in the management board.                                                                                             | ➖        | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 4   | Each active feature branch has an open draft PR against main.                                                                                                | ➖        | Not checked this week since it was already reviewed in week 4. | Week 4 Wednesday Lecture (Lecture 08)                                             |
| 5   | The team has a “definition of done” (BVA) fully documented for the part of the system that is done. (needed for Letter Grade D)                              | ➖        | Not checked this week since it was already reviewed in week 4. | Project grading rubrics                                                           |
| 6   | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code. (needed for Letter Grade C)                                       | ➖        | Not checked this week since it was already reviewed in week 4. | Project grading rubrics                                                           |
| 7   | Collaboration: Quality of discussion in PR reviews and work item comments on the board.                                                                      | ➖        | Not checked this week since it was already reviewed in week 4. |                                                                                   |

## Additional Comments
- **Strengths:** Checkstyle and SpotBugs are properly integrated on `main` with real configuration files; week 6 planning/reporting is in good shape and merged.
- **Action item:** Merge **PR `#36` (Week 5 feedback)** after the team reads it, so the feedback workflow stays green week to week.
- **Note:** If your local clone predates PR `#35`, pull `main` to see the static-analysis setup reflected in `build.gradle.kts` and `config/checkstyle/`.
