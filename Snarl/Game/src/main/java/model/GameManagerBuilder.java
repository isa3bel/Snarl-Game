package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * Builds a Snarl game.
 */
public class GameManagerBuilder {

  private final Level level;
  private ArrayList<Location> playerLocations;
  private ArrayList<Location> adversaryLocations;
  private final ArrayList<Player> players;
  private final ArrayList<Adversary> adversaries;
  private Key key;

  /**
   * Initializes a game manager with the bare minimum required - the level.
   * @param level the first level of the Snarl game
   * @throws IllegalArgumentException if level is null
   */
  public GameManagerBuilder(Level level) throws IllegalArgumentException {
    if (level == null) {
      throw new IllegalArgumentException("must pass in a level to create a Snarl game");
    }
    this.level = level;
    calculateCharacterLocations(level);
    this.players = new ArrayList<>();
    this.adversaries = new ArrayList<>();
  }

  /**
   * Creates the list of locations where players or adversaries can be automatically placed.
   * @param level the level in which the players will be placed
   */
  private void calculateCharacterLocations(Level level) {
    ArrayList<Location> roomTiles = level
        .filter((space, location) -> space.acceptVisitor(new IsTraversable()))
        .keySet()
        .stream()
        .sorted((location1, location2) -> location1.yCoordinate == location2.yCoordinate
            ? location1.xCoordinate - location2.xCoordinate
            : location1.yCoordinate - location2.yCoordinate
        )
        .collect(Collectors.toCollection(ArrayList::new));
    Space topLeft = level.get(roomTiles.get(0));
    Space bottomRight = level.get(roomTiles.get(roomTiles.size() - 1));

    this.playerLocations = roomTiles
        .stream()
        .filter(location -> topLeft.sameGroup(level.get(location)))
        .collect(Collectors.toCollection(ArrayList::new));
    this.adversaryLocations = roomTiles
        .stream()
        .filter(location -> bottomRight.sameGroup(level.get(location)))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Adds a player with a generated game location (the most top left available space in the level).
   * @return this builder with the player
   */
  public GameManagerBuilder addPlayer() throws IllegalStateException {
    if (this.playerLocations.size() < 1) {
      throw new IllegalStateException("top left room does not have enough available tiles" +
          " for automatic player placement");
    }
    return this.addPlayer(this.playerLocations.remove(0));
  }

  /**
   * Adds a player with the given location.
   * @param location the location where the player should be added
   * @return this builder with the player
   * @throws IllegalArgumentException if there are already 4 players in the game
   */
  public GameManagerBuilder addPlayer(Location location) throws IllegalArgumentException {
    if (this.players.size() >= 4) {
      throw new IllegalArgumentException("cannot have more than 4 players in a Snarl game");
    }
    this.players.add(new Player(location, this.players.size() + 1));
    return this;
  }

  /**
   * Adds an enemy with a generated game location (the most bottom right available space in the level).
   * @return this builder with the enemy
   */
  public GameManagerBuilder addEnemy() {
    if (this.adversaryLocations.size() < 1) {
      throw new IllegalStateException("bottom right room does not have enough available tiles" +
          " for automatic adversary placement");
    }
    return this.addEnemy(this.adversaryLocations.remove(0));
  }

  /**
   * Adds an enemy to the game at the given location.
   * @param location the location to place the adversary
   * @return this builder with the adversary
   */
  public GameManagerBuilder addEnemy(Location location) {
    this.adversaries.add(new Adversary(location));
    return this;
  }

  /**
   * Adds the key for this level at the given location.
   * @param location the location of the key in the game
   * @return this builder with the item
   * @throws IllegalStateException if the key in the level already exists
   */
  public GameManagerBuilder addKey(Location location) throws IllegalStateException {
    if (this.key != null) {
      throw new IllegalStateException("this game already has a key");
    }
    this.key = new Key(location);
    return this;
  }

  /**
   * Creates the GameManager with these values.
   * @return the constructed GameManager
   */
  public GameManager build() throws IllegalStateException {
    ArrayList<Character> characters = new ArrayList<>();
    characters.addAll(this.players);
    characters.addAll(this.adversaries);

    ArrayList<Item> items = new ArrayList<>();
    if (this.key != null) items.add(this.key);
    return new GameManager(this.level, characters, items);
  }
}
