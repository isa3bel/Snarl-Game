# Hit-Combat System

The hit-combat system does not interfere with any implementation of the Snarl Protocol. The initial implementation was that any Adversary could "attack" a player, and a Player could "defend" itself, by settings its current location to Ejected. In this new implementation, both adversaries AND players can attack each other, as well as sustain attacks by other characters through "health points".

We moved the attack and defend implementation methods to the abstract Character class, since it wasn't specific to adversaries anymore. We also created a new MoveResult type called "Attack", so we could distinguish between a normal, "Ok" move with a special, "Attack" move. When a player needs to defend itself, it decrements the amount of health points it has by the amount of damage that the attacker is causing (the amount of health points and damage points are set in each character type's constructor). Once the character has no more health points, it is ejected from the game.

Zombies and Ghosts are slightly different in terms of health/attack points they have._
	- Zombies start with 4 points and can cause 2 points worth of damage
	- Ghosts start with 6 points and cause 1 point worth of damage

When a character attacks another character WITHOUT ejecting it from the game, it will cause damage and return to the original location it started at (implemented in attack() in Character class). If a character attacks and ejects another character, the attacker will take over the space that the attackee was on. Both  of these move cases are reflected in the View.