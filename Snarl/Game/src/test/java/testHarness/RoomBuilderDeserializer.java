package testHarness;

import com.google.gson.*;
import model.Location;
import model.RoomBuilder;

import java.lang.reflect.Type;

/**
 * Deserializes a RoomBuilder from the room JsonObject.
 */
public class RoomBuilderDeserializer implements JsonDeserializer<RoomBuilder> {

  @Override
  public RoomBuilder deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = getObject(jsonElement);
    Location origin = context.deserialize(jsonObject.get("origin"), Location.class);

    RoomBuilder roomBuilder = initializeRoomBuilder(jsonObject.get("bounds"), origin);
    calculateLayout(jsonObject.get("layout"), roomBuilder, origin);
    return roomBuilder;
  }

  /**
   * Initializes a RoomBuilder representing the size and origin of the room in the given object.
   * @param jsonElement the bounds object of this room
   * @param origin the origin of this room
   * @return the room builder initialized to the correct size
   * @throws JsonParseException if bounds is not an object or if rows and cols are not ints
   */
  private static RoomBuilder initializeRoomBuilder(JsonElement jsonElement, Location origin)
      throws JsonParseException {
    JsonObject bounds = getObject(jsonElement);
    int rows;
    int cols;

    try {
      rows = bounds.get("rows").getAsInt();
      cols = bounds.get("columns").getAsInt();
    }
    catch (ClassCastException exception) {
      String notRows = bounds.get("rows").toString();
      String notCols = bounds.get("columns").toString();
      throw new JsonParseException(String.format("expected rows and cols of bounds to be integers, got: %s, %s",
          notRows, notCols));
    }

    // DECISION: our room builder automatically creates a buffer around the room, but we
    //  expect the origin to actually be the top left corner of the walkable tiles in a room.
    //  we add 1 to the coordinates here because this origin is referencing the top left
    //  of the layout array (which also includes the room borders), not top left of the room
    return new RoomBuilder(origin.xCoordinate + 1, origin.yCoordinate + 1, cols, rows);
  }

  /**
   * Adds the layout of the room from the given JsonElement to the RoomBuilder
   * @param jsonElement the tile layout
   * @param roomBuilder the room builder to set the layout on
   * @param origin the origin location of the given room
   * @throws JsonParseException if the layout is not a 2D JsonArray
   */
  private static void calculateLayout(JsonElement jsonElement, RoomBuilder roomBuilder, Location origin)
      throws JsonParseException {
    JsonArray layout = getArray(jsonElement);

    for (int rowIdx = 0; rowIdx < layout.size(); rowIdx++) {
      JsonArray row = getArray(layout.get(rowIdx));
      for (int colIdx = 0; colIdx < row.size(); colIdx++) {
        int type = row.get(colIdx).getAsInt();
        boolean isBoundary = rowIdx == 0 || colIdx == 0 || rowIdx == layout.size() - 1 || colIdx == row.size() - 1;
        LayoutTile layoutTile = LayoutTile.fromOrdinal(type, new Location(rowIdx, colIdx));
        layoutTile.addTo(roomBuilder, origin, isBoundary);
      }
    }
  }

  /**
   * Gets the JsonElement as an array if possible.
   * @param jsonElement the element to
   * @return the JsonArray
   * @throws JsonParseException if the given element is not a JsonArray
   */
  private static JsonArray getArray(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("expected an array, given: " + jsonElement.toString());
    }
    return (JsonArray) jsonElement;
  }

  /**
   * Gets the JsonElement as an object if possible.
   * @param jsonElement the element to
   * @return the JsonObject
   * @throws JsonParseException if the given element is not a JsonObject
   */
  private static JsonObject getObject(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonObject()) {
      throw new JsonParseException("expected an object, given: " + jsonElement.toString());
    }
    return (JsonObject) jsonElement;
  }
}
