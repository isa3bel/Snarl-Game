package model;

import java.util.ArrayList;

/**
 * Constructs a hallway on a 2d array of spaces.
 */
public class HallwayBuilder extends SpaceBuilder {

  private final RoomBuilder toRoom;
  private final RoomBuilder fromRoom;
  private final ArrayList<Location> waypoints;

  /**
   * Constructor for this HallwayBuilder
   * @param toRoom the room that this HallwayBuilder leads to
   * @param fromRoom the room that this HallwayBuilder goes to
   * @throws IllegalArgumentException if either the 'toRoom' or 'fromRoom' room is null
   */
  public HallwayBuilder(RoomBuilder toRoom, RoomBuilder fromRoom) throws IllegalArgumentException {
    if (toRoom == null || fromRoom == null) {
      throw new IllegalArgumentException("You must provide two rooms");
    }

    this.toRoom = toRoom;
    this.fromRoom = fromRoom;
    this.waypoints = new ArrayList<>();
  }

  /**
   * Add a waypoint at the given coordinates toRoom this hallway.
   * @param row the row coordinates of the waypoint
   * @param column the column coordinates of the waypoint
   * @return this builder with the waypoint
   */
  public HallwayBuilder addWaypoint(int row, int column) {
    this.waypoints.add(new Location(row, column));
    return this;
  }

  /**
   * Builds this hallway on the array of spaces
   * @param spaces the 2d array toRoom mutate with this hallway
   */
  void build(ArrayList<ArrayList<Space>> spaces) {
    // DECISION: does not verify that both rooms are actually in a level
    // DECISION: does not verify that a hallway does not overlap with itself
    ArrayList<Location> hallwayTiles = this.waypoints.size() == 0
        ? this.toRoom.calculateLocationsFromRoomDoors(this.fromRoom)
        : this.calculateLocationsFromWaypoints();

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
  private ArrayList<Location> calculateLocationsFromWaypoints() {
    ArrayList<Location> locations = new ArrayList<>();
    Location firstWaypoint = this.waypoints.get(0);
    Location startingPoint = this.fromRoom.getClosestDoorOnAxis(firstWaypoint);
    Location previousPoint = startingPoint;

    for (Location nextWaypoint : this.waypoints) {
      locations.addAll(previousPoint.to(nextWaypoint));
      previousPoint = nextWaypoint;
    }

    Location endingPoint = this.toRoom.getClosestDoorOnAxis(previousPoint);
    locations.addAll(previousPoint.to(endingPoint));

    locations.remove(startingPoint);
    locations.remove(endingPoint);
    return locations;
  }
}
