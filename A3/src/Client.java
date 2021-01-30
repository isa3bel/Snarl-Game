import java.io.*;
import java.net.*;

public class Client {

  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;

  Client(String host, int port) throws IOException {
    this.socket = new Socket(host, port);

    InputStreamReader inputReader = new InputStreamReader(this.socket.getInputStream());
    this.in = new BufferedReader(inputReader);

    OutputStream output = this.socket.getOutputStream();
    this.out = new PrintWriter(output, true);
  }

  private void sendMessageToServer(String message) throws IOException {
    System.out.println("sending message: " + message);
    this.out.println(message);
  }

  private String readMessages() throws IOException {
    String messages = "";
    String current = "";

    while(current != null && !current.equals("END")) {
      messages += current;
      current = this.in.readLine();
    }

    return messages;
  }

  private void closeConnection() throws IOException {
    this.in.close();
    this.out.close();
    this.socket.close();
  }

  public static void main(String[] args) throws IOException {
    Client client = new Client("localhost", 8000);
    System.out.println("client created");
    client.sendMessageToServer("1234");
    client.sendMessageToServer("END");
    String received = client.readMessages();
    System.out.println("received from server: " + received);
    System.out.println("closing connection");
    client.closeConnection();
  }

}
