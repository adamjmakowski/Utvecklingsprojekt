package Model;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Message implements Serializable {
    private User sender;
    private ArrayList<User> receivers;
    private String text;
    private ImageIcon image;
    private Date received;
    private Date delivered;

    public Message(User sender, ArrayList<User> receivers, String text, ImageIcon image) {
        this.sender = sender;
        this.receivers = receivers;
        this.text = text;
        this.image = image;
    }

    public User getSender() {
        return sender;
    }

    public ArrayList<User> getReceivers() {
        return receivers;
    }

    public void setReceived() {
        this.received = new Date();
    }

    public void setDelivered() {
        this.delivered = new Date();
    }

    public Date getReceived() {
        return received;
    }

    public Date getDelivered() {
        return delivered;
    }


    public ImageIcon getImage() {
        return image;
    }

    public String getText() {
        return text;
    }
}

