package model.builders;

import model.level.Location;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LevelBuilderTest {

  RoomBuilder roomX1Y1;

  @Before
  public void setUp() {
    this.roomX1Y1 = new RoomBuilder(new Location(0, 0), 3, 3)
        .addDoor(new Location(2, 1));
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

  @Test
  public void testCannotCreateMultipleKeys() {
    // TODO: test error for multiple keys
  }
}
