import javax.swing.*;
import java.io.*;
import java.net.Socket;


public class JoinWhiteBoard {
    private Socket socket;
    private static String ip = "";
    private static int port = 0;
    private static String username = "";
    DataInputStream in;
    DataOutputStream out;



    public JoinWhiteBoard(String username, String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        User user = new User(username, socket, false);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        /* send user permission to server ask for join request*/
        out.writeUTF("USER_PRE:" + username);
        out.flush();
        System.out.println("Waiting for permission to join.");
        String permission;
        /* read the join result from server */
        while(true){
            permission = in.readUTF();
            if (permission.equals("YES")){
                break;
            }
            else if(permission.equals("NO")){
                break;
            }
        }

        /* if permission is YES, create the client to generate a client*/
        if (permission.equals("YES")) {
            Client paintClient = new Client(socket, user,false);
            paintClient.start();
            System.out.println("You are allowed to join.");
            out.writeUTF("");
            out.flush();
        /* if permission is Repetition, the system will stop and send message to user*/
        } else if (permission.equals("Repetition")) {
            JOptionPane.showMessageDialog(null, "This username Repetition.", "Display Message", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
            /* if permission is NO, the system will stop and send message to user*/
        } else if(permission.equals("NO")){
            JOptionPane.showMessageDialog(null, "You are refused to join in the whiteboard.", "Display Message", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }




    }



    public static void main(String[] args) {
        /* read inputs from command line */
        ip = args[0];
        port = Integer.parseInt(args[1]);
        username = args[2];

        try {
            /* generate a whiteboard for clients*/
            JoinWhiteBoard joinWhiteBoard = new JoinWhiteBoard(username, ip, port);

        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server client connection failed.");
        }

    }

}
