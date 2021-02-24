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
  private final Location queryLocation;
  private final Location roomOrigin;

  LocationQuery(Level level, Location queryLocation, Location roomOrigin) {
    this.level = level;
    this.queryLocation = queryLocation;
    this.roomOrigin = roomOrigin;
  }

  /**
   * Calculates the answer to this LocationQuery.
   * @return the string representing the answer to this query
   */
  public String getAnswer() {
    if(!this.isInRoom()) {
      return "[ \"Failure: Point \", " + this.locationToString(this.queryLocation)
          + ", \" is not in room at \", " + this.locationToString(this.roomOrigin) + "]";
    }

    Space space = this.level.get(this.queryLocation);
    HashMap<Location, Space> validMoves = this.level.filter(new CalculateMovableTiles(this.queryLocation, space));
    String joinedMoves = validMoves
        .keySet()
        .stream()
        .sorted((location1, location2) -> location1.row == location2.row
          ? location1.column - location2.column
          : location1.row - location2.row)
        .map(this::locationToString)
        .collect(Collectors.joining(", "));
    String allValidStringMoves = String.format("[ %s ]", joinedMoves);

    return "[ \"Success: Traversable points from \", "
        + this.locationToString(this.queryLocation) + ", \" in room at \", "
        + this.locationToString(this.roomOrigin) + ", \" are \", "
        + allValidStringMoves + " ]";
  }

  /**
   * Given a location converts it to a string following specific format.
   * @param location the location to convert
   * @return a string representing the converted location
   */
  private String locationToString(Location location) {
    return "[" + location.row + ", " + location.column + "]";
  }

  /**
   * Is this location in the room?
   * @return a bool representing if the location is in the room
   */
  private boolean isInRoom() {
    Space originSpace = this.level.get(this.roomOrigin);
    Space location;

    try {
      location = this.level.get(this.queryLocation);
    } catch (IndexOutOfBoundsException exception) {
      return false;
    }

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
      return this.location.isAdjacentTo(thatLocation)
          && this.space.sameGroup(thatSpace)
          && thatSpace.acceptVisitor(new IsTraversable());
    }
  }
}
