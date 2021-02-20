package model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A location on the xy plane of the snarl games.
 */
public class Location {

  public int xCoordinate;
  public int yCoordinate;

  /**
   * Creates a location with the given coordinates.
   * @param xCoordinate x coordinate of this location
   * @param yCoordinate y coordinate of this location
   * @throws IllegalArgumentException if x or y are negative
   */
  public Location(int xCoordinate, int yCoordinate) throws IllegalArgumentException {
    if (xCoordinate < 0 || yCoordinate < 0) {
      throw new IllegalArgumentException("location coordinates must be non-negative, given: "
          + xCoordinate + ", " + yCoordinate);
    }

    this.xCoordinate = xCoordinate;
    this.yCoordinate = yCoordinate;
  }

  /**
   * Calculates all the locations between this Location and that Location.
   * @param end the location to go to
   * @return the locations between this and that
   * @throws IllegalArgumentException if this Location and that are not on the same axis
   */
  public ArrayList<Location> to(Location end) throws IllegalArgumentException {
    if (!new SameAxis(this).test(end)) {
      throw new IllegalArgumentException("locations must be on one of the same axes");
    }

    ArrayList<Location> locations = new ArrayList<>();
    locations.add(this);

    return this.xCoordinate == end.xCoordinate
        ? this.calculateYAxisPoints(end, locations)
        : this.calculateXAxisPoints(end, locations);
  }

  /**
   * Calculates all the points on the x axis from this to that.
   * @param end end location
   * @param locations the locations so far
   * @return all the locations from the first Location to that
   */
  private ArrayList<Location> calculateXAxisPoints(Location end, ArrayList<Location> locations) {
    int xPosnBefore = this.xCoordinate - 1;
    int xPosnNext = this.xCoordinate + 1;

    if (this.equals(end)) {
      return locations;
    }

    Location next = this.xCoordinate > end.xCoordinate
        ? new Location(xPosnBefore, this.yCoordinate)
        : new Location(xPosnNext, this.yCoordinate);

    locations.add(next);
    return next.calculateXAxisPoints(end, locations);
  }

  /**
   * Calculates all the points on the yCoordinate axis from this to that.
   * @param end end location
   * @param locations the locations so far
   * @return all the locations from the first Location to that
   */
  private ArrayList<Location> calculateYAxisPoints(Location end, ArrayList<Location> locations) {
    if (this.equals(end)) {
      return locations;
    }

    int yPosnBelow = this.yCoordinate - 1;
    int yPosnAbove = this.yCoordinate + 1;

    Location next = this.yCoordinate > end.yCoordinate
        ? new Location(this.xCoordinate, yPosnBelow)
        : new Location(this.xCoordinate, yPosnAbove);

    locations.add(next);
    return next.calculateYAxisPoints(end, locations);
  }

  /**
   * Calculates the euclidian distance to the given Location.
   * @param other the location to compare to
   * @return the euclidian distance to that location
   */
  public int euclidianDistance(Location other) {
    return Math.abs(this.xCoordinate - other.xCoordinate)
        + Math.abs(this.yCoordinate - other.yCoordinate);
  }

  /**
   * Is this location directly next to that location?
   * @param that location to compare to
   * @return if this is adjacent to that
   */
  public boolean isAdjacentTo(Location that) {
    int xDiff = Math.abs(this.xCoordinate - that.xCoordinate);
    int yDiff = Math.abs(this.yCoordinate - that.yCoordinate);
    return xDiff + yDiff == 1;
  }

  @Override
  public String toString() {
    return "(" + xCoordinate +", " + yCoordinate + ")";
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    Location that = (Location) other;
    return this.xCoordinate == that.xCoordinate && this.yCoordinate == that.yCoordinate;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.xCoordinate, this.yCoordinate);
  }

  /**
   * Function object to calculate if some Location is on the same axis as a given Location.
   */
  static class SameAxis implements Predicate<Location> {

    Location location;

    SameAxis(Location location) {
      this.location = location;
    }

    @Override
    public boolean test(Location other) {
      return this.location.xCoordinate == other.xCoordinate
          || this.location.yCoordinate == other.yCoordinate;
    }
  }
}
