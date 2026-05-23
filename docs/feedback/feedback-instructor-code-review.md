# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.

Please note that this feedback does not include evaluation of your progress, the proper use of linters, the quality of your test cases, or your compliance of TDD/BDD workflow.
You can find the weekly feedback from your dedicated PM/TA for that.

## Week 6 Code Review
I have read every line of production code currently in the main branch.
One thing I noticed are the following 3 lines in Hex.java:
```
public final int hexId;
public final Resource resource;
public final int hexRollNum;
```

If the reason to make them public is purely for the tests, it's better to keep them private, and then add package private getters to call in the tests, like:

```
getHexRollNum(){
  return this.hexRollNum;
}
```
(Having no public/private/protected at the beginning means "package private").

The refactoring should be easy with the modern IDE features (for instance, after you change the field to private and add the package private getter, IntelliJ gives an option for the test code that mentions h.hexRollNum to "replace with getter".)

Also, there is use of magic number in Hex. And there are some extra empty lines at the end of Hex that should be deleted.

Otherwise, looks good! Look forward to reviewing more of your domain logic in the next review.

Please approve and merge the PR once the team has read the feedback. Thanks!