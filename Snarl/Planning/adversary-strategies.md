# Diagram Key
`Z` = zombie  
`G` = ghost  
`X` = wall  
`E` = exit  
`K` = key  
`P` = player  
` ` = traversable tile  

# Zombie Strategy
1. If the player is within 8 tiles of the zombie, go to the valid move that will result in the zombie being closest to the player.  
  e.g.
  ```
  XXXXXXX     XXXXXXX
  X  P  X     X  P  X
  X     X  -> X  Z  X
  XK Z EX     XK   EX
  XXXXXXX     XXXXXXX
  ```
2. Otherwise, if the key is still in the game, go to the valid move that will result in the zombie being closest to the key. 
  e.g.
  ```
  XXXXXXX
  XXXPXXX
  XXX XXX
  XXXDXXX
  XXX XXX        .
  XXX XXX        .
  XXX XXX        .
  XXXDXXX     XXXXXXX
  X     X     X     X
  X     X  -> X     X
  XK Z EX     XKZ  EX
  XXXXXXX     XXXXXXX
  ``` 
3. Otherwise, go to the valid move that will result in the zombie being closest to the exit.  
  e.g.
  ```
  XXXXXXX
  XXXPXXX
  XXX XXX
  XXXDXXX
  XXX XXX        .
  XXX XXX        .
  XXX XXX        .
  XXXDXXX     XXXXXXX
  X     X     X     X
  X     X  -> X     X
  X  Z EX     X   ZEX
  XXXXXXX     XXXXXXX
  ``` 

# Ghost Strategy
1. If the player is within 8 tiles of the ghost, go the valid move that will result in the ghost being closest to the player.  
  e.g.
  ```
  XXXXXXX     XXXXXXX
  X  P  X     X  P  X
  X     X  -> X  Z  X
  XK Z EX     XK   EX
  XXXXXXX     XXXXXXX
  ```
2. Otherwise, if the key is still in the game and within 5 tiles, go to the valid move that will result in the ghost being closest to the key.  
  e.g.
  ```
  XXXXXXX
  XXXPXXX
  XXX XXX
  XXXDXXX
  XXX XXX        .
  XXX XXX        .
  XXX XXX        .
  XXXDXXX     XXXXXXX
  X     X     X     X
  X     X  -> X     X
  XK G EX     XKG  EX
  XXXXXXX     XXXXXXX
  ``` 
3. Otherwise, go to the valid move that will result in the ghost being closest to the nearest wall.  
  e.g.
  ```
  XXXXXXX
  XXXPXXX
  XXXKXXX
  XXXDXXX
  XXX XXX        .
  XXX XXX        .
  XXX XXX        .
  XXXDXXX     XXXXXXX
  X     X     X     X
  X     X  -> X     X
  X  G EX     X    EX
  XXXXXXX     XXXGXXX  // note: this will move the ghost to some other random location in the level
  ``` 
