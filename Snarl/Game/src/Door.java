package src;

public class Door extends Space {

  @Override
  <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitDoor(this);
  }
}
