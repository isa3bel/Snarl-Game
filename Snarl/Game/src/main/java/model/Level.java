package model;

import com.sun.tools.javac.file.Locations;
import java.util.ArrayList;
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
   * Calculate the tiles that the predicate returns true for
   * @param function the function that we apply to each space
   * @param <T>
   * @return an arraylist of spaces that the predicate returns true for
   */
  public <T> ArrayList<Location> calculateMovableTiles(BiPredicate<Space, Location> function) {
    ArrayList<Location> validTiles = new ArrayList<>();

    for (int row = 0; row < spaces.size(); row++) {
      for (int col = 0; col < spaces.size(); col++) {
        if (function.test(spaces.get(row).get(col), new Location(row, col))) {
          validTiles.add(new Location(row, col));
        }
      }
    }

    return validTiles;
  }

}
