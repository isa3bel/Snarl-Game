package model.builders;

import java.util.HashMap;
import model.GameManager;
import model.controller.Controller;
import model.controller.StdinController;
import model.level.Location;
import model.characters.*;
import model.level.Level;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Builds a Snarl game.
 */
public class GameManagerBuilder {

  private final int currentLevel;
  private final Level[] levels;
  private final HashMap<String, Controller> playerNames;
  private final ArrayList<Controller> zombies;
  private final ArrayList<Controller> ghosts;

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
    this.playerNames = new HashMap<>();
    this.zombies = new ArrayList<>();
    this.ghosts = new ArrayList<>();
  }

  /**
   * Adds a player with a generated game location (the most top left available space in the level).
   * @return this builder with the player
   * @throws IllegalStateException if there are already 4 players registered
   */
  public GameManagerBuilder addPlayer(String name) throws IllegalStateException {
    if (this.playerNames.size() >= 4) {
      throw new IllegalStateException("cannot have more than 4 players in a Snarl game");
    }
    this.playerNames.put(name, new StdinController(name));
    return this;
  }

  /**
   * Adds a player with a generated game location (the most top left available space in the level).
   * @param controller the controller for this playere
   * @return this builder with the player
   * @throws IllegalStateException if there are already 4 players registered
   */
  public GameManagerBuilder addPlayer(String name, Controller controller) throws IllegalStateException {
    if (this.playerNames.size() >= 4) {
      throw new IllegalStateException("cannot have more than 4 players in a Snarl game");
    }
    this.playerNames.put(name, controller);
    return this;
  }

  /**
   * Adds a player with a generated game location (the most top left available space in the level).
   * @param controller the controller that will control the added zombie
   * @return this builder with the player
   */
  public GameManagerBuilder addZombie(Controller controller) {
    this.zombies.add(controller);
    return this;
  }

  /**
   * Adds a player with a generated game location (the most top left available space in the level).
   * @param controller the controller that will control the added ghost
   * @return this builder with the player
   */
  public GameManagerBuilder addGhost(Controller controller) {
    this.ghosts.add(controller);
    return this;
  }

  /**
   * Creates the GameManager with these values.
   * @return the constructed GameManager
   */
  public GameManager build() throws IllegalStateException {
    for (int levelIdx = 0; levelIdx < this.levels.length; levelIdx++) {
      ArrayList<Adversary> adversaries = this.makeAdversaryList(levelIdx);
      this.levels[levelIdx].addAdversaries(adversaries);
    }
    ArrayList<Location> validPlayerStartingLocations = this.levels[this.currentLevel].calculateValidActorPositions();

    if (validPlayerStartingLocations.size() < playerNames.size()) {
      throw new IllegalStateException("level does not have enough valid starting positions for players");
    }

    ArrayList<Player> players = this.playerNames.keySet().stream()
        .map(name -> new Player(validPlayerStartingLocations.remove(0), name, this.playerNames.get(name)))
        .collect(Collectors.toCollection(ArrayList::new));
    return new GameManager(this.currentLevel, this.levels, players);
  }

  /**
   * Make a list of adversaries to add at the level given.
   * @param levelIdx the number of the level given
   * @return a list of adversaries that will be places in that level
   */
  private ArrayList<Adversary> makeAdversaryList(int levelIdx) {
    ArrayList<Adversary> adversaries = new ArrayList<>();

    // We add one to levelIdx because the arrays are 0-indexed,
    // but calculating the number of adversaries requires the level to start at 1-indexed.

    int numZombies = ((levelIdx + 1) / 2) + 1;
    for (int zombieIdx = 0; zombieIdx < numZombies; zombieIdx++) {
      // location is set in level.addAdversary
      Zombie zombie = this.zombies.size() == 0
          ? new Zombie(null, "zombie" + zombieIdx)
          : new Zombie(null, "zombie" + zombieIdx, this.zombies.remove(0));
      adversaries.add(zombie);
    }

    int numGhost = ((levelIdx + 1) - 1) / 2;
    for (int ghostIdx = 0; ghostIdx < numGhost; ghostIdx++) {
      // location is set in level.addAdversary
      Ghost ghost = this.ghosts.size() == 0
          ? new Ghost(null, "ghost" + ghostIdx)
          : new Ghost(null, "ghost" + ghostIdx, this.ghosts.remove(0));
      adversaries.add(ghost);
    }
    return adversaries;
  }
}
