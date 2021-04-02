# How to play the Snarl Game

- Run the executable, the game can be played in the terminal

- Input each player's name. This input cannot be blank

- Each player's view will be displayed. The terminal will prompt each user for their choice of movement

Important: A person's location is shown in the form [row, column], where higher rows are further down, and higher columns are further right. The row field essentially controls up and down motions, while the column field controls the left and right motion.

- If a player choses an invalid move, the terminal will re-prompt the user for their move and will not re-show the view

- If a player is "ejected" by an adversary, they cannot make additional moves for that round, and their view will not be displayed

- A player can move to an exit before it's unlocked, but nothing will happen and the player's icon will show instead of the exit until they move away from the exit space

- When a level advances the process will continue and will show both users an updated view as well as prompt them for new moves

- When the game ends the program will terminate and display an appropriate message for the users. Note: levels start at index 0
