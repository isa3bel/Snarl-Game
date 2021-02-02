import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TownNetworkInterfaceIntegrationTest {

  // createTownNetwork construction errors
  @Test
  public void errorsWhenConnectingNonExistentTownValue() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));

    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));

    try {
      TownNetworkInterface.createTownNetwork(towns, roads);
      assertTrue(false);
    }
    catch (IllegalArgumentException illegalArgumentException) {
      assertEquals("roads passed in contain a town not in this network", illegalArgumentException.getMessage());
    }
  }

  @Test
  public void errorsWhenConnectingNonExistentTownKey() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Boston"));

    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));

    try {
      TownNetworkInterface.createTownNetwork(towns, roads);
      assertTrue(false);
    }
    catch (IllegalArgumentException illegalArgumentException) {
      assertEquals("roads passed in contain a town not in this network", illegalArgumentException.getMessage());
    }
  }

  @Test
  public void errorsForNullTowns() {
    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));

    try {
      TownNetworkInterface.createTownNetwork(null, roads);
      assertTrue(false);
    }
    catch (IllegalArgumentException illegalArgumentException) {
      assertEquals("cannot make a town network with null towns or roads", illegalArgumentException.getMessage());
    }
  }

  @Test
  public void errorsForNullRoads() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));
    towns.add(new Town("Boston"));

    try {
      TownNetworkInterface.createTownNetwork(towns, null);
      assertTrue(false);
    }
    catch (IllegalArgumentException illegalArgumentException) {
      assertEquals("cannot make a town network with null towns or roads", illegalArgumentException.getMessage());
    }
  }

  // place character errors
  @Test
  public void cannotPlaceCharacterAtNonExistentTown() {
    List<Town> towns = new ArrayList<>();
    List<Pair<String, String>> roads = new ArrayList<>();
    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    Character character = new Character("Ferd");
    try {
      townNetwork.addCharacterToTown(character, "Boston");
      assertTrue(false);
    }
    catch (IllegalStateException illegalStateException) {
      assertEquals("No town with name: Boston", illegalStateException.getMessage() );
    }
  }

  @Test
  public void cannotPlaceCharacterAtNonExistentTownWithOtherTowns() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));
    towns.add(new Town("Boston"));
    towns.add(new Town("Cambridge"));
    towns.add(new Town("Dallas"));

    List<Pair<String, String>> roads = new ArrayList<>();
    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    Character character = new Character("Ferd");
    try {
      townNetwork.addCharacterToTown(character, "El Paso");
      assertTrue(false);
    }
    catch (IllegalStateException illegalStateException) {
      assertEquals("No town with name: El Paso", illegalStateException.getMessage() );
    }
  }


  @Test
  public void placingCharacterOverwritesPreviousCharacter() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));
    towns.add(new Town("Boston"));

    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));

    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    Character ferd = new Character("Ferd");
    townNetwork.addCharacterToTown(ferd, "Austin");

    Character other = new Character("Other");
    townNetwork.addCharacterToTown(other, "Austin");

    try {
      townNetwork.reachableWithoutCollision("Ferd", "Austin");
      assertTrue(false);
    }
    catch (IllegalStateException illegalStateException) {
      assertEquals("town network does not have character with name: Ferd", illegalStateException.getMessage() );
    }
  }

  // integration tests
  @Test
  public void simplestTown() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Boston"));

    List<Pair<String, String>> roads = new ArrayList<>();

    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    String characterName = "Ferd";
    Character ferd = new Character(characterName);
    townNetwork.addCharacterToTown(ferd, "Boston");

    boolean canReach = townNetwork.reachableWithoutCollision(characterName, "Boston");
    assertTrue(canReach);
  }

  @Test
  public void basicTown() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));
    towns.add(new Town("Boston"));

    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));

    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    String characterName = "Ferd";
    Character ferd = new Character(characterName);
    townNetwork.addCharacterToTown(ferd, "Austin");

    boolean canReach = townNetwork.reachableWithoutCollision(characterName, "Boston");
    assertTrue(canReach);
  }

  @Test
  public void basicTownWithOtherCharacter() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));
    towns.add(new Town("Boston"));

    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));

    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    String characterName = "Ferd";
    Character ferd = new Character(characterName);
    townNetwork.addCharacterToTown(ferd, "Austin");

    String otherCharacterName = "Other";
    Character other = new Character(otherCharacterName);
    townNetwork.addCharacterToTown(other, "Boston");

    boolean canReach = townNetwork.reachableWithoutCollision(characterName, "Boston");
    assertFalse(canReach);
  }

  @Test
  public void complexTownWithOtherCharacter() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));
    towns.add(new Town("Boston"));
    towns.add(new Town("Cambridge"));
    towns.add(new Town("Dallas"));
    towns.add(new Town("El Paso"));

    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));
    roads.add(new Pair<>("Austin", "Cambridge"));
    roads.add(new Pair<>("Cambridge", "Dallas"));
    roads.add(new Pair<>("El Paso", "Dallas"));

    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    String characterName = "Ferd";
    Character ferd = new Character(characterName);
    townNetwork.addCharacterToTown(ferd, "Austin");

    String otherCharacterName = "Other";
    Character other = new Character(otherCharacterName);
    townNetwork.addCharacterToTown(other, "Boston");

    boolean ferdCanReachBoston = townNetwork.reachableWithoutCollision(characterName, "Boston");
    assertFalse(ferdCanReachBoston);

    boolean ferdCanReachElPaso = townNetwork.reachableWithoutCollision(characterName, "El Paso");
    assertTrue(ferdCanReachElPaso);
  }

  @Test
  public void complexTownNotReachable() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));
    towns.add(new Town("Boston"));
    towns.add(new Town("Cambridge"));
    towns.add(new Town("Dallas"));
    towns.add(new Town("El Paso"));

    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));
    roads.add(new Pair<>("Boston", "Cambridge"));
    roads.add(new Pair<>("Cambridge", "Dallas"));
    roads.add(new Pair<>("El Paso", "Dallas"));

    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    String characterName = "Ferd";
    Character ferd = new Character(characterName);
    townNetwork.addCharacterToTown(ferd, "Austin");

    String otherCharacterName = "Other";
    Character other = new Character(otherCharacterName);
    townNetwork.addCharacterToTown(other, "Boston");

    boolean ferdCanReachBoston = townNetwork.reachableWithoutCollision(characterName, "Boston");
    assertFalse(ferdCanReachBoston);

    boolean ferdCanReachElPaso = townNetwork.reachableWithoutCollision(characterName, "Boston");
    assertFalse(ferdCanReachElPaso);
  }

  @Test
  public void complexTownReachableAround() {
    List<Town> towns = new ArrayList<>();
    towns.add(new Town("Austin"));
    towns.add(new Town("Boston"));
    towns.add(new Town("Cambridge"));
    towns.add(new Town("Dallas"));
    towns.add(new Town("El Paso"));

    List<Pair<String, String>> roads = new ArrayList<>();
    roads.add(new Pair<>("Austin", "Boston"));
    roads.add(new Pair<>("Boston", "Cambridge"));
    roads.add(new Pair<>("Austin", "Cambridge"));
    roads.add(new Pair<>("Cambridge", "Dallas"));
    roads.add(new Pair<>("El Paso", "Dallas"));

    TownNetworkInterface townNetwork = TownNetworkInterface.createTownNetwork(towns, roads);

    String characterName = "Ferd";
    Character ferd = new Character(characterName);
    townNetwork.addCharacterToTown(ferd, "Austin");

    String otherCharacterName = "Other";
    Character other = new Character(otherCharacterName);
    townNetwork.addCharacterToTown(other, "Boston");

    boolean ferdCanReachElPaso = townNetwork.reachableWithoutCollision(characterName, "El Paso");
    assertTrue(ferdCanReachElPaso);
  }
}
