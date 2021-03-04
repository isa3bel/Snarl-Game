package model;

public interface WallVisitor<T> {

  T visitWall(Wall wall);
  T visitEdgeWall(EdgeWall wall);
  T visitVoidWall(VoidWall wall);

}
