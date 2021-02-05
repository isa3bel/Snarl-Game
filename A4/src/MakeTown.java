package src;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * travellerClient.Command to make a town network using the server.
 */
public class MakeTown implements Command {

  private String[] towns;
  private Road[] roads;

  /**
   * Makes a command to create a town network with the given roads.
   * @param roads the roads of the town network
   * @throws IllegalArgumentException if any of the roads do not have both a to and from
   */
  MakeTown(Road[] roads) throws IllegalArgumentException {
    if (roads.length == 0) {
      throw new IllegalArgumentException("town must be created with at least one road");
    }
    if (Arrays.stream(roads).anyMatch(road -> road.getFrom() == null || road.getTo() == null)) {
      throw new IllegalArgumentException("to make a town, all roads must have a non-null to and from field");
    }

    this.towns = MakeTown.calculateTownsFromRoads(roads);
    this.roads = roads;
  }

  /**
   * Calculates the towns from the list of roads
   * @param roads the list of roads connecting the towns
   * @return an array of strings representing the towns' names
   */
  private static String[] calculateTownsFromRoads(Road[] roads) {
    ArrayList<String> roadsTemp = new ArrayList<>();
    for (Road road : roads) {
      if (!roadsTemp.contains(road.getTo())) roadsTemp.add(road.getTo());
      if (!roadsTemp.contains(road.getFrom())) roadsTemp.add(road.getFrom());
    }

    return roadsTemp.toArray(new String[0]);
  }

  /**
   * Visit this command with the given visitor.
   * @param visitor visitor to apply to this MakeTown
   */
  @Override
  public void accept(Command.Visitor visitor) {
    visitor.visitMakeTown(this);
  }

}
