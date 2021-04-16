package model.remote;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.*;
import java.net.Socket;

public class SocketReader {

  private final Socket socket;
  private final BufferedReader in;
  private final PrintWriter out;
  private final Gson gson = new Gson();

  public SocketReader(Socket socket) throws IOException {
    this.socket = socket;

    InputStreamReader inputReader = new InputStreamReader(this.socket.getInputStream());
    this.in = new BufferedReader(inputReader);

    OutputStream output = this.socket.getOutputStream();
    this.out = new PrintWriter(output, true);
  }

  /**
   * Reads a message from the socket.
   * @return the message from the socket
   * @throws IOException if an IO error occurs
   */
  public String readMessage() throws IOException {
    StringBuilder response = new StringBuilder();

    while (true) {
      String nextLine = this.in.readLine();
      if (nextLine == null) return null;

      response.append(nextLine);
      try {
        // TODO: in case someone sends messages without \n at the end, we need to
        //  actually do more parsing work here -- is this possible (?)
        gson.fromJson(response.toString(), JsonElement.class);
        break;
      } catch (JsonParseException ignored) {
        // message is not complete, continue reading
      }
    }

    return response.toString();
  }

  /**
   * Send a message across the socket.
   * @param message the message to send across the socket
   */
  public void sendMessage(String message) {
    this.out.println(message);
  }

  /**
   * Closes the socket connection with the server.
   */
  public void closeConnection() {
    try {
      this.in.close();
      this.out.close();
      this.socket.close();
    }
    catch (IOException exception) {
      System.out.println("failed to closed server connection");
    }
  }

}
