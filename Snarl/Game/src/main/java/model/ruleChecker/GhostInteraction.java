package model.ruleChecker;

import model.characters.Ghost;
import model.level.Location;
import model.level.Wall;

/**
 * Controls an adversary interaction with an Interactable - what happens and under what conditions.
 */
public class GhostInteraction extends AdversaryInteraction {

  Location randomValidLocation;

  public GhostInteraction(Ghost ghost, Location randomValidLocation) {
    super(ghost);
    this.randomValidLocation = randomValidLocation;
  }

  @Override
  public MoveResult visitWall(Wall wall) {
    this.adversary.moveTo(this.randomValidLocation);
    return null;
  }

}
