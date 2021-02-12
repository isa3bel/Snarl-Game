package model;

import java.util.ArrayList;

public class LevelBuilder {

  private ArrayList<RoomBuilder> rooms;
  private ArrayList<HallwayBuilder> hallways;

  public LevelBuilder() {
    this.rooms = new ArrayList<>();
  }

  public LevelBuilder addRoom(RoomBuilder room) throws IllegalArgumentException {
    // TODO: does overlap need to be verified or can we assume valid input?
    if (this.rooms.stream().anyMatch(existingRoom -> room.overlaps(existingRoom))) {
      throw new IllegalArgumentException("cannot add a room that overlaps with another room");
    }
    if (this.hallways.stream().anyMatch(existingHallway -> room.overlaps(existingHallway))) {
      throw new IllegalArgumentException("cannot add a room that overlaps with another room");
    }

    this.rooms.add(room);
    return this;
  }

  public LevelBuilder addHallway(HallwayBuilder hallway) {
    // TODO: does overlap need to be verified or can we assume valid input?
    if (this.rooms.stream().anyMatch(existingRoom -> existingRoom.overlaps(hallway))) {
      throw new IllegalArgumentException("cannot add a room that overlaps with another room");
    }
    if (this.hallways.stream().anyMatch(existingHallway -> existingHallway.overlaps(hallway))) {
      throw new IllegalArgumentException("cannot add a room that overlaps with another room");
    }

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
