package model.characters;

import model.level.Level;
import model.level.Location;

import java.util.ArrayList;

public class AdversaryMoveValidator extends MoveValidator<Adversary> {

  AdversaryMoveValidator(Adversary character, Location location) {
    super(character, location);
  }

  @Override
  public boolean isValid(Level level, ArrayList<Character> characters) {
    return true;
  }

}
