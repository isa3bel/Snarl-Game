package testHarness;

import java.util.ArrayList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import model.Level;
import model.Location;
import model.Space;
import model.Wall;

public class LocationQuery implements Question {

  private Level level;
  public Location location;

  LocationQuery(Level level, Location location) {
    this.level = level;
    this.location = location;
  }

  /**
   * Gets the string of the result
   * @return the string representing the answer to this query
   */
  public String getQueryResult() {
    ArrayList<Location> validMoves = this.level.calculateMovableTiles(new CalculateMovableTiles(location));
    String allValidStringMoves = "[ ";
    for(int i = 0; i < validMoves.size() - 1; i++) {
      allValidStringMoves += locationToString(validMoves.get(i)) + ", ";
    }
    allValidStringMoves+= locationToString(validMoves.get(validMoves.size() - 1)) + "]";

    return "[ \"Success: Traversable points from \", "
        + locationToString(this.location) + ", \" in room at \", [0, 1] , \" are \",\n" + allValidStringMoves + "\n"
        + "]";
  }

  public String locationToString(Location loc) {
    return "[" + loc.xCoordinate + ", " + loc.yCoordinate + "]";
  }

  /**
   * Function object that determines valid tiles reachable from a specific point
   */
  protected static class CalculateMovableTiles implements BiPredicate<Space, Location> {

    private Location pointOfInterest;

    CalculateMovableTiles(Location loc) {
      this.pointOfInterest = loc;
    }

    @Override
    /**
     * Determines if a given space can be reached to from this point of interest
     */
    public boolean test(Space space, Location locationOfSpace) {
      Location belowSpace = new Location(locationOfSpace.xCoordinate, locationOfSpace.yCoordinate - 1);
      Location aboveSpace = new Location(locationOfSpace.xCoordinate, locationOfSpace.yCoordinate + 1);
      Location leftSpace = new Location(locationOfSpace.xCoordinate - 1, locationOfSpace.yCoordinate);
      Location rightSpace = new Location(locationOfSpace.xCoordinate + 1, locationOfSpace.yCoordinate);

      if (space instanceof Wall) {
        return false;
      }

      return this.pointOfInterest.equals(belowSpace)
          || this.pointOfInterest.equals(aboveSpace)
          || this.pointOfInterest.equals(leftSpace)
          || this.pointOfInterest.equals(rightSpace);
    }

    @Override
    /**
     * Determines if two locations are equal
     */
    public boolean equals(Object other) {
      if (this == other) {
        return true;
      }
      if (other == null || getClass() != other.getClass()) {
        return false;
      }
      Location that = (Location) other;
      return this.pointOfInterest.xCoordinate == that.xCoordinate
          && this.pointOfInterest.yCoordinate == that.yCoordinate;
    }
  }

}
