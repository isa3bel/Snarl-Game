package src;

public abstract class Space {

  String roomOrHallway;

  abstract <T> T acceptVisitor(SpaceVisitor<T> visitor);
}
