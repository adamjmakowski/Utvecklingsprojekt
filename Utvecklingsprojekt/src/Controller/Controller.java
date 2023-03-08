package Controller;

import View.*;
import Model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Controller {

    private final ChatView chatView;
    private final ContactListView contactListView;
    private final Client messageClient;
    private String [] contactlist = {"Contact 1", "Contact 2", "Contact 3"};
    private File attachment;
    private Message Message;
    private Message messageRecieved;
    private ImageIcon image;
    private User user;

    public Controller() throws IOException {
        // Create views
        chatView = new ChatView(new ImageIcon("Utvecklingsprojekt/Images/test.png"), new ImageIcon("Utvecklingsprojekt/Images/test.png"), this);
        contactListView = new ContactListView(contactlist);

        image = chatView.getProfilePicture();

        // Create message client and server
        String userName = chatView.usernameInput();
        User user = new User(userName,image);
        messageClient = new Client("127.0.0.1", 3343);



        // Add listeners to views
        contactListView.addAddButtonListener(new AddButtonListener());

        // Set initial contact list
        contactListView.setContactList(contactlist);

        // Add contact list to chat view
        chatView.setContactList(contactlist);


        contacts = new Contacts();
        contacts.readContactsFromFile("files/contacts.dat");

        String[] onlineUsers = getUsernames(contacts.getOnlineContacts());
        String[] savedContacts = getUsernames(contacts.getSavedContacts());

        contactListView = new ContactListView(onlineUsers);

        // Set initial contact list
        contactListView.setContactList(onlineUsers);

        // Add contact list to chat view
        chatView.setContactList(savedContacts);

        // Show initial view
        chatView.setVisible(true);

    }

    //Ansluter och skickar användaren till servern.
    private void connect() {
        User user = getUserFromView();
        user.setConnected(true);
    }


   public void recieveMessage(String message) {
        chatView.addMessage(message);
   }

    public void handleAttachment(){
        System.out.println(attachment);
        System.out.println("attached successfully");
    }

    public void handleMessage() throws IOException {
        System.out.println("controller:"+Message);
        messageClient.sendMessage(Message);
    }

    public void handleContact() {
        contactListView.setVisible(true);
        System.out.println("contactlist");
    }



    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Do something when add button is clicked
            System.out.println("test");

        }
    }

    private class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Do something when delete button is clicked
            int index = contactListView.getSelectedContactIndex();
            if (index >= 0) {
                chatView.setContactList(contactlist);
            }
        }
    }

    public String [] getContactList() {return contactlist;}

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public static void main(String[] args) throws IOException {
        Controller controller = new Controller();
    }
}
