package view;

import model.*;

import java.util.ArrayList;
import java.util.function.Function;

/**
 * Creates the view for a Snarl game in with ASCII characters.
 */
public class ASCIIView implements View {

  @Override
  public void draw(Level level) {
    ArrayList<ArrayList<String>> list = level.map(new ASCIISpace());
    for (ArrayList<String> arr : list) {
      for (String s : arr) {
        System.out.print(s);
      }
      System.out.println();
    }
  }

  protected static class ASCIISpace implements SpaceVisitor, Function<Space, String> {

    @Override
    public String visitDoor(Door door) {
      return "D";
    }

    @Override
    public String visitExit(Exit exit) {
      return "E";
    }

    @Override
    public String visitWall(Wall wall) {
      return "X";
    }

    @Override
    public String visitTile(Tile tile) {
      return " ";
    }

    @Override
    public String apply(Space space) {
      return space.acceptVisitor(this);
    }
  }
}
