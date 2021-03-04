package testHarness;

import model.*;

import java.util.Arrays;
import java.util.Comparator;

public class ReachableQuery implements SpaceVisitor<Location[]>, WallVisitor<Location[]> {

  private Level level;
  private Location location;

  ReachableQuery(Level level, Location location) {
    this.level = level;
    this.location = location;
  }

  @Override
  public Location[] visitDoor(Door door) {
    return this.calculateLocationsForRoom();
  }

  @Override
  public Location[] visitExit(Exit exit) {
    return this.calculateLocationsForRoom();
  }

  @Override
  public Location[] visitAnyWall(Wall wall) {
    return wall.acceptWallVisitor(this);
  }

  @Override
  public Location[] visitWall(Wall wall) {
    return this.calculateLocationsForRoom();
  }

  @Override
  public Location[] visitEdgeWall(EdgeWall wall) {
    // TODO: clarify if walls on the outer edge of the room are in the room?
    return this.calculateLocationsForRoom();
  }

  @Override
  public Location[] visitVoidWall(VoidWall wall) {
    return new Location[0];
  }

  @Override
  public Location[] visitTile(Tile tile) {
    return this.calculateLocationsForRoom();
  }

  @Override
  public Location[] visitHallwayTile(HallwayTile tile) {
    return this.calculateRoomOriginsForHallway(this.location);
  }

  /**
   * Calculates the room origins for the doors on either side of this hallway.
   * @param locationInHallway a location in the relevant hallway
   * @return the origins of the rooms that the hallway connects to
   */
  private Location[] calculateRoomOriginsForHallway(Location locationInHallway) {
    return this.level
        .filter(IsAndNeighborIs.doorNeighboringHallway(this.level, this.level.get(locationInHallway)))
        .keySet()
        .stream()
        .map(this::calculateRoomOrigin)
        .toArray(Location[]::new);
  }

  /**
   * Calculates the room origins for the rooms that can be reached by this room through exactly one hallway.
   * @return the list of room origins that are reachable
   */
  private Location[] calculateLocationsForRoom() {
    Location thisRoomsOrigin = this.calculateRoomOrigin(this.location);
    Location[] hallwaysOutOfRoom = this.calculateHallwaysOutOfRoom();
    return Arrays.stream(hallwaysOutOfRoom)
        .map(hallwayLocation -> {
          Location[] roomOriginsForHallway = calculateRoomOriginsForHallway(hallwayLocation);
          // DECISION: if a room connects back to itself, it will still be included in the "connected rooms"
          return !roomOriginsForHallway[0].equals(thisRoomsOrigin)
              ? roomOriginsForHallway[0]
              : roomOriginsForHallway[1];
        })
        .toArray(Location[]::new);
  }

  /**
   * Calculates the origin of the room that the given location is in.
   * @param locationInRoom the location inside of the room
   * @return the location of this room's origin
   * @throws IllegalStateException if the location is not in a room
   */
  private Location calculateRoomOrigin(Location locationInRoom) throws IllegalStateException {
    // PRECONDITION: this location is in a ROOM (not a hallway)
    return this.level
        .filter((space, location) -> this.level.get(locationInRoom).sameGroup(space))
        .keySet()
        .stream()
        .min(Comparator.comparingInt(location -> location.row + location.column))
        .orElseThrow(() -> new IllegalStateException("given location is not in a room"));
  }

  /**
   * Finds the doors in this level that are leading out of the room in the
   * @return the locations the doors that
   */
  private Location[] calculateHallwaysOutOfRoom() {
    return this.level
        .filter(IsAndNeighborIs.hallwayNeighboringDoor(this.level, this.level.get(this.location)))
        .keySet()
        .toArray(new Location[0]);
  }
}
