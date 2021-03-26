package view;

import model.characters.*;
import model.characters.Character;
import model.ruleChecker.Interaction;
import model.item.Item;
import model.item.ItemVisitor;
import model.item.Key;
import model.level.*;
import model.level.Exit;

public class JSONInteraction extends Interaction<Character> {

  private final Location location;
  private final StringBuilder objectString;
  private final StringBuilder actors;
  private final StringBuilder isLocked;

  public JSONInteraction(Location location, StringBuilder objectString, StringBuilder actors) {
    this(location, objectString, actors, null);
  }

  public JSONInteraction(Location location, StringBuilder objectString, StringBuilder actors, StringBuilder isLocked) {
    super(null);
    this.location = location;
    this.objectString = objectString;
    this.actors = actors;
    this.isLocked = isLocked;
  }

  @Override
  public void visitItem(Item item) {
    if (!this.location.equals(item.getCurrentLocation())) return;

    String delimiter = this.objectString.length() == 0 ? "" : ",\n";
    String itemString = item.acceptVisitor(new KeyObjectType(this.location));
    this.objectString.append(delimiter).append(itemString);
  }

  @Override
  public void visitExit(Exit exit) {
    if (this.isLocked != null) this.isLocked.append(exit.isLocked());
    String delimiter = this.objectString.length() == 0 ? "" : ",\n";
    this.objectString
        .append(delimiter)
        .append("{ \"type\": \"exit\", \"position\": ")
        .append(this.location.toString())
        .append(" }");
  }

  @Override
  public void visitAdversary(Adversary adversary) {
    if (!this.location.equals(adversary.getCurrentLocation())) return;
    String delimiter = this.actors.length() == 0 ? "" : ",\n";
    String type = adversary.acceptVisitor(new TypeString());
    String adversaryJson = "{\n  \"type\": \"" + type + "\",\n  \"name\": \"" + adversary.getName() +
        "\",\n  \"position\": " + this.location.toString()+ "\n}";
    this.actors.append(delimiter).append(adversaryJson);
  }

  /**
   * Calculates the object type for a key.
   */
  private static class KeyObjectType implements ItemVisitor<String> {
    private final Location location;

    public KeyObjectType(Location location) {
      this.location = location;
    }

    @Override
    public String visitKey(Key key) {
      return "{ \"type\": \"key\", \"position\": " + this.location.toString() + " }";
    }
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
