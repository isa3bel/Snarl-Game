Date: March 3rd, 2021  
To: Khoury College of Computer Sciences at Northeastern University  
From: Lynnsey Martin and Isabel Bolger  
Subject: Game Manager and Player Design  

Dear Management at Growl Inc.,  

This document is for a proposal of the design for further details on the interface and extensibility of the Player and the rest of the game objects. At a high level, our design follows a MVC pattern, meaning that the individual view for a player can be altered with different view components. The game manager will be responsible for progressing the game such as doing a person’s turn, doing a round, and progressing the characters of the game. As specified in the previous milestone, the GameStateValidator and MoveValidator interfaces will validate the user’s move, as well as ensure the game state is valid. In addition, the Interactions will visit interactables to validate interactions when a player is on a tile with an object. For further details, continue with the descriptions below.  

Since players should only be able to see at most 2 grid units away from any cardinal or diagonal direction, this will be implemented as a new view class.The view will have all the game information, but will only draw some portion of the game state based on the player’s visibility. As such, we will have interfaces as defined below while the GameManager manages the creation of the view:  

```java
class PlayerASCIIView implements View {
  Player player;

  void renderLevel(Level level) { ... }
  void placeCharacters(List<Character> character) { ... }
  void draw() { ... }
}

abstract class Character {
  // ... other player methods ...
  View makeView() { ... }
  void updateControllerWithView(View view) { ... }
}
```

Through the PlayerMoveValidator class, all forms of validation (including whether the player can remain in one place) will be allowed or disallowed. As described in the previous assignment, this will be delegated to the isValid method of the PlayerMoveValidator class.  

Validation for an interaction between a Player and other Interactables will take place in the PlayerInteraction class. For example, to validate that an interaction only occurs between a Player and an Item when they have the same location, the PlayerInteraction may look like:  

```java
class PlayerInteraction extends Interaction {
  Player player;
  // ... other interaction methods ...
  void visitItem(Item item) {
    if (item.getCurrentLocation().equals(player.getCurrentLocation())
      return;
    item.pickedUp(player);
}
```

In terms of the Player component, our concept of a Player class includes a field that keeps track of its current location in relation to the origin. To notify the user of a player’s location in relation to the origin, that would only be a small adjustment to our PlayerView (described at the start of this memo).  

The GameManager controls the order of interactions and player turns through the doTurn method that will complete a character’s turn and update the game accordingly. This can easily be adjusted as necessary and while this order is maintained for now, it can be reinforced or updated as necessary in that method by sorting the list of Interactables to interact with or merely shifting the order of the interact method calls.  

Below, you will find a detailed interface of the GameManager. For the corresponding interface of our Player, see player.md. We hope this provides some further clarity on the interface between our player and the rest of the Snarl game. If you have any further questions, please feel free to follow up. Thank you.  

Sincerely,  
Isabel Bolger & Lynnsey Martin  

```java
class GameManager {
  // ... other fields ... 
  ArrayList<Character> characters;

  // complete an entire "round" of the snarl game - every character takes their turn
  void doRound() {
    // first, update all the characters with their "view"
    for (Character currentCharacter : this.characters) {
       View characterView = currentCharacter.getView();
       this.buildView(characterView);
       currentCharacter.updateWithView(characterView);
    }
    
    // then, actually do their turns
    this.characters.forEach(this::doTurn);
  }

  // move a character and update the world
  void doTurn(Character character) {
    // calculate and get the next move until it is valid according to the MoveValidator (extended for each type of character)
    MoveValidator nextMove;
    do {
      moveValidator = character.getNextMove();
    while (!moveValidator.isValid());
    moveValidator.executeMove();
    
    // after the player has been moved, go through and calculate the interactions (first enemies, then items, then doors - items and doors inside level)
    Interaction interaction = character.calculateInteraction();
    this.characters.forEach(character -> character.acceptVisitor(interaction));
    this.level.interact(interaction);
  }
}
```


