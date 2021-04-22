package model.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.controller.AdversaryAI;
import model.controller.GhostAI;
import model.controller.ZombieAI;
import model.level.Location;
import testHarness.deserializer.LocationDeserializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.Socket;

public class AdversaryClient {

  private Location nextMove;
  private final Type aiClass;
  private final SocketReader socketReader;
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Location.class, new LocationDeserializer())
      .create();

  private AdversaryClient(Type aiClass, SocketReader socketReader) {
    this.aiClass = aiClass;
    this.socketReader = socketReader;
  }

  /**
   * Accepts user moves and sends the move across the socket.
   * @return is the socket still connected
   * @throws IOException if the socket is disconnected
   */
  private boolean listenToSocket() throws IOException {
    String received = this.socketReader.readMessage();

    if (received == null) return false;
    if (received.equals("\"move\"")) {
      System.out.println("moving to " + this.nextMove);
      String moveString = "{ \"type\": \"move\", \"to\": " + this.nextMove + "\n}";
      this.socketReader.sendMessage(moveString);
    }
    else if (received.contains("\"type\": \"adversary-update\"")) {
      AdversaryAI adversaryAI = this.gson.fromJson(received, aiClass);
      this.nextMove = adversaryAI.calculateNextMove();
    }
    else {
      System.out.println(received);
    }
    return true;
  }

  /**
   * Runs the client side of the game.
   * @param args the arguments to run the client program
   * @throws IOException if there is an IO error
   */
  public static void main(String[] args) throws IOException {
    // setup socket object
    ArgumentParser argumentParser = new ArgumentParser(args);
    SocketReader socketReader = new SocketReader(new Socket(argumentParser.ip, argumentParser.port));

    Type aiClass;
    switch (argumentParser.type) {
      case "zombie":
        aiClass = ZombieAI.class; break;
      case "ghost":
        aiClass = GhostAI.class; break;
      default:
        throw new IllegalArgumentException("illegal adversary type, expected one of [\"ghost\", \"zombie\"]. given: "
            + argumentParser.type);
    }
    AdversaryClient client = new AdversaryClient(aiClass, socketReader);

    System.out.println("client created");

    handshake(socketReader, argumentParser.type);
    // play the game
    while (true) {
      if (!client.listenToSocket()) {
        System.out.println("Game over");
        break;
      }
    }
  }

  /**
   * Initiate connection with the server.
   */
  private static void handshake(SocketReader socketReader, String type) throws IOException {
    // Reading a welcome message
    String received = socketReader.readMessage();
    // Print welcome message
    System.out.println(received);
    // Reading the server's message to prompt user's name
    received = socketReader.readMessage();
    if (!received.equals("\"name\"")) {
      throw new IllegalStateException("Server expected to send \"name\". Got: " + received);
    }

    // Sending user's name to Server
    socketReader.sendMessage("{\"type\": \"" + type + "\", \"name\": \"adversary\" }");
  }
}
