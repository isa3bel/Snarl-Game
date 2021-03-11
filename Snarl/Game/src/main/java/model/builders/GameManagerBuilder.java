package model.builders;

import model.GameManager;
import model.level.Location;
import model.characters.*;
import model.characters.Character;
import model.level.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Builds a Snarl game.
 */
public class GameManagerBuilder {

  private final int currentLevel;
  private final Level[] levels;
  private final ArrayList<Location> playerLocations;
  private final ArrayList<Location> adversaryLocations;
  private final ArrayList<Player> players;
  private final ArrayList<Adversary> adversaries;

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
    this.adversaryLocations = levels[currentLevel].calculateAdversaryLocations();
    this.players = new ArrayList<>();
    this.adversaries = new ArrayList<>();
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
    Adversary adversary = new Random().nextBoolean()
        ? new Ghost(location, null)
        : new Zombie(location, null);
    this.adversaries.add(adversary);
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

    return new GameManager(this.currentLevel, this.levels, characters);
  }
}
