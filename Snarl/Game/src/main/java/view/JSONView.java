package view;

import java.util.List;
import model.characters.Character;
import model.level.Level;

public class JSONView implements View{

  private final String roomString;
  private final String hallwayString;
  private String levelString;
  private String playerString;
  private String adversaryString;
  private String exitLockedString;

  public JSONView(String roomString, String hallwayString) {
    this.roomString = roomString;
    this.hallwayString = hallwayString;
  }

  @Override
  public void renderLevel(Level level) {
    StringBuilder objectsStringBuilder = new StringBuilder();
    StringBuilder isLocked = new StringBuilder();
    level.filter((a, b) -> true).keySet().forEach(location -> {
          JSONObject jsonObject = new JSONObject(location, objectsStringBuilder, isLocked);
          level.interact(location, jsonObject);
        });

    this.exitLockedString = isLocked.toString();
    this.levelString = "{\n  \"type\": \"level\",\n"
        + "  \"rooms\": " + this.roomString + ",\n"
        + "  \"objects\": " + "[ " + objectsStringBuilder + " ],\n"
        + "  \"hallways\": " + this.hallwayString + "\n}";
  }

  @Override
  public void placeCharacters(List<Character> characters) {
    StringBuilder playerString = new StringBuilder();
    StringBuilder adversaryString = new StringBuilder();

    characters.forEach(character ->
        character.acceptVisitor(new JSONCharacter(playerString, adversaryString)));
    this.playerString = playerString.toString();
    this.adversaryString = adversaryString.toString();
  }

  @Override
  public void draw() {
    System.out.print("{\n\"type\": \"state\",\n"
        + "\"level\": " + this.levelString + ",\n"
        + "\"players\": [" + this.playerString + "],\n"
        + "\"adversaries\": [" + this.adversaryString
        + "],\n\"exit-locked\": " + this.exitLockedString + "}");
  }
}
