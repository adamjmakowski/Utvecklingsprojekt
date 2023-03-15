package Controller;

import Model.*;
import View.*;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class ClientController {
    private ClientView gui;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String address;
    private int port;
    private boolean running;
    private Contacts contacts;
    private ContactsView contactsView;
    private ArrayList<User> receiverList = new ArrayList<>();

    public ClientController(String address, int port) {
        this.address = address;
        this.port = port;
        gui = new ClientView(this);
        contacts = new Contacts();
    }
    public void buttonPressed(ButtonType button) {
        switch (button) {
            case Connect:
                connect();
                contacts.readContactsFromFile("files/contacts.dat");
                break;
            case Disconnect:
                contacts.writeContactsToFile("files/contacts.dat");
                disconnect();
                break;
            case Send:
                sendMessage(getMessageFromView());
                for (int i = 0; i < receiverList.size(); i++) {
                    receiverList.remove(i);
                }
                break;
            case Contacts:
                contactsView = new ContactsView(this);
                contactsView.createFrame();
                String[] onlineUsers = getUsernames(contacts.getOnlineContacts());
                String[] savedContacts = getUsernames(contacts.getSavedContacts());
                contactsView.setOnlineUserList(onlineUsers);
                contactsView.setSavedContactList(savedContacts);
                break;
            case ContactsSend:
                updateReceiverList();
                break;
            case ContactsAdd:
                int index = contactsView.getListIndex();
                User u = contacts.getContactAt(index);
                contacts.setSavedContact(u);
                break;
            case ContactsDelete:
                int i = contactsView.getContactListIndex();
                contacts.removeContact(i);
        }

    }
    //Ansluter och skickar användaren till servern.
    private void connect() {
        try {
            socket = new Socket(address, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        User user = getUserFromView();
        user.setConnected(true);
        sendUserToServer(user);
        new ListenToServer().start();
    }
    private void disconnect() {
        setRunning(false);
        User user = getUserFromView();
        user.setConnected(false);
        sendUserToServer(user);
        gui.resetGUI();
        gui.getTaChatbox().setText("");
        gui.getTaChatbox().append("Du har lämnat chatten\n");
    }

    public void contactListIndicesChanged(int index) {
    }
    private User getUserFromView() {
        String username = gui.getTfName().getText();
        ImageIcon profilePicture = gui.getProfilePicture();
        User user = new User(username, profilePicture);
        return user;
    }
    private Message getMessageFromView() {
        Message message = null;
        String text = gui.getTfMessage().getText();
        ImageIcon icon = gui.getMessageImage();
        User user = getUserFromView();
        if(receiverList.isEmpty()) {
            message = new Message(user, contacts.getOnlineContacts(), text, icon);
        } else {
            ArrayList<User> newReceiverList = new ArrayList<>(receiverList);
            message = new Message(user, newReceiverList, text, icon);
            gui.getLblSendTo().setText("Send to: ");
            gui.resetLabel();
        }
        return message;
    }
    private void sendUserToServer(User user) {
        try {
            oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void sendMessage(Message message) {
        try {
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateReceiverList() {
        int index = contactsView.getListIndex();
        if(index > -1) {
            User receiver = contacts.getContactAt(index);
            gui.setTextForLabel(receiver.getUsername());
            receiverList.add(receiver);
        } else {
            int contactIndex = contactsView.getContactListIndex();
            User receiver = contacts.getSavedContactAt(contactIndex);
            gui.setTextForLabel(receiver.getUsername());
            receiverList.add(receiver);
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
    private String[] getUsernames(ArrayList<User> users) {
        String[] username = new String[users.size()];
        for(int i = 0; i < users.size(); i++) {
            username[i] = users.get(i).getUsername();
        }
        return username;
    }

    //is an inner class called ListenToServer that extends Thread and listens to incoming messages from the server.
    // If the incoming message is a Model.User object,
    // it either adds the user to the list of online contacts or removes the user from the list of online contacts.
    // If the incoming message is a Message object, it displays the message in the chat box.

    private class ListenToServer extends Thread {

        public void run() {
            running = true;
            while(running) {
                try {
                    Object obj = ois.readObject();
                    if(obj instanceof User) {
                        if(((User) obj).isConnected()) {
                            contacts.setOnlineContact((User)obj);
                            gui.displayUser(GUIUtilities.createUserLabel(((User) obj).getImage(), ((User) obj).getUsername()));
                        } else {
                            contacts.removeOnlineContact((User)obj);
                            gui.removeUser(GUIUtilities.createUserLabel(((User) obj).getImage(), ((User) obj).getUsername()));
                        }
                    } else if (obj instanceof Message) {
                        Message message = (Message)obj;
                        System.out.println(message.getText());
                        if(message.getImage() == null) {
                            gui.getTaChatbox().append(message.getSender().getUsername()+ ": " + message.getText() + '(' + message.getDelivered() + ')' + "\n");
                        } else {
                            gui.displayImage(message.getSender().getUsername(), message.getText(), message.getImage());
                        }
                    }
                } catch (IOException e) {
                } catch (ClassNotFoundException e) {
                }
            }
        }
    }
}
