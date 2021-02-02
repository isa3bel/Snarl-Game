package travellerClient;

/**
 * Placeholder interface while waiting for the server library.
 * Methods here specified in the Traveller.md file in A2, therefore are expected.
 */
public class ClientTownNetworkGarbagePlaceholder {

  // only public for tests
  // (ideally this would be made in the test file as needed,
  // but it is also serving as a place holder in the code)
  public String constructor;
  public String placeCharacter;
  public String canReachTownAlone;

  public ClientTownNetworkGarbagePlaceholder(String constructor) {
    // garbage mock for tests
    this.constructor = constructor;
  }

  void placeCharacter(String characterName, String townName) {
    // garbage mock for tests
    this.placeCharacter = characterName + " " + townName;
  }

  boolean canReachTownAlone(String characterName, String townName) {
    // garbage mock for tests
    this.canReachTownAlone = characterName + " " + townName;
    return false;
  }

}
