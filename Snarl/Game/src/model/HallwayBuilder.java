package model;

import java.util.ArrayList;

public class HallwayBuilder {

  RoomBuilder to;
  RoomBuilder from;
  ArrayList<Location> waypoints;

  public HallwayBuilder(RoomBuilder to, RoomBuilder from) throws IllegalArgumentException {
    if (to == null || from == null) {
      throw new IllegalArgumentException();
    }
  }

  public HallwayBuilder waypoint(int x, int y) {
    this.waypoints.add(new Location(x, y));
    return this;
  }

  void build(ArrayList<ArrayList<Space>> spaces) {
    ArrayList<Location> hallwayTiles = this.waypoints.size() == 0 ?
        this.to.betweenDoors(this.from) : this.fromWaypoints();

    for (Location location : hallwayTiles) {
      spaces.get(location.y).set(location.x, new HallwayTile(this.toString()));
    }
  }

  private ArrayList<Location> fromWaypoints() {
    ArrayList<Location> locations = new ArrayList<>();

    // TODO: implement this to use the waypoints
    return locations;
  }

  boolean overlaps(HallwayBuilder hallway) {
    // TODO: implement this
    return false;
  }
}
