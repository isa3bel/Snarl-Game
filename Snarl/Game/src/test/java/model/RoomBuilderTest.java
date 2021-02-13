package model;

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
      assertEquals("top left coordinate must have positive x" +
          " and y coordinates, given: 0, 1", e.getMessage());
    }
  }

  @Test
  public void testPositiveYTopLeft() {
    try {
      new RoomBuilder(1, -2, 1, 1);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("top left coordinate must have positive x" +
          " and y coordinates, given: 1, -2", e.getMessage());
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
  public void testExitTrue() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .exit(0, 6);

    assertTrue(room1.hasExit());
  }

  @Test
  public void testExitFalse() {
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .door(24, 4);

    assertFalse(room2.hasExit());
  }
}
