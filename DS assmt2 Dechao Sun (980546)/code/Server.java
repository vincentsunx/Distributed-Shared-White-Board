import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

/**
 * Server class will run the ServerThread to achieve multi-thread
 */
public class Server extends Thread {
    public ServerSocket serverSocket = null;
    public UserList userList;
    public PaintSourceList sourceList;
    private static int port = 0;
    private static String ip = "";

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        userList = new UserList();
        sourceList = new PaintSourceList();
    }


    public void run() {
        try {
            connection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  construct socket connection
     * @throws IOException
     */
    public void connection() throws IOException {
        while (true) {
            Socket clientSocket = null;
            clientSocket = serverSocket.accept();
            System.out.println("Connected");
            ServerThread serverThread = new ServerThread(clientSocket,this);
            serverThread.start();
        }
    }

    public synchronized void addUser(User user) throws IOException {
            userList.addUser(user);
    }


    public synchronized void removeUser(User user) throws IOException {
            userList.removeUser(user);
    }

    public synchronized void addSource(PaintSource source) {
            sourceList.addData(source);
    }


    public synchronized void resetSourceList(String source) {
            sourceList.resetSource(source);
    }


    public static void main(String[] args)  {
        ip = args[0];
        port = Integer.parseInt(args[1]);
        try{
            /* new a server to start the system */
            Server server = new Server(port);
            server.start();

        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Invalid port number");
        }

    }

}
