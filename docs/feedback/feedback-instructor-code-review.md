# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main
branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding
lectures are considered, too.

Please note that this feedback does not include evaluation of your progress, the proper use of linters, the quality of
your test cases, or your compliance of TDD/BDD workflow.
You can find the weekly feedback from your dedicated PM/TA for that.

## Week 10 Feedback

The following table contains the _Grading Rubrics for the Team_ that was released on Canvas in the beginning of the
quarter (Canvas -> Project Resources -> Grading).
The instructor reviewed all the code in your main branch against every rubric item except "Product Completion."
If any item is not satisfied, your grade will not be penalized as long as your team corrects it between now and the
final submission.

Note that the instructor may have granted some teams specific exceptions, which are not reflected in this feedback.
So don't worry if an item is marked "significant issue found" but you've been granted an exception for it.
Just make sure to document that exception in your README.md when you finalize your submission.

After this feedback, make sure any new code you add continues to satisfy the rubric items.

*How to read the rubrics: The evaluation starts with the criteria for F and progresses through D, C, B, and A. The grade
advances to the next level if all criteria for the current grade are met or exceeded.*

Status:

- ✅: The rubric item is met or exceeded!
- ⚠️: Attention needed
- ❌: Significant issue found
- ➖: Not evaluated by the instructor in this feedback

| Category                   | Status | Item                                                                                                                                                                            | Notes                                                                                                                 |
|----------------------------|--------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------|
| **F: Not Sufficient Work** |        |                                                                                                                                                                                 |                                                                                                                       |
|                            | ✅      | The project does not meet all the criteria of D.                                                                                                                                |                                                                                                                       |
| **D: Progressing**         |        |                                                                                                                                                                                 |                                                                                                                       |
| **Product Completion**     | ➖      | **_Note: This review does not evaluate anything in the "Product Completion" category._**                                                                                        |                                                                                                                       |
|                            | ➖      | If the game has multiple cards or characters, at least one type is fully implemented.                                                                                           |                                                                                                                       |
|                            | ➖      | The game is playable.                                                                                                                                                           |                                                                                                                       |
| **Product Quality**        |        |                                                                                                                                                                                 |                                                                                                                       |
|                            | ✅      | The team has a "definition of done" (BVA) fully documented for the part of the system that is done.                                                                             |                                                                                                                       |
|                            | ✅      | All the automated tests correctly reflect the BVA analysis, including but not limited to using the correct input values identified by BVA and using the appropriate assertions. |                                                                                                                       |
|                            | ✅      | Automated testing may indicate some failures.                                                                                                                                   |                                                                                                                       |
| **Process Quality**        |        |                                                                                                                                                                                 |                                                                                                                       |
|                            | ✅      | The team uses the project management board very rarely.                                                                                                                         |                                                                                                                       |
|                            | ✅      | GitHub repository branch protection rules are not fully set up so that people can push into main without a pull request approval.                                               |                                                                                                                       |
|                            | ✅      | Continuous Integration (CI) is not fully set up or only set up in the last two weeks.                                                                                           |                                                                                                                       |
| **C: Satisfactory**        |        |                                                                                                                                                                                 |                                                                                                                       |
| **Product Completion**     | ➖      | **_Note: This review does not evaluate anything in the "Product Completion" category._**                                                                                        |                                                                                                                       |
|                            | ➖      | If the game has multiple cards or characters, most of the types are fully implemented.                                                                                          |                                                                                                                       |
|                            | ➖      | The win condition is implemented.                                                                                                                                               |                                                                                                                       |
| **Product Quality**        |        |                                                                                                                                                                                 |                                                                                                                       |
|                            | ✅      | All the automated tests pass.                                                                                                                                                   |                                                                                                                       |
|                            | ✅      | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code.                                                                                      |                                                                                                                       |
|                            | ✅      | There is evidence that the team uses mutation testing and code coverage to improve the quality of the test cases, but not all the issues are solved.                            |                                                                                                                       |
|                            | ❌      | The team has set up some code style standards, but not all the code satisfies the standard.                                                                                     | Checkstyle reports some issues. See build/reports/checkstyle/main.html                                                |
| **Process Quality**        |        |                                                                                                                                                                                 |                                                                                                                       |
|                            | ✅      | The team uses the project management board steadily and frequently, but the description of each task is very vague.                                                             |                                                                                                                       |
|                            | ✅      | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval.                                                |                                                                                                                       |
|                            | ✅      | Continuous Integration (CI) is not fully set up or only set up in the last five weeks.                                                                                          |                                                                                                                       |
|                            | ❌      | The team rarely documents each week's planning and progress.                                                                                                                    | please complete weekly-reports/report.md                                                                              |
| **B: Good Work**           |        |                                                                                                                                                                                 |                                                                                                                       |
| **Product Completion**     | ➖      | **_Note: This review does not evaluate anything in the "Product Completion" category._**                                                                                        |                                                                                                                       |
|                            | ➖      | (Applicable only if the chosen game has more than one winning condition) All the winning conditions are implemented.                                                            |                                                                                                                       |
|                            | ➖      | If the game has multiple cards or characters, all the types are fully implemented.                                                                                              |                                                                                                                       |
| **Product Quality**        |        |                                                                                                                                                                                 |                                                                                                                       |
|                            | ✅      | There is evidence that the team uses mutation testing and code coverage as soon as the concepts are introduced and there is no issue revealed by them.                          |                                                                                                                       |
|                            | ✅      | 100% mutants should be killed (except for equivalent mutant).                                                                                                                   |                                                                                                                       |
|                            | ❌      | 100% cyclomatic coverage for non-GUI and non-enum code.                                                                                                                         | BoardGraph.java has some missing coverage that I am concerned about. Is there any particular justification for those? |
|                            | ❌      | The code fully satisfies the style standards the team set up.                                                                                                                   | Checkstyle reports some issues. See build/reports/checkstyle/main.html                                                |
|                            | ✅      | There is progress on integration testing.                                                                                                                                       |                                                                                                                       |
| **Process Quality**        |        |                                                                                                                                                                                 |                                                                                                                       |
|                            | ✅      | The team uses the project management board steadily and frequently, and the description of each task is detailed.                                                               |                                                                                                                       |
|                            | ✅      | Continuous Integration (CI) is fully set up from the beginning.                                                                                                                 |                                                                                                                       |
|                            | ❌      | The team documents every week's planning and progress evaluation professionally.                                                                                                | please complete weekly-reports/report.md                                                                              |
| **A: Excellent**           |        |                                                                                                                                                                                 |                                                                                                                       |
| **Product Completion**     | ➖      | **_Note: This review does not evaluate anything in the "Product Completion" category._**                                                                                        |                                                                                                                       |
|                            | ➖      | Support of locale: the software allows the user to choose between at least two languages at the beginning of the game.                                                          |                                                                                                                       |
|                            | ➖      | The code supports easily adding new locales without changing existing code.                                                                                                     |                                                                                                                       |
| **Product Quality**        |        |                                                                                                                                                                                 |                                                                                                                       |
|                            | ❌      | The code fully satisfied the code standards discussed in Clean Code.                                                                                                            | Doesn't seem that all my Week7-8 comments below are addressed. Found no additional issues.                            |
|                            | ➖      | Integration testing is done on at least 2 main features.                                                                                                                        |                                                                                                                       |

