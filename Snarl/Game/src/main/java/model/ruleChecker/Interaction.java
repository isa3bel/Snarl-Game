package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Character;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;

public abstract class Interaction<T extends Character> implements InteractableVisitor<Void> {

  protected T character;

  public Interaction(T character) {
    this.character = character;
  }

  @Override
  public Void visitKey(Key key) {
    // nothing should happen
    return null;
  }

  @Override
  public Void visitPlayer(Player player) {
    // nothing should happen
    return null;
  }

  @Override
  public Void visitAdversary(Adversary adversary) {
    // nothing should happen
    return null;
  }

  @Override
  public Void visitExit(Exit exit) {
    // nothing should happen
    return null;
  }

}
