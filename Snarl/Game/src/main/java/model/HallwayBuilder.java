package model;

import java.util.ArrayList;

/**
 * Constructs a hallway on a 2d array of spaces.
 */
public class HallwayBuilder extends SpaceBuilder {

  private final RoomBuilder to;
  private final RoomBuilder from;
  private final ArrayList<Location> waypoints;

  public HallwayBuilder(RoomBuilder to, RoomBuilder from) throws IllegalArgumentException {
    if (to == null || from == null) {
      throw new IllegalArgumentException("You must provide two rooms");
    }

    this.to = to;
    this.from = from;
    this.waypoints = new ArrayList<>();
  }

  /**
   * Add a waypoint at the given coordinates to this hallway.
   * @param x the x coordinates of the waypoint
   * @param y the y coordinates of the waypoint
   * @return this builder with the waypoint
   */
  public HallwayBuilder waypoint(int x, int y) {
    this.waypoints.add(new Location(x, y));
    return this;
  }

  /**
   * Builds this hallway on the array of spaces
   * @param spaces the 2d array to mutate with this hallway
   */
  void build(ArrayList<ArrayList<Space>> spaces) {
    // DECISION: does not verify that both rooms are actually in a level
    // DECISION: does not verify that a hallway does not overlap with itself
    ArrayList<Location> hallwayTiles = this.waypoints.size() == 0 ?
        this.to.betweenDoors(this.from) : this.fromWaypoints();

    for (Location location : hallwayTiles) {
      Location outer = new Location(location.x + 1, location.y + 1);
      this.initSize(outer, spaces);
      spaces.get(location.y).set(location.x, new HallwayTile(this.toString()));
    }
  }

  /**
   * Connects between the rooms through the waypoints.
   * @return the locations connecting all these waypoints
   */
  private ArrayList<Location> fromWaypoints() {
    ArrayList<Location> locations = new ArrayList<>();
    Location first = this.from.closestDoorOnAxis(this.waypoints.get(0));
    Location prev = first;
    for (Location next : this.waypoints) {
      locations.addAll(prev.to(next));
      prev = next;
    }
    Location last = this.to.closestDoorOnAxis(prev);
    locations.addAll(prev.to(last));

    locations.remove(first);
    locations.remove(last);
    return locations;
  }
}
