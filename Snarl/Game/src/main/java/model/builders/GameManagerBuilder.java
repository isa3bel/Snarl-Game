package model.builders;

import model.GameManager;
import model.level.Location;
import model.characters.*;
import model.level.Level;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Builds a Snarl game.
 */
public class GameManagerBuilder {

  private final int currentLevel;
  private final Level[] levels;
  private final ArrayList<Location> playerLocations;
  private final HashMap<Integer, ArrayList<Location>> adversaryLocations;
  private final ArrayList<Player> players;
  private final HashMap<Integer, ArrayList<Adversary>> adversaries;

  /**
   * Initializes a game manager with the bare minimum required - the level.
   * @param levels the array of levels for a snarl game
   * @throws IllegalArgumentException if level is null
   */
  public GameManagerBuilder(int currentLevel, Level[] levels) throws IllegalArgumentException {
    if (levels == null) {
      throw new IllegalArgumentException("must pass in an array of levels to create a Snarl game");
    }
    if (levels.length == 0) {
      throw new IllegalArgumentException("level array must be non empty");
    }
    if (currentLevel >= levels.length) {
      throw new IllegalArgumentException("current level must point to a level in levels," +
          " expected a number in range [0, " + levels.length + "), got: " + currentLevel);
    }
    this.currentLevel = currentLevel;
    this.levels = levels;
    this.playerLocations = levels[currentLevel].calculatePlayerLocations();
    this.players = new ArrayList<>();

    this.adversaryLocations = new HashMap<>();
    for (int levelIdx = 0; levelIdx < levels.length; levelIdx++) {
      this.adversaryLocations.put(levelIdx, levels[levelIdx].calculateAdversaryLocations());
    }
    this.adversaries = new HashMap<>();
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
    this.players.add(new Player(location, this.players.size() + 1, null));
    return this;
  }

  /**
   * Adds a player with a generated game location (the most top left available space in the level).
   * @return this builder with the player
   */
  public GameManagerBuilder addAdversary(int level) throws IllegalStateException {
    if (level >= this.levels.length) {
      throw new IllegalArgumentException("level does not exist in this game");
    }
    if (this.adversaryLocations.get(level).size() < 1) {
      throw new IllegalStateException("top left room does not have enough available tiles" +
          " for automatic adversary placement");
    }
    return this.addAdversary(level, this.adversaryLocations.get(level).remove(0));
  }

  /**
   * Adds a player with the given location.
   * @param location the location where the player should be added
   * @return this builder with the player
   * @throws IllegalArgumentException if there are already 4 players in the game
   */
  public GameManagerBuilder addAdversary(int level, Location location) throws IllegalArgumentException {
    if (level >= this.levels.length) {
      throw new IllegalArgumentException("level does not exist in this game");
    }
    ArrayList<Adversary> levelAdversaries = this.adversaries.computeIfAbsent(level, key -> new ArrayList<>());

    levelAdversaries.add(new Ghost(location, "ghost" + levelAdversaries.size()));
    return this;
  }

  /**
   * Creates the GameManager with these values.
   * @return the constructed GameManager
   */
  public GameManager build() throws IllegalStateException {
    for (int levelIdx = 0; levelIdx < this.levels.length; levelIdx++) {
      this.levels[levelIdx].addAdversaries(this.adversaries.get(levelIdx));
    }
    return new GameManager(this.currentLevel, this.levels, this.players);
  }
}
