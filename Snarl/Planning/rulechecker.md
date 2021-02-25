Date: February 24th, 2021  
To: Khoury College of Computer Sciences at Northeastern University  
From: Lynnsey Martin and Isabel Bolger  
Subject: Snarl Game Rule Checker Design  

Dear Management at Growl Inc.,  

This document is a proposal of the design for the ruler checker, which will be responsible for validating various parts of the Snarl game. At a high level, we are implementing the responsibilities of a rule checker in 3 main classes called: Interaction, MoveValidator, and GameStateValidator.  

We chose to split up the validation among three classes. This is to follow the single responsibility principle (e.g. only one of interaction validation, movement validation, or gamestate validation per class) and keep validation functionality local to the information that it is validating.  

The three primary classes described above allow us to easily implement different types of rules based on the rules for specific game objects. For example, a MoveValidator will be extended by, at minimum, a PlayerMoveValidator and an AdversaryMoveValidator. As necessary, more MoveValidators can be created by extending the common, abstract MoveValidator or another similar validator and overwriting its methods.  

The MoveValidator class will have the rules for validating a move, which will be done through the method “isValid.” The location of the move to be validated would be stored as a field in the MoveValidator class. Since validation of a move will also require the level and other characters in a level, the method will take that info as arguments. For the different characters that can move, a different class will extend the MoveValidator and update the rules accordingly.  

```
abstract class MoveValidator<T extends Character> {
  Location nextMove;

  boolean isValid(Level level, List<Character> characters);
}
```

Our game has a concept of “Interactables,” which are items in the game that can be “interacted with.” An Interaction will be an object that validates and/or executes the Interaction between a character and an Interactable. Therefore, we need the Interaction to be a visitor for an Interactable. The rules for the specific interaction will be based in the visitor methods - e.g. an interaction between a player and an Item will be in the PlayerInteraction class in a “visitItem(Item item)” method.  

```
abstract class Interaction<T extends Character> implements InteractableVisitor {
  T character;

  void visitSpace(Space space);
  void visitAdversary(Adversary adversary); 
  void visitPlayer(Player player);
  void visitItem(Item item); 
}
```

Finally, the GameStateValidator object will validate the GameState based on the valid configurations of a Level and the Characters in the game. Therefore, our GameStateValidator will have a validator method that will return whether or not the state is valid according to the rules of the game. For example, that players are not standing on wall tiles, in the same positions as other characters, etc. This would return a boolean for whether that state is valid. Second, the GameStateValidator would also have a method to evaluate the state of the game - e.g. whether the game is won, lost, or still playing. This would return an enum of the potential game states.  

```
class GameStateValidator {
	boolean isValid(Level level, List<Character> characters);
	GameState evaluateGameState(int currentLevel, Level[] levels,  List<Character> characters);
}
```

This separation of a RuleChecker into separate objects fits our current design and the requirements of the assignment without requiring significant future changes. It anticipates potential for changing the rules in many of the ways possible (e.g. allowing different possibilities for interactions or movements). If you have any questions, please follow up.  

Sincerely,  
Lynnsey Martin & Isabel Bolger  
