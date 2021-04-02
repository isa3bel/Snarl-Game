package view;

import model.*;
import model.builders.HallwayBuilder;
import model.builders.LevelBuilder;
import model.builders.RoomBuilder;
import model.characters.Adversary;
import model.characters.Player;
import model.characters.Zombie;
import model.level.Level;
import model.level.Location;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class ASCIIViewTest {

  private ASCIIView view;
  private ByteArrayOutputStream out;
  private GameManager singleRoom;
  private GameManager twoRooms;
  private GameManager complicated;
  private GameManager anotherComplicated;
  private GameManager singleRoomWithCharacters;
  private GameManager twoRoomsTopRight;

  @Before
  public void setup() {
    this.view = new ASCIIView();
    this.out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
  }

  private void setupSingleRoom() {
    RoomBuilder singleRoomBuilder = new RoomBuilder(new Location(0, 0),22,10)
        .addWall(new Location(2, 2 ))
        .addWall(new Location(3, 2))
        .addWall(new Location(6, 5))
        .addWall(new Location(7, 5))
        .addWall(new Location(8, 5))
        .addWall(new Location(6,6))
        .addWall(new Location(7, 6))
        .addWall(new Location(8, 6));
    Level singleRoomLevel = new LevelBuilder()
        .addRoom(singleRoomBuilder)
        .addKey(new Location(3, 6))
        .addExit(new Location(6, 0))
        .build();
    Level[] levels = { singleRoomLevel };
    this.singleRoom = new GameManager(0, levels, new ArrayList<>());
  }

  private void setupRoomWithPlayersAndAdversaries() {
    RoomBuilder singleRoomBuilder = new RoomBuilder(new Location(0, 0),22,10)
        .addWall(new Location(2, 2 ))
        .addWall(new Location(3, 2))
        .addWall(new Location(6, 5))
        .addWall(new Location(7, 5))
        .addWall(new Location(8, 5))
        .addWall(new Location(6,6))
        .addWall(new Location(7, 6))
        .addWall(new Location(8, 6));
    Level singleRoomLevelWithCharacters = new LevelBuilder()
        .addRoom(singleRoomBuilder)
        .addKey(new Location(2, 5))
        .addExit(new Location(6, 0))
        .build();
    Level[] levels = { singleRoomLevelWithCharacters };

    ArrayList<Adversary> adversaries = new ArrayList<>();
    Zombie zombie1 = new Zombie(null, "zombie1");
    adversaries.add(zombie1);
    singleRoomLevelWithCharacters.addAdversaries(adversaries);
    zombie1.moveTo(new Location(6, 1));

    ArrayList<Player> players = new ArrayList<>();
    players.add(new Player(new Location(7, 1), "1"));
    players.add(new Player(new Location(8, 1), "2"));
    this.singleRoomWithCharacters = new GameManager(0, levels, players);
  }

  private void setupTwoRooms() {
    RoomBuilder room1 = new RoomBuilder(new Location(0,0),22, 10)
        .addDoor(new Location(4, 21));
    RoomBuilder room2 = new RoomBuilder(new Location(2, 24), 12, 5)
        .addDoor(new Location(4, 24));
    Level twoRoomsLevel = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .addHallway(new HallwayBuilder(room1, room2, null))
        .addKey(new Location(4,4))
        .addExit(new Location(6, 0))
        .build();
    Level[] levels = { twoRoomsLevel };
    this.twoRooms = new GameManager(0, levels, new ArrayList<>());
  }

  private void setupComplicatedLevel() {
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
        .addKey(new Location(7, 3))
        .addExit(new Location(6, 0))
        .build();
    Level[] levels = { complicatedLevel };
    this.complicated = new GameManager(0, levels, new ArrayList<>());
  }

  private void setupAnotherComplicatedLevelWithPlayer() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0),22, 10)
        .addDoor(new Location(4, 21))
        .addDoor(new Location(9, 16));
    RoomBuilder room2 = new RoomBuilder(new Location(2, 24), 12, 5)
        .addDoor(new Location(4, 24))
        .addDoor(new Location(2, 34));
    RoomBuilder room3 = new RoomBuilder(new Location(11, 27), 6, 4)
        .addDoor(new Location(13, 27));
    RoomBuilder room4 = new RoomBuilder(new Location(6, 34), 6, 3)
        .addDoor(new Location(6, 36))
        .addDoor(new Location(8, 36));
    RoomBuilder room5 = new RoomBuilder(new Location(7, 21), 8, 5)
        .addDoor(new Location(9, 28));

    HallwayBuilder hallway1to2 = new HallwayBuilder(room1, room2, null);
    Location[] hallway1to3Waypoints = new Location[]{new Location(13, 16)};
    HallwayBuilder hallway1to3 = new HallwayBuilder(room1, room3, hallway1to3Waypoints);
    Location[] hallway2to4Waypoints = new Location[]{
        new Location(5, 36),
        new Location(5, 45),
        new Location(2, 45)
    };
    HallwayBuilder hallway2to4 = new HallwayBuilder(room2, room4, hallway2to4Waypoints);
    Location[] hallway5to4Waypoints = new Location[]{new Location(9, 36)};
    HallwayBuilder hallway5to4 = new HallwayBuilder(room5, room4, hallway5to4Waypoints);

    Level anotherComplicatedLevel = new LevelBuilder()
        .addExit(new Location(6, 0))
        .addKey(new Location(3,3))
        .addRoom(room1)
        .addRoom(room2)
        .addRoom(room3)
        .addRoom(room4)
        .addRoom(room5)
        .addHallway(hallway1to2)
        .addHallway(hallway1to3)
        .addHallway(hallway2to4)
        .addHallway(hallway5to4)
        .build();
    Level[] levels = { anotherComplicatedLevel };

    ArrayList<Adversary> adversaries = new ArrayList<>();
    Zombie zombie1 = new Zombie(null, "zombie1");
    Zombie zombie2 = new Zombie(null, "zombie2");
    adversaries.add(zombie1);
    adversaries.add(zombie2);
    anotherComplicatedLevel.addAdversaries(adversaries);
    zombie1.moveTo(new Location(13, 30));
    zombie2.moveTo(new Location(13, 31));

    ArrayList<Player> players = new ArrayList<>();
    players.add(new Player(new Location(1, 1), "1"));
    players.add(new Player(new Location(1, 2), "2"));
    players.add(new Player(new Location(1, 3), "3"));
    this.anotherComplicated = new GameManager(0, levels, players);
  }

  private void setupTwoRoomsTopRight() {
    RoomBuilder room1 = new RoomBuilder(new Location(0, 0), 3, 4)
        .addDoor(new Location(3, 1));
    RoomBuilder room2 = new RoomBuilder(new Location(0, 2), 4, 5)
        .addDoor(new Location(1, 5));
    Level level = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .addExit(new Location(0, 1))
        .build();
    Level[] levels = { level };

    ArrayList<Adversary> adversaries = new ArrayList<>();
    Zombie zombie1 = new Zombie(null, "zombie1");
    Zombie zombie2 = new Zombie(null, "zombie2");
    adversaries.add(zombie1);
    adversaries.add(zombie2);
    level.addAdversaries(adversaries);
    zombie1.moveTo(new Location(3, 4));
    zombie2.moveTo(new Location(3, 3));

    ArrayList<Player> players = new ArrayList<>();
    players.add(new Player(new Location(1, 1), "1"));
    players.add(new Player(new Location(2, 1), "2"));
    this.twoRoomsTopRight = new GameManager(0, levels, players);
  }

  @Test
  public void testSingleRoom() {
    this.setupSingleRoom();
    singleRoom.buildView(view);
    view.draw();
    String expected = "XXXXXXXXXXXXXXXXXXXXXX\n"
        + "X                    X\n"
        + "X X                  X\n"
        + "X X   K              X\n"
        + "X                    X\n"
        + "X                    X\n"
        + "E    XX              X\n"
        + "X    XX              X\n"
        + "X    XX              X\n"
        + "XXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }

  @Test
  public void testTwoRooms() {
    this.setupTwoRooms();
    twoRooms.buildView(view);
    view.draw();
    String expected = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXXX\n"
        + "X                    XXXX          X\n"
        + "X   K                D  D          X\n"
        + "X                    XXXX          X\n"
        + "E                    XXXXXXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }

  @Test
  public void testComplicatedLevel() {
    this.setupComplicatedLevel();
    complicated.buildView(view);
    view.draw();
    String expected = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXXX\n"
        + "X X                  XXXXXXXXXXXXXXX\n"
        + "X X                  XXXX          X\n"
        + "X                    D  D          X\n"
        + "X                    XXXX          X\n"
        + "E    XX              XXXXXXXXXXXXXXX\n"
        + "X  K XX              XXXXXXXXXXXXXXX\n"
        + "X    XX              XXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXXDXXXXXXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXX XXXXXXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXX XXXXXXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXX XXXXXXXXXXX    XXXX\n"
        + "XXXXXXXXXXXXXXXX           D    XXXX\n"
        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }

  @Test
  public void testAnotherComplicatedLevel() {
    this.setupAnotherComplicatedLevelWithPlayer();
    anotherComplicated.buildView(view);
    view.draw();
    String expected = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n"
        + "X123                 XXXXXXXXXXXXXXXXXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXD           X\n"
        + "X  K                 XXXX          XXXXXXXXXX X\n"
        + "X                    D  D          XXXXXXXXXX X\n"
        + "X                    XXXX          X          X\n"
        + "E                    XXXXXXXXXXXXXXXDXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXX    XXXXXXXX\n"
        + "X                    X      XXXXXXXXDXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXXDXXXXX      D        XXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXX XXXXX      XXXXXXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXX XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXX XXXXXXXXXXX    XXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXX           D  AAXXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }

  @Test
  public void testSingleRoomWithCharacters() {
    this.setupRoomWithPlayersAndAdversaries();
    singleRoomWithCharacters.buildView(view);
    view.draw();
    String expected = "XXXXXXXXXXXXXXXXXXXXXX\n"
        + "X                    X\n"
        + "X X  K               X\n"
        + "X X                  X\n"
        + "X                    X\n"
        + "X                    X\n"
        + "EA   XX              X\n"
        + "X1   XX              X\n"
        + "X2   XX              X\n"
        + "XXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }

  @Test
  public void testTwoRoomsTopRight() {
    this.setupTwoRoomsTopRight();
    twoRoomsTopRight.buildView(view);
    view.draw();
    String expected = "XEXXXX\n" +
        "X1X  D\n" +
        "X2X  X\n" +
        "XDXAAX\n" +
        "XXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }
}
