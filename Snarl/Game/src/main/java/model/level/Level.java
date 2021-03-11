package model.level;

import model.interactions.Interaction;
import model.item.Item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A level in the Snarl dungeon crawler.
 */
public class Level {

  private final ArrayList<ArrayList<Space>> spaces;
  private final ArrayList<Item> items;

  /**
   * Constructor for this Level
   * @param spaces that make up this level
   */
  public Level(ArrayList<ArrayList<Space>> spaces, ArrayList<Item> items) {
    this.spaces = spaces;
    this.items = items;
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
   * Execute the given interaction on this level.
   * @param location the location that this interaction is taking place
   * @param interaction the interaction that is occurring
   */
  public void interact(Location location, Interaction interaction) {
    this.items.forEach(item -> item.acceptVisitor(interaction));
    if (location != null) this.get(location).acceptVisitor(interaction);
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

  /**
   * Maps the given consumer across all of these items.
   * @param itemConsumer the function object to apply to all the items
   */
  public void mapItems(Consumer<Item> itemConsumer) {
    // TODO: if we do keep this as is, then we should combine this better with this.interact
    this.items.forEach(itemConsumer);
  }

  /**
   * Find all available locations in the room with the "most" something tile in the level.
   * @param comparator how to sort the tiles to find the "most"
   * @return all the available tiles in that room
   */
  private ArrayList<Location> calculateCharacterLocations(Comparator<Location> comparator) {
    ArrayList<Location> roomTiles = this
        .filter((space, location) -> space.acceptVisitor(new IsValidStartingLocation()))
        .keySet()
        .stream()
        .sorted(comparator)
        .collect(Collectors.toCollection(ArrayList::new));
    Space firstTile = this.get(roomTiles.get(0));
    return roomTiles
        .stream()
        .filter(location -> firstTile.sameGroup(this.get(location)))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Calculate the locations for automatically placing players in top left room.
   * @return the list of available locations in the top left room
   */
  public ArrayList<Location> calculatePlayerLocations() {
    return this.calculateCharacterLocations((location1, location2) -> location1.row == location2.row
        ? location1.column - location2.column
        : location1.row - location2.row);
  }

  /**
   * Calculate the locations for automatically placing adversaries in bottom right room.
   * @return the list of available locations in the bottom right room
   */
  public ArrayList<Location> calculateAdversaryLocations() {
    return this.calculateCharacterLocations((location1, location2) -> location1.row == location2.row
        ? location2.column - location1.column
        : location2.row - location1.row);
  }

}
