package model.builders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import model.level.Level;
import model.level.Location;
import org.junit.Before;
import org.junit.Test;

public class GameManagerBuilderTest {

  private Level singleRoomLevel;
  private GameManagerBuilder gameManagerBuilder;

  @Before
  public void setup() {
    RoomBuilder singleRoomBuilder = new RoomBuilder(new Location(1,1),20,8)
        .addWall(new Location(1,1))
        .addWall(new Location(2,1))
        .addWall(new Location(5,4))
        .addWall(new Location(6,4))
        .addWall(new Location(7,4))
        .addWall(new Location(5,5))
        .addWall(new Location(6,5))
        .addWall(new Location(7,5));
    this.singleRoomLevel = new LevelBuilder()
        .addRoom(singleRoomBuilder)
        .addExit(new Location(0, 6))
        .build();
    Level[] levels = { this.singleRoomLevel };
    this.gameManagerBuilder = new GameManagerBuilder(0, levels);
  }

  @Test
  public void testConstructorNullLevels() {
    try {
      new GameManagerBuilder(0, null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("must pass in an array of levels to create a Snarl game", e.getMessage());
    }
  }

  @Test
  public void testCurrentLevelGreaterThanLevelsSize() {
    Level[] levels = { this.singleRoomLevel };
    try {
      new GameManagerBuilder(1, levels);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("current level must point to a level in levels," +
          " expected a number in range [0, 1), got: 1", e.getMessage());
    }
  }

  @Test
  public void testTooManyPlayers() {
    try {
      this.gameManagerBuilder
          .addPlayer("a")
          .addPlayer("b")
          .addPlayer("c")
          .addPlayer("d")
          .addPlayer("e");
    } catch (IllegalStateException e) {
      assertEquals("cannot have more than 4 players in a Snarl game", e.getMessage());
    }
  }
}
