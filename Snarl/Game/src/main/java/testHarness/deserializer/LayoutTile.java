package testHarness.deserializer;

import model.builders.RoomBuilder;
import model.level.Location;

/**
 * The types of tiles in a tile layout.
 */
abstract class LayoutTile {

  protected final Location location;

  LayoutTile(Location location) {
    this.location = location;
  }

  /**
   * Add this LayoutTile to the given RoomBuilder.
   * @param roomBuilder the RoomBuilder to add this tile to
   * @param origin the origin of the given room
   * @param isBoundary is the tile on the boundary of the room
   */
  abstract void addTo(RoomBuilder roomBuilder, Location origin, boolean isBoundary);

  /**
   * Creates a layout tile of the specified type (0 is Wall, 1 is Walkable, 2 is Door).
   * @param value the int type of the tile
   * @param location the location of this tile
   * @return the resulting LayoutTile
   */
  static LayoutTile fromOrdinal(int value, Location location) {
    switch (value) {
      case 0: return new Wall(location);
      case 1: return new Walkable(location);
      case 2: return new Door(location);
      default:
        throw new IllegalArgumentException("Invalid ordinal for layout tile - expected: 0, 1, or 2; given: " + value);
    }
  }

  private static class Wall extends LayoutTile {

    Wall(Location location) {
      super(location);
    }

    /**
     * Adds a wall to the inner part of the room (ignores if the tile is on the boundary.
     * @param roomBuilder the RoomBuilder to add the wall to
     * @param origin the origin of the room
     * @param isBoundary is this tile on the room's boundary
     */
    void addTo(RoomBuilder roomBuilder, Location origin, boolean isBoundary) {
      if (isBoundary) return;
      // DECISION: our representation of a room does not include the boundaries
      //  need to subtract 1 so that the origin is from the top left of the room,
      //  not the top left of the layout array
      roomBuilder.addWall(this.location.row - 1, this.location.column - 1);
    }
  }

  private static class Walkable extends LayoutTile {

    Walkable(Location location) {
      super(location);
    }

    /**
     * Walkables are the default of a room layout, don't need to mutate the RoomBuilder.
     * @param roomBuilder the RoomBuilder to add the walkable to
     * @param origin the origin of the room
     * @param isBoundary is this tile on the room's boundary
     */
    void addTo(RoomBuilder roomBuilder, Location origin, boolean isBoundary) {
      // doesn't need to change the roomBuilder as the default inside a room is a walkable
    }
  }

  private static class Door extends LayoutTile {

    Door(Location location) {
      super(location);
    }

    /**
     * Adds a door the given RoomBuilder at this location.
     * @param roomBuilder the RoomBuilder to add the door to
     * @param origin the origin of the room
     * @param isBoundary is this tile on the room's boundary
     */
    void addTo(RoomBuilder roomBuilder, Location origin, boolean isBoundary) {
      roomBuilder.addDoor(this.location.row + origin.row, this.location.column + origin.column);
    }
  }
}
