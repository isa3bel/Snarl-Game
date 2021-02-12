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
}
