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
This review was refreshed after syncing the local workspace with `origin`’s `main`. Findings below cite files present in this clone (for example `build.gradle.kts`, `config/checkstyle/`, `docs/weekly-reports/week6report.md`, and `src/`). Open/merged status of specific PRs was re-checked via the public GitHub REST API. Branch protection policy details remain partially behind authenticated endpoints.

## Checklist
Status:
- ✅: All done/Good job!
- ⚠️: Attention needed
- ❌: Significant issue found
- ➖: No basis to evaluate

### Past Feedback
| # | Item                                                                                                 | Status | Reviewer Notes | Source Instructions or Resources |
|---|------------------------------------------------------------------------------------------------------|:------:|----------------|----------------------------------|
| 0 | The team has closed and merged the past Feedback PR(s), indicating that they have read the feedback. | ❌ | PR `#36` ("Week 5 feedback") is still **open** (`merged_at` is null). Week 4 feedback PR `#26` was merged earlier; merge `#36` after the team has read week 5 feedback so the workflow stays current. |                                  |

### Software Process Quality
| # | Item                                                                                                                                         | Status | Reviewer Notes | Source Instructions or Resources                                                                              |
|---|----------------------------------------------------------------------------------------------------------------------------------------------|:------:|----------------|---------------------------------------------------------------------------------------------------------------|
| 1 | Checkstyle: Checkstyle is set up or there is a to-do item planned in the Project Management board for this task. (needed for Letter Grade B) | ✅ | `build.gradle.kts` applies the `checkstyle` plugin with `configFile = file("config/checkstyle/google_checks.xml")`, and `config/checkstyle/google_checks.xml` exists. Satisfies the “set up in repo” path; no board-only substitute needed. | Week 6 Monday lecture (Lecture 11); the build script and config file in the repository for Lab: Code Coverage |
| 2 | SpotBugs: SpotBugs is set up or there is a to-do item planned in the Project Management board for this task. (needed for Letter Grade B)     | ✅ | `com.github.spotbugs` plugin `6.0.25`, `spotbugs { ... }` block, and `tasks.spotbugsMain` HTML report configuration in `build.gradle.kts`. CI still runs `./gradlew build` on push/PR to `main`. | Week 6 Monday lecture (Lecture 11); the build script and config file in the repository for Lab: Code Coverage |

### Planning & Progress Evaluation
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 4 | The team documents every week’s planning and progress evaluation professionally. (needed for Letter Grade B)                                                 | ✅        | `docs/weekly-reports/week6report.md` is present on pulled `main` with dated range, planning, per-member work (with PR links), and goals—meets the week-level reporting bar. | Week 4 Wednesday Lecture (Lecture 08), Project grading rubrics                    |

### Progress & Collaboration
| # | Item                                                                                                                                                                                   |  Status   | Reviewer Notes      | Source Instructions or Resources                 |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|--------------------------------------------------|
| 5 | Overall development progress (recall the recommended order is: Game Setup Phase -> One turn of the game -> Multiple turns -> One win condition -> Other win conditions (if applicable) | ⚠️        | `src/main/java` still only has `domain` types (`Hex`, `Player`, enums) and no merged game-round layer in this tree; tests remain focused on `HexTests`. The week 6 report describes turn mechanics and PR `#41`; **GitHub API still shows PR `#41` open** (`merged_at` null), so “one turn on `main`” is still in flight. Keep merging toward the recommended sequence. | Canvas assignment Project: Week 4 and 5 Guidance |

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
- **Strengths:** After syncing with `origin`, Checkstyle and SpotBugs are clearly present in `build.gradle.kts` with a real Checkstyle rules file under `config/checkstyle/`; week 6 written reporting is solid.
- **Action item:** Merge **PR `#36` (Week 5 feedback)** once the team has read it (`merged_at` is still null on GitHub), so each week’s feedback loop stays complete.
- **Next merge target:** Land **PR `#41`** (or follow-ups) when ready so `main` reflects the turn/game-round progress already described in the week 6 report.
