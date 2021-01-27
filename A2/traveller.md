Java 1.8

# Java API for Traveller

## Specifies the entry point for the Traveler game

For designing the Traveler module, we decided that the best approach would be to specify a public class (perhaps called Traveler) which would be the entry point for the API. This class would represent the overall network of the game, and contain two methods: 

- public void placeCharacter(String characterName, String townName) - places the given character in the given town
- public boolean canReachTownAlone(String characterName, String townName) - determines if the given character can reach the given town without running into another character

Upon instantiating an object of this “Traveler” class, the network of “towns” or “nodes” should be created. The network of towns is suggested to be an ArrayList of towns or nodes. 


## Suggested implementation details
### Implementation of Traveller


### Town representation
Towns should have a concept of the other towns that are directly reachable from that Town, as well as the characters currently occupying the Town. Therefore, we might expect for a Town to have a public interface with methods that check if a Town has a character with a given name or calculates whether another town is reachable without seeing any characters.  
  
Some challenges might be in making sure that Towns are not while traversing the map, so it is crucial to make sure that the implementation details account for keeping track of visited Towns in the network.

### Character representation
The character interface should be relatively simple. The primary feature of this interface should be a method to confirm that some Character has a given identity, e.g. in the case of this application, that some Character has a given name.  
