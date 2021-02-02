import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Command to make a town network using the server.
 */
public class MakeTown implements Command {

  private Road[] roads;

  /**
   * Makes a command to create a town network with the given roads.
   * @param roads the roads of the town network
   * @throws IllegalArgumentException if any of the roads do not have both a to and from
   */
  MakeTown(Road[] roads) throws IllegalArgumentException {
    if (Arrays.stream(roads).anyMatch(road -> road.getFrom() == null || road.getTo() == null)) {
      throw new IllegalArgumentException("to make a town, all roads must have a non-null to and from field");
    }

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
    ClientTownNetworkGarbagePlaceholder createdTownNetwork = new ClientTownNetworkGarbagePlaceholder(serverString);
    return createdTownNetwork;
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
    int idx = 0;
    for (String townList : reachable.values()) {
      result += townList;
      result += idx++ < reachable.size() - 1 ? "; " : "";
    }
    return result;
  }

}
