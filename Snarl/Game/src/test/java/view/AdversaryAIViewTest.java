package view;

import model.GameManager;
import model.builders.HallwayBuilder;
import model.builders.LevelBuilder;
import model.builders.RoomBuilder;
import model.characters.Adversary;
import model.characters.Ghost;
import model.characters.Player;
import model.characters.Zombie;
import model.level.Ejected;
import model.level.Level;
import model.level.Location;
import model.ruleChecker.GhostMoveValidator;
import model.ruleChecker.ZombieMoveValidator;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AdversaryAIViewTest {

  Zombie zombie;
  Ghost ghost;

  private GameManager setupMultiRoomLevelWithAdversaries() {
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

    this.zombie = new Zombie(null, "zombie");
    this.ghost = new Ghost(null, "ghost");
    Ghost ghostOutOfRange = new Ghost(null, "ghost out of range");
    ArrayList<Adversary> adversaries = new ArrayList<>();
    adversaries.add(zombie);
    adversaries.add(ghost);
    adversaries.add(ghostOutOfRange);
    complicatedLevel.addAdversaries(adversaries);
    zombie.moveTo(new Location(7, 2));
    ghost.moveTo(new Location(7, 1));
    ghostOutOfRange.moveTo(new Location(8, 1));

    Player player1 = new Player(new Location(5, 1), "ferd");
    Player player2 = new Player(new Location(7, 3), "dio");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    return new GameManager(0, new Level[]{complicatedLevel}, players);
  }

  private GameManager zombieNoValidMoves() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0),22, 10)
        .addDoor(new Location(4, 21))
        .addDoor(new Location(9, 16))
        .addWall(new Location(5, 20))
        .addWall(new Location(4, 19));
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
    this.ghost = new Ghost(null, "ghost");
    Ghost ghostOutOfRange = new Ghost(null, "ghost out of range");
    ArrayList<Adversary> adversaries = new ArrayList<>();
    adversaries.add(zombie);
    adversaries.add(ghost);
    adversaries.add(ghostOutOfRange);
    complicatedLevel.addAdversaries(adversaries);
    zombie.moveTo(new Location(4, 20));
    ghost.moveTo(new Location(3, 20));
    ghostOutOfRange.moveTo(new Location(8, 1));

    Player player1 = new Player(new Location(5, 1), "ferd");
    Player player2 = new Player(new Location(7, 3), "dio");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
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

    this.zombie = new Zombie(null, "zombie");
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

  private GameManager keyIsPickedUp() {
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
        .addKey(new Ejected(new Location(6, 1)))
        .addExit(new Location(6, 0))
        .build();

    this.zombie = new Zombie(null, "zombie");
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
  public void testZombieView() {
    GameManager gameManager = this.setupMultiRoomLevelWithAdversaries();
    AdversaryAIView view = new AdversaryAIView(this.zombie.getCurrentLocation(),
        location -> new ZombieMoveValidator(this.zombie, location));
    gameManager.buildView(view);
    assertEquals("{ \"type\": \"adversary-update\",\n" +
        "\"validMoves\": [[8, 2], [6, 2], [7, 3]],\n" +
        "\"nearestPlayerLocation\": [7, 3],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0] }", view.toString());
  }

  @Test
  public void testGhostView() {
    GameManager gameManager = this.setupMultiRoomLevelWithAdversaries();
    AdversaryAIView view = new AdversaryAIView(this.ghost.getCurrentLocation(),
        location -> new GhostMoveValidator(this.ghost, location));
    gameManager.buildView(view);
    assertEquals("{ \"type\": \"adversary-update\",\n" +
        "\"validMoves\": [[7, 0], [6, 1]],\n" +
        "\"nearestPlayerLocation\": [5, 1],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0] }", view.toString());
  }

  @Test
  public void testZombieCanOnlyStayInPlaceView() {
    GameManager gameManager = this.zombieNoValidMoves();
    AdversaryAIView view = new AdversaryAIView(this.zombie.getCurrentLocation(),
        location -> new ZombieMoveValidator(this.zombie, location));
    gameManager.buildView(view);
    assertEquals("{ \"type\": \"adversary-update\",\n" +
        "\"validMoves\": [[4, 20]],\n" +
        "\"nearestPlayerLocation\": [5, 1],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [4, 19] }", view.toString());
  }

  @Test
  public void testGhostCanOnlyStayInPlaceView() {
    GameManager gameManager = this.ghostNoValidMoves();
    AdversaryAIView view = new AdversaryAIView(this.ghost.getCurrentLocation(),
        location -> new GhostMoveValidator(this.ghost, location));
    gameManager.buildView(view);
    assertEquals("{ \"type\": \"adversary-update\",\n" +
        "\"validMoves\": [[7, 1]],\n" +
        "\"nearestPlayerLocation\": [5, 1],\n" +
        "\"keyLocation\": [6, 1],\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0] }", view.toString());
  }

  @Test
  public void testNoKey() {
    GameManager gameManager = this.keyIsPickedUp();
    AdversaryAIView view = new AdversaryAIView(this.ghost.getCurrentLocation(),
        location -> new GhostMoveValidator(this.ghost, location));
    gameManager.buildView(view);
    assertEquals("{ \"type\": \"adversary-update\",\n" +
        "\"validMoves\": [[7, 1]],\n" +
        "\"nearestPlayerLocation\": [5, 1],\n" +
        "\"keyLocation\": null,\n" +
        "\"exitLocation\": [6, 0],\n" +
        "\"closestWall\": [7, 0] }", view.toString());
  }
}
