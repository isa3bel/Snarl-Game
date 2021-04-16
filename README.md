# How to play the Snarl Game this Server

- Run the `snarlServer` executable with the arguments that you require (specified under [Testing Task > snarlServer](https://course.ccs.neu.edu/cs4500sp21/p09.html))

- Connect with your client before the maximum wait time has passed (defaults to 60 seconds if not specified in program arguments)

- After the wait time has passed with no new clients or if the max number of clients connects, the game will begin. The server will begin sending messages as specified by the protocol [here](https://course.ccs.neu.edu/cs4500sp21/protocol.html). Client messages are expected to follow the same format.

# How to play the Snarl Game with this Client

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
    - You are `+`
    
- When the game ends, the client will print the results provided by the server and end the program.
