package model;

public interface Interactable {

  // TODO: consider if we want/need a getCurrentLocation or isAtLocation method
  void acceptVisitor(InteractableVisitor visitor);

}
