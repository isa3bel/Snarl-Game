package src;

public class Wall extends Space {

  @Override
  <T> T acceptVisitor(SpaceVisitor<T> visitor) {
    return null;
  }
}
