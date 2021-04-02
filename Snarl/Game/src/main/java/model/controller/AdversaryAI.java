package model.controller;

import model.level.Location;

import java.util.Arrays;
import java.util.Comparator;

public abstract class AdversaryAI {

  // what is the max distance that an adversary "cares" about a player
  protected static final int PLAYER_RANGE = 8;

  protected final Location[] validMoves;
  protected final Location nearestPlayerLocation;
  protected final Location keyLocation;
  protected final Location exitLocation;
  protected final Location closestWall;

  protected AdversaryAI(Location[] validMoves, Location nearestPlayerLocation, Location keyLocation,
      Location exitLocation, Location closestWall) {
    this.validMoves = validMoves;
    this.nearestPlayerLocation = nearestPlayerLocation;
    this.keyLocation = keyLocation;
    this.exitLocation = exitLocation;
    this.closestWall = closestWall;
  }

  /**
   * Calculates the next move of a zombie based on the distance of valid moves to players, key, and exit.
   * @return the next move the zombie should take based on known data
   */
  public abstract Location calculateNextMove();

  /**
   * Calculate the valid move location that is closest to the given location.
   * @param location the location to th
   * @return valid move location closest to given
   */
  protected Location calculateValidMoveCloseTo(Location location) {
    return Arrays.stream(this.validMoves)
        .min(Comparator.comparingInt(move -> move.euclidianDistance(location)))
        .orElseThrow(() -> new IllegalStateException("no valid zombie move"));
  }
}
