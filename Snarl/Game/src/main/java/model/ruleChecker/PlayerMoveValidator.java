package model.ruleChecker;

import model.characters.Character;
import model.characters.Player;
import model.level.Level;
import model.level.Location;

import java.util.List;

public class PlayerMoveValidator extends MoveValidator {

  public PlayerMoveValidator(Player character, Location location) {
    super(character, location);
  }

  @Override
  public boolean isValid(Level level, List<Character> characters) {
    return true;
  }

}
