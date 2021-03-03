For full details on the game manager and player interface, see game-manager.md
This file will outline the interface for the player.

```java
// any "character" in the game - something that moves and has a turn
// can both interact and be interacted with
abstract class Character implements Interactable {
  Location currentLocation;
  // the actual communicator to the thing that will be controlling this character
  // (e.g. the library for automated adversaries, wrapper for STDIN or TCP connection, etc.)
  Controller controller;
  
  // directs the limited game state to the user
  void updateWithGameState(View view) {
    this.controller.updateWithGameState(view);
  }
  
  // moves this character to the given location
  void moveTo(Location nextLocation) {
    // note - location will be readonly so mutation here is not a concern
    this.currentLocation = nextLocation;
  }
}

class Player extends Character {
  int id;
  
  <T> T acceptVisitor(InteractableVisitor<T> visitor) {
    return visitor.visitPlayer(this);
  }
  
  MoveValidator getNextMove() {
    Location requestedMove = this.controller.getRequestedMove();
    return new PlayerMoveValidator(this, requestedMove);
  }

  Interaction makeInteraction() {
    return new PlayerInteraction(gameManager, this);
  }
}

```
