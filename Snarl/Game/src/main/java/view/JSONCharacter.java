package view;

import model.characters.*;
import model.level.Location;

/**
 * Translates a character into json.
 */
public class JSONCharacter implements CharacterVisitor<Void> {
  private StringBuilder playerString;
  private StringBuilder adversaryString;

  JSONCharacter(StringBuilder playerString, StringBuilder adversaryString) {
    this.playerString = playerString;
    this.adversaryString = adversaryString;
  }

  @Override
  public Void visitPlayer(Player player) {
    String delimiter = this.playerString.length() == 0 ? "" : ",\n";
    Location location = player.getCurrentLocation();
    if (location == null) return null;

    String playerJson = "{\n  \"type\": \"player\",\n  \"name\": \"" + player.getName() + "\",\n  \"position\": [ "
        + location.row + ", " + location.column + " ]\n}";
    this.playerString.append(delimiter).append(playerJson);

    return null;
  }

  @Override
  public Void visitAdversary(Adversary adversary) {
    String delimiter = this.adversaryString.length() == 0 ? "" : ",\n";
    String type = adversary.acceptVisitor(new TypeString());
    String adversaryJson = "{\n  \"type\": \"" + type + "\",\n  \"name\": \"" + adversary.getName() +
        "\",\n  \"position\": [ " + adversary.getCurrentLocation().row + ", "
        + adversary.getCurrentLocation().column + " ]\n}";
    this.adversaryString.append(delimiter).append(adversaryJson);

    return null;
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
