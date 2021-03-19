package model.builders;

import model.level.Location;
import model.level.*;
import model.level.Door;
import model.level.EdgeWall;

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
  private final HashSet<Location> walls;

  /**
   * Instantiates a RoomBuilder with the required properties.
   * @param location the location of the top left corner of the room
   * @param width width of the room
   * @param height height of the room
   * @throws IllegalArgumentException if row coordinate or column are negative or width or height are not positive
   */
  public RoomBuilder(Location location, int width, int height) throws IllegalArgumentException {
    if (width < 3 || height < 3) {
      throw new IllegalArgumentException("width and height must be at least 3, given: " + width + ", " + height);
    }

    this.topLeft = location;
    this.width = width;
    this.height = height;
    this.doors = new HashSet<>();
    this.walls = new HashSet<>();
  }

  /**
   * Adds a door to this room.
   * @param doorLoc the door's coordinates (location)
   * @return this RoomBuilder with the door
   * @throws IllegalArgumentException if the door is not on the room's boundary
   */
  public RoomBuilder addDoor(Location doorLoc) throws IllegalArgumentException {
    // DECISION: not validating if a door is reachable (e.g. no walls in front or
    // the door is on the corner of the room
    // e.g. XXXXD  OR XXDX
    //      XX  X     X XX  (if the room is technically 2 wide but
    //      XXXXX     XXXX   a wall is placed at the edge of the room)
    checkDoorPlacement(doorLoc);

    this.doors.add(doorLoc);
    return this;
  }

  /**
   * Determines whether the given door coordinates are on a room boundary
   * @param doorLoc the door's coordinates (location)
   */
  private void checkDoorPlacement(Location doorLoc) throws IllegalArgumentException {
    if (!(doorLoc.getRow() == this.topLeft.getRow()
        || doorLoc.getRow() == this.topLeft.getRow() + this.height - 1
        || doorLoc.getColumn() == this.topLeft.getColumn()
        || doorLoc.getColumn() == this.topLeft.getColumn() + this.width - 1)) {
      throw new IllegalArgumentException("door must be on room boundary - row of " +
          this.topLeft.getRow() + " or " + (this.topLeft.getRow() + this.height - 1) + " or column on " +
          this.topLeft.getColumn() + " or " + (this.topLeft.getColumn() + this.width - 1) + ", given: "
          + doorLoc.getRow() + ", " + doorLoc.getColumn());
    }
    if (doorLoc.getRow() < this.topLeft.getRow()
        || doorLoc.getRow() > this.topLeft.getRow() + this.height - 1
        || doorLoc.getColumn() < this.topLeft.getColumn()
        || doorLoc.getColumn() > this.topLeft.getColumn() + this.width - 1) {
      throw new IllegalArgumentException("door must be on room bounds - row between " +
          this.topLeft.getRow() + " and " + (this.topLeft.getRow() + this.height - 1) + " and column between" +
          this.topLeft.getColumn() + " and " + (this.topLeft.getColumn() + this.width - 1) + ", given: "
          + doorLoc.getRow() + ", " + doorLoc.getColumn());
    }
  }

  /**
   * Adds a wall to this room.
   * @param wallLoc the wall's location
   * @return this RoomBuilder with the wall
   * @throws IllegalArgumentException if x coordinate or y coordinate are negative or outside room bounds
   */
  public RoomBuilder addWall(Location wallLoc) throws IllegalArgumentException {
    if (wallLoc.getRow() >= this.height || wallLoc.getColumn() >= this.width) {
      throw new IllegalArgumentException("wall added to this room must be inside room bounds of (" +
          this.height + ", " + this.width +  "), given: " + wallLoc.getRow() + ", " + wallLoc.getColumn());
    }

    this.walls.add(new Location(this.topLeft.getRow() + wallLoc.getRow(), this.topLeft.getColumn() + wallLoc.getColumn()));
    return this;
  }

  /**
   * Adds this room to the given spaces array.
   * @param spaces the spaces to configure this room on
   * @throws IllegalStateException if the room has no doors or has a door that is also a wall
   */
  public void build(ArrayList<ArrayList<Space>> spaces) throws IllegalStateException {
    if (this.doors.stream().anyMatch(this.walls::contains)) {
      throw new IllegalStateException("no door can also be a wall");
    }

    Location bottomRightWall = new Location(this.topLeft.getRow() + this.height - 1,
        this.topLeft.getColumn() + this.width - 1);
    this.initSize(bottomRightWall, spaces);
    this.setRoomSpaces(bottomRightWall, spaces);

    this.doors.forEach(door -> spaces.get(door.getRow()).set(door.getColumn(), new Door(this.toString())));
    this.walls.forEach(wall -> spaces.get(wall.getRow()).set(wall.getColumn(), new Wall(this.toString())));
  }

  /**
   * Initialize the room tiles to be in the same group.
   * @param bottomRightWall the wall corner tile at the bottom right of the room
   * @param spaces the 2d spaces array of this level
   */
  private void setRoomSpaces(Location bottomRightWall, ArrayList<ArrayList<Space>> spaces) {
    Location topLeftWall = new Location(this.topLeft.getRow(), this.topLeft.getColumn());
    // for all the locations in the 2D space in the rectangle defined by topLeftWall and bottomRightWall
    for (int currY = topLeftWall.getRow(); currY <= bottomRightWall.getRow(); currY++) {
      for (int currX = topLeftWall.getColumn(); currX <= bottomRightWall.getColumn(); currX++) {
        // check if the current location should be a wall tile or room tile
        Predicate<Location> currPointAxis = new Location.SameAxis(new Location(currY, currX));
        boolean isWallBoundary = currPointAxis.test(topLeftWall) || currPointAxis.test(bottomRightWall);

        // assign the space accordingly
        Space space = isWallBoundary ? new EdgeWall(this.toString()) : new Tile(this.toString());
        spaces.get(currY).set(currX, space);
      }
    }
  }

  /**
   * Gets the door in this room that is closest to any door in that room.
   * @param otherRoom the room this room is connecting to
   * @return the location of the door leading to the other room
   */
  Location[] getClosestConnectingDoors(RoomBuilder otherRoom) {
    Optional<Location[]> maybeDoorPair = this.doors.stream()
        .map(door -> {
          try {
            return new Location[]{door, otherRoom.getClosestDoorOnAxis(door)};
          }
          catch (IllegalArgumentException exception) {
            return null;
          }
        })
        .filter(doorPair -> doorPair != null)
        .min(Comparator.comparingInt(doorPair -> doorPair[0].euclidianDistance(doorPair[1])));

    return maybeDoorPair.orElseThrow(
        () -> new IllegalStateException("no valid doors to connect these rooms")
    );
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
    return doorLocation.orElseThrow(() -> new IllegalArgumentException("no doors on the same axis as the given location"));
  }
}
