package Model;

import java.util.ArrayList;

public class ChatModel {
    private ArrayList<String> users;
    private ArrayList<String> contacts;

    public ChatModel() {
        this.users = new ArrayList<String>();
        this.contacts = new ArrayList<String>();
    }

    public void addUser(String user) {
        this.users.add(user);
    }

    public String[] getUsers() {
        return this.users.toArray(new String[0]);
    }

    public void addContact(String contact) {
        this.contacts.add(contact);
    }

    public String[] getContacts() {
        return this.contacts.toArray(new String[0]);
    }
}
