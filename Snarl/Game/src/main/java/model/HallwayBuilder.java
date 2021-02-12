package model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Constructs a hallway on a 2d array of spaces.
 */
public class HallwayBuilder {

  RoomBuilder to;
  RoomBuilder from;
  ArrayList<Location> waypoints;

  public HallwayBuilder(RoomBuilder to, RoomBuilder from) throws IllegalArgumentException {
    if (to == null || from == null) {
      throw new IllegalArgumentException();
    }

    this.to = to;
    this.from = from;
    this.waypoints = new ArrayList<>();
  }

  public HallwayBuilder waypoint(int x, int y) {
    this.waypoints.add(new Location(x, y));
    return this;
  }

  /**
   * Builds this hallway on the array of spaces
   * @param spaces the 2d array to mutate with this hallway
   */
  void build(ArrayList<ArrayList<Space>> spaces) {
    ArrayList<Location> hallwayTiles = this.waypoints.size() == 0 ?
        this.to.betweenDoors(this.from) : this.fromWaypoints();

    for (Location location : hallwayTiles) {
      spaces.get(location.y).set(location.x, new HallwayTile(this.toString()));
    }
  }

  /**
   * Connects between the rooms through the waypoints.
   * @return the locations connecting all these waypoints
   */
  private ArrayList<Location> fromWaypoints() {
    ArrayList<Location> locations = new ArrayList<>();
    Location first = this.from.doorOnAxis(this.waypoints.get(0));
    Location prev = first;
    for (Location next : this.waypoints) {
      locations.addAll(prev.to(next));
      prev = next;
    }
    Location last = this.to.doorOnAxis(first);
    locations.addAll(prev.to(last));

    locations.remove(first);
    locations.remove(last);
    return locations;
  }
}
