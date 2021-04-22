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

public class ZombieMoveValidatorTest {

  Zombie zombie;

  private GameManager zombieHasValidMoves() {
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

    this.zombie = new Zombie(null, "zombie");
    Ghost ghost = new Ghost(null, "ghost");
    ArrayList<Adversary> adversaries = new ArrayList<>();
    adversaries.add(zombie);
    adversaries.add(ghost);
    complicatedLevel.addAdversaries(adversaries);
    zombie.moveTo(new Location(4, 20));
    ghost.moveTo(new Location(6, 1));

    Player player1 = new Player(new Location(4, 19), "ferd");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    return new GameManager(0, new Level[]{complicatedLevel}, players);
  }

  private GameManager zombieNoValidMoves() {
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

    this.zombie = new Zombie(null, "zombie");
    Ghost ghost = new Ghost(null, "ghost");
    Ghost blockingGhost1 = new Ghost(null, "blocking ghost 1");
    Ghost blockingGhost2 = new Ghost(null, "blocking ghost 2");
    ArrayList<Adversary> adversaries = new ArrayList<>();
    adversaries.add(zombie);
    adversaries.add(ghost);
    adversaries.add(blockingGhost1);
    adversaries.add(blockingGhost2);
    complicatedLevel.addAdversaries(adversaries);
    zombie.moveTo(new Location(7, 1));
    ghost.moveTo(new Location(6, 1));
    blockingGhost1.moveTo(new Location(8, 1));
    blockingGhost2.moveTo(new Location(7, 2));

    Player player1 = new Player(new Location(5, 1), "ferd");
    Player player2 = new Player(new Location(7, 3), "dio");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    return new GameManager(0, new Level[]{complicatedLevel}, players);
  }


  @Test
  public void testCurrentLocationIsValidWhenNoOtherMovesAre() {
    GameManager gameManager = this.zombieNoValidMoves();
    MoveValidator moveValidator = new ZombieMoveValidator(this.zombie, new Location(7, 1));
    assertTrue(gameManager.isMoveValid(this.zombie.getCurrentLocation(), moveValidator));
  }

  @Test
  public void testAdversaryIsNotValid() {
    GameManager gameManager = this.zombieNoValidMoves();
    MoveValidator moveValidator = new ZombieMoveValidator(this.zombie, new Location(6, 1));
    assertFalse(gameManager.isMoveValid(this.zombie.getCurrentLocation(), moveValidator));
  }

  @Test
  public void test2AwayIsNotValid() {
    GameManager gameManager = this.zombieNoValidMoves();
    MoveValidator moveValidator = new ZombieMoveValidator(this.zombie, new Location(5, 1));
    assertFalse(gameManager.isMoveValid(this.zombie.getCurrentLocation(), moveValidator));
  }

  @Test
  public void testWallIsInvalid() {
    GameManager gameManager = this.zombieNoValidMoves();
    MoveValidator moveValidator = new ZombieMoveValidator(this.zombie, new Location(7, 0));
    assertFalse(gameManager.isMoveValid(this.zombie.getCurrentLocation(), moveValidator));
  }

  @Test
  public void testDoorIsInvalid() {
    GameManager gameManager = this.zombieHasValidMoves();
    MoveValidator moveValidator = new ZombieMoveValidator(this.zombie, new Location(4, 21));
    assertFalse(gameManager.isMoveValid(this.zombie.getCurrentLocation(), moveValidator));
  }

  @Test
  public void testPlayerIsValid() {
    GameManager gameManager = this.zombieHasValidMoves();
    MoveValidator moveValidator = new ZombieMoveValidator(this.zombie, new Location(4, 19));
    assertTrue(gameManager.isMoveValid(this.zombie.getCurrentLocation(), moveValidator));
  }

  @Test
  public void currentLocationIsInvalidIfOtherMoves() {
    GameManager gameManager = this.zombieHasValidMoves();
    MoveValidator moveValidator = new ZombieMoveValidator(this.zombie, new Location(4, 20));
    assertFalse(gameManager.isMoveValid(this.zombie.getCurrentLocation(), moveValidator));
  }
}
