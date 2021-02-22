package testHarness;

import java.util.HashMap;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import model.*;

/**
 * A query about the neighbors to a location in a room.
 */
public class LocationQuery implements Question {

  private final Level level;
  private final Location location;
  private final Location roomOrigin;

  LocationQuery(Level level, Location location, Location roomOrigin) {
    this.level = level;
    this.location = location;
    this.roomOrigin = roomOrigin;
  }

  /**
   * Calculates the answer to this LocationQuery.
   * @return the string representing the answer to this query
   */
  public String getAnswer() {
    if(!this.isInRoom()) {
      return "[ \"Failure: Point \", " + this.locationToString(this.location)
          + ", \" is not in room at \", " + this.locationToString(this.roomOrigin) + "]";
    }

    Space space = this.level.get(this.location);
    HashMap<Location, Space> validMoves = this.level.filter(new CalculateMovableTiles(this.location, space));
    String joinedMoves = validMoves.keySet().stream().map(this::locationToString)
        .collect(Collectors.joining(", "));
    String allValidStringMoves = String.format("[ %s ]", joinedMoves);

    return "[ \"Success: Traversable points from \", "
        + this.locationToString(this.location) + ", \" in room at \", "
        + this.locationToString(this.roomOrigin) + ", \" are \",\n"
        + allValidStringMoves + "\n" + "]";
  }

  /**
   * Given a location converts it to a string following specific format.
   * @param loc the location to convert
   * @return a string representing the converted location
   */
  private String locationToString(Location loc) {
    return "[" + loc.xCoordinate + ", " + loc.yCoordinate + "]";
  }

  /**
   * Is this location in the room?
   * @return a bool representing if the location is in the room
   */
  private boolean isInRoom() {
    Space originSpace = this.level.get(this.roomOrigin);
    Space location = this.level.get(this.location);
    return originSpace.sameGroup(location);
  }

  /**
   * Function object that calculates tiles reachable from a specific point
   */
  private static class CalculateMovableTiles implements BiPredicate<Space, Location> {

    private final Space space;
    private final Location location;

    CalculateMovableTiles(Location location, Space space) {
      this.location = location;
      this.space = space;
    }

    /**
     * Is the given space at the given location reachable from this pointOfInterest?
     * @param thatSpace the space of the given location
     * @param thatLocation the location of the space
     * @return whether the space is reachable from this pointOfInterest
     */
    @Override
    public boolean test(Space thatSpace, Location thatLocation) {
      return this.location.isAdjacentTo(thatLocation) && this.space.sameGroup(thatSpace) &&
          thatSpace.acceptVisitor(new IsTraverable());
    }
  }
}
