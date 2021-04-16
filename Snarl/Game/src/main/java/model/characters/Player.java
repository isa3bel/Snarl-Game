package model.characters;

import model.controller.Controller;
import model.item.Item;
import model.level.Ejected;
import model.level.Exited;
import model.level.Level;
import model.ruleChecker.*;
import model.level.Location;
import view.View;

import java.util.ArrayList;

/**
 * A player controller by a user in the Snarl dungeon crawler.
 */
public class Player extends Character implements Comparable<Player>{

  private int timesExited;
  private int keysFound;
  private int timesEjected;

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
    this.currentLocation = new Ejected(this.currentLocation);
    this.timesEjected++;
  }

  /**
   * Add the given item to this player's inventory
   * @param item the item that the player is picking up
   */
  public void addToInventory(Item item) {
    System.out.println("Player " + this.getName() + " found the key");
    // right now a player doesn't really have an inventory, so there is nothing to add to the inventory
    this.keysFound++;
  }

  /**
   * Make this player exit the level.
   */
  public void exit() {
    System.out.println("Player " + this.getName() + " exited");
    this.timesExited++;
    this.moveTo(new Exited(this.currentLocation));
  }

  /**
   * Score this player and return the result.
   * @return the string representation of this players score
   */
  public String score() {
    return "{ \"type\": \"player-score\",\n" +
        "  \"name\": " + this.getName() + ",\n" +
        "  \"exits\": " + this.timesExited + ",\n" +
        "  \"ejects\": " + this.timesEjected + ",\n" +
        "  \"keys\": " + this.keysFound + "\n}";
  }

  @Override
  public PlayerMoveValidator getNextMove() {
    Location nextLocation = this.controller.getNextMove();
    return nextLocation == null
        ? new PlayerMoveValidator(this, this.currentLocation)
        : new PlayerMoveValidator(this, nextLocation);
  }

  @Override
  public Interaction makeInteraction(Level level, ArrayList<Player> players) {
    return new PlayerInteraction(this);
  }

  @Override
  public void updateController(View view) {
    this.controller.update(view);
  }

  @Override
  public <T> T acceptVisitor(CharacterVisitor<T> visitor) {
    return visitor.visitPlayer(this);
  }

  public <T> T acceptVisitor(InteractableVisitor<T> visitor) {
    return visitor.visitPlayer(this);
  }

  @Override
  public int compareTo(Player otherPlayer) {
    return otherPlayer.timesExited - this.timesExited;
  }
}
