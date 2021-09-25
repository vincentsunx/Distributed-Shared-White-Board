import java.io.*;
import java.net.Socket;


public class CreateWhiteBoard {
    private Socket socket;
    private static String ip = "";
    private static int port = 0;
    private static String username = "";
    DataInputStream in;
    DataOutputStream out;



    public CreateWhiteBoard(String username, String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        User user = new User(username, socket, true);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        out.writeUTF(username);
        out.flush();

        /* construct a client for communication using with server(ServerThread) */
        Client paintClient = new Client(socket, user, true);
        paintClient.start();
        System.out.println("You are allowed to join.");
        out.writeUTF("");
        out.flush();


    }



    public static void main(String[] args) {
        /* read input from command line */
        ip = args[0];
        port = Integer.parseInt(args[1]);
        username = args[2];

        try {
            /* generate a WhiteBoard for the manager*/
            CreateWhiteBoard createWhiteBoard = new CreateWhiteBoard(username, ip, port);

        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server client connection failed.");
        }

    }

}
