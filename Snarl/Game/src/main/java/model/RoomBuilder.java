package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Helper to create a room in a level.
 */
public class RoomBuilder extends SpaceBuilder {

  private final Location topLeft;
  private final int width;
  private final int height;
  private final HashSet<Location> doors;
  private Location exit;
  private boolean exitLocked;
  private final HashSet<Location> walls;

  /**
   * Instantiates a RoomBuilder with the required properties.
   * @param row row of the top left corner of this room
   * @param column column of the top left corner of this room
   * @param width width of the room
   * @param height height of the room
   * @throws IllegalArgumentException if row coordinate or column are negative or width or height are not positive
   */
  public RoomBuilder(int row, int column, int width, int height) throws IllegalArgumentException {
    if (row <= 0 || column <= 0) {
      throw new IllegalArgumentException("top left coordinate must have positive row and column, given: " +
          row + ", " + column);
    }
    if (width <= 0 || height <= 0) {
      throw new IllegalArgumentException("width and height must be positive, given: " + width + ", " + height);
    }

    this.topLeft = new Location(row, column);
    this.width = width;
    this.height = height;
    this.doors = new HashSet<>();
    this.walls = new HashSet<>();
  }

  /**
   * Adds a door to this room.
   * @param doorRow the door's row
   * @param doorColumn the door's column
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the door is not on the room's boundary
   */
  public RoomBuilder addDoor(int doorRow, int doorColumn) throws IllegalArgumentException {
    // DECISION: not validating if a door is reachable (e.g. no walls in front or
    // the door is on the corner of the room
    // e.g. XXXXD  OR XXDX
    //      XX  X     X XX  (if the room is technically 2 wide but
    //      XXXXX     XXXX   a wall is placed at the edge of the room)
    checkDoorPlacement(doorRow, doorColumn);

    this.doors.add(new Location(doorRow, doorColumn));
    return this;
  }

  /**
   * Adds a level exit to this room.
   * @param exitRow the exit's row
   * @param exitColumn the exit's column
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the exit is not on the room's boundary
   */
  public RoomBuilder addExit(int exitRow, int exitColumn) throws IllegalArgumentException {
    return this.addExit(exitRow, exitColumn, false);
  }

  /**
   * Adds a level exit to this room with a given locked status.
   * @param exitRow the exit's x coordinate
   * @param exitColumn the exit's y coordinate
   * @param locked the exit's locked status
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the exit is not on the room's boundary
   */
  public RoomBuilder addExit(int exitRow, int exitColumn, boolean locked) throws IllegalArgumentException {
    checkDoorPlacement(exitRow, exitColumn);
    if (this.exit != null) {
      throw new IllegalStateException("an exit has already been made in this room");
    }
    this.exit = new Location(exitRow, exitColumn);
    this.exitLocked = locked;
    return this;
  }

  /**
   * Determines whether the given door coordinates are on a room boundary
   * @param doorRow the door's row
   * @param doorColumn the door's column
   */
  private void checkDoorPlacement(int doorRow, int doorColumn) throws IllegalArgumentException {
    if (!(doorRow == this.topLeft.row - 1
        || doorRow == this.topLeft.row + this.height
        || doorColumn == this.topLeft.column - 1
        || doorColumn == this.topLeft.column + this.width)) {
      throw new IllegalArgumentException("door must be on room boundary - row of " +
          (this.topLeft.row - 1) + " or " + (this.topLeft.row + this.height) + " or column on " +
          (this.topLeft.column - 1) + " or " + (this.topLeft.column + this.width) + ", given: "
          + doorRow + ", " + doorColumn);
    }
    if (doorRow < this.topLeft.row - 1
        || doorRow > this.topLeft.row + this.height
        || doorColumn < this.topLeft.column - 1
        || doorColumn > this.topLeft.column + this.width) {
      throw new IllegalArgumentException("door must be on room bounds - row between " +
          (this.topLeft.row - 1) + " and " + (this.topLeft.row + this.height) + " and column between" +
          (this.topLeft.column - 1) + " and " + (this.topLeft.column + this.width) + ", given: "
          + doorRow + ", " + doorColumn);
    }
  }

  /**
   * Adds a wall to this room.
   * @param wallRow the wall's column relative to topLeft
   * @param wallColumn the wall's row relative to topLeft
   * @return this RoomBuilder with the wall
   * @throws IllegalArgumentException if x coordinate or y coordinate are negative or outside room bounds
   */
  public RoomBuilder addWall(int wallRow, int wallColumn) throws IllegalArgumentException {
    if (wallRow >= this.height || wallColumn >= this.width) {
      throw new IllegalArgumentException("wall added to this room must be inside room bounds of (" +
          this.height + ", " + this.width +  "), given: " + wallRow + ", " + wallColumn);
    }

    this.walls.add(new Location(this.topLeft.row + wallRow, this.topLeft.column + wallColumn));
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

    Location bottomRightWall = new Location(this.topLeft.row + this.height, this.topLeft.column
        + this.width);
    this.initSize(bottomRightWall, spaces);
    this.setRoomSpaces(bottomRightWall, spaces);

    this.doors.forEach(door -> spaces.get(door.row).set(door.column, new Door(this.toString())));
    if (this.exit != null) spaces.get(this.exit.row).set(this.exit.column, new Exit(this.toString()));
    this.walls.forEach(wall -> spaces.get(wall.row).set(wall.column, new Wall(this.toString())));
  }

  /**
   * Initialize the room tiles to be in the same group.
   * @param bottomRightWall the wall corner tile at the bottom right of the room
   * @param spaces the 2d spaces array of this level
   */
  private void setRoomSpaces(Location bottomRightWall, ArrayList<ArrayList<Space>> spaces) {
    Location topLeftWall = new Location(this.topLeft.row - 1, this.topLeft.column - 1);
    // for all the locations in the 2D space in the rectangle defined by topLeftWall and bottomRightWall
    for (int currY = topLeftWall.row; currY <= bottomRightWall.row; currY++) {
      for (int currX = topLeftWall.column; currX <= bottomRightWall.column; currX++) {
        // check if the current location should be a wall tile or room tile
        Predicate<Location> currPointAxis = new Location.SameAxis(new Location(currY, currX));
        boolean isWallBoundary = currPointAxis.test(topLeftWall) || currPointAxis.test(bottomRightWall);

        // assign the space accordingly
        Space space = isWallBoundary ? new Wall(this.toString()) : new Tile(this.toString());
        spaces.get(currY).set(currX, space);
      }
    }
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
    Optional<Location> doorLocation = this.doors
        .stream()
        .filter(new Location.SameAxis(locationToFind))
        .min(Comparator.comparingInt(door -> door.euclidianDistance(locationToFind)));
    doorLocation.orElseThrow(() -> new IllegalArgumentException("no doors on the same axis as the given location"));

    return doorLocation.get();
  }
}
