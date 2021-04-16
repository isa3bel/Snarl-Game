package model.level;

import model.GameManager;
import model.characters.Adversary;
import model.ruleChecker.*;
import model.item.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * A level in the Snarl dungeon crawler.
 */
public class Level {

  private final ArrayList<ArrayList<Space>> spaces;
  private final ArrayList<Item> items;
  private final ArrayList<Adversary> adversaries;

  /**
   * Constructor for this Level
   * @param spaces that make up this level
   */
  public Level(ArrayList<ArrayList<Space>> spaces, ArrayList<Item> items) {
    this.spaces = spaces;
    this.items = items;
    this.adversaries = new ArrayList<>();
  }

  /**
   * Adds adversaries to this level and assigns them random locations inside the level.
   * @param adversaries the adversaries to add to this level
   */
  public void addAdversaries(ArrayList<Adversary> adversaries) {
    if (adversaries == null) return;

    ArrayList<Location> validStartingLocations = calculateValidActorPositions();

    if (validStartingLocations.size() < adversaries.size()) {
      throw new IllegalStateException("level does not have enough valid positions for adversaries");
    }

    this.adversaries.addAll(adversaries);
    for (int idx = 0; idx < this.adversaries.size(); idx++) {
      this.adversaries.get(idx).moveTo(validStartingLocations.get(idx));
    }
  }

  /**
   * Make all the adversaries do their turn in the given GameManager.
   * @param gameManager the game manager which is running this
   * @return the status of the game after all the adversaries have their turn
   */
  public GameStatus doAdversaryTurns(GameManager gameManager) {
    GameStatus status = GameStatus.PLAYING;
    for (Adversary adversary : this.adversaries) {
      adversary.updateController(gameManager);
      status = gameManager.doTurn(adversary);

      if (status != GameStatus.PLAYING) return status;
    }
    return status;
  }

  /**
   * Gets the space at the given location.
   * @param location the location to fetch the space from
   * @return the space object
   */
  public Space get(Location location) throws IndexOutOfBoundsException {
    try {
      return this.spaces.get(location.getRow()).get(location.getColumn());
    }
    catch (IndexOutOfBoundsException exception) {
      throw new IndexOutOfBoundsException(String.format("location %s not in level", location.toString()));
    }
  }

  /**
   * Execute the given interaction on this level.
   * @param interaction the interaction that is occurring
   * @param location the location where the interaction is occurring
   */
  public <T> void interact(InteractableVisitor<T> interaction, Location location) {
    this.interact(interaction, (a, b) -> null, location);
  }

  /**
   * Execute the given interaction on this level.
   * @param interaction the interaction that is occurring
   */
  public <T> T interact(InteractableVisitor<T> interaction, BinaryOperator<T> collector, Location location) {
    T actorResult = this.adversaries.stream()
        .map(adversary -> adversary.acceptVisitor(interaction))
        .filter(Objects::nonNull)
        .reduce(collector)
        .orElse(null);
    T itemResult = this.items.stream()
        .map(item -> item.acceptVisitor(interaction))
        .filter(Objects::nonNull)
        .reduce(collector)
        .orElse(null);
    if (location != null) this.get(location).acceptVisitor(interaction);
    return collector.apply(actorResult, itemResult);
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
   * Make a list of all the possible positions where an actor could be in the level.
   * @return a list of all the locations in this level where an actor could start
   */
  public ArrayList<Location> calculateValidActorPositions() {
    IsValidStartingLocation isValidStartingLocation = new IsValidStartingLocation();
    this.interact(isValidStartingLocation, null);
    ArrayList<Location> validLocations = new ArrayList<>(this.filter(isValidStartingLocation).keySet());
    Collections.shuffle(validLocations);
    return validLocations;
  }
}
