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
    this.singleRoom = new GameManager(singleRoomLevel);
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
    this.twoRooms = new GameManager(twoRoomsLevel);
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
    this.complicatedLevel = new GameManager(twoRoomsLevel);
  }

  private void setupAnotherComplicatedLevel() {
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
    this.anotherComplicatedLevel = new GameManager(twoRoomsLevel);
  }


  @Test
  public void testSingleRoom() {
    this.setupSingleRoom();
    singleRoom.draw(view);
    String expected = "XXXXXXXXXXXXXXXXXXXXXX\n" +
        "XXX                  X\n" +
        "X                    X\n" +
        "X                    X\n" +
        "X    XXX             X\n" +
        "X    XXX             X\n" +
        "E                    X\n" +
        "X                    X\n" +
        "X                    X\n" +
        "XXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }

  @Test
  public void testTwoRooms() {
    this.setupTwoRooms();
    twoRooms.draw(view);
    String expected = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXXX\n" +
        "X                    XXXX          X\n" +
        "X                    D  D          X\n" +
        "X                    XXXX          X\n" +
        "E                    XXXXXXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }

  @Test
  public void testComplicatedLevel() {
    this.setupComplicatedLevel();
    complicatedLevel.draw(view);
    String expected = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" +
        "XXX                  XXXXXXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXXX\n" +
        "X                    XXXX          X\n" +
        "X    XXX             D  D          X\n" +
        "X    XXX             XXXX          X\n" +
        "E                    XXXXXXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXXDXXXXXXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXX XXXXXXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXX XXXXXXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXX XXXXXXXXXXX    XXXX\n" +
        "XXXXXXXXXXXXXXXX           D    XXXX\n" +
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }

  @Test
  public void testAnotherComplicatedLevel() {
    this.setupAnotherComplicatedLevel();
    anotherComplicatedLevel.draw(view);
    String expected = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" +
        "XXX                  XXXXXXXXXXXXXXXXXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXD           X\n" +
        "X                    XXXX          XXXXXXXXXX X\n" +
        "X    XXX             D  D          XXXXXXXXXX X\n" +
        "X    XXX             XXXX          X          X\n" +
        "E                    XXXXXXXXXXXXXXXDXXXXXXXXXX\n" +
        "X                    XXXXXXXXXXXXXX    XXXXXXXX\n" +
        "X                    X      XXXXXXXXDXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXXDXXXXX      D        XXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXX XXXXX      XXXXXXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXX XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXX XXXXXXXXXXX    XXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXX           D    XXXXXXXXXXXXXXX\n" +
        "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }
}
