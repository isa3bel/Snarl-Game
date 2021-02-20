package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 * A level in the Snarl dungeon crawler.
 */
public class Level {

  private final ArrayList<ArrayList<Space>> spaces;

  /**
   * Constructor for this Level
   * @param spaces that make up this level
   */
  Level(ArrayList<ArrayList<Space>> spaces) {
    this.spaces = spaces;
  }

  /**
   * Gets the space at the given location.
   * @param location the location to fetch the space from
   * @return the space object
   */
  public Space get(Location location) {
    return this.spaces.get(location.yCoordinate).get(location.xCoordinate);
  }

  /**
   * Maps the given function across all of the spaces in this level.
   * @param function the function to apply to the spaces
   * @param <T> the result type of the function
   * @return the result of the mapped functions
   */
  public <T> ArrayList<ArrayList<T>> map(Function<Space, T> function) {
    ArrayList<ArrayList<T>> result = new ArrayList<>();
    for(ArrayList<Space> row : spaces) {
      ArrayList<T> resultRow = new ArrayList<>();
      for(Space s : row) {
        resultRow.add(function.apply(s));
      }
      result.add(resultRow);
    }
    return result;
  }

  /**
   * Filters out the spaces that the predicate selects and maps the locations to the space
   * @param function the function that we apply to each space
   * @return an map of the location -> space of the filtered spaces
   */
  public HashMap<Location, Space> filter(BiPredicate<Space, Location> function) {
    HashMap<Location, Space> validTiles = new HashMap<>();

    for (int col = 0; col < this.spaces.size(); col++) {
      for (int row = 0; row < this.spaces.get(col).size(); row++) {
        Location location = new Location(col, row);
        Space space = this.spaces.get(col).get(row);

        if (function.test(space, location)) {
          validTiles.put(location, space);
        }
      }
    }

    return validTiles;
  }

}
