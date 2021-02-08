Dear CEO of Growl Inc.,   

I am writing to you in regards to the design and description of the Snarl game. The main components of this game consist of game management, characters, items, interactions, and a controller. More specifically, there are specific types of characters, such as players, and enemies, as well as specific items, such as a key. The way we designed these components make it easy for us to add additional characters and items. See local.md for UML

The rooms are made up of walls, tiles, and doors. There are two types of doors, a door that leads to another room, and a door that leads to another level.
See local.md for UML for the components that relate to a "room".

The way we designed components interacting with one another is through the visitor pattern. Classes that implement that Interactable interface, such as the characters, and items, are able to handle interacting with a character (enemy, player), via the "visitor design pattern" (specifically with the use of those that implement the Interaction interface). Our design makes it easy for us to customize various interactions now or in the future. See local.md for UML

The game management class is the highest level class that manages the state of the game, and handles rounds, levels, and moves of the character. The game management class needs characters and a list of rooms. Note that this class is specific for each level, as in once the level advances, this class must mutate the rooms in which it's keeping track of. See local.md for UML
