package model.ruleChecker;

import model.level.Location;

public interface Interactable {

  Location getCurrentLocation();
  void acceptVisitor(InteractableVisitor visitor);

}
