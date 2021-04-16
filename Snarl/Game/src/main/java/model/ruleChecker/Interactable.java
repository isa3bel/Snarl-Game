package model.ruleChecker;

import model.level.Location;

public interface Interactable {

  Location getCurrentLocation();
  <T> T acceptVisitor(InteractableVisitor<T> visitor);

}
