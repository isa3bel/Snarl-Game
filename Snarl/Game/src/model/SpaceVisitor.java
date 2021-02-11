package model;

public interface SpaceVisitor<T> {
  T visitDoor(Door door);
  T visitExit(Exit exit);
  T visitWall(Wall wall);
  T visitTile(Tile tile);
}
