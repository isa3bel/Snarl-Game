package model.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.level.Location;
import testHarness.deserializer.LocationDeserializer;

import java.io.IOException;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {

  private final SocketReader socketReader;
  private final Gson gson = new GsonBuilder()
      .registerTypeAdapter(Location.class, new LocationDeserializer())
      .create();

  private Client(SocketReader socketReader) {
    this.socketReader = socketReader;
  }

  /**
   * Accepts user moves and sends the move across the socket.
   * @param systemIn the reader for System.in
   * @return is the socket still connected
   * @throws IOException if the socket is disconnected
   */
  private boolean listenToSocket(Scanner systemIn) throws IOException {
    String received = this.socketReader.readMessage();

    if (received == null) return false;
    if (received.equals("\"move\"")) {
      this.handlePlayerMove(systemIn);
    }
    else if (received.contains("\"type\": \"player-update\"")) {
      this.printPlayerUpdateView(received);
    }
    else {
      System.out.println(received);
    }
    return true;
  }

  /**
   * Formats the json of the player update view into ASCII.
   * @param playerUpdateView the player update view string from the server
   */
  private void printPlayerUpdateView(String playerUpdateView) {
    PlayerUpdateViewer view = gson.fromJson(playerUpdateView, PlayerUpdateViewer.class);
    System.out.println(view.toString());
  }

  /**
   * Handles the interaction with the user when we need to ask for their move.
   * @param systemIn reader for System.in
   */
  private void handlePlayerMove(Scanner systemIn) {
    System.out.println("Choose a move: ");
    int row;
    int col;
    try {
      row = systemIn.nextInt();
      col = systemIn.nextInt();
    } catch(InputMismatchException exception) {
      systemIn.next();
      System.out.println("Invalid location, expected \"row col\" (e.g. 3 1). Please try again.");
      this.handlePlayerMove(systemIn);
      return;
    }
    String moveString = "{ \"type\": \"move\", \"to\": [ " + row + ", " + col + " ]\n}";
    this.socketReader.sendMessage(moveString);
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
    Client client = new Client(socketReader);

    System.out.println("client created");
    Scanner systemIn = new Scanner(System.in);

    handshake(socketReader, systemIn);
    // play the game
    while (true) {
      if (!client.listenToSocket(systemIn)) {
        System.out.println("Game over");
        break;
      }
    }
  }

  /**
   * Initiate connection with the server.
   */
  private static void handshake(SocketReader socketReader, Scanner systemIn) throws IOException {
    // Reading a welcome message
    String received = socketReader.readMessage();
    // Print welcome message
    System.out.println(received);
    // Reading the server's message to prompt user's name
    received = socketReader.readMessage();
    if (received.equals("\"name\"")) {
      System.out.println("Input your name: ");
      // Reading user's name
      String nameInput;
      while (true) {
        nameInput = systemIn.nextLine();
        if (nameInput.matches("[a-zA-Z0-9]+")) break;
        System.out.println("Invalid name. Must be alpha numeric (no spaces)");
      }
      System.out.println("Registering: " + nameInput);
      // Sending user's name to Server
      socketReader.sendMessage("{\"type\": \"player\", \"name\": \"" + nameInput + "\" }");
    }
    else {
      throw new IllegalStateException("Server expected to send \"name\". Got: " + received);
    }
  }
}
