package model.builders;

import model.level.Location;
import model.level.Space;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class HallwayBuilderTest {

  @Mock(name="to")
  private RoomBuilder to;
  @Mock(name="from")
  private RoomBuilder from;
  @Spy
  private ArrayList<Location> waypoints = new ArrayList<>();
  @InjectMocks
  private HallwayBuilder hallwayMocked;

  private ArrayList<ArrayList<Space>> spaces;
  private RoomBuilder roomX1Y1;
  private RoomBuilder roomX5Y5;

  @Before
  public void setup() {
    this.spaces = new ArrayList<>();
    this.roomX1Y1 = new RoomBuilder(new Location(0, 0), 3, 3)
        .addDoor(new Location(2,1));
    this.roomX5Y5 = new RoomBuilder(new Location(4, 4), 3, 3)
        .addDoor(new Location(5,4));
  }

  @Test
  public void testToRoomCannotBeNull() {
    try {
      new HallwayBuilder(null, this.roomX5Y5, null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("You must provide two rooms", e.getMessage());
    }
  }

  @Test
  public void testFromRoomCannotBeNull() {
    try {
      new HallwayBuilder(this.roomX1Y1, null, null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("You must provide two rooms", e.getMessage());
    }
  }

  @Test
  public void testWaypointIsOnSameAxisAsDoor() {
    Location[] waypoints = new Location[]{new Location(2, 3)};
    try {
      HallwayBuilder hallway = new HallwayBuilder(this.roomX5Y5, this.roomX1Y1, waypoints);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("no doors on the same axis as the given location", e.getMessage());
    }
  }

  @Test
  public void testWaypointIsOnSameAxisAsOtherWaypoint() {
    Location[] waypoints = new Location[]{new Location(2, 3), new Location(3, 4)};
    HallwayBuilder hallway = new HallwayBuilder(this.roomX5Y5, this.roomX1Y1, waypoints);

    try {
      hallway.build(spaces);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("locations must be on one of the same axes", e.getMessage());
    }
  }
}
