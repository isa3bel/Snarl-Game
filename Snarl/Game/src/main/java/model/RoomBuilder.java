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
   * @throws IllegalArgumentException if x coordinate or y coordinate are negative or width or height are not positive
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
    // DECISION: not validating if a door is reachable (e.g. no walls in front or
    // the door is on the corner of the room
    // e.g. XXXXD  OR XXDX
    //      XX  X     X XX  (if the room is technically 2 wide but
    //      XXXXX     XXXX   a wall is placed at the edge of the room)
    checkDoorPlacement(doorX, doorY);

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
  public RoomBuilder addExit(int exitX, int exitY) throws IllegalArgumentException {
    checkDoorPlacement(exitX, exitY);
    if (this.exit != null) {
      throw new IllegalStateException("an exit has already been made in this room");
    }

    this.exit = new Location(exitX, exitY);
    return this;
  }

  /**
   * Determines whether the given door coordinates are on a room boundary
   * @param doorX the door's x coordinate
   * @param doorY the door's y coordinate
   */
  private void checkDoorPlacement(int doorX, int doorY) throws IllegalArgumentException {
    if (!(doorX == this.topLeft.xCoordinate - 1
        || doorX == this.topLeft.xCoordinate + this.width
        || doorY == this.topLeft.yCoordinate - 1
        || doorY == this.topLeft.yCoordinate + this.height)) {
      throw new IllegalArgumentException("door must be on room boundary - x value on " +
          (this.topLeft.xCoordinate - 1) + " or " + (this.topLeft.xCoordinate + this.width) + " or y value on " +
          (this.topLeft.yCoordinate - 1) + " or " + (this.topLeft.yCoordinate + this.height) + ", given: " + doorX + ", " + doorY);
    }
  }

  /**
   * Adds a wall to this room.
   * @param wallX the wall's x coordinate relative to topLeft
   * @param wallY the wall's y coordinate relative to topLeft
   * @return this RoomBuilder with the wall
   * @throws IllegalArgumentException if x coordinate or y coordinate are negative or outside room bounds
   */
  public RoomBuilder addWall(int wallX, int wallY) throws IllegalArgumentException {
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

    Location bottomRight = new Location(this.topLeft.xCoordinate + this.width, this.topLeft.yCoordinate
        + this.height);
    this.initSize(bottomRight, spaces);

    for (int currY = this.topLeft.yCoordinate; currY < this.topLeft.yCoordinate + this.height; currY++) {
      for (int currX = this.topLeft.xCoordinate; currX < this.topLeft.xCoordinate + this.width; currX++) {
        spaces.get(currY).set(currX, new Tile(this.toString()));
      }
    }

    this.doors.forEach(door -> spaces.get(door.yCoordinate).set(door.xCoordinate, new Door(this.toString())));
    if (this.exit != null) spaces.get(this.exit.yCoordinate).set(this.exit.xCoordinate, new Exit(this.toString()));
    this.walls.forEach(wall -> spaces.get(wall.yCoordinate).set(wall.xCoordinate, new Wall(this.toString())));
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
   * @param otherRoom the room to go to
   * @return the locations between the door on this room and the door on the given room
   */
  ArrayList<Location> calculateLocationsFromRoomDoors(RoomBuilder otherRoom) {
    Location startingDoor = this.getClosestConnectingDoor(otherRoom);
    Location endingDoor = otherRoom.getClosestDoorOnAxis(startingDoor);

    ArrayList<Location> locations = startingDoor.to(endingDoor);
    locations.remove(startingDoor);
    locations.remove(endingDoor);
    return locations;
  }

  /**
   * Gets the door in this room that is closest to any door in that room.
   * @param otherRoom the room this room is connecting to
   * @return the location of the door leading to the other room
   */
  private Location getClosestConnectingDoor(RoomBuilder otherRoom) {
    Optional<Location> maybeDoor = this.doors.stream().min(Comparator.comparingInt(
        thisDoor -> otherRoom.doors.stream()
            .filter(new Location.SameAxis(thisDoor))
            .map(thisDoor::euclidianDistance)
            .min(Comparator.comparingInt(a -> a))
            .orElse(Integer.MAX_VALUE)));

    return maybeDoor.orElseThrow(() -> new IllegalStateException("no valid doors to connect these rooms"));
  }

  /**
   * Find the door on the same axis of the given Location.
   * @param locationToFind the location to find on the axis
   * @return the location of the door on the same axis as this location
   * @throws IllegalArgumentException if the given location is not on the same axis as any of the doors in this room
   */
  Location getClosestDoorOnAxis(Location locationToFind) throws IllegalArgumentException {
    Optional<Location> doorLocation = this.doors.stream().filter(new Location.SameAxis(locationToFind))
        .min(Comparator.comparingInt(door -> door.euclidianDistance(locationToFind)));
    doorLocation.orElseThrow(() -> new IllegalArgumentException("no doors on the same axis as the given location"));

    return doorLocation.get();
  }
}
