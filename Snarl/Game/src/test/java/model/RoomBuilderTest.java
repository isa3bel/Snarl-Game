package model;

import java.util.ArrayList;

import model.builders.RoomBuilder;
import model.level.Door;
import model.level.Space;
import model.level.Wall;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoomBuilderTest {

  @Test
  public void testPositiveXTopLeft() {
    try {
      new RoomBuilder(0, 1, 1, 1);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("top left coordinate must have positive row" +
          " and column, given: 0, 1", e.getMessage());
    }
  }

  @Test
  public void testPositiveYTopLeft() {
    try {
      new RoomBuilder(1, -2, 1, 1);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("top left coordinate must have positive row" +
          " and column, given: 1, -2", e.getMessage());
    }
  }

  @Test
  public void testPositiveWidth() {
    try {
      new RoomBuilder(1, 1, -1, 1);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("width and height must be positive, given: -1, 1", e.getMessage());
    }
  }

  @Test
  public void testPositiveHeight() {
    try {
      new RoomBuilder(1, 1, 1, 0);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("width and height must be positive, given: 1, 0", e.getMessage());
    }
  }

  @Test
  public void testBuildNoDoor() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8);

    try {
      room1.build(new ArrayList<>());
    } catch(IllegalStateException e) {
      assertEquals("room must have at least 1 door", e.getMessage());
    }
  }

  @Test
  public void testBuildDoorIsWall() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addDoor(0,2)
        .addWall(0,2);

    try {
      room1.build(new ArrayList<>());
    } catch(IllegalStateException e) {
      assertEquals("no door can also be a wall", e.getMessage());
    }
  }

  @Test
  public void testBadDoorPlacement() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8);
    try {
      room1.addDoor(2,-9);
    } catch(IllegalArgumentException e) {
      assertEquals("door must be on room boundary - row of 0 or 9 or column on 0 or 21, given: 2, -9", e.getMessage());
    }
  }

  @Test
  public void testSuccessfulBuild() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addDoor(0,2)
        .addWall(2,6);

    ArrayList<ArrayList<Space>> spaces = new ArrayList<>();
    room1.build(spaces);

    assertTrue(spaces.get(0).get(2) instanceof Door);
    assertTrue(spaces.get(3).get(7) instanceof Wall);
  }
}
