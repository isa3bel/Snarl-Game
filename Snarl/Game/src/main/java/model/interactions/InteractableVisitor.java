package model.interactions;

import model.characters.Adversary;
import model.characters.Player;
import model.level.Exit;
import model.item.Item;

public interface InteractableVisitor {

  void visitItem(Item item);
  void visitPlayer(Player player);
  void visitAdversary(Adversary adversary);
  void visitExit(Exit exit);

}
