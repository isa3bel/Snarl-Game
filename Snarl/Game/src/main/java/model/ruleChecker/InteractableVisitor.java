package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Player;
import model.item.Exit;
import model.item.Key;
import model.level.Wall;

public interface InteractableVisitor<T> {

  T visitKey(Key key);
  T visitPlayer(Player player);
  T visitAdversary(Adversary adversary);
  T visitExit(Exit exit);
  T visitWall(Wall wall);

}
