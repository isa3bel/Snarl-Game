package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;

public interface InteractableVisitor<T> {

  T visitKey(Key key);
  T visitPlayer(Player player);
  T visitAdversary(Adversary adversary);
  T visitExit(Exit exit);

}
