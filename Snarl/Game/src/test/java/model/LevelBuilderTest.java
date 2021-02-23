package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LevelBuilderTest {

  RoomBuilder roomX1Y1;

  @Before
  public void setUp() {
    this.roomX1Y1 = new RoomBuilder(1, 1, 1, 1).addDoor(2, 1);

  }
  @Test
  public void testBuildingLevelWithoutExit() {
    LevelBuilder twoRoomsLevel = new LevelBuilder().addRoom(this.roomX1Y1);
    try {
      twoRoomsLevel.build();
    } catch (IllegalStateException e) {
      assertEquals("level must have exactly one level exit", e.getMessage());
    }
  }
}
