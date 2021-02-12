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
    ArrayList<ArrayList<String>> spaces = level.map(new ASCIISpace());

    for (ArrayList<String> row : spaces) {
      for (String space : row) {
        System.out.print(space);
      }
      System.out.print("\n");
    }
  }

  protected static class ASCIISpace implements SpaceVisitor<String>, Function<Space, String> {

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
