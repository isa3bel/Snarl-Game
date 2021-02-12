package model;

import java.util.ArrayList;

/**
 * A way to build to a Level.
 */
public class LevelBuilder {

  private ArrayList<RoomBuilder> rooms;
  private ArrayList<HallwayBuilder> hallways;

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
   * @param hallway the hallwaya to add
   * @return the LevelBuilder with the added hallway
   */
  public LevelBuilder addHallway(HallwayBuilder hallway) {
    this.hallways.add(hallway);
    return this;
  }

  /**
   * Creates the level that this RoomBuilder would create
   * @return
   */
  public Level build() {
    ArrayList<ArrayList<Space>> spaces = new ArrayList<>();

    this.rooms.stream().forEach(room -> room.build(spaces));
    this.hallways.stream().forEach(hallway -> hallway.build(spaces));
    return new Level(spaces);
  }
}