### Other comments

Please delete all the .gitkeep files from your repository.
Those are added as placeholders when a directory is empty.
Once we have any meaningful files under the directory, we should delete those .gitkeep files.

## Week 7-8 Code Review

This review is for the code your team developed in Week 7 and 8.
I apologize for this delayed code review (should have been given last Friday but I got really sick...).
As compensation, I will add one extra code review in Week 10 (around Thursday).

So much progress!! I am so excited for your team!

In terms of code quality,

1. AMAZING job by creating your own exceptions! 100% good work!
2. `BoardGraph` class seems to have comments that can be deleted. One example:

```
    // Add a new GraphNode to the Map
    boolean addGraphNodeObject(GraphNode graphNode) {
```

3. Also, in `GraphEdge`, there is an incomplete comment :):

``` 
    // keep track of the
    private boolean roadBuilt;
```

4. `TradeManager#acceptTrade` method can be broken into multiple methods. Let's see,

```
    public void acceptTrade(TradeOffer offer, Player acceptingPlayer) {
        Player offerer = offer.getOfferingPlayer();
        
        //// Have a validateAcceptTradeInput method: 
        if (offerer == acceptingPlayer) {
            throw new IllegalArgumentException("A player cannot accept their own trade.");
        }
        if (!offers.contains(offer)) {
            throw new IllegalArgumentException("Trade not found.");
        }

        //// Have one method for the following 6 lines of code?
        ResourceQuantity giving = offer.getGiving();
        ResourceQuantity receiving = offer.getReceiving();

        Resource givingResource = giving.getResource();
        int givingQuantity = giving.getQuantity();

        Resource receivingResource = receiving.getResource();
        int receivingQuantity = receiving.getQuantity();

        //// Another method for the following 2 if
        if (offerer.getResourceCount(givingResource) < givingQuantity) {
            throw new IllegalStateException("Offering player has insufficient resources.");
        }
        if (acceptingPlayer.getResourceCount(receivingResource) < receivingQuantity) {
            throw new IllegalStateException("Accepting player has insufficient resources.");
        }

        //// Another method for the following 4 lines of code?
        offerer.updateResources(givingResource, -givingQuantity);
        offerer.updateResources(receivingResource, receivingQuantity);
        acceptingPlayer.updateResources(receivingResource, -receivingQuantity);
        acceptingPlayer.updateResources(givingResource, givingQuantity);

        offers.remove(offer);
    }
```

Otherwise, looks good! Keep up the good work!! I look forward to your final product :)!

## Week 6 Code Review

I have read every line of production code currently in the main branch.
One thing I noticed are the following 3 lines in Hex.java:

```
public final int hexId;
public final Resource resource;
public final int hexRollNum;
```

If the reason to make them public is purely for the tests, it's better to keep them private, and then add package
private getters to call in the tests, like:

```
getHexRollNum(){
  return this.hexRollNum;
}
```

(Having no public/private/protected at the beginning means "package private").

The refactoring should be easy with the modern IDE features (for instance, after you change the field to private and add
the package private getter, IntelliJ gives an option for the test code that mentions h.hexRollNum to "replace with
getter".)

Also, there is use of magic number in Hex. And there are some extra empty lines at the end of Hex that should be
deleted.

Otherwise, looks good! Look forward to reviewing more of your domain logic in the next review.

Please approve and merge the PR once the team has read the feedback. Thanks!