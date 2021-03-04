package model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Represents a class that constructs itself in a 2D array of spaces.
 */
public abstract class SpaceBuilder {

  /**
   * Initialize the given spaces array to the required size for this room.
   * @param spaces the spaces array
   */
  protected void initSize(Location location, ArrayList<ArrayList<Space>> spaces) {
    int maxX = Math.max(location.column, spaces.stream().map(row -> row.size() - 1)
        .max(Comparator.comparingInt(a -> a)).orElse(0));
    int maxY = Math.max(location.row, spaces.size() - 1);

    // guarantee the min number of rows
    while (spaces.size() <= maxY) {
      spaces.add(new ArrayList<>());
    }

    // for each row in spaces
    for (ArrayList<Space> row : spaces) {
      // guarantee the min number of spaces in that row
      while (row.size() <= maxX) {
        row.add(new VoidWall());
      }
    }
  }

  abstract void build(ArrayList<ArrayList<Space>> spaces);
}
