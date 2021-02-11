package src;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import src.Command.Visitor;

public class Level {
  private ArrayList<ArrayList<Space>> spaces;

  private Level(ArrayList<ArrayList<Space>> spaces) {
    this.spaces = spaces;
  }

  <T> ArrayList<ArrayList<T>> map(Function<Space, T> function) {
    ArrayList<ArrayList<T>> result = new ArrayList<>();
    for(ArrayList<Space> row : spaces) {
      ArrayList<T> resultRow = new ArrayList();
      for(Space s : row) {
        resultRow.add(function.apply(s));
      }
      result.add(resultRow);
    }

    return result;
  }


  static class LevelBuilder {

    public LevelBuilder() {

    }

    public Level createLevel() {
      Level level = new Level();
    }

  }

}
