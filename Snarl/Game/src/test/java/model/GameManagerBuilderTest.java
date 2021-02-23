package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class GameManagerBuilderTest {

  private Level singleRoomLevel;
  private GameManagerBuilder gameManagerBuilder;

  @Before
  public void setup() {
    RoomBuilder singleRoomBuilder = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .addWall(1,1)
        .addWall(2,1)
        .addWall(5,4)
        .addWall(6,4)
        .addWall(7,4)
        .addWall(5,5)
        .addWall(6,5)
        .addWall(7,5);
    this.singleRoomLevel = new LevelBuilder().addRoom(singleRoomBuilder).build();
    this.gameManagerBuilder = new GameManagerBuilder(this.singleRoomLevel);
  }

  @Test
  public void testConstructorNullLevel() {
    try {
      new GameManagerBuilder(null);
      fail();
    }
    catch (IllegalArgumentException e) {
      assertEquals("must pass in a level to create a Snarl game", e.getMessage());
    }
  }

  @Test
  public void testKeyAlreadyAdded() {

    try {
      this.gameManagerBuilder
          .addKey(new Location(4,4))
          .addKey(new Location(3,4));
      fail();
    } catch (IllegalStateException e) {
      assertEquals("this game already has a key", e.getMessage());
    }
  }

  @Test
  public void testTooManyPlayers() {
    try {
      this.gameManagerBuilder
          .addKey(new Location(4,4))
          .addPlayer(new Location(4,4))
          .addPlayer(new Location(3,4))
          .addPlayer(new Location(2,4))
          .addPlayer(new Location(1,4))
          .addPlayer(new Location(5,4))
          .build();
    } catch (IllegalArgumentException e) {
      assertEquals("cannot have more than 4 players in a Snarl game", e.getMessage());
    }
  }
}
