package view;

import java.util.List;

import model.characters.*;
import model.level.Level;
import model.level.Location;

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
    StringBuilder adversaryStringBuilder = new StringBuilder();
    StringBuilder isLocked = new StringBuilder();
    JSONInteraction jsonObject = new JSONInteraction(objectsStringBuilder, adversaryStringBuilder, isLocked,
        location -> true);
    level.interact(jsonObject, null);

    this.exitLockedString = isLocked.toString();
    this.levelString = "{\n  \"type\": \"level\",\n"
        + "  \"rooms\": " + this.roomString + ",\n"
        + "  \"objects\": " + "[ " + objectsStringBuilder + " ],\n"
        + "  \"hallways\": " + this.hallwayString + "\n}";

    this.adversaryString = adversaryStringBuilder.toString();
  }

  @Override
  public void placePlayers(List<Player> players) {
    StringBuilder playerStringBuilder = new StringBuilder();

    players.forEach(player -> {
      String delimiter = playerStringBuilder.length() == 0 ? "" : ",\n";
      Location location = player.getCurrentLocation();
      if (location == null) return;

      String playerJson = "{\n  \"type\": \"player\",\n  \"name\": \"" + player.getName() + "\",\n  \"position\": [ "
          + location.getRow() + ", " + location.getColumn() + " ]\n}";
      playerStringBuilder.append(delimiter).append(playerJson);
    });

    this.playerString = playerStringBuilder.toString();
  }

  @Override
  public void draw() {
    System.out.print(this.toString());
  }

  @Override
  public String toString() {
    return "{\n\"type\": \"state\",\n"
        + "\"level\": " + this.levelString + ",\n"
        + "\"players\": [" + this.playerString + "],\n"
        + "\"adversaries\": [" + this.adversaryString
        + "],\n\"exit-locked\": " + this.exitLockedString + "}";
  }


  /**
   * The string of the type of this adversary.
   */
  private static class TypeString implements AdversaryVisitor<String> {

    @Override
    public String visitGhost(Ghost ghost) {
      return "ghost";
    }

    @Override
    public String visitZombie(Zombie zombie) {
      return "zombie";
    }
  }
}
