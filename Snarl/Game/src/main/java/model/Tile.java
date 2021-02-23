package model;

/**
 * An available space in the Snarl level.
 */
public class Tile extends Space {

  Tile(String room) {
    this.group = room;
  }

  @Override
  public <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitTile(this);
  }
}
