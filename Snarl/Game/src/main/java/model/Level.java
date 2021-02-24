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
  public Space get(Location location) throws IndexOutOfBoundsException {
    try {
      return this.spaces.get(location.row).get(location.column);
    }
    catch (IndexOutOfBoundsException exception) {
      throw new IndexOutOfBoundsException(String.format("location %s not in level", location.toString()));
    }
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

    for (int row = 0; row < this.spaces.size(); row++) {
      for (int column = 0; column < this.spaces.get(row).size(); column++) {
        Location location = new Location(row, column);
        Space space = this.spaces.get(row).get(column);

        if (function.test(space, location)) {
          validTiles.put(location, space);
        }
      }
    }

    return validTiles;
  }

}
