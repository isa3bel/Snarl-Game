package model;

import java.util.ArrayList;

/**
 * A way to build to a Level.
 */
public class LevelBuilder {

  private final ArrayList<RoomBuilder> rooms;
  private final ArrayList<HallwayBuilder> hallways;

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
    // DECISION: no validation to assert that there are no overlaps
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
   * Creates the level that this RoomBuilder would create.
   * @return the Level built by this LevelBuilder
   * @throws IllegalStateException when the level has more than one exit
   */
  public Level build() throws IllegalStateException {
    // DECISION: there is only one exit for a level per level
    if (this.rooms.stream().filter(RoomBuilder::hasExit).count() != 1) {
      throw new IllegalStateException("level must have exactly one level exit");
    }

    ArrayList<ArrayList<Space>> spaces = new ArrayList<>();
    this.rooms.forEach(room -> room.build(spaces));
    this.hallways.forEach(hallway -> hallway.build(spaces));
    return new Level(spaces);
  }
}
