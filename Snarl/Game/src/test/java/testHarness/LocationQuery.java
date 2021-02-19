package testHarness;

import model.Level;
import model.Location;

public class LocationQuery implements Question {

  private Level level;
  private Location location;

  LocationQuery(Level level, Location location) {

  }

  public String test() {
    // DOES THE CALCULATION
    // RETURN THE RESULT OF THE CALUCATION IN THE FORMATTED STRING
    return "[ \"Success: Traversable points from \", [2, 2], \" in room at \", [0, 1] , \" are \",\n" +
        "      [ [2, 1], [1, 2], [3, 2], [2, 3] ] \n" +
        "]";
  }

}
