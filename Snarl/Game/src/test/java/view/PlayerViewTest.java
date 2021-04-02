package view;

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

import static org.junit.Assert.assertEquals;

public class PlayerViewTest {

  private Level setupSimpleLevel() {
    RoomBuilder singleRoomBuilder = new RoomBuilder(new Location(0, 0),22,10)
        .addWall(new Location(2, 2 ))
        .addWall(new Location(3, 2))
        .addWall(new Location(6, 5))
        .addWall(new Location(7, 5))
        .addWall(new Location(8, 5))
        .addWall(new Location(6,6))
        .addWall(new Location(7, 6))
        .addWall(new Location(8, 6));
    return new LevelBuilder()
        .addRoom(singleRoomBuilder)
        .addKey(new Location(3, 6))
        .addExit(new Location(6, 0))
        .build();
  }

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

    Zombie zombie = new Zombie(null, "zombie");
    Ghost ghost = new Ghost(new Location(7, 1), "ghost");
    Ghost ghostOutOfRange = new Ghost(new Location(8, 1), "ghost out of range");
    ArrayList<Adversary> adversaries = new ArrayList<>();
    adversaries.add(zombie);
    adversaries.add(ghost);
    adversaries.add(ghostOutOfRange);
    complicatedLevel.addAdversaries(adversaries);
    zombie.moveTo(new Location(7, 2));
    ghost.moveTo(new Location(7, 1));
    ghostOutOfRange.moveTo(new Location(8, 1));
    return complicatedLevel;
  }

  @Test
  public void testSimplePlayer() {
    Player player = new Player(new Location(3, 3), "ferd");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    Level level = this.setupSimpleLevel();
    GameManager gameManager = new GameManager(0, new Level[]{level}, players);
    View view = new PlayerView(player);
    gameManager.buildView(view);
    assertEquals("     \n" +
            " X   \n" +
            " Xf  \n" +
            "     \n" +
            "     \n",
        view.toString());
  }

  @Test
  public void testSimplePlayerWithKey() {
    Player player = new Player(new Location(3, 4), "ferd");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    Level level = this.setupSimpleLevel();
    GameManager gameManager = new GameManager(0, new Level[]{level}, players);
    View view = new PlayerView(player);
    gameManager.buildView(view);
    assertEquals("     \n" +
            "X    \n" +
            "X f K\n" +
            "     \n" +
            "     \n",
        view.toString());
  }

  @Test
  public void testSimplePlayerWithExit() {
    Player player = new Player(new Location(5, 1), "ferd");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player);
    Level level = this.setupSimpleLevel();
    GameManager gameManager = new GameManager(0, new Level[]{level}, players);
    View view = new PlayerView(player);
    gameManager.buildView(view);
    assertEquals("XX X \n" +
            "XX   \n" +
            "XXf  \n" +
            "XE   \n" +
            "XX   \n",
        view.toString());
  }

  @Test
  public void testNearOtherPlayersAndAdversaries() {
    Player player1 = new Player(new Location(5, 1), "ferd");
    Player player2 = new Player(new Location(7, 3), "dio");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    Level level = this.setupMultiRoomLevelWithAdversaries();
    GameManager gameManager = new GameManager(0, new Level[]{level}, players);
    View view = new PlayerView(player1);
    gameManager.buildView(view);
    assertEquals("XX X \n" +
            "XX   \n" +
            "XXf  \n" +
            "XEK  \n" +
            "XXGZd\n",
        view.toString());
  }

  @Test
  public void testInHallway() {
    Player player1 = new Player(new Location(5, 1), "ferd");
    Player player2 = new Player(new Location(4, 23), "dio");
    ArrayList<Player> players = new ArrayList<>();
    players.add(player1);
    players.add(player2);
    Level level = this.setupMultiRoomLevelWithAdversaries();
    GameManager gameManager = new GameManager(0, new Level[]{level}, players);
    View view = new PlayerView(player2);
    gameManager.buildView(view);
    assertEquals("XXXXX\n" +
            "XXXX \n" +
            "D dD \n" +
            "XXXX \n" +
            "XXXXX\n",
        view.toString());
  }

}
