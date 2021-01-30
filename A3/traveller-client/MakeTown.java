import java.util.HashMap;
import java.util.Map;

public class MakeTown implements Command {

  private Road[] roads;

  MakeTown(Road[] roads) {
    this.roads = roads;
  }

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

  public String toString() {
    Map<String, String> reachable = this.getReachableRoadsByTown();
    String result = "make: ";
    for (String townList : reachable.values()) {
      result += townList;
    }
    return result;
  }

}
