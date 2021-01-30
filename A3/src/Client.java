import java.io.*;
import java.net.*;

public class Client {

  private Socket socket = null;
  private BufferedReader input = null;
  private DataOutputStream out = null;

  public Client(String host, int port) {

    // establish a connection
    try {
      socket = new Socket(host, port);
      System.out.println("Connected");

      input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      out = new DataOutputStream((socket.getOutputStream()));
    } catch (IOException i) {
      System.out.print(i);
    }

    String line = "";

    while(!line.equals("END")) {
      System.out.print("gets here");
      try {
        line = input.readLine();
        out.writeUTF(line);
      } catch(IOException exception) {
        System.out.println(exception);
      }
    }

    try {
      input.close();
      out.close();
      socket.close();
    } catch(IOException exception) {
      System.out.println(exception);
    }
  }

  public static void main(String[] args) throws IOException {
    Client c = new Client("localhost", 8080);

  }

}

//import java.net.*;
//import java.io.*;
//
//public class Client {
//  public static void main(String[] args) throws IOException {
//    Socket s= new Socket("localhost", 8080);
//
//    PrintWriter pr = new PrintWriter(s.getOutputStream());
//    pr.println("hello");
//    pr.flush();
//
//    InputStreamReader in = new InputStreamReader(s.getInputStream());
//    BufferedReader bf = new BufferedReader(in);
//
//    String str = bf.readLine();
//    System.out.println("client : " + str);
//
//
//  }
//}
