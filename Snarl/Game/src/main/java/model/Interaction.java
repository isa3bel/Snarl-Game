package model;

public abstract class Interaction<T extends Character> implements InteractableVisitor {

  protected T character;

  public Interaction(T character) {
    this.character = character;
  }

  @Override
  public void visitItem(Item item) {
    // nothing should happen
  }

  @Override
  public void visitPlayer(Player player) {
    // nothing should happen
  }

  @Override
  public void visitAdversary(Adversary adversary) {
    // nothing should happen
  }

  @Override
  public void visitExit(Exit exit) {
    // nothing should happen
  }

}
