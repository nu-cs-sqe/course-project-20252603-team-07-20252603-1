# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.

Please note that this feedback does not include evaluation of your progress, the proper use of linters, the quality of your test cases, or your compliance of TDD/BDD workflow.
You can find the weekly feedback from your dedicated PM/TA for that.

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