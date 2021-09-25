import java.net.Socket;


public class User {
    private String name = "";
    private boolean isManager;
    private Socket socket;

    /* constructor for the User class */
    public User(String username, Socket socket, boolean isManager){
        this.name = username;
        this.socket = socket;
        this.isManager = isManager;
    }



    public String getName() {
        return this.name;
    }

    public Socket getSocket(){
        return this.socket;
    }

    public boolean getisManager(){
        return this.isManager;
    }
}
