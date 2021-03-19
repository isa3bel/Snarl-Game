# Milestone 6 - Refactoring Report

**Team members:**
Isabel Bolger and Lynnsey Martin

**Github team/repo:**
Edhelen: https://github.ccs.neu.edu/CS4500-S21/Edhelen

## Plan

List areas of your code/design you'd like to improve with brief descriptions,
anything that comes to mind. Tip: translate this list into Github Issues.
1. Ensure all builders took in a Location object instead of 2 ints
2. Change the column and row fields in the Location class to be private. Use getters instead
3. Change the room builder to count the boundaries of the room as part of the origin and bounds

## Changes

Summarize the work you have performed during this week.

1. Ensure all builders took in a Location object instead of 2 ints
- For all methods in the room builders that took in an int for row and an int for column, we changed it to take in a Location object and then accessed the row and column fields from there. This change was pretty big in terms of lines of code being modified, however it was a trivial change, and didn't change funcitonality

2. Change the column and row fields in the Location class to be private. Use getters instead
- We added a getRow() anad getColumn() method in the Location class. Although this change doesn't change functionality, it makes the Location object more of a "read-only" object. This change would defend our program in case we accidentally return a Location object instead of a copy. 

3. Change the room builder to count the boundaries of the room as part of the origin and bounds
- Based on the testing case for milestone #3, our current representation of a room as only the walkable tiles in that room may not be accurate. It would be helpful to realign our construction to the definition described in milestone 3 (if that continues to stay consistent). This requires the origin of the "room" and the rows and columns describing its width and height include the outer boundary of the room (e.g. the wall tiles around).
 
## Future Work

For future work, we recognize the need to update our representation of a level and have plans to relocate adversaries into the level in order to account for the likelihood that adversaries will only exist in a single level (and not carry on with the players). Therefore, we plan to move adversaries (and their construction) inside the level (as opposed to the GameManager).  
  
We also recognize the need to change our representation of a Location sooner rather than later. We currently are limited between only representing a Location as a point on the map or null, but our players will also need to differentiate between being Ejected and having Exited. Therefore, we plan to make Ejected and Exited Locations that extend the current Locations that can represent if a player is not playing any longer.  

## Conclusion

This week was valuable not only to execute on refactoring, but to also to allow us to critically analyze the decisions we made in our code and how they have needed to evolve. While we still may not have all the information for future planning that we wanted, this week allowed us to consider the ways our design may not be fully future-proofed or flexible moving forward. 
