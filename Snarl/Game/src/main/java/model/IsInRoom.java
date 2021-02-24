package model;

/**
 * Visits a space to see if it is in the same room as this Space group.
 */
public class IsInRoom implements SpaceVisitor<Boolean> {

  private Space group;

  IsInRoom() {}

  IsInRoom(Space group) {
    this.group = group;
  }

  /**
   * A door is not in a room.
   * @param door the door
   * @return false
   */
  @Override
  public Boolean visitDoor(Door door) {
    return false;
  }

  /**
   * An exit is not in a room.
   * @param exit the exit
   * @return false
   */
  @Override
  public Boolean visitExit(Exit exit) {
    return false;
  }

  /**
   * A wall is not in a room.
   * @param wall the wall
   * @return false
   */
  @Override
  public Boolean visitWall(Wall wall) {
    return false;
  }

  /**
   * A tile is in this room if the group is not specified or the groups are the same.
   * @param tile the tile
   * @return if the tile is in the room
   */
  @Override
  public Boolean visitTile(Tile tile) {
    return this.group == null || tile.sameGroup(group);
  }

  /**
   * A HallwayTile is not in a room.
   * @param tile the tile
   * @return false
   */
  @Override
  public Boolean visitHallwayTile(HallwayTile tile) {
    return false;
  }
}
