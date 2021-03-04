package testHarness;

import model.Level;
import model.Location;
import model.Space;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * A query about the neighbors to a location in a room.
 */
public class LevelQuery extends Question {

  private final Level level;
  private final Location queryLocation;

  LevelQuery(Level level, Location queryLocation) {
    this.level = level;
    this.queryLocation = queryLocation;
  }

  /**
   * Deserializes a LevelQuery from the given string.
   * @param levelQuery the query to deserialize
   * @return the LevelQuery
   */
  static Question deserialize(String levelQuery) {
    return Question.deserialize(levelQuery, new LevelQueryDeserializer());
  }

  /**
   * Calculates the answer to this LocationQuery.
   * @return the string representing the answer to this query
   */
  public String getAnswer() {
    Space space = this.level.get(this.queryLocation);
    return "{ \"traversable\": " + space.acceptVisitor(new IsTraversable()) + ", " +
        "\"object\": " + this.getObjectType() + ", " +
        "\"type\": " + space.acceptVisitor(new RoomType()) + ", " +
        "\"reachable\": [ " + this.calculateReachableRooms(space) + " ] }";
  }

  /**
   * Calculates the object type at this space.
   * @return the object type at this space
   */
  private String getObjectType() {
    ObjectType objectTypeCalculator = new ObjectType(this.queryLocation);
    this.level.interact(this.queryLocation, objectTypeCalculator);
    return objectTypeCalculator.getObjectType();
  }

  /**
   * Calculates the string of locations that are reachable from the queryLocation.
   * @param space the space at the location this query is being calculated for
   * @return
   */
  private String calculateReachableRooms(Space space) {
    Location[] reachableRoomOrigins = space.acceptVisitor(new ReachableQuery(this.level, this.queryLocation));
    return Arrays.stream(reachableRoomOrigins)
        .map(this::locationToString)
        .collect(Collectors.joining(", "));
  }
}
