import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TravellerServer {

  private ServerSocket server;
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  private TravellerServer(int port) throws IOException {
    System.out.println("Using port" + port);
    this.server = new ServerSocket(port);
    System.out.println("Server started");
    System.out.println("Waiting for client...");

    this.socket = server.accept();
    System.out.println("Client accepted");

    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    this.out = new PrintWriter(socket.getOutputStream(), true);
  }

  private void sendMessage(String message) throws IOException {
    System.out.println("sending message: " + message);
    this.out.println(message);
  }

  private String readMessages() throws IOException {
    String messages = "";
    String current = "";

    while(!current.equals("END")) {
      messages += current;
      current = this.in.readLine();
    }

    return messages;
  }

  private void closeConnection() throws IOException {
    this.out.close();
    this.in.close();
    this.socket.close();
    this.server.close();
  }

  public static void main(String[] args) throws IOException {
    TravellerServer server = new TravellerServer(8000);
    System.out.println("server created");
    String messages = server.readMessages();
    System.out.println("client said: " + messages);
    System.out.println("closing connection");
    server.closeConnection();
  }

}