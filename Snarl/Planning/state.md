Date: February 13th, 2021  
To: Khoury College of Computer Sciences at Northeastern University  
From: Lynnsey Martin and Isabel Bolger  
Subject: Snarl game management system  
  
Dear Management at Growl Inc.,  
  
This document is a proposal of the implementation of a game management system for Snarl. It will come in three parts - first, a high level description of the system that will run a game and the basic functionality that will run the dungeon. The second section of this memo will detail the subcomponents of a Snarl game and the scope of the information that they have access to. Finally, we will highlight the internal interactions of a Snarl game that allows the state in the dungeon to progress.  
  
A GameManagement object will store the level layout, the number of players, any automated adversaries, and items in the level (including keys). That data will be kept in the format of a Level, a list of Characters (the players and the adversaries combined), and a list of Items.  
  
A round in the game consists of each Character in GameManagement doing their “turn.” Rounds will repeat until the game has been won or lost. At the start of each round, GameManagement will update a Character with their view of the game state. Characters in the game will complete their turn in the order defined by the List of Characters maintained in GameManagement. Each character’s turn will consist of:  
 - GameManagement queries the Character for its requested move,  
 - GameManagement ensures that the move is valid according to the level layout and game specific rules for the Character’s movement (if invalid, repeat the first and second bullets until valid),  
 - GameManagement tells the Character where to move and the potential objects that the Character will interact with (a Character will calculate the Interactions and update any game objects accordingly), and  
 - GameManagent determines if the game has been won or lost.  
  
These interactions will require interfaces and classes including (not limited to) the following.  
  
```
class GameManagement {
  int currentLevel;
  List<Level> levels;
  List<Character> characters;
  List<Item> items;

  // do a round, is there another
  boolean doRound();
  boolean doTurn(Character c);
}

class Level {
  List<List<Space>> spaces;
  Space getSpace(Location location);
}

abstract class Character {
  Location currentLocation;
  Controller controller;
  
  // decides how to update controller with game info
  void updateWithState(Level level, List<Character> players, List<>);
  // queries controller, returns location wrapped in a validator
  MoveValidator nextMove();
  // moves this character and calculates interactions accordingly 
  void moveAndInteract(Location loc, List<Interactable> objects);
}
```

Finally, a crucial detail in this plan is the concept of Interactables and Interactions. An Interactable is any interface representing any game object that may be “interacted” with by a Character. Currently, that may include Character, Item, Door, and Exit.  
  
An Interaction describes what happens when a Character interacts with an Interactable. An interaction will be a visitor to an Interactable. For each extension of Character, it will have an implementation e.g. a PlayerInteraction defining Player-Interactable interactions and an EnemyInteraction defining Enemy-Interactable interactions. A PlayerInteraction might look like:  
  
```
class PlayerInteraction implements Interaction {
  Player player;

  // handles the interaction between a player and a door (e.g. moves
  // the Player to the next room
  void visitDoor(Door door);

  // handles the interaction between a player and an item (e.g. adds
  // the item to the player’s inventory
  void visitItem(Item item);

  // ... visit methods for other Interactables ...
}
```
  
These Interactions will be created by the Character at the end of its turn. An Interaction will mutate the Character and/or the given Interactable based on the game rules for an interaction between those game objects.  
  
If you have any questions, please reach out.  
  
Sincerely,  
Isabel Bolger and Lynnsey Martin  
