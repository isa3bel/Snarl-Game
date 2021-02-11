package model;

import java.util.ArrayList;
import java.util.function.Function;

public class Level {
  private final ArrayList<ArrayList<Space>> spaces;

  private Level(ArrayList<ArrayList<Space>> spaces) {
    this.spaces = spaces;
  }

  public <T> ArrayList<ArrayList<T>> map(Function<Space, T> function) {
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

  public static class LevelBuilder {

    private ArrayList<ArrayList<Space>> spaces;

    public LevelBuilder(ArrayList<ArrayList<Space>> spaces) {
      this.spaces = spaces;
    }

    public Level createLevel() {
      Level level = new Level(this.spaces);

      return level;
    }

  }

}
