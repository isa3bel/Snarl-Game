package model.builders;

import model.item.Item;
import model.item.Key;
import model.level.Location;
import model.item.Exit;
import model.level.Level;
import model.level.Space;

import java.util.ArrayList;

/**
 * A way to build to a Level.
 */
public class LevelBuilder {

  private final ArrayList<RoomBuilder> rooms;
  private final ArrayList<HallwayBuilder> hallways;
  private Location key;
  private Location exit;
  private boolean exitLocked;

  public LevelBuilder() {
    this.rooms = new ArrayList<>();
    this.hallways = new ArrayList<>();
  }

  /**
   * Adds a room to the level.
   * @param room the room to add
   * @return the LevelBuilder with the added room
   */
  public LevelBuilder addRoom(RoomBuilder room) {
    this.rooms.add(room);
    return this;
  }

  /**
   * Adds a hallway to the level.
   * @param hallway the hallway to add
   * @return the LevelBuilder with the added hallway
   */
  public LevelBuilder addHallway(HallwayBuilder hallway) {
    // DECISION: no validation to assert that there are no overlaps
    // DECISION: a hallway can be right next to a room, making it look like the room
    // extends outward
    // e.g. XXXXXXXXXXXXX instead of forcing XXXXXXXXXXXXX
    //      XD         DX  and additional    X           X
    //      X   XXXXXX  X  set of waypoints  XDXXXXXXXXXDX
    //      XXXXXXXXXXXXX      as seen ->    X   XXXXXX  X
    //                                       XXXXXXXXXXXXX
    this.hallways.add(hallway);
    return this;
  }

  /**
   * Adds a key for this game at the given location.
   * @param location the location to place this key at
   * @return the game manager with this key
   * @throws IllegalStateException if there is not a valid exit in this game
   */
  public LevelBuilder addKey(Location location) throws IllegalArgumentException {
    if (location == null) {
      throw new IllegalArgumentException("key must have a location");
    }
    this.key = location;
    return this;
  }

  /**
   * Adds a level exit to this room.
   * @param exitLocation the exit's location in the level
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the exit is not on the room's boundary
   */
  public LevelBuilder addExit(Location exitLocation) throws IllegalStateException {
    return this.addExit(exitLocation, true);
  }

  /**
   * Adds a level exit to this room with a given locked status.
   * @param exitLocation the exit's location in the level
   * @param locked the exit's locked status
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the exit is not on the room's boundary
   */
  public LevelBuilder addExit(Location exitLocation, boolean locked) throws IllegalStateException {
    if (this.exit != null) {
      throw new IllegalStateException("an exit has already been made in this room");
    }
    this.exit = exitLocation;
    this.exitLocked = locked;
    return this;
  }

  /**
   * Creates the level that this RoomBuilder would create.
   * @return the Level built by this LevelBuilder
   * @throws IllegalStateException when the level has more than one exit
   */
  public Level build() throws IllegalStateException {
    if (this.exit == null) {
      throw new IllegalStateException("level must have exactly one level exit");
    }

    ArrayList<ArrayList<Space>> spaces = new ArrayList<>();
    this.rooms.forEach(room -> room.build(spaces));
    this.hallways.forEach(hallway -> hallway.build(spaces));
    Exit exit = new Exit(this.exit, this.exitLocked);

    ArrayList<Item> items = new ArrayList<>();
    Level level = new Level(spaces, items);
    items.add(exit);
    if (this.key != null) items.add(new Key(this.key, exit));
    return level;
  }
}
