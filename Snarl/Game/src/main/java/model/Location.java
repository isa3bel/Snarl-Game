package model;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A location on the xy plane of the snarl games.
 */
public class Location {

  int x;
  int y;

  /**
   * Creates a location with the given coordinates.
   * @param x
   * @param y
   * @throws IllegalArgumentException if x or y are negative
   */
  public Location(int x, int y) throws IllegalArgumentException {
    if (x < 0 || y < 0) {
      throw new IllegalArgumentException("location coordinates must be non-negative, given: " + x + ", " + y);
    }

    this.x = x;
    this.y = y;
  }

  /**
   * Calculates all the locations between this Location and that Location.
   * @param that the location to go ot
   * @return the locations between this and that
   * @throws IllegalArgumentException if this Location and that are not on the same axis
   */
  public ArrayList<Location> to(Location that) throws IllegalArgumentException {
    if (!new SameAxis(this).test(that)) {
      throw new IllegalArgumentException("locations must be on one of the same axes");
    }

    ArrayList<Location> locations = new ArrayList<>();
    locations.add(this);
    return this.x == that.x ? this.alongY(that, locations) : this.alongX(that, locations);
  }

  /**
   * Calculates all the points on the x axis from this to that.
   * @param that where to end
   * @param locations the locations so far
   * @return all the locations from the first Location to that
   */
  private ArrayList<Location> alongX(Location that, ArrayList<Location> locations) {
    if (this.equals(that)) return locations;

    Location next = this.x > that.x ? new Location(this.x - 1, this.y) : new Location(this.x + 1, this.y);
    locations.add(next);
    return next.alongX(that, locations);
  }

  /**
   * Calculates all the points on the y axis from this to that.
   * @param that where to end
   * @param locations the locations so far
   * @return all the locations from the first Location to that
   */
  private ArrayList<Location> alongY(Location that, ArrayList<Location> locations) {
    if (this.equals(that)) return locations;

    Location next = this.y > that.y ? new Location(this.x, this.y - 1) : new Location(this.x, this.y + 1);
    locations.add(next);
    return next.alongY(that, locations);
  }

  @Override
  public String toString() {
    return "(" +  x +", " + y + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location that = (Location) o;
    return this.x == that.x &&
        this.y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.x, this.y);
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
    public boolean test(Location location) {
      return this.location.x == location.x || this.location.y == location.y;
    }
  }
}
