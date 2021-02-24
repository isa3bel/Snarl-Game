package model;

import java.util.ArrayList;

/**
 * A way to build to a Level.
 */
public class LevelBuilder {

  private final ArrayList<RoomBuilder> rooms;
  private final ArrayList<HallwayBuilder> hallways;
  private Location key;

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
   * Creates a key for the given level.
   * @param level the level that this key is for
   * @return this builder with the item
   * @throws IllegalStateException if the key in the level already exists
   */
  private Key makeKey(Level level) throws IllegalStateException {
    Location forDoorAt = level
        .filter((space, spaceLocation) -> space.acceptVisitor(new IsExit()))
        .keySet()
        .stream()
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("exit exists in this level"));

    Space maybeExit = level.get(forDoorAt);
    if (!(maybeExit instanceof Exit)) {
      throw new IllegalStateException("location of given exit for this key is not actually an exit");
    }

    Exit exit = (Exit) maybeExit;
    return new Key(this.key, exit);
  }

  /**
   * Creates the level that this RoomBuilder would create.
   * @return the Level built by this LevelBuilder
   * @throws IllegalStateException when the level has more than one exit
   */
  public Level build() throws IllegalStateException {
    ArrayList<ArrayList<Space>> spaces = new ArrayList<>();
    this.rooms.forEach(room -> room.build(spaces));
    this.hallways.forEach(hallway -> hallway.build(spaces));

    ArrayList<Item> items = new ArrayList<>();
    Level level = new Level(spaces, items);
    if (this.key != null) items.add(this.makeKey(level));
    return level;
  }
}
