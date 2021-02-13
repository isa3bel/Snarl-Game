package model;

import java.lang.reflect.Array;
import org.junit.*;
import java.util.*;
import static org.junit.Assert.*;

public class HallwayBuilderTest {

  private GameManager singleRoom;

  @Test
  public void testNullRooms() {
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .door(24, 4);
    try {
      new HallwayBuilder(null, room2);
      fail();
    } catch(IllegalArgumentException e) {
      assertEquals("You must provide two rooms", e.getMessage());
    }
  }

  @Test
  public void testWaypointMethod() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .exit(0, 6)
        .door(21, 4)
        .door(16, 9)
        .wall(1, 1)
        .wall(2, 1)
        .wall(5, 4)
        .wall(6, 4)
        .wall(7, 4)
        .wall(5, 5)
        .wall(6, 5)
        .wall(7, 5);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .door(24, 4);
    HallwayBuilder hallway1 = new HallwayBuilder(room1, room2);

    assertTrue(hallway1.waypoints.isEmpty());

    hallway1.waypoint(1, 2);
    assertTrue(hallway1.waypoints.size() == 1);
    assertEquals(hallway1.waypoints.get(0).x, 1);
    assertEquals(hallway1.waypoints.get(0).y, 2);
  }


}
