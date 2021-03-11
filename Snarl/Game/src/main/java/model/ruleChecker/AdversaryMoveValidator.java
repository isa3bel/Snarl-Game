package model.ruleChecker;

import model.characters.Adversary;
import model.characters.Character;
import model.level.Level;
import model.level.Location;

import java.util.List;

public class AdversaryMoveValidator extends MoveValidator {

  public AdversaryMoveValidator(Adversary character, Location location) {
    super(character, location);
  }

  @Override
  public boolean isValid(Level level, List<Character> characters) {
    return true;
  }

}
