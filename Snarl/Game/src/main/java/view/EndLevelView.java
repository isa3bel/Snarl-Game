package view;

import model.characters.Adversary;
import model.characters.Character;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Level;
import model.level.Wall;
import model.ruleChecker.InteractableVisitor;

import java.util.List;
import java.util.stream.Collectors;

public class EndLevelView implements View {

  private String keyRetriever;
  private String exits;
  private String ejects;

  @Override
  public void renderLevel(Level level) {
    level.interact(new KeyRetriever(this), null);
  }

  @Override
  public void placePlayers(List<Player> players) {
    String exitedPlayerNames = players.stream()
        .filter(player -> !player.isInGame() && !player.getCurrentLocation().isDead())
        .map(Character::getName)
        .collect(Collectors.joining("\", \""));
    this.exits = exitedPlayerNames.equals("") ? "[ ]" : "[ \"" + exitedPlayerNames + "\" ]";
    String ejectedPlayerNames = players.stream()
        .filter(player -> !player.isInGame() && player.getCurrentLocation().isDead())
        .map(Character::getName)
        .collect(Collectors.joining("\", \""));
    this.ejects = ejectedPlayerNames.equals("") ? "[ ]" : "[ \"" + ejectedPlayerNames + "\" ]";
  }

  @Override
  public void draw() {
    System.out.println(this.toString());
  }

  @Override
  public String toString() {
    return "{ \"type\": \"end-level\",\n" +
        "  \"key\": " + this.keyRetriever + ",\n" +
        "  \"exits\": " + this.exits + ",\n" +
        "  \"ejects\": " + this.ejects + "\n}";
  }

  private static class KeyRetriever implements InteractableVisitor<Void> {

    private final EndLevelView endLevelView;

    private KeyRetriever(EndLevelView endLevelView) {
      this.endLevelView = endLevelView;
    }

    @Override
    public Void visitKey(Key key) {
      this.endLevelView.keyRetriever = key.getRetrievedBy();
      return null;
    }

    @Override
    public Void visitPlayer(Player player) {
      return null;
    }

    @Override
    public Void visitAdversary(Adversary adversary) {
      return null;
    }

    @Override
    public Void visitExit(Exit exit) {
      return null;
    }

    @Override
    public Void visitWall(Wall wall) {
      return null;
    }
  }
}
