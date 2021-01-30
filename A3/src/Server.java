import java.io.*;
import java.net.*;

public class Server {

  public Server(int port) {
    ServerSocket server = null;
    Socket socket = null;
    DataInputStream in;
    DataOutputStream out = null;

    System.out.println("Using port" + port);

    try {
      server = new ServerSocket(port);
      System.out.println("Server started");
      System.out.println("Waiting for client...");

      socket = server.accept();
      System.out.println("Client accepted");

      in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
      out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

      String line = "";
      String name = null;

      while(!line.equals("END")) {
        try {
          line = in.readUTF();
          System.out.println(line + " client said");
        } catch (IOException i) {
          System.out.println(i);
        }
      }

      socket.close();
      in.close();

    } catch (IOException exception) {
      System.out.println(exception);
    }
  }

  public static void main(String[] args) throws IOException {
    Server s = new Server(8080);

  }

}

//import java.net.*;
//import java.io.*;
//
//public class Server {
//  public static void main(String[] args) throws IOException {
//    ServerSocket ss= new ServerSocket(8080);
//    Socket s = ss.accept();
//
//    InputStreamReader in = new InputStreamReader(s.getInputStream());
//    BufferedReader bf = new BufferedReader(in);
//
//    PrintWriter pr = new PrintWriter(s.getOutputStream());
//    pr.println("hello");
//    pr.flush();
//  }
//}