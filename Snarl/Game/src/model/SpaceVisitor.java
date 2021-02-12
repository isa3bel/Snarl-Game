package model;

/**
 * Function object to visit anything that extends Space.
 * @param <T>
 */
public interface SpaceVisitor<T> {
  String visitDoor(Door door);
  String visitExit(Exit exit);
  String visitWall(Wall wall);
  String visitTile(Tile tile);
}
