package view;

import model.characters.*;
import model.item.Key;
import model.item.Exit;
import model.level.Location;
import model.ruleChecker.InteractableVisitor;

import java.util.function.Predicate;

public class JSONInteraction implements InteractableVisitor<Void> {

  private final StringBuilder objectString;
  private final StringBuilder actorString;
  private final StringBuilder isLockedString;
  private final Predicate<Location> shouldIncludeInView;

  public JSONInteraction(StringBuilder objectString, StringBuilder actorString,
      Predicate<Location> shouldIncludeInView) {
    this(objectString, actorString, new StringBuilder(), shouldIncludeInView);
  }

  public JSONInteraction(StringBuilder objectString, StringBuilder actorString, StringBuilder isLockedString,
      Predicate<Location> shouldIncludeInView) {
    if (objectString == null || actorString == null || isLockedString == null) {
      throw new IllegalArgumentException("all arguments must be non-null");
    }
    this.objectString = objectString;
    this.actorString = actorString;
    this.isLockedString = isLockedString;
    this.shouldIncludeInView = shouldIncludeInView;
  }

  @Override
  public Void visitKey(Key key) {
    if (!this.shouldIncludeInView.test(key.getCurrentLocation())) return null;
    String delimiter = this.objectString.length() == 0 ? "" : ",\n";
    String keyString = "{ \"type\": \"key\", \"position\": " + key.getCurrentLocation().toString() + " }";
    this.objectString.append(delimiter).append(keyString);
    return null;
  }

  @Override
  public Void visitPlayer(Player player) {
    if (!this.shouldIncludeInView.test(player.getCurrentLocation())) return null;
    String delimiter = this.actorString.length() == 0 ? "" : ",\n";
    String adversaryJson = "{\n  \"type\": \"player\",\n  \"name\": \"" + player.getName() +
        "\",\n  \"position\": " + player.getCurrentLocation().toString()+ "\n}";
    this.actorString.append(delimiter).append(adversaryJson);
    return null;
  }

  @Override
  public Void visitAdversary(Adversary adversary) {
    if (!this.shouldIncludeInView.test(adversary.getCurrentLocation())) return null;
    String delimiter = this.actorString.length() == 0 ? "" : ",\n";
    String type = adversary.acceptVisitor(new TypeString());
    String adversaryJson = "{\n  \"type\": \"" + type + "\",\n  \"name\": \"" + adversary.getName() +
        "\",\n  \"position\": " + adversary.getCurrentLocation().toString()+ "\n}";
    this.actorString.append(delimiter).append(adversaryJson);
    return null;
  }

  @Override
  public Void visitExit(Exit exit) {
    if (!this.shouldIncludeInView.test(exit.getCurrentLocation())) return null;
    String delimiter = this.objectString.length() == 0 ? "" : ",\n";
    String exitString = "{ \"type\": \"exit\", \"position\": " + exit.getCurrentLocation().toString() + " }";
    this.objectString.append(delimiter).append(exitString);
    this.isLockedString.append(exit.isLocked());
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
