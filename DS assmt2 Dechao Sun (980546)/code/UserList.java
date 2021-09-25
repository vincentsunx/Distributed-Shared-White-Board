import java.io.IOException;
import java.io.DataOutputStream;
import java.util.ArrayList;




public class UserList {
    private ArrayList<User> userlist;
    /* constructor for UserList Class */
    public UserList() {
        userlist = new ArrayList<User>();
    }

    /* add user to userlist*/
    public synchronized void addUser(User user) throws IOException {
        userlist.add(user);
        update("ADD_USER:" + user.getName());
    }
    /* remove user to userlist*/
    public synchronized void removeUser(User user) throws IOException {
        userlist.remove(user);
        update("REMOVE_USER:" + user.getName());

    }
    /* check duplicate name */
    public boolean isduplicateName(String name){
        for(User user : userlist){
            if(name.equals(user.getName())){
                return true;
            }
        }
        return false;
    }

    public String toString() {
        ArrayList<String> names = new ArrayList<String>();

        for (User user : userlist) {
            names.add(user.getName());
        }

        return String.join(",", names);
    }

    /* send user permission info*/
    public synchronized void userPre(String data) throws IOException {
        DataOutputStream out = new DataOutputStream(userlist.get(0).getSocket().getOutputStream());
        out.writeUTF(data);
        out.flush();
    }

    public synchronized void update(String data) throws IOException {
        for(User user : userlist){
            DataOutputStream out = new DataOutputStream(user.getSocket().getOutputStream());
            out.writeUTF(data);
            out.flush();
        }
    }

    public ArrayList<User> getList() {
        return userlist;
    }

    public int getSize() {
        return userlist.size();
    }


}
