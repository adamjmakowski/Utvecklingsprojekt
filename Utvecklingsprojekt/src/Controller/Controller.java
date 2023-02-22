package Controller;

import View.*;
import Model.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Controller {

    private final ChatView chatView;
    private final ContactListView contactListView;
    private final MessageClient messageClient;
    private String [] contactlist = {"Contact 1", "Contact 2", "Contact 3"};
    private File attachment;
    private String Message;
    private String userName;

    public Controller() throws IOException, ClassNotFoundException {
        // Create views
        chatView = new ChatView(new ImageIcon("Images/test.png"), new ImageIcon("Images/test.png"), this);
        contactListView = new ContactListView(contactlist);

        // Create message client and server
        userName = chatView.usernameInput();
        messageClient = new MessageClient("127.0.0.1", 3343, userName);

        // Add listeners to views
        contactListView.addAddButtonListener(new AddButtonListener());
        contactListView.addDeleteButtonListener(new DeleteButtonListener());


        // Set initial contact list
        contactListView.setContactList(contactlist);

        // Add contact list to chat view
        chatView.setContactList(contactlist);

        // Show initial view
        chatView.setVisible(true);
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

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Controller controller = new Controller();
    }
}
