package database;

import java.io.Serializable;
import java.util.ArrayList;

public class Credentials implements Serializable {
    public final int id;
    public final String username;
    public final String password;

    private ArrayList<Integer> penID;

    public Credentials(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.penID = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Credentials{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public ArrayList<Integer> getPenID() {
        return penID;
    }

    public void setPenID(ArrayList<Integer> penID) {
        this.penID = penID;
    }
}
