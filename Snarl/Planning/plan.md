Date: February 5th, 2021  
To: Khoury College of Computer Sciences at Northeastern University  
From: Lynnsey Martin and Isabel Bolger  
Subject: Traveller implementation from other team  
  
Dear CEO of Growl Inc.,   

I am writing to you in regards to the design and description of the Snarl game. The main components of this game consist of game management, characters, items, interactions, and a controller. See local.md for UML

A character represents anything that moves, and will be extended by player and enemy. A character always knows its location and a Controller, which handles planning the movement of the Character. The Controller interface can be implemented for different kinds of connections (for example, over a TCP connection, through STDIN, or an automated controller for enemies). In addition to its location and who to talk to about movement (by extending character), a player knows its health and inventory while an enemy only knows the attack points that it has.  
  
The rooms are made up of walls, tiles, and doors. There are two types of doors: a door that leads to another room, and a door that leads to another level.  
  
The game management class is the highest level class that manages the state of the game, and handles rounds, levels, and each character's turn. The game management class knows the characters, items, and rooms in a level. Note that this class is specific for each level, as in once the level advances, this class must mutate the rooms in which it's keeping track of.  
  
The way we designed components interacting with one another is through the visitor pattern. Classes that implement that Interactable interface, such as the characters, doors, and items, are able to handle interacting with a character (enemy, player) or the game through the Interaction interface.
  
This design prioritizes scalability, since this game will change in the future. While flexible, it also ensures that only essential knowledge is shared between elements of the game and that we follow the single responsibility principle for each class.  
  
To begin implementing this game, we will have stages of development as outline below:  
 1. Create a level with a single room (no doors) with any number of players that can navigate around the room. This will require setting up the basic game management system, a character/player, some kind of controller for that player, and the view (unspecified in this outline as we do not know where and how the game will be displayed).
 2. Create a level with a multiple rooms and doors. This will will require expanding the game management system to create multiple rooms, handle positions across those different rooms, allow interactable objects, handle their associated interactions, and place items in a room (like a key).
 3. Add an enemy to a room. This will require creating an enemy, its automated controller, its interactions with the rest of the world, and a way to represent "expelled" characters.
 4. Finally, advance levels in the game. This requires making an additional type of door and a way to reset the game to initiate a new level.
  
Sincerely,  
Isabel Bolger and Lynnsey Martin  
