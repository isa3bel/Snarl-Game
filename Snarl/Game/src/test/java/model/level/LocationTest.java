package model.level;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class LocationTest {

  private Location x0y0;
  private Location x13y5;
  private Location x4y12;
  private Location x1y4;
  private Location x4y4;

  @Before
  public void setup() {
    this.x0y0 = new Location(0, 0);
    this.x13y5 = new Location(13, 5);
    this.x4y12 = new Location(4, 12);
    this.x1y4 = new Location(1, 4);
    this.x4y4 = new Location(4, 4);
  }

  @Test
  public void testNegativeX() {
    try {
      new Location(-1, 0);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("location coordinates must be non-negative, given: -1, 0", e.getMessage());
    }
  }

  @Test
  public void testNegativeY() {
    try {
      new Location(0, -1);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("location coordinates must be non-negative, given: 0, -1", e.getMessage());
    }
  }

  @Test
  public void testTo() {
    ArrayList<Location> justX0Y0 = new ArrayList<>();
    justX0Y0.add(this.x0y0);
    assertEquals(justX0Y0, this.x0y0.to(this.x0y0));

    ArrayList<Location> alongX = new ArrayList<>();
    int[] xs = {4, 3, 2, 1};
    for (int x : xs) {
      alongX.add(new Location(x, 4));
    }
    assertEquals(alongX, this.x4y4.to(this.x1y4));

    alongX.sort(Comparator.comparingInt(location -> location.getRow()));
    assertEquals(alongX, this.x1y4.to(this.x4y4));

    ArrayList<Location> alongY = new ArrayList<>();
    int[] ys = {4, 5, 6, 7, 8, 9, 10, 11, 12};
    for (int y : ys) {
      alongY.add(new Location(4, y));
    }
    assertEquals(alongY, this.x4y4.to(this.x4y12));
  }

  @Test
  public void testToError() {
    try {
      this.x0y0.to(this.x1y4);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("locations must be on one of the same axes", e.getMessage());
    }
  }

  @Test
  public void testEuclidianDistance() {
    assertEquals(5, this.x0y0.euclidianDistance(this.x1y4));
    assertEquals(5, this.x1y4.euclidianDistance(this.x0y0));
    assertEquals(0, this.x1y4.euclidianDistance(this.x1y4));
    assertEquals(16, this.x13y5.euclidianDistance(this.x4y12));
    assertEquals(8, this.x4y4.euclidianDistance(this.x4y12));
  }

  @Test
  public void testToString() {
    assertEquals("(0, 0)", this.x0y0.toString());
    assertEquals("(13, 5)", this.x13y5.toString());
    assertEquals("(4, 12)", this.x4y12.toString());
    assertEquals("(1, 4)", this.x1y4.toString());
  }

  @Test
  public void testEquals() {
    assertTrue(this.x0y0.equals(this.x0y0));
    assertTrue(this.x0y0.equals(new Location(0, 0)));
    assertFalse(this.x13y5.equals(this.x1y4));
  }

  @Test
  public void testSameAxis() {
    Predicate<Location> sameAsX4Y4 = new Location.SameAxis(this.x4y4);
    assertTrue(sameAsX4Y4.test(this.x1y4));
    assertTrue(sameAsX4Y4.test(this.x4y12));
    assertTrue(sameAsX4Y4.test(this.x4y4));
    assertFalse(sameAsX4Y4.test(this.x0y0));
    assertFalse(sameAsX4Y4.test(this.x13y5));
  }
}
