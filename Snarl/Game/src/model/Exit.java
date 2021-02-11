package model;

public class Exit extends Space {

  @Override
  <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return visitor.visitExit(this);
  }
}
