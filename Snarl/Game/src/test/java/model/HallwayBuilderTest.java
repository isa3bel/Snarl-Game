package model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    this.roomX1Y1 = new RoomBuilder(1, 1, 1, 1).addDoor(2, 1);
    this.roomX5Y5 = new RoomBuilder(5, 5, 1, 1).addDoor(5, 4);
    this.hallwayMocked = new HallwayBuilder(this.roomX1Y1, this.roomX5Y5);
  }

  @Test
  public void testToIsNotNull() {
    try {
      new HallwayBuilder(null, this.roomX5Y5);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("You must provide two rooms", e.getMessage());
    }
  }

  @Test
  public void testFromIsNotNull() {
    try {
      new HallwayBuilder(this.roomX1Y1, null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("You must provide two rooms", e.getMessage());
    }
  }

  @Test
  public void testWaypointIsOnSameAxisAsDoor() {
    HallwayBuilder hallway = new HallwayBuilder(this.roomX5Y5, this.roomX1Y1);
    hallway.addWaypoint(2, 3);

    try {
      hallway.build(spaces);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("no doors on the same axis as the given location", e.getMessage());
    }
  }

  @Test
  public void testWaypointIsOnSameAxisAsOtherWaypoint() {
    HallwayBuilder hallway = new HallwayBuilder(this.roomX5Y5, this.roomX1Y1);
    hallway.addWaypoint(2, 3);
    hallway.addWaypoint(3, 4);

    try {
      hallway.build(spaces);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("locations must be on one of the same axes", e.getMessage());
    }
  }

  @Test
  public void testWaypoints() {
    // currently failing because the injection isn't working - need to figure out these mocks
    hallwayMocked.addWaypoint(5, 1);
    Mockito.verify(waypoints).add(new Location(3, 1));
  }
}
