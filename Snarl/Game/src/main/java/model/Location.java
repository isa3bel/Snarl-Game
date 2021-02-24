package model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A location on the xy plane of the snarl games.
 */
public class Location {

  public int row;
  public int column;

  /**
   * Creates a location with the given coordinates.
   * @param row row of this location
   * @param column column of this location
   * @throws IllegalArgumentException if row or column are negative
   */
  public Location(int row, int column) throws IllegalArgumentException {
    if (row < 0 || column < 0) {
      throw new IllegalArgumentException("location coordinates must be non-negative, given: "
          + row + ", " + column);
    }

    this.row = row;
    this.column = column;
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

    return this.row == end.row
        ? this.calculateColumnAxisPoints(end, locations)
        : this.calculateRowAxisPoints(end, locations);
  }

  /**
   * Calculates all the points on the x axis from this to that.
   * @param end end location
   * @param locations the locations so far
   * @return all the locations from the first Location to that
   */
  private ArrayList<Location> calculateRowAxisPoints(Location end, ArrayList<Location> locations) {
    if (this.equals(end)) {
      return locations;
    }

    int rowBefore = this.row - 1;
    int rowNext = this.row + 1;

    Location next = this.row > end.row
        ? new Location(rowBefore, this.column)
        : new Location(rowNext, this.column);

    locations.add(next);
    return next.calculateRowAxisPoints(end, locations);
  }

  /**
   * Calculates all the points on the yCoordinate axis from this to that.
   * @param end end location
   * @param locations the locations so far
   * @return all the locations from the first Location to that
   */
  private ArrayList<Location> calculateColumnAxisPoints(Location end, ArrayList<Location> locations) {
    if (this.equals(end)) {
      return locations;
    }

    int columnBefore = this.column - 1;
    int columnNext = this.column + 1;

    Location next = this.column > end.column
        ? new Location(this.row, columnBefore)
        : new Location(this.row, columnNext);

    locations.add(next);
    return next.calculateColumnAxisPoints(end, locations);
  }

  /**
   * Calculates the euclidian distance to the given Location.
   * @param other the location to compare to
   * @return the euclidian distance to that location
   */
  public int euclidianDistance(Location other) {
    return Math.abs(this.row - other.row)
        + Math.abs(this.column - other.column);
  }

  /**
   * Is this location directly next to that location?
   * @param that location to compare to
   * @return if this is adjacent to that
   */
  public boolean isAdjacentTo(Location that) {
    int rowDiff = Math.abs(this.row - that.row);
    int columnDiff = Math.abs(this.column - that.column);
    return rowDiff + columnDiff == 1;
  }

  @Override
  public String toString() {
    return "(" + row +", " + column + ")";
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
    return this.row == that.row && this.column == that.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.row, this.column);
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
      return this.location.row == other.row
          || this.location.column == other.column;
    }
  }
}
