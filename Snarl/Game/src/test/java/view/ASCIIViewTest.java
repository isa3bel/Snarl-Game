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

  @Before
  public void setup() {
    this.view = new ASCIIView();
    this.out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));

    RoomBuilder singleRoomBuilder = new RoomBuilder(1,1,20,8)
        .exit(0, 6)
        .wall(1,1)
        .wall(2,1)
        .wall(5,4)
        .wall(6,4)
        .wall(7,4)
        .wall(5,5)
        .wall(6,5)
        .wall(7,5);
    Level singleRoomLevel = new LevelBuilder().room(singleRoomBuilder).build();
    this.singleRoom = new GameManager(singleRoomLevel);

    RoomBuilder room1 = new RoomBuilder(1,1,20,8)
        .exit(0, 6)
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

  @Test
  public void testSingleRoom() {
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
    twoRooms.draw(view);
    String expected = "XXXXXXXXXXXXXXXXXXXXXX\n" +
        "X                    X\n" +
        "X                    XXXXXXXXXXXXXXX\n" +
        "X                    XXXX          X\n" +
        "X                    D  D          X\n" +
        "X                    XXXX          X\n" +
        "E                    XXXXXXXXXXXXXXX\n" +
        "X                    X\n" +
        "X                    X\n" +
        "XXXXXXXXXXXXXXXXXXXXXX\n";
    assertEquals(expected, new String(out.toByteArray()));
  }
}
