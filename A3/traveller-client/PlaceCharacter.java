/**
 * Command to place a character at a given town.
 */
public class PlaceCharacter implements Command {

  private String character;
  private String town;

  PlaceCharacter(String character, String town) {
    this.character = character;
    this.town = town;
  }

  /**
   * Executes the server's placeCharacter method.
   * @param townNetwork the town network on which to do the command
   * @return the town network after the character has been placed
   * @throws IllegalArgumentException if the town network has not been created before trying to execute this command
   *   (e.g. townNetwork is null)
   */
  @Override
  public ClientTownNetworkGarbagePlaceholder doCommand(ClientTownNetworkGarbagePlaceholder townNetwork)
    throws IllegalArgumentException {
    if (townNetwork == null) {
      throw new IllegalArgumentException("must create a town network before executing commands on the network");
    }

    townNetwork.placeCharacter(this.character, this.town);
    return townNetwork;
  }
}
