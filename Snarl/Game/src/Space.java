package src;

public abstract class Space { // a space can be available, a door, or a level door, not available

  String roomOrHallway;

  abstract <T> T acceptVisitor(SpaceVisitor<T> visitor);


}
