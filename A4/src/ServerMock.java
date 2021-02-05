package src;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMock {

  private ServerSocket server;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  private ServerMock(int port) throws IOException {
    this.server = new ServerSocket(port);
    this.socket = server.accept();
    System.out.println("Client accepted");

    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.out = new PrintWriter(socket.getOutputStream(), true);
  }

  private void sendMessage(String message) throws IOException {
    this.out.println(message);
  }

  private String readMessages() throws IOException {
    return this.in.readLine();
  }

  private void closeConnection() throws IOException {
    this.out.close();
    this.in.close();
    this.socket.close();
    this.server.close();
  }

  public static void main(String[] args) throws IOException {
    ServerMock server = new ServerMock(8080);
    BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      String messages = server.readMessages();
      System.out.println("message received:" + messages);

      String stdin = stdinReader.readLine();
      if (stdin == null) break;
      if (stdin.equals(" ")) continue;
      System.out.println("sending: " + stdin);
      server.sendMessage(stdin);
    }

    System.out.println("closing connection");
    server.closeConnection();
  }

}