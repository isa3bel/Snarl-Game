package model.ruleChecker;

import model.builders.HallwayBuilder;
import model.builders.LevelBuilder;
import model.builders.RoomBuilder;
import model.characters.Adversary;
import model.characters.Ghost;
import model.characters.Player;
import model.characters.Zombie;
import model.level.Level;
import model.level.Location;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PlayerMoveValidatorTest {

  private Level setupMultiRoomLevelWithAdversaries() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0),22, 10)
        .addDoor(new Location(4, 21))
        .addDoor(new Location(9, 16))
        .addWall(new Location(2, 2 ))
        .addWall(new Location(3, 2))
        .addWall(new Location(6, 5))
        .addWall(new Location(7, 5))
        .addWall(new Location(8, 5))
        .addWall(new Location(6,6))
        .addWall(new Location(7, 6))
        .addWall(new Location(8, 6));
    RoomBuilder room2 = new RoomBuilder(new Location(2, 24), 12, 5)
        .addDoor(new Location(4, 24));
    RoomBuilder room3 = new RoomBuilder(new Location(11, 27), 6, 4)
        .addDoor(new Location(13, 27));

    HallwayBuilder hallway1to2 = new HallwayBuilder(room1, room2, null);
    Location[] hallway1to3Waypoints = new Location[]{new Location(13, 16)};
    HallwayBuilder hallway1to3 = new HallwayBuilder(room1, room3, hallway1to3Waypoints);

    Level complicatedLevel = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .addRoom(room3)
        .addHallway(hallway1to2)
        .addHallway(hallway1to3)
        .addKey(new Location(6, 1))
        .addExit(new Location(6, 0))
        .build();

    Zombie zombie = new Zombie(new Location(7, 2), "zombie");
    Ghost ghost = new Ghost(new Location(7, 1), "ghost");
    Ghost ghostOutOfRange = new Ghost(new Location(8, 1), "ghost out of range");
    ArrayList<Adversary> adversaries = new ArrayList<>();
    adversaries.add(zombie);
    adversaries.add(ghost);
    adversaries.add(ghostOutOfRange);
    complicatedLevel.addAdversaries(adversaries);
    return complicatedLevel;
  }

  @Test
  public void testSameLocationIsValid() {
    Player player = new Player(new Location(5, 1), 0, "player");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    PlayerMoveValidator validator = new PlayerMoveValidator(player, new Location(5, 1));
    boolean isValid = validator.isValid(this.setupMultiRoomLevelWithAdversaries(), players);
    assertTrue(isValid);
  }

  @Test
  public void testTwoUnitsAwayIsValid() {
    Player player = new Player(new Location(5, 1), 0, "player");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    PlayerMoveValidator validator = new PlayerMoveValidator(player, new Location(3, 1));
    boolean isValid = validator.isValid(this.setupMultiRoomLevelWithAdversaries(), players);
    assertTrue(isValid);
  }

  @Test
  public void testKeyIsValid() {
    Player player = new Player(new Location(5, 1), 0, "player");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    PlayerMoveValidator validator = new PlayerMoveValidator(player, new Location(6, 1));
    boolean isValid = validator.isValid(this.setupMultiRoomLevelWithAdversaries(), players);
    assertTrue(isValid);
  }

  @Test
  public void testExitIsValid() {
    Player player = new Player(new Location(5, 1), 0, "player");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    PlayerMoveValidator validator = new PlayerMoveValidator(player, new Location(6, 0));
    boolean isValid = validator.isValid(this.setupMultiRoomLevelWithAdversaries(), players);
    assertTrue(isValid);
  }

  @Test
  public void testAdversaryIsValid() {
    Player player = new Player(new Location(5, 1), 0, "player");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    PlayerMoveValidator validator = new PlayerMoveValidator(player, new Location(7, 1));
    boolean isValid = validator.isValid(this.setupMultiRoomLevelWithAdversaries(), players);
    assertTrue(isValid);
  }

  @Test
  public void testWallIsNotValid() {
    Player player = new Player(new Location(5, 1), 0, "player");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    PlayerMoveValidator validator = new PlayerMoveValidator(player, new Location(5, 0));
    boolean isValid = validator.isValid(this.setupMultiRoomLevelWithAdversaries(), players);
    assertFalse(isValid);
  }

  @Test
  public void testPlayerIsNotValid() {
    Player player = new Player(new Location(5, 1), 0, "player");
    Player otherPlayer = new Player(new Location(4, 1), 0, "other");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    players.add(otherPlayer);
    PlayerMoveValidator validator = new PlayerMoveValidator(player, new Location(4, 1));
    boolean isValid = validator.isValid(this.setupMultiRoomLevelWithAdversaries(), players);
    assertFalse(isValid);
  }

  @Test
  public void testEuclidianDistance3IsNotValid() {
    Player player = new Player(new Location(5, 1), 0, "player");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    PlayerMoveValidator validator = new PlayerMoveValidator(player, new Location(4, 3));
    boolean isValid = validator.isValid(this.setupMultiRoomLevelWithAdversaries(), players);
    assertFalse(isValid);
  }
}
