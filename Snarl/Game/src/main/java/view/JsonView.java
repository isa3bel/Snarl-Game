package view;

import model.Character;
import model.Level;

import java.util.List;

public class JsonView implements View {

  private String roomsList;
  private String hallwaysList;

  public JsonView(String roomsList, String hallwaysList) {
    this.roomsList = roomsList;
    this.hallwaysList = hallwaysList;
  }

  @Override
  public void renderLevel(Level level) {

  }

  @Override
  public void placeCharacters(List<Character> character) {

  }

  @Override
  public void draw() {

  }
}
