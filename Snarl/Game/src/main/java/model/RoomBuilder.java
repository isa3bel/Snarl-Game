package model;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;

/**
 * Helper to create a room in a level.
 */
public class RoomBuilder extends SpaceBuilder {

  private final Location topLeft;
  private final int width;
  private final int height;
  private final HashSet<Location> doors;
  private Location exit;
  private final HashSet<Location> walls;

  /**
   * Instantiates a RoomBuilder with the required properties.
   * @param x x coordinate of the top left corner of this room
   * @param y y coordinate of the top left corner of this room
   * @param width width of the room
   * @param height height of the room
   * @throws IllegalArgumentException if x or y are negative or width or height are not positive
   */
  public RoomBuilder(int x, int y, int width, int height) throws IllegalArgumentException {
    if (x <= 0 || y <= 0) {
      throw new IllegalArgumentException("top left coordinate must have positive x and y coordinates, given: " +
          x + ", " + y);
    }
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("width and height must be positive, given: " + width + ", " + height);
    }

    this.topLeft = new Location(x, y);
    this.width = width;
    this.height = height;
    this.doors = new HashSet<>();
    this.walls = new HashSet<>();
  }

  /**
   * Adds a door to this room.
   * @param doorX the door's x coordinate
   * @param doorY the door's y coordinate
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the door is not on the room's boundary
   */
  public RoomBuilder door(int doorX, int doorY) throws IllegalArgumentException {
    if (!(doorX == this.topLeft.x - 1 || doorX == this.topLeft.x + this.width ||
        doorY == this.topLeft.y - 1 || doorY == this.topLeft.y + this.height)) {
      throw new IllegalArgumentException("door must be on room boundary - x value on " +
          (this.topLeft.x - 1) + " or " + (this.topLeft.x + this.width) + " or y value on " +
          (this.topLeft.y - 1) + " or " + (this.topLeft.y + this.height) + ", given: " + doorX + ", " + doorY);
    }
    // DECISION: not validating if a door is reachable (e.g. no walls in front or
    // the door is on the corner of the room
    // e.g. XXXXD  OR XXDX
    //      XX  X     X XX  (if the room is technically 2 wide but
    //      XXXXX     XXXX   a wall is placed at the edge of the room)
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
    if (!(exitX == this.topLeft.x - 1 || exitX == this.topLeft.x + this.width ||
        exitY == this.topLeft.y - 1 || exitY == this.topLeft.y + this.height)) {
      throw new IllegalArgumentException("door must be on room boundary - x value on " +
          (this.topLeft.x - 1) + " or " + (this.topLeft.x + this.width) + " or y value on " +
          (this.topLeft.y - 1) + " or " + (this.topLeft.y + this.height) + ", given: " + exitX + ", " + exitY);
    }
    if (this.exit != null) {
      throw new IllegalStateException("an exit has already been made in this room");
    }

    this.exit = new Location(exitX, exitY);
    return this;
  }

  /**
   * Adds a wall to this room.
   * @param wallX the wall's x coordinate relative to topLeft
   * @param wallY the wall's y coordinate relative to topLeft
   * @return this RoomBuilder with the wall
   * @throws IllegalArgumentException if x or y are negative or outside room bounds
   */
  public RoomBuilder wall(int wallX, int wallY) throws IllegalArgumentException {
    if (wallX >= this.width || wallY >= this.height) {
      throw new IllegalArgumentException("wall added to this room must be inside room bounds of (" +
          this.width + ", " + this.height +  "), given: " + wallX + ", " + wallY);
    }
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
    if (this.doors.stream().anyMatch(this.walls::contains)) {
      throw new IllegalStateException("no door can also be a wall");
    }
    if (this.walls.contains(this.exit)) {
      throw new IllegalStateException("level exit cannot also be a wall");
    }
    if (this.doors.contains(this.exit)) {
      throw new IllegalStateException("level exit cannot also be a door");
    }

    Location bottomRight = new Location(this.topLeft.x + this.width, this.topLeft.y + this.height);
    this.initSize(bottomRight, spaces);

    for (int currY = this.topLeft.y; currY < this.topLeft.y + this.height; currY++) {
      for (int currX = this.topLeft.x; currX < this.topLeft.x + this.width; currX++) {
        spaces.get(currY).set(currX, new Tile(this.toString()));
      }
    }

    this.doors.forEach(door -> spaces.get(door.y).set(door.x, new Door(this.toString())));
    if (this.exit != null) spaces.get(this.exit.y).set(this.exit.x, new Exit(this.toString()));
    this.walls.forEach(wall -> spaces.get(wall.y).set(wall.x, new Wall(this.toString())));
  }

  /**
   * Does this room have an exit?
   * @return whether this room would be built with an exit
   */
  boolean hasExit() {
    return this.exit != null;
  }

  /**
   * Gets all the locations that would make up a hallway directly between these two rooms.
   * @param room the room to go to
   * @return the locations between the door on this room and the door on the given room
   */
  ArrayList<Location> betweenDoors(RoomBuilder room) {
    Location first = this.doorToRoom(room);
    Location last = room.closestDoorOnAxis(first);

    ArrayList<Location> locations = first.to(last);
    locations.remove(first);
    locations.remove(last);
    return locations;
  }

  /**
   * Gets the door on this room that is closest to any door on that room.
   * @param that the room this is connecting to
   * @return the door leading to that room
   */
  private Location doorToRoom(RoomBuilder that) {
    Optional<Location> maybeDoor = this.doors.stream().min(Comparator.comparingInt(
        thisDoor -> that.doors.stream()
            .filter(new Location.SameAxis(thisDoor))
            .map(thisDoor::euclidianDistance)
            .min(Comparator.comparingInt(a -> a))
            .orElse(Integer.MAX_VALUE)));

    return maybeDoor.orElseThrow(() -> new IllegalStateException("no valid doors to connect these rooms"));
  }

  /**
   * Find the door on the same axis of the given Location.
   * @param location the location to find on the axis
   * @return the location of the door on the same axis as this location
   * @throws IllegalArgumentException if the given location is not on the same axis as any of the doors in this room
   */
  Location closestDoorOnAxis(Location location) throws IllegalArgumentException {
    Optional<Location> maybeLocation = this.doors.stream().filter(new Location.SameAxis(location))
        .min(Comparator.comparingInt(door -> door.euclidianDistance(location)));
    maybeLocation.orElseThrow(() -> new IllegalArgumentException("no doors on the same axis as the given location"));
    return maybeLocation.get();
  }
}
