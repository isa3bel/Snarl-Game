package model;


import java.util.ArrayList;
import java.util.Optional;

/**
 * Helper to create a room in a level.
 */
public class RoomBuilder {

  private Location topLeft;
  private int width;
  private int height;
  private ArrayList<Location> doors = new ArrayList<>();
  private Location exit;
  private ArrayList<Location> walls = new ArrayList<>();

  /**
   * Instantiates a RoomBuilder with the reqiured properties.
   * @param x x coordinate of the top left corner of this room
   * @param y y coordinate of the top left corner of this room
   * @param width width of the room
   * @param height height of the room
   * @throws IllegalArgumentException if x or y are negative or width or height are not positive
   */
  public RoomBuilder(int x, int y, int width, int height) throws IllegalArgumentException {
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("width and height must be positive, given: " + width + ", " + height);
    }

    this.topLeft = new Location(x, y);
    this.width = width;
    this.height = height;
  }

  /**
   * Adds a door to this room.
   * @param doorX the door's x coordinate
   * @param doorY the door's y coordinate
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the door is not on the room's boundary
   */
  public RoomBuilder door(int doorX, int doorY) throws IllegalArgumentException {
    if (doorX != this.topLeft.x && doorX != this.topLeft.x + this.width) {
      throw new IllegalArgumentException("door must be on room boundary - x value should be " +
          this.topLeft.x + " or " + (this.topLeft.x + this.width) + ", given: " + doorX);
    }
    if (doorY != this.topLeft.y && doorY != this.topLeft.y + this.height) {
      throw new IllegalArgumentException("door must be on room boundary - y value should be " +
          this.topLeft.y + " or " + (this.topLeft.y + this.height) + ", given: " + doorY);
    }

    this.doors.add(new Location(doorX, doorY));
    return this;
  }

  /**
   * Adds a level exit to this room.
   * @param exitX the exit's x coordinate
   * @param exitY the exit's y coordinate
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the exit is not on the room's boundary
   */
  public RoomBuilder exit(int exitX, int exitY) throws IllegalArgumentException {
    if (exitX != this.topLeft.x && exitX != this.topLeft.x + this.width) {
      throw new IllegalArgumentException("exit must be on room boundary - x value should be " +
          this.topLeft.x + " or " + (this.topLeft.x + this.width) + ", given: " + exitX);
    }
    if (exitY != this.topLeft.y && exitY != this.topLeft.y + this.height) {
      throw new IllegalArgumentException("exit must be on room boundary - y value should be " +
          this.topLeft.y + " or " + (this.topLeft.y + this.height) + ", given: " + exitY);
    }
    if (this.exit != null) {
      throw new IllegalStateException("an exit has already been made in this room");
    }

    this.exit = new Location(exitX, exitY);
    return this;
  }


  /**
   * Adds a wall to this room.
   * @param wallX the wall's x coordinate
   * @param wallY the wall's y coordinate
   * @return this RoomBuilder with the wall
   * @throws IllegalArgumentException if x or y are negative
   */
  public RoomBuilder wall(int wallX, int wallY) throws IllegalArgumentException {
    this.walls.add(new Location(wallX, wallY));
    return this;
  }

  /**
   * Adds this room to the given spaces array.
   * @param spaces the spaces to configure this room on
   * @throws IllegalStateException if the room has no doors or has a door that is also a wall
   */
  void build(ArrayList<ArrayList<Space>> spaces) throws IllegalStateException {
    if (this.doors.size() < 1 && this.exit == null) {
      throw new IllegalStateException("room must have at least 1 door");
    }
    if (this.doors.stream().anyMatch(door -> this.walls.contains(door))) {
      throw new IllegalStateException("no door can also be a wall");
    }

    this.initSize(spaces);
    for (int currY = this.topLeft.y; currY < this.topLeft.y + this.height; currY++) {
      for (int currX = this.topLeft.x; currX < this.topLeft.x + this.width; currX++) {
        spaces.get(currY).set(currX, new Tile(this.toString()));
      }
    }
  }

  /**
   * Does this room have an exit?
   * @return whether this room would be built with an exit
   */
  boolean hasExit() {
    return this.exit != null;
  }

  /**
   * Initialize the given spaces array to the required size for this room.
   * @param spaces the spaces array
   */
  private void initSize(ArrayList<ArrayList<Space>> spaces) {
    int maxX = this.topLeft.x + this.width;
    int maxY = this.topLeft.y + this.height;

    // guarantee the min number of rows
    while (spaces.size() < maxY) {
      spaces.add(new ArrayList<>());
    }

    // for each row in spaces
    for (int y = 0; y < maxY; y++) {
      ArrayList<Space> row = spaces.get(y);
      // guarantee the min number of spaces in that row
      while (row.size() < maxX) {
        row.add(null);
      }
    }
  }

  /**
   * Gets all the locations that would make up a hallway directly between these two rooms.
   * @param room the room to go to
   * @return the locations between the door on this room and the door on the given room
   */
  ArrayList<Location> betweenDoors(RoomBuilder room) {
    Optional<Location> maybeFirst = this.doors.stream().filter(
        thisDoor ->  room.doors.stream().anyMatch(new Location.SameAxis(thisDoor))
    ).findFirst();
    maybeFirst.orElseThrow(() -> new IllegalStateException("no valid doors to connect these rooms"));

    Location first = maybeFirst.get();
    Location last = doorOnAxis(first);
    return first.to(last);
  }

  /**
   * Find the door on the same axis of the given Location.
   * @param location the location to find on the axis
   * @return the location of the door on the same axis as this location
   * @throws IllegalArgumentException if the given location is not on the same axis as any of the doors in this room
   */
  Location doorOnAxis(Location location) throws IllegalArgumentException {
    Optional<Location> maybeLocation = this.doors.stream().filter(new Location.SameAxis(location)).findFirst();
    maybeLocation.orElseThrow(() -> new IllegalArgumentException("no doors on the same axis as the given location"));
    return maybeLocation.get();
  }

  boolean overlaps(RoomBuilder room) {
    // TODO: implement this
    return false;
  }

  boolean overlaps(HallwayBuilder hallway) {
    // TODO: implement this
    return false;
  }
}
