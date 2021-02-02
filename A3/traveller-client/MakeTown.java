import java.util.HashMap;
import java.util.Map;

/**
 * Command to make a town network using the server.
 */
public class MakeTown implements Command {

  private Road[] roads;

  MakeTown(Road[] roads) {
    this.roads = roads;
  }

  /**
   * Does this command (creates a town network with these raods).
   * @param townNetwork garbage value to be passed in to create the town network
   * @return the town network created by the server
   * @throws IllegalArgumentException if townNetwork is non-null (because that means this was not the
   *   first command called)
   */
  @Override
  public ClientTownNetworkGarbagePlaceholder doCommand(ClientTownNetworkGarbagePlaceholder townNetwork)
    throws IllegalArgumentException {
    if (townNetwork != null) {
      throw new IllegalArgumentException("creating the town must be the first command called");
    }

    String serverString = this.makeStringForServer();
    // use the server to make the town network, return that value
    return null;
  }

  /**
   * Gets all the towns reachable for each town in this network.
   * @return a map of one town name to all the towns it can reach
   */
  private Map<String, String> getReachableRoadsByTown() {
    HashMap<String, String> reachable = new HashMap<>();
    for (Road road : this.roads) {
      String tos = reachable.get(road.getFrom());
      if (tos == null) {
        tos = road.getFrom() + ": " + road.getTo();
      }
      else {
        tos += ", " + road.getTo();
      }

      reachable.put(road.getFrom(), tos);
    }
    return reachable;
  }

  /**
   * Creates the string to pass to the server that will create a town.
   * @return a string of the format "A: B, C, D; B: A, D; C: A; D: A, B" where the name before the colon
   * is the town and the reachable towns follow, separated by commas. these value for each town will be
   * separated by semi-colons
   */
  private String makeStringForServer() {
    Map<String, String> reachable = this.getReachableRoadsByTown();
    String result = "";
    for (String townList : reachable.values()) {
      result += townList + "; ";
    }
    return result;
  }

}
