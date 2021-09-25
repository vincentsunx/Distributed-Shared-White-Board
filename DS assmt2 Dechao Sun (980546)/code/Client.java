import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends Thread {
    private User user;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Boolean isManager;
    private UserList userList;
    private PaintBoard board;
    private ArrayList<User> initUsers = new ArrayList<User>();
    private ArrayList<String> chatInfo = new ArrayList<String>();
    private WhiteBoardGUI boardGUI;

    /* constructor for Client*/
    public Client(Socket socket, User user, Boolean isManager) throws IOException {
        this.socket = socket;
        this.user = user;
        this.isManager = isManager;

        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        /* generate boardGUI*/
        boardGUI = new WhiteBoardGUI(socket, user, isManager, initUsers);
        /* generate paintboard to the boardGUI*/
        board= boardGUI.getDrawBoard();

        boardGUI.setVisible(true);
    }



    public void run() {
        try {
            readMessage(initUsers, chatInfo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "You are disconnected from the server.");
            System.exit(0);
        }
    }


    /* reading and process msg from server */
    public void readMessage(ArrayList<User> initUsers, ArrayList<String> chatInfo) throws IOException {
        while (true) {
            String readData = in.readUTF();
            System.out.println("readdata  "+readData);
            String[] request = readData.split(":");

            if (request[0].equals("REMOVE_USER")){
                kickUser(request[1], initUsers);
            }
            else if (request[0].equals("INIT_USERS")){
                initUser(request[1], initUsers);
            }
            else if (request[0].equals("ADD_USER")){
                addUser(request[1], initUsers);
            }
            else if (request[0].equals("USER_PRE")){
                userAllow(request[1]);
            }
            else if (request[0].equals("DRAW")){
                drawMsg(request[1]);
            }
            else if (request[0].equals("INIT_DATA")){
                if(request.length > 1){
                    initSource(request[1]);
                }
                else{
                    initSource("");
                }

            }
            else if(request[0].equals("CLEAR")){
                clear();
            }
            else if(request[0].equals("SEND_MSG")){
                msgProcess(request[1], chatInfo);
            }


        }
    }

    /* init User */
    public void initUser(String users, ArrayList<User> initUsers) throws IOException {
        if(users.contains(",")){
            initUsers.clear();
            String[] names = users.split(",");
            for (String name : names) {
                User user = new User(name, socket, isManager);
                initUsers.add(user);
            }
        }
        else{
            User user = new User(users,socket,isManager);
            initUsers.add(user);
        }

        boardGUI.updateUserList(initUsers);
    }


    public void addUser(String users, ArrayList<User> initUsers) throws IOException {
        User user = new User(users,socket,isManager);
        initUsers.add(user);
        boardGUI.updateUserList(initUsers);
    }




    public void kickUser(String username, ArrayList<User> initUsers) throws IOException {
        if (user.getName().equals(username)) {
            JOptionPane.showMessageDialog(null,
                    "You have been kicked out.");
            System.exit(0);
        }
        for (User user : initUsers){
            if(username.equals(user.getName())){
                initUsers.remove(user);
                break;
            }
        }
        boardGUI.updateUserList(initUsers);
    }

    public void userAllow(String username) throws IOException {
        int response = JOptionPane.showConfirmDialog(null, "Allow " + username + " to join?",
                "permission", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.NO_OPTION) {
            out.writeUTF("NO");
            out.flush();
        } else if (response == JOptionPane.YES_OPTION) {
            out.writeUTF("YES");
            out.flush();
        }
    }



    public void drawMsg(String info) {
        PaintSource data = new PaintSource(info);

        if (data.getUsername().equals(user.getName())){
            return;
        }
        board.getSourceList().addData(data);
        board.repaint();
    }


    public void initSource(String msg) {
        PaintSourceList initList = new PaintSourceList(msg);
        board.setSourceList(initList);
        board.repaint();
    }

    public void clear() {
        PaintSourceList emptyList = new PaintSourceList();
        board.setSourceList(emptyList);
        board.repaint();
    }


    public void msgProcess(String info, ArrayList<String> chatInfo) throws IOException {
        String[] infoSplit = info.split(" ");
        String newStr = infoSplit[0] + ":" + infoSplit[1];
        chatInfo.add(newStr);
        boardGUI.updateChatList(chatInfo);

    }
}
