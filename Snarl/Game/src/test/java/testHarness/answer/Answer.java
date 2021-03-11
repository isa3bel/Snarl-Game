package testHarness.answer;

import model.Location;

public abstract class Answer {

  /**
   * Given a location converts it to a string following specific format.
   * @param location the location to convert
   * @return a string representing the converted location
   */
  protected String locationToString(Location location) {
    return "[" + location.row + ", " + location.column + "]";
  }

}
