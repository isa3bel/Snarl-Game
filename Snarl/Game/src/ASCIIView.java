package src;

import java.util.function.Function;

public class ASCIIView implements View {

  @Override
  public void draw(Level l) {
    ArrayList<ArrayList<String>> list = l.map(new ASCIISpace());
    // nested for loop, inner most print single string, outer loop print new line
  }

  protected static class ASCIISpace implements SpaceVisitor, Function<Space, String> {

    @Override
    public Object visitDoor(Door door) {
      return "d";
    }

    @Override
    public String visitExit(Exit exit) {

    }

    @Override
    public String visitWall(Wall wall) {
      return "X";
    }

    @Override
    public Object visitTile(Tile tile) {
      return null;
    }

    @Override
    public String apply(Space space) {
      return null;
    }
  }
}
