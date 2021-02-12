package model;

/**
 * An available space in a room.
 */
public class Tile extends Space {

  Tile(String room) {
    this.group = room;
  }

  @Override
  <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitTile(this);
  }
}
