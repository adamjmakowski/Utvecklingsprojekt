package Model;

import javax.swing.*;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private ImageIcon image;
    private boolean isConnected;

    public User(String username, ImageIcon image) {
        this.username = username;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getImage() {
        return image;
    }
//method to return a hash code value for the username field.
// This is because the username field is used as the key in a HashMap in the Model.Clients class.
//by overriding the hashCode() method, we ensure that two objects with the same username field will have the same hash code and will be stored in the same bucket in the HashMap.
// This will improve the performance of the Model.Clients class, especially when dealing with a large number of clients.
    public int HaschCode() {
        return username.hashCode();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }
//The equals method is overridden to allow for the comparison of Model.User objects based on their username.
// This is useful for operations such as searching for a specific user in an ArrayList or HashMap.
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof User) {
            return username.equals(((User)obj).getUsername());
        }
        return false;
    }
}
