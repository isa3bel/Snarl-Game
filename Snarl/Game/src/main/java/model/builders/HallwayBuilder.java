package model.builders;

import model.level.Location;
import model.level.HallwayTile;
import model.level.Space;

import java.util.ArrayList;

/**
 * Constructs a hallway on a 2d array of spaces.
 */
public class HallwayBuilder extends SpaceBuilder {

  private final Location toDoor;
  private final Location fromDoor;
  private final Location[] waypoints;

  /**
   * Constructor for this HallwayBuilder
   * @param toRoom the room that this HallwayBuilder leads to
   * @param fromRoom the room that this HallwayBuilder goes to
   * @throws IllegalArgumentException if either the 'toRoom' or 'fromRoom' room is null
   */
  public HallwayBuilder(RoomBuilder toRoom, RoomBuilder fromRoom, Location[] waypoints) throws IllegalArgumentException {
    if (toRoom == null || fromRoom == null) {
      throw new IllegalArgumentException("You must provide two rooms");
    }

    if (waypoints == null || waypoints.length == 0) {
      Location[] doorPair = toRoom.getClosestConnectingDoors(fromRoom);
      this.toDoor = doorPair[0];
      this.fromDoor = doorPair[1];
      this.waypoints = new Location[0];
    }
    else {
      this.toDoor = toRoom.getClosestDoorOnAxis(waypoints[waypoints.length - 1]);
      this.fromDoor = fromRoom.getClosestDoorOnAxis(waypoints[0]);
      this.waypoints = waypoints.clone();
    }
  }

  public HallwayBuilder(Location toDoor, Location fromDoor, Location[] waypoints) {
    if (toDoor == null || fromDoor == null) {
      throw new IllegalArgumentException("You must provide two doors");
    }

    this.toDoor = toDoor;
    this.fromDoor = fromDoor;
    this.waypoints = waypoints == null ? new Location[0] : waypoints.clone();
  }

  /**
   * Builds this hallway on the array of spaces
   * @param spaces the 2d array toRoom mutate with this hallway
   */
  public void build(ArrayList<ArrayList<Space>> spaces) {
    // DECISION: does not verify that both rooms are actually in a level
    // DECISION: does not verify that a hallway does not overlap with itself
    ArrayList<Location> hallwayTiles = this.calculateLocations();

    for (Location location : hallwayTiles) {
      Location outer = new Location(location.row + 1, location.column + 1);
      this.initSize(outer, spaces);
      spaces.get(location.row).set(location.column, new HallwayTile(this.toString()));
    }
  }

  /**
   * Connects between the rooms through the waypoints.
   * @return the locations connecting all these waypoints
   */
  private ArrayList<Location> calculateLocations() {
    ArrayList<Location> locations = new ArrayList<>();
    Location previousPoint = this.fromDoor;

    for (Location nextWaypoint : this.waypoints) {
      locations.addAll(previousPoint.to(nextWaypoint));
      previousPoint = nextWaypoint;
    }

    locations.addAll(previousPoint.to(this.toDoor));
    locations.remove(this.toDoor);
    locations.remove(this.fromDoor);
    return locations;
  }
}
