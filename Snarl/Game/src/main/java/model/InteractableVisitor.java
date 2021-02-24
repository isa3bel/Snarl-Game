package model;

public interface InteractableVisitor {

  void visitItem(Item item);
  void visitPlayer(Player player);
  void visitAdversary(Adversary adversary);
  void visitExit(Exit exit);

}
