package src;

public class Tile extends Space {

  @Override
  <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitTile(this);
  }
}
