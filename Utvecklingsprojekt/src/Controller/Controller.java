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
    private final MessageView messageView;
    private final MessageClient messageClient;
    private final MessageServer messageServer;
    private String [] contactlist = {"Contact 1", "Contact 2", "Contact 3"};

    public Controller() throws IOException, ClassNotFoundException {
        // Create views
        chatView = new ChatView(new ImageIcon("Images/test.png"), new ImageIcon("Images/test.png"));
        contactListView = new ContactListView(contactlist);
        messageView = new MessageView();

        // Create message client and server
        messageServer = new MessageServer(3343);
        messageClient = new MessageClient("127.0.0.1", 3343, "username");

        // Add listeners to views
        contactListView.addAddButtonListener(new AddButtonListener());
        contactListView.addDeleteButtonListener(new DeleteButtonListener());
        chatView.addSendListener(new SendListener());
        chatView.addAttachListener(new AttachListener());


        // Set initial contact list
        contactListView.setContactList(contactlist);

        // Add contact list to chat view
        chatView.setContactList(contactlist);

        // Add message view to chat view
        chatView.setMessageView(messageView);

        // Show initial view
        chatView.setVisible(true);
    }

    private class SendListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Do something when send button is clicked
            messageView.addMessage(chatView.getMessageText());
            chatView.clearMessageText();
        }
    }

    public class AttachListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Do something when attach button is clicked
            System.out.println("Attach button clicked");
            File selectedFile = chatView.showFileOpenDialog();
            System.out.println("Selected file: " + selectedFile);
            if (selectedFile != null) {
                messageView.addAttachment(selectedFile.getName());
            }
        }
    }


    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Do something when add button is clicked


        }
    }

    public class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Do something when delete button is clicked
            int index = contactListView.getSelectedContactIndex();
            if (index >= 0) {

                chatView.setContactList(contactlist);
            }
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Controller controller = new Controller();
    }
}
