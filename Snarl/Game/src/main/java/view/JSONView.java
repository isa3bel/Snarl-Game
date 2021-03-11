package view;

import java.util.List;
import model.Character;
import model.JSONObject;
import model.Level;

public class JSONView implements View{

  private String roomString;
  private String hallwayString;
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
    StringBuilder levelStringBuilder = new StringBuilder();
    StringBuilder isLocked = new StringBuilder();
    level.filter((a, b) -> true).keySet().stream().forEach(location -> {
          JSONObject jsonObject = new JSONObject(location, levelStringBuilder, isLocked);
          level.interact(location, jsonObject);
        });
    this.exitLockedString = isLocked.toString();
    this.levelString = "{ \"type\": \"level\",\n"
        + "\"rooms\": " + this.roomString + "\n"
        + "\"objects\": " + "[ " + levelStringBuilder + " ]\n"
        + "\"hallways\": " + this.hallwayString + "\n" + "}";
  }

  @Override
  public void placeCharacters(List<Character> characters) {
    StringBuilder playerString = new StringBuilder();
    StringBuilder adversaryString = new StringBuilder();

    characters.forEach(character -> {
        character.acceptVisitor(new JSONCharacter(playerString, adversaryString));
    });
    this.playerString = playerString.toString();
    this.adversaryString = adversaryString.toString();
  }

  @Override
  public void draw() {
    System.out.print("{\n" + "\"type\": \"state\",\n"
        + "\"level\": " + this.levelString + "\n"
        + "\"players\": " + this.playerString + "\n"
        + "\"adversaries\": " + this.adversaryString
        + "\"exit-locked\": " + this.exitLockedString);
  }
}
