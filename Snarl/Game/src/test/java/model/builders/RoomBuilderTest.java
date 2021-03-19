package model.builders;

import java.util.ArrayList;

import model.level.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoomBuilderTest {

  @Test
  public void testPositiveWidth() {
    try {
      new RoomBuilder(new Location(1,1), 2, 4);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("width and height must be at least 3, given: 2, 4", e.getMessage());
    }
  }

  @Test
  public void testPositiveHeight() {
    try {
      new RoomBuilder(new Location(1,1), 1, 6);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("width and height must be at least 3, given: 1, 6", e.getMessage());
    }
  }

  @Test
  public void testBuildNoDoor() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0),20,8);

    try {
      room1.build(new ArrayList<>());
    } catch(IllegalStateException e) {
      assertEquals("room must have at least 1 door", e.getMessage());
    }
  }

  @Test
  public void testBuildDoorIsWall() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0),20,8)
        .addDoor(new Location(0,2))
        .addWall(new Location(0,2));

    try {
      room1.build(new ArrayList<>());
    } catch(IllegalStateException e) {
      assertEquals("no door can also be a wall", e.getMessage());
    }
  }

  @Test
  public void testBadDoorPlacement() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0),20,8);
    try {
      room1.addDoor(new Location(2,3));
    } catch(IllegalArgumentException e) {
      assertEquals("door must be on room boundary - row of 0 or 7 or column on 0 or 19, given: 2, 3", e.getMessage());
    }
  }

  @Test
  public void testSuccessfulBuild() {
    RoomBuilder room1 = new RoomBuilder(new Location(0,0),22,10)
        .addDoor(new Location(0,2))
        .addWall(new Location(2,6));

    ArrayList<ArrayList<Space>> spaces = new ArrayList<>();
    room1.build(spaces);

    assertTrue(spaces.get(0).get(2) instanceof Door);
    assertTrue(spaces.get(2).get(6) instanceof Wall);
    assertTrue(spaces.get(0).get(1) instanceof EdgeWall);
    assertEquals(10, spaces.size());
    spaces.forEach(row ->  assertEquals(22, row.size()));
  }


  @Test
  public void testSuccessfulBuildWithFilledInVoidSpaces() {
    RoomBuilder room1 = new RoomBuilder(new Location(2, 3),3, 3)
        .addDoor(new Location(2,4));

    ArrayList<ArrayList<Space>> spaces = new ArrayList<>();
    room1.build(spaces);

    assertTrue(spaces.get(0).get(0) instanceof VoidWall);
    assertTrue(spaces.get(3).get(4) instanceof Tile);
    assertTrue(spaces.get(2).get(3) instanceof EdgeWall);
    assertEquals(5, spaces.size());
    spaces.forEach(row ->  assertEquals(6, row.size()));
  }
}
