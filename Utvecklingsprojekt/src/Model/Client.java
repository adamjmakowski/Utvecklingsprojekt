package Model;

import View.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Client {

    private Socket socket;
    private int port;
    private String ipAddress;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private Contacts contacts;
    private ArrayList<User> receiverList = new ArrayList<>();
    private ChatView chatView;
    private ContactListView contactListView;
    private File attachment;

    private boolean isRunning;


    public Client(String ipAddress, int port) {
        System.out.println("Client started");

        this.port = port;
        this.ipAddress = ipAddress;
        try {
            socket = new Socket(ipAddress, port); //där den ska kopplar upp sig
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Listener().start();//--> körs med egen tråd (Listener ärver Thread)


    }


    public void handleAttachment(){
        System.out.println(attachment);
        System.out.println("attached successfully");
    }

    public void handleMessage() throws IOException {
        sendMessage(getMessageFromView());
        for (int i = 0; i < receiverList.size(); i++) {
            receiverList.remove(i);
        }
    }

    public void handleContact() {
        contactListView.setVisible(true);
    }

    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Do something when add button is clicked
            int index = contactListView.getSelectedContactIndex();
            User u = contacts.getContactAt(index);
            contacts.setSavedContact(u);
            System.out.println("test");

        }
    }

    private class DeleteButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Do something when delete button is clicked
            int index = contactListView.getSelectedContactIndex();
            if (index >= 0) {
                contacts.removeContact(index);
            }
        }
    }

    public File getAttachment() {
        return attachment;
    }

    public void setAttachment(File attachment) {
        this.attachment = attachment;
    }

   /* public void buttonPressed(ButtonType button) throws IOException {
        switch (button) {

          case ButtonType.Disconnect:
                contacts.writeContactsToFile("files/contacts.dat");
                disconnect();
                break;

           case ButtonType.ContactsSend://välja vilka Kontakter som du att skicka till.
                updateReceiverList();
                break;

        }
    }
*/


   /* private void disconnect() {
        isRunning = false;
        User user = getUserFromView();
        user.setConnected(false);
        sendUserToServer(user);
        gui.resetGUI();
        gui.getTaChatbox().setText("");
        gui.getTaChatbox().append("Du har lämnat chatten\n");
    }
*/

    private Message getMessageFromView() {
        Message message = null;
        String text = chatView.getMessageText();
        ImageIcon icon = chatView.getMessageImage();
        User user = getUserFromView();
        if(receiverList.isEmpty()) {
            message = new Message(user, contacts.getOnlineContacts(), text, icon);
        } else {
            ArrayList<User> newReceiverList = new ArrayList<>(receiverList);
            message = new Message(user, newReceiverList, text, icon);

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


    public void sendMessage(Message message) throws IOException {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
        }
    }

    private void updateReceiverList() {
        int index = contactListView.getSelectedContactIndex();
        if(index > -1) {//välja index från lisatn-- vem ska kontakta!
            User receiver = contacts.getContactAt(index);
            //gui.setTextForLabel(receiver.getUsername());
            receiverList.add(receiver);
        } else {//No selected index in the contacts list, keep the saved contact at the selected index (samma senaste kontaktad)
            int contactIndex = contactListView.getSelectedContactIndex();
            User receiver = contacts.getSavedContactAt(contactIndex);
            //  gui.setTextForLabel(receiver.getUsername());
            receiverList.add(receiver);
        }
    }
    //Denna metod används för att fylla kontaktlistan i GUI med användarnamnen.
// Där tar vi en ArrayList av User-objekt och returnerar en array av strängar som innehåller användarnamn.
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
    private class Listener extends Thread {
        @Override
        public void run() { // waiting for massages from server's broadcastMessage()
            System.out.println("Listener run starting...");
            Object obj;
            try {
                while ((obj = ois.readObject()) != null) {
                    if (obj instanceof User) {
                        if (((User) obj).isConnected()) {
                            contacts.setOnlineContact((User) obj);
                            //chatView.displayUser(GUIUtilities.createUserLabel(((User) obj).getImage(), ((User) obj).getUsername()));
                        } else {
                            contacts.removeOnlineContact((User) obj);
                           // chatView.removeUser(GUIUtilities.createUserLabel(((User) obj).getImage(), ((User) obj).getUsername()));
                        }
                    } else if (obj instanceof Message) {
                        Message message = (Message) obj;
                        System.out.println(message.getText());
                        if (message.getImage() == null) {
                            //chatView.getTaChatbox().append(message.getSender().getUsername() + ": " + message.getText() + '(' + message.getDelivered() + ')' + "\n");
                        } else {
                            //chatView.displayImage(message.getSender().getUsername(), message.getText(), message.getImage());
                        }
                    }
                }//while
            } catch(IOException e){
                e.printStackTrace();
            } catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }//run()

    }//Listener
}//Controller.MessageClient
