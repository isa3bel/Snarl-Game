package model.ruleChecker;

import model.GameManager;
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

public class GhostMoveValidatorTest {

  Ghost ghost;

  private GameManager ghostHasValidMoves() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0),22, 10)
        .addDoor(new Location(4, 21))
        .addDoor(new Location(9, 16))
        .addWall(new Location(3, 20));
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

    Zombie zombie = new Zombie(null, "zombie");
    this.ghost = new Ghost(null, "ghost");
    ArrayList<Adversary> adversaries = new ArrayList<>();
    adversaries.add(zombie);
    adversaries.add(ghost);
    complicatedLevel.addAdversaries(adversaries);
    zombie.moveTo(new Location(6, 1));
    ghost.moveTo(new Location(4, 20));

    Player player1 = new Player(new Location(4, 19), "ferd");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    return new GameManager(0, new Level[]{complicatedLevel}, players);
  }

  private GameManager ghostNoValidMoves() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0),22, 10)
        .addDoor(new Location(4, 21))
        .addDoor(new Location(9, 16));
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

    Zombie zombie = new Zombie(null, "zombie");
    this.ghost = new Ghost(null, "ghost");
    Ghost blockingGhost1 = new Ghost(null, "blocking ghost 1");
    Ghost blockingGhost2 = new Ghost(null, "blocking ghost 2");
    Ghost blockingGhost3 = new Ghost(null, "blocking ghost 3");
    ArrayList<Adversary> adversaries = new ArrayList<>();
    adversaries.add(zombie);
    adversaries.add(ghost);
    adversaries.add(blockingGhost1);
    adversaries.add(blockingGhost2);
    adversaries.add(blockingGhost3);
    complicatedLevel.addAdversaries(adversaries);
    zombie.moveTo(new Location(6, 1));
    ghost.moveTo(new Location(7, 1));
    blockingGhost1.moveTo(new Location(8, 1));
    blockingGhost2.moveTo(new Location(7, 0));
    blockingGhost3.moveTo(new Location(7, 2));

    Player player1 = new Player(new Location(5, 1), "ferd");
    Player player2 = new Player(new Location(7, 3), "dio");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    return new GameManager(0, new Level[]{complicatedLevel}, players);
  }

  @Test
  public void testCurrentLocationIsValidWhenNoOtherMovesAre() {
    GameManager gameManager = this.ghostNoValidMoves();
    MoveValidator moveValidator = new GhostMoveValidator(this.ghost, new Location(7, 1));
    assertTrue(gameManager.isMoveValid(moveValidator));
  }

  @Test
  public void testAdversaryIsNotValid() {
    GameManager gameManager = this.ghostNoValidMoves();
    MoveValidator moveValidator = new GhostMoveValidator(this.ghost, new Location(6, 1));
    assertFalse(gameManager.isMoveValid(moveValidator));
  }

  @Test
  public void test2AwayIsNotValid() {
    GameManager gameManager = this.ghostNoValidMoves();
    MoveValidator moveValidator = new GhostMoveValidator(this.ghost, new Location(5, 1));
    assertFalse(gameManager.isMoveValid(moveValidator));
  }

  @Test
  public void testWallIsValid() {
    GameManager gameManager = this.ghostHasValidMoves();
    MoveValidator moveValidator = new GhostMoveValidator(this.ghost, new Location(3, 20));
    assertTrue(gameManager.isMoveValid(moveValidator));
  }

  @Test
  public void testDoorIsValid() {
    GameManager gameManager = this.ghostHasValidMoves();
    MoveValidator moveValidator = new GhostMoveValidator(this.ghost, new Location(4, 21));
    assertTrue(gameManager.isMoveValid(moveValidator));
  }

  @Test
  public void testPlayerIsValid() {
    GameManager gameManager = this.ghostHasValidMoves();
    MoveValidator moveValidator = new GhostMoveValidator(this.ghost, new Location(4, 19));
    assertTrue(gameManager.isMoveValid(moveValidator));
  }

  @Test
  public void currentLocationIsInvalidIfOtherMoves() {
    GameManager gameManager = this.ghostHasValidMoves();
    MoveValidator moveValidator = new GhostMoveValidator(this.ghost, new Location(4, 20));
    assertFalse(gameManager.isMoveValid(moveValidator));
  }
}
