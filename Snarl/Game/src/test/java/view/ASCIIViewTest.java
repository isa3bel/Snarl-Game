package view;

import model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class ASCIIViewTest {

  private ASCIIView view;
  private ByteArrayOutputStream out;
  private GameManager singleRoom;
  private GameManager twoRooms;
  private GameManager complicatedLevel;
  private GameManager anotherComplicatedLevel;
  private GameManager singleRoomWithCharacters;
  private GameManager twoRoomsTopRight;

  @Before
  public void setup() {
    this.view = new ASCIIView();
    this.out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
  }

  private void setupSingleRoom() {
    RoomBuilder singleRoomBuilder = new RoomBuilder(1,1,20,8)
        .addExit(6, 0)
        .addWall(1,1)
        .addWall(2,1)
        .addWall(5,4)
        .addWall(6,4)
        .addWall(7,4)
        .addWall(5,5)
        .addWall(6,5)
        .addWall(7,5);
    Level singleRoomLevel = new LevelBuilder().addRoom(singleRoomBuilder).build();
    this.singleRoom = new GameManagerBuilder(singleRoomLevel)
        .addKey(new Location(3, 6))
        .build();
  }

  private void setupRoomWithPlayersAndAdversaries() {
    RoomBuilder singleRoomBuilder = new RoomBuilder(1,1,20,8)
        .addExit(6, 0)
        .addWall(1,1)
        .addWall(2,1)
        .addWall(5,4)
        .addWall(6,4)
        .addWall(7,4)
        .addWall(5,5)
        .addWall(6,5)
        .addWall(7,5);
    Level singleRoomLevelWithCharacters = new LevelBuilder().addRoom(singleRoomBuilder).build();
    this.singleRoomWithCharacters = new GameManagerBuilder(singleRoomLevelWithCharacters)
        .addKey(new Location(2, 5))
        .addEnemy(new Location(6, 1))
        .addPlayer(new Location(7, 1))
        .addPlayer(new Location(8, 1))
        .build();
  }

  private void setupTwoRooms() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(6, 0)
        .addDoor(4, 21);
    RoomBuilder room2 = new RoomBuilder(3, 25, 10, 3)
        .addDoor(4, 24);
    Level twoRoomsLevel = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .addHallway(new HallwayBuilder(room1, room2))
        .build();
    this.twoRooms = new GameManagerBuilder(twoRoomsLevel)
        .addKey(new Location(4,4))
        .build();
  }

  private void setupComplicatedLevel() {
    RoomBuilder room1 = new RoomBuilder(1,1,20, 8)
        .addExit(6, 0)
        .addDoor(4, 21)
        .addDoor(9, 16)
        .addWall(1, 1)
        .addWall(2, 1)
        .addWall(5, 4)
        .addWall(6, 4)
        .addWall(7, 4)
        .addWall(5, 5)
        .addWall(6, 5)
        .addWall(7, 5);
    RoomBuilder room2 = new RoomBuilder(3, 25, 10, 3)
        .addDoor(4, 24);
    RoomBuilder room3 = new RoomBuilder(12, 28, 4, 2)
        .addDoor(13, 27);
    HallwayBuilder hallway1 = new HallwayBuilder(room1, room2);
    HallwayBuilder hallway2 = new HallwayBuilder(room1, room3).addWaypoint(13, 16);
    Level twoRoomsLevel = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .addRoom(room3)
        .addHallway(hallway1)
        .addHallway(hallway2)
        .build();
    this.complicatedLevel = new GameManagerBuilder(twoRoomsLevel)
        .addKey(new Location(7, 3))
        .build();
  }

  private void setupAnotherComplicatedLevelWithPlayer() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(6, 0)
        .addDoor(4, 21)
        .addDoor(9, 16);
    RoomBuilder room2 = new RoomBuilder(3, 25, 10, 3)
        .addDoor(4, 24)
        .addDoor(2, 34);
    RoomBuilder room3 = new RoomBuilder(12, 28, 4, 2)
        .addDoor(13, 27);
    RoomBuilder room4 = new RoomBuilder(7, 35, 4, 1)
        .addDoor(6, 36)
        .addDoor(8, 36);
    RoomBuilder room5 = new RoomBuilder(8, 22, 6, 3)
        .addDoor(9, 28);
    HallwayBuilder hallway1to2 = new HallwayBuilder(room1, room2);
    HallwayBuilder hallway1to3 = new HallwayBuilder(room1, room3)
        .addWaypoint(13, 16);
    HallwayBuilder hallway2to4 = new HallwayBuilder(room2, room4)
        .addWaypoint(5, 36)
        .addWaypoint(5, 45)
        .addWaypoint(2, 45);
    HallwayBuilder hallway5to4 = new HallwayBuilder(room5, room4)
        .addWaypoint(9, 36);
    Level twoRoomsLevel = new LevelBuilder()
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
    this.anotherComplicatedLevel = new GameManagerBuilder(twoRoomsLevel)
        .addKey(new Location(3,3))
        .addPlayer()
        .addPlayer()
        .addPlayer()
        .addEnemy()
        .addEnemy()
        .build();
  }

  private void setupTwoRoomsTopRight() {
    RoomBuilder room1 = new RoomBuilder(1, 1, 1, 2)
        .addDoor(3, 1);
    RoomBuilder room2 = new RoomBuilder(1, 3, 2, 3)
        .addDoor(1, 5);
    Level level = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .build();
    this.twoRoomsTopRight = new GameManagerBuilder(level)
        .addPlayer()
        .addPlayer()
        .addEnemy()
        .addEnemy()
        .build();
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
    complicatedLevel.buildView(view);
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
    anotherComplicatedLevel.buildView(view);
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
        + "XXXXXXXXXXXXXXXX XXXXXXXXXXXAA  XXXXXXXXXXXXXXX\n"
        + "XXXXXXXXXXXXXXXX           D    XXXXXXXXXXXXXXX\n"
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
    String expected = "XXXXXX\n" +
        "X1XAAD\n" +
        "X2X  X\n" +
        "XDX  X\n" +
        "XXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }
}
