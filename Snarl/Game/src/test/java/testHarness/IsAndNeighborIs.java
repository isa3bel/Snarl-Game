package testHarness;

import model.*;

import java.util.function.BiPredicate;

/**
 * Function object to check if a space is a hallway neighboring a door in this room of this level.
 */
public class IsAndNeighborIs implements BiPredicate<Space, Location> {

  private final Level level;
  private final Space currentRoom;
  private final SpaceVisitor<Boolean> is;
  private final SpaceVisitor<Boolean> neighborIs;

  private IsAndNeighborIs(Level level, Space currentRoom, SpaceVisitor<Boolean> is, SpaceVisitor<Boolean> neighbors) {
    this.level = level;
    this.currentRoom = currentRoom;
    this.is = is;
    this.neighborIs = neighbors;
  }

  /**
   * Creates a predicate for checking if a space is a hallway tile neighboring a door in the same group as currentRoom.
   * @param level the level to be searching in
   * @param currentRoom the current room
   * @return the function object to filter by
   */
  public static BiPredicate<Space, Location> hallwayNeighboringDoor(Level level, Space currentRoom) {
    return new IsAndNeighborIs(level, currentRoom, new IsHallway(), new IsDoor());
  }

  /**
   * Creates a predicate for asking if a space is a door neighboring a hallway tile in the same group as currentRoom.
   * @param level the level to be searching in
   * @param currentRoom the currentRoom
   * @return the function object to filter by
   */
  public static BiPredicate<Space, Location> doorNeighboringHallway(Level level, Space currentRoom) {
    return new IsAndNeighborIs(level, currentRoom, new IsDoor(), new IsHallway());
  }

  @Override
  public boolean test(Space space, Location location) {
    // TODO: this won't work for something like
    //  XXXXXXXXX
    //  X       D--
    //  XXXXDXXXX |
    //      |------
    //  XXXXXXXXD---
    //  X       X  |
    //  XXXXXXXXX
    return space.acceptVisitor(this.is)
        && !this.level.filter(new IsNeighborTo(this.currentRoom, location, this.neighborIs)).isEmpty();
  }

  /**
   * Helper class that tests if a space is of a given type and neighbors a given location.
   */
  private static class IsNeighborTo implements BiPredicate<Space, Location> {

    private final Space room;
    private final Location location;
    private final SpaceVisitor<Boolean> neighborIs;

    private IsNeighborTo(Space room, Location location, SpaceVisitor<Boolean> neighborIs) {
      this.room = room;
      this.location = location;
      this.neighborIs = neighborIs;
    }

    @Override
    public boolean test(Space space, Location spaceLocation) {
      return space.sameGroup(this.room)
          && spaceLocation.isAdjacentTo(this.location)
          && space.acceptVisitor(this.neighborIs);
    }
  }

  /**
   * Space visitor for if the space is a hallway.
   */
  private static class IsHallway implements SpaceVisitor<Boolean> {

    @Override
    public Boolean visitDoor(Door door) {
      return false;
    }

    @Override
    public Boolean visitExit(Exit exit) {
      return false;
    }

    @Override
    public Boolean visitAnyWall(Wall wall) {
      return false;
    }

    @Override
    public Boolean visitTile(Tile tile) {
      return false;
    }

    @Override
    public Boolean visitHallwayTile(HallwayTile tile) {
      return true;
    }
  }

  /**
   * Space visitor for if the space is a door.
   */
  private static class IsDoor implements SpaceVisitor<Boolean> {

    @Override
    public Boolean visitDoor(Door door) {
      return true;
    }

    @Override
    public Boolean visitExit(Exit exit) {
      return false;
    }

    @Override
    public Boolean visitAnyWall(Wall wall) {
      return false;
    }

    @Override
    public Boolean visitTile(Tile tile) {
      return false;
    }

    @Override
    public Boolean visitHallwayTile(HallwayTile tile) {
      return false;
    }
  }
}
