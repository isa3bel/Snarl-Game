package model;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import model.observer.Observer;
import model.observer.LocalObserver;
import model.observer.Publisher;
import model.builders.GameManagerBuilder;
import model.builders.LevelBuilder;
import model.builders.RoomBuilder;
import model.level.Level;
import model.level.Location;
import org.junit.Before;
import org.junit.*;
import view.ObserverView;

public class PublisherTest {

  private Publisher publisher;
  private ByteArrayOutputStream out;
  private GameManager gm;

  @Before
  public void setup() {
    RoomBuilder singleRoomBuilder = new RoomBuilder(new Location(0, 0),22,10);
    Level singleRoomLevel = new LevelBuilder()
        .addRoom(singleRoomBuilder)
        .addKey(new Location(3, 6))
        .addExit(new Location(6, 0))
        .build();
    Level[] levels = { singleRoomLevel };
    this.gm = new GameManagerBuilder(0, levels).build();
    publisher = new Publisher();
    this.out = new ByteArrayOutputStream();
    System.setOut(new PrintStream(out));
  }


  @Test
  public void testObserverUpdatedWhenAddedToPublisher() {
    Observer o = new LocalObserver();
    ObserverView view = new ObserverView();
    publisher.addObserver(o);
    this.gm.buildView(view);

    publisher.update(this.gm);
    assertEquals(view.toString(), this.out.toString());
  }


}
