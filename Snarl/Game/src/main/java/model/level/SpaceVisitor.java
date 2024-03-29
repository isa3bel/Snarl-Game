package model.level;

/**
 * Function object to visit anything that extends Space.
 * @param <T>
 */
public interface SpaceVisitor<T> {

  T visitDoor(Door door);
  T visitAnyWall(Wall wall);
  T visitTile(Tile tile);
  T visitHallwayTile(HallwayTile tile);

}
