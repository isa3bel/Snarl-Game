/**
 * Executes the command to query if a passage is safe.
 */
public class PassageSafe implements Command {

  private String character;
  private String town;

  PassageSafe(String character, String town) {
    this.character = character;
    this.town = town;
  }

  /**
   * Executes asking if the passage is safe and prints the result from the server.
   * @param townNetwork the town network on which to do the command
   * @return the townNetwork after executing the command
   * @throws IllegalArgumentException when townNetwork does not exist (is null)
   */
  @Override
  public ClientTownNetworkGarbagePlaceholder doCommand(ClientTownNetworkGarbagePlaceholder townNetwork)
    throws IllegalArgumentException {
    if (townNetwork == null) {
      throw new IllegalArgumentException("must create a town network before executing commands on the network");
    }

    boolean isReachable = townNetwork.canReachTownAlone(this.character, this.town);
    System.out.println(isReachable);
    return townNetwork;
  }
}
