# How to play the Snarl Game this Server

- Run the `snarlServer` executable with the arguments that you require (specified under [Testing Task > snarlServer](https://course.ccs.neu.edu/cs4500sp21/p09.html))

- Connect with your client before the maximum wait time has passed (defaults to 60 seconds if not specified in program arguments)

- After the wait time has passed with no new clients or if the max number of clients connects, the game will begin. The server will begin sending messages as specified by the protocol [here](https://course.ccs.neu.edu/cs4500sp21/protocol.html). Client messages are expected to follow the same format.

# How to play the Snarl Game with this Client

- This program can be played entirely from the terminal, not extra GUI required.

- Run the `snarlClient` executable with the arguments that you require (specified under [Testing Task > snarlServer](https://course.ccs.neu.edu/cs4500sp21/p09.html))

- Connect your client to the Snarl Server which is hosting the game.

- The client will then prompt you for your name. Input an alphanumeric name for the game.

- When game play begins, the messages from the server will align with the protocol outlined [here](https://course.ccs.neu.edu/cs4500sp21/protocol.html).
  - When you are prompted for a move, the move input must be specified as two integers, the global row and column e.g. `3 1`.
  - If your move is invalid, the client will continue to prompt you for a move until it is valid.
  - When you receive a `player-update-message`, the level layout will be printed as an ASCII map of the players visible spaces. Your current location as well as any potential messages from the server will be dispayed alongside this map. _(Please note: the map can only represent one thing at each location - this means that if an adversary or a player is stading on top of the key or exit, the key or exit will not be visible and it will appear that only the actor is standing at that location)._
    - Walls are `X`
    - Walkable tiles are ` `
    - Doors are `D`
    - Other players are `P`
    - Zombies are `Z`
    - Ghosts are `G`
    - The key is `K`
    - The exit is `E`
    - You are `+` when playing over TCP and the first letter of your username when playing locally
    
- When the game ends, the client will print the results provided by the server and end the program.

# Combat/Hit System

- In this version of the game, adversaries and players can "attack" each other. To attack an adversary, simply choose a space that an advesary is located at. This will "attack" the adversary. When an adversary is attacked, their health points will decrement by the amount of points that a player can cause damage to._
	- Players start with 8 points and cause 2 points worth of damage
	- Zombies have 4 points and cause 2 points worth of damage
	- Ghosts have 6 points and cause 1 point worth of damage

- When a character is attacked, the program will print the amount of points that it has left. Once a character has no more health points left, they are ejected from the game. For adversaries, it means that they will no longer be making moves, and will not be shown in the view. For playeres, it means that they are ejected, and can no longer choose additional moves until the game advances levels.
  
# Remote Adversaries

- For each adversary you want to run remotely, run an instance of the `snarlAdversaryClient` executable.
- In addition to the `--port` and `--address` arguments, the adversary client also accepts a `--type` argument which must be one of "zombie" or "ghost" for the type of adversary this remove client will control.
- Adversaries still only exist in a single level. Remote adversary clients will be used in the earliest levels first. If there are more remote adversaries provided than adveraries in the game, nothing will happen (the extra adversaries will just not be used).
- Actor registration has updated to still request the registration with `"name"` message. Now, the server expects a response in the form:
```
{
  "type": (actor-type),
  "name": string
}
```
where `(actor-type)` represents one of "player", "zombie", or "ghost".
- The `snarlServer` sends "adversary-update" messages to the client when the adversary is updated. These updates come in the form:
```
{
  "type": "adversary-update",
  "validMoves": (location-list),
  "nearestPlayerLocation": (location),
  "keyLocation": (location),
  "exitLocation": (location),
  "closestWall": (location)
}
```
- When the server is requesting a move from the client, it will send a `"move"` message and expect a returned message `{ "type": "move", "to": (location) }`
