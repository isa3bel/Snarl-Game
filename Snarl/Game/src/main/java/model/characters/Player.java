package model.characters;

import model.GameManager;
import model.controller.Controller;
import model.item.Item;
import model.level.Ejected;
import model.level.Level;
import model.ruleChecker.*;
import model.level.Location;
import view.PlayerView;

import java.util.ArrayList;

/**
 * A player controller by a user in the Snarl dungeon crawler.
 */
public class Player extends Character implements Comparable<Player>{

  private int timesExited;
  private int keysFound;

  /**
   * Creates a player at the given location with the given id.
   * @param location the location to initialize the player at
   * @param name the player's name
   */
  public Player(Location location, String name) {
    super(location, name);
    this.timesExited = 0;
    this.keysFound = 0;
  }

  public String leaderBoard() {
    return this.getName() + " found " + this.keysFound + " keys. Exited " + this.timesExited + " times.";
  }

  /**
   * Creates a player at the given location with the given id and controller.
   * @param location the location to initialize the player at
   * @param name the player's name
   */
  public Player(Location location, String name, Controller controller) {
    super(location, name, controller);
    this.timesExited = 0;
    this.keysFound = 0;
  }

  /**
   * Updates the player after defending against an attack from an adversary.
   */
  public void defend() {
    System.out.println("Player " + this.getName() + " was expelled");
    this.currentLocation = new Ejected(this.currentLocation);
  }

  public void addToInventory(Item item) {
    // right now a player doesn't need an inventory, so this isn't necessary
    // TODO: THIS IS ONLY USED IN THE MILESTONE 7 TESTING TASK
    this.keysFound++;
  }

  public void incrementTimesExited() {
    this.timesExited++;
  }

  @Override
  public PlayerMoveValidator getNextMove() {
    Location nextLocation = this.controller.getNextMove();
    return nextLocation == null
        ? new PlayerMoveValidator(this, this.currentLocation)
        : new PlayerMoveValidator(this, nextLocation);

  }

  @Override
  public Interaction<Player> makeInteraction(Level level, ArrayList<Player> players) {
    return new PlayerInteraction(this);
  }

  @Override
  public void updateController(GameManager gameManager) {
    PlayerView view = new PlayerView(this);
    gameManager.buildView(view);
    this.controller.update(view);
  }

  @Override
  public <T> T acceptVisitor(CharacterVisitor<T> visitor) {
    return visitor.visitPlayer(this);
  }

  public void acceptVisitor(InteractableVisitor visitor) {
    visitor.visitPlayer(this);
  }

  @Override
  public int compareTo(Player otherPlayer) {
    return otherPlayer.timesExited - this.timesExited;
  }
}
