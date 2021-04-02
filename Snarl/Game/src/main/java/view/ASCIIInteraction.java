package view;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Location;
import model.ruleChecker.Interactable;
import model.ruleChecker.InteractableVisitor;

import java.util.ArrayList;

/**
 * Translates an Interactable to its ASCII representation.
 */
public class ASCIIInteraction implements InteractableVisitor<Void> {

  protected final ArrayList<ArrayList<String>> render;

  ASCIIInteraction(ArrayList<ArrayList<String>> render) {
    this.render = render;
  }

  /**
   * Replaces the value at the interactables location in this.render with the given draw string.
   * @param interactable the interactable to be drawing
   * @param draw the string that will be used to "draw" this interactable
   */
  protected void drawAt(Interactable interactable, String draw) {
    Location location = interactable.getCurrentLocation();
    if (location.isInLevel()) {
      this.render.get(location.getRow()).set(location.getColumn(), draw);
    }
  }

  @Override
  public Void visitKey(Key key) {
    this.drawAt(key, "K");
    return null;
  }

  @Override
  public Void visitExit(Exit exit) {
    this.drawAt(exit, "E");
    return null;
  }

  @Override
  public Void visitPlayer(Player player) {
    if(!player.isInGame()) return null;
    String playerString = player.getName().substring(0, 1);
    this.drawAt(player, playerString);
    return null;
  }

  @Override
  public Void visitAdversary(Adversary adversary) {
    this.drawAt(adversary, "A");
    return null;
  }
}
