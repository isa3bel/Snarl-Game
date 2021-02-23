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
        .addExit(0, 6)
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
        .addKey(new Location(6,3))
        .build();
  }

  private void setupRoomWithPlayersAndAdversaries() {
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
    Level singleRoomLevelWithCharacters = new LevelBuilder().addRoom(singleRoomBuilder).build();
    this.singleRoomWithCharacters = new GameManagerBuilder(singleRoomLevelWithCharacters)
        .addKey(new Location(5,2))
        .addEnemy(new Location(1,6))
        .addPlayer(new Location(1,7))
        .addPlayer(new Location(1,8))
        .build();
  }

  private void setupTwoRooms() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .addDoor(21, 4);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .addDoor(24, 4);
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
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .addDoor(21, 4)
        .addDoor(16, 9)
        .addWall(1, 1)
        .addWall(2, 1)
        .addWall(5, 4)
        .addWall(6, 4)
        .addWall(7, 4)
        .addWall(5, 5)
        .addWall(6, 5)
        .addWall(7, 5);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .addDoor(24, 4);
    RoomBuilder room3 = new RoomBuilder(28, 12, 4, 2)
        .addDoor(27, 13);
    HallwayBuilder hallway1 = new HallwayBuilder(room1, room2);
    HallwayBuilder hallway2 = new HallwayBuilder(room1, room3).addWaypoint(16, 13);
    Level twoRoomsLevel = new LevelBuilder()
        .addRoom(room1)
        .addRoom(room2)
        .addRoom(room3)
        .addHallway(hallway1)
        .addHallway(hallway2)
        .build();
    this.complicatedLevel = new GameManagerBuilder(twoRoomsLevel)
        .addKey(new Location(3,7))
        .build();
  }

  private void setupAnotherComplicatedLevelWithPlayer() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .addDoor(21, 4)
        .addDoor(16, 9);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .addDoor(24, 4)
        .addDoor(34, 2);
    RoomBuilder room3 = new RoomBuilder(28, 12, 4, 2)
        .addDoor(27, 13);
    RoomBuilder room4 = new RoomBuilder(35, 7, 4, 1)
        .addDoor(36, 6)
        .addDoor(36,8);
    RoomBuilder room5 = new RoomBuilder(22, 8, 6, 3)
        .addDoor(28, 9);
    HallwayBuilder hallway1to2 = new HallwayBuilder(room1, room2);
    HallwayBuilder hallway1to3 = new HallwayBuilder(room1, room3).addWaypoint(16, 13);
    HallwayBuilder hallway2to4 = new HallwayBuilder(room2, room4)
        .addWaypoint(36, 5)
        .addWaypoint(45, 5)
        .addWaypoint(45, 2)
        .addWaypoint(45, 2);
    HallwayBuilder hallway5to4 = new HallwayBuilder(room5, room4)
        .addWaypoint(36, 9);
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
    RoomBuilder room1 = new RoomBuilder(1, 1, 1, 2).addDoor(1, 3);
    RoomBuilder room2 = new RoomBuilder(3, 1, 2, 3).addDoor(5, 1);
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
        + "XXX                  X\n"
        + "X                    X\n"
        + "X     K              X\n"
        + "X    XXX             X\n"
        + "X    XXX             X\n"
        + "E                    X\n"
        + "X                    X\n"
        + "X                    X\n"
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
        + "XXX                  XXXXXXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXXX\n"
        + "X                    XXXX          X\n"
        + "X    XXX             D  D          X\n"
        + "X    XXX             XXXX          X\n"
        + "E                    XXXXXXXXXXXXXXX\n"
        + "X  K                 XXXXXXXXXXXXXXX\n"
        + "X                    XXXXXXXXXXXXXXX\n"
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
        + "XXX                  X\n"
        + "X    K               X\n"
        + "X                    X\n"
        + "X    XXX             X\n"
        + "X    XXX             X\n"
        + "EA                   X\n"
        + "X1                   X\n"
        + "X2                   X\n"
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
