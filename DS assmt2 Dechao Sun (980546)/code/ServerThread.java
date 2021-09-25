import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket client;
    private DataInputStream in;
    private DataOutputStream out;
    private User user;
    private Server server;


    /* constructor for ServerThread class */
    public ServerThread(Socket socket, Server server) throws IOException {

        this.client = socket;
        this.server = server;
        in = new DataInputStream(client.getInputStream());
        out = new DataOutputStream(client.getOutputStream());
        String name ="";
        String info = in.readUTF();
        if(info.contains(":")){
            name = info.split(":")[1];
        }
        else{
            name = info;
        }

        UserList userList = server.userList;
        /* create user with isManager boolean*/
        if (server.userList.getSize() == 0) {
            user = new User(name, client, true);
        } else {
            user = new User(name, client, false);
        }

        /* check duplicate name */
        if (userList.isduplicateName(name)) {
            out.writeUTF("Repetition");
            out.flush();
            return;
        }
        /* send user permission */
        if (server.userList.getSize() > 0) {
            if (info.split(":")[0].equals("USER_PRE")) {
                server.userList.userPre(info);
            }
        }


    }
    public void run() {
        try {
            /* add user to server userlist */
            server.addUser(user);
            /* read info from user and send back to other users*/
            if (server.userList.getSize() > 1) {
                String new_info = in.readUTF();

                if (new_info.equals("YES")) {
                    out.writeUTF("YES");
                } else if (new_info.equals("NO")) {
                    out.writeUTF("NO");
                }
            }
            /* read requests from clients*/
            Request();
        } catch (IOException e) {
        } finally {
            try {
                /* remove user from server userlist */
                server.removeUser(user);
            } catch (IOException e) {
            }
        }
    }

    public void Request() throws IOException {
        /* if number of user in userlist is larger than 1, send INIT_USERS to that client*/
        if (server.userList.getList().size() > 1) {
            server.userList.update("INIT_USERS:" + server.userList.toString());

        }
        /* send empty*/
        else{
            out.writeUTF("");
        }
        /* if number of source in sourcelist is larger than 1, send INIT_DATA to that user */
        if (server.sourceList.painList.size() > 1) {
            out.writeUTF("INIT_DATA:" + server.sourceList.toString());
            out.flush();
        }
        /* keep reading request from clients*/
        while (true) {
            String dataBuffer = in.readUTF();
            System.out.println("request   " + dataBuffer);

            String[] data = dataBuffer.split(":");
            if (data[0].equals("DRAW")) {

                server.addSource(new PaintSource(data[1]));
            }
            else if (data[0].equals("INIT")) {
                if(data.length >1){
                    server.resetSourceList(data[1]);
                }
                else{
                    server.resetSourceList("");
                }

            }
            /* update info to all the users in the userlist */
            server.userList.update(dataBuffer);
        }
    }


}
