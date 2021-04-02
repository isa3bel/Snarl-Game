package model.controller;

import model.level.Location;

/**
 * Controls a ghost adversary by chasing the nearest player or object or going to a nearby wall.
 */
public class GhostAI extends AdversaryAI {

  private static final int KEY_RANGE = 5;

  private GhostAI(Location[] validMoves, Location nearestPlayerLocation, Location keyLocation,
                  Location exitLocation, Location closestWall) {
    super(validMoves, nearestPlayerLocation, keyLocation, exitLocation, closestWall);
  }
  
  /**
   * Calculates the next move of a zombie based on the distance of valid moves to players, key, and exit.
   * @return the next move the zombie should take based on known data
   */
  public Location calculateNextMove() {
    if (this.nearestPlayerLocation != null) {
      Location closestToPlayer = this.calculateValidMoveCloseTo(this.nearestPlayerLocation);
      if (closestToPlayer.euclidianDistance(this.nearestPlayerLocation) < PLAYER_RANGE) return closestToPlayer;
    }
    if (this.keyLocation != null && this.keyLocation.isInLevel()) {
      Location closestToKey = this.calculateValidMoveCloseTo(this.keyLocation);
      if (closestToKey.euclidianDistance(this.keyLocation) < KEY_RANGE) return closestToKey;
    }
    return this.calculateValidMoveCloseTo(this.closestWall);
  }
}
