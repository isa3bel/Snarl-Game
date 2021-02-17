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
    Level singleRoomLevel = new LevelBuilder().room(singleRoomBuilder).build();
    this.singleRoom = new GameManager(singleRoomLevel);
  }

  private void setupTwoRooms() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .door(21, 4);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .door(24, 4);
    Level twoRoomsLevel = new LevelBuilder()
        .room(room1)
        .room(room2)
        .hallway(new HallwayBuilder(room1, room2))
        .build();
    this.twoRooms = new GameManager(twoRoomsLevel);
  }

  private void setupComplicatedLevel() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .door(21, 4)
        .door(16, 9)
        .addWall(1, 1)
        .addWall(2, 1)
        .addWall(5, 4)
        .addWall(6, 4)
        .addWall(7, 4)
        .addWall(5, 5)
        .addWall(6, 5)
        .addWall(7, 5);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .door(24, 4);
    RoomBuilder room3 = new RoomBuilder(28, 12, 4, 2)
        .door(27, 13);
    HallwayBuilder hallway1 = new HallwayBuilder(room1, room2);
    HallwayBuilder hallway2 = new HallwayBuilder(room1, room3).waypoint(16, 13);
    Level twoRoomsLevel = new LevelBuilder()
        .room(room1)
        .room(room2)
        .room(room3)
        .hallway(hallway1)
        .hallway(hallway2)
        .build();
    this.complicatedLevel = new GameManager(twoRoomsLevel);
  }

  private void setupAnotherComplicatedLevel() {
    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .addExit(0, 6)
        .door(21, 4)
        .door(16, 9)
        .addWall(1, 1)
        .addWall(2, 1)
        .addWall(5, 4)
        .addWall(6, 4)
        .addWall(7, 4)
        .addWall(5, 5)
        .addWall(6, 5)
        .addWall(7, 5);
    RoomBuilder room2 = new RoomBuilder(25, 3, 10, 3)
        .door(24, 4)
        .door(34, 2);
    RoomBuilder room3 = new RoomBuilder(28, 12, 4, 2)
        .door(27, 13);
    RoomBuilder room4 = new RoomBuilder(35, 7, 4, 1)
        .door(36, 6)
        .door(36,8);
    RoomBuilder room5 = new RoomBuilder(22, 8, 6, 3)
        .door(28, 9);
    HallwayBuilder hallway1to2 = new HallwayBuilder(room1, room2);
    HallwayBuilder hallway1to3 = new HallwayBuilder(room1, room3).waypoint(16, 13);
    HallwayBuilder hallway2to4 = new HallwayBuilder(room2, room4)
        .waypoint(36, 5)
        .waypoint(45, 5)
        .waypoint(45, 2)
        .waypoint(45, 2);
    HallwayBuilder hallway5to4 = new HallwayBuilder(room5, room4)
        .waypoint(36, 9);
    Level twoRoomsLevel = new LevelBuilder()
        .room(room1)
        .room(room2)
        .room(room3)
        .room(room4)
        .room(room5)
        .hallway(hallway1to2)
        .hallway(hallway1to3)
        .hallway(hallway2to4)
        .hallway(hallway5to4)
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
