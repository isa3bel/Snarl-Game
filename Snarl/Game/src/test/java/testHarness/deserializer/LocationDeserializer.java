package testHarness.deserializer;

import com.google.gson.*;
import model.Location;

import java.lang.reflect.Type;

/**
 * Deserializes a location from JsonElements of the format [x, y].
 */
public class LocationDeserializer implements JsonDeserializer<Location> {

  /**
   * Converts the given jsonElement to a Location if it is a Location.
   * @param jsonElement the (potential) location
   * @return the Location
   * @throws JsonParseException if the element does not follow the format [x, y]
   */
  @Override
  public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
      throws JsonParseException {
    JsonArray jsonArray = getValidArray(jsonElement);
    int[] axes = getAxes(jsonArray);
    return new Location(axes[0], axes[1]);
  }

  /**
   * Converts the given JsonElement to an int array for the location if it fits the valid location shape.
   * @param jsonElement the (potential) JsonArray
   * @return the valid array
   * @throws JsonParseException if the element does not fit the valid construction for a location object
   */
  private static JsonArray getValidArray(JsonElement jsonElement) throws JsonParseException {
    if (!jsonElement.isJsonArray()) {
      throw new JsonParseException("Expecting a JSON array, received: " + jsonElement.toString());
    }

    JsonArray jsonArray = (JsonArray) jsonElement;
    if (jsonArray.size() != 2) {
      throw new JsonParseException("The size of the json array must be exactly 2");
    }
    return jsonArray;
  }

  /**
   * Gets a 2 element array of the values of this location on the axis.
   * @param jsonArray the array to get the ints from
   * @return the x and y coordinates in an array
   * @throws JsonParseException if the JsonArray isn't only ints
   */
  private static int[] getAxes(JsonArray jsonArray) throws JsonParseException {
    int[] axes = new int[2];
    try {
      axes[0] = jsonArray.get(0).getAsInt();
      axes[1] = jsonArray.get(1).getAsInt();
    } catch (ClassCastException exception) {
      String notX = jsonArray.get(0).toString();
      String notY = jsonArray.get(1).toString();
      throw new JsonParseException(String.format("Location expected integer x and y, received: %s, %s", notX, notY));
    }
    return axes;
  }
}
