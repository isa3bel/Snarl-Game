package model.controller;

import model.level.Location;

/**
 * Controls a zombie adversary by chasing the nearest player or object.
 */
public class ZombieAI extends AdversaryAI {

  private ZombieAI(Location[] validMoves, Location nearestPlayerLocation, Location keyLocation,
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
      return this.calculateValidMoveCloseTo(this.keyLocation);
    }
    return this.calculateValidMoveCloseTo(this.exitLocation);
  }
}
