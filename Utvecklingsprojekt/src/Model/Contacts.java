package Model;

import java.io.*;
import java.util.ArrayList;

//Denna klass används av Client.
//Klienten lägger till en uppkopplad användare till Kontakter.
//Klassen sparar Kontakter på hårddisken då klienten avslutas.
//Mottagare kan väljas dels från sparade kontakter och dels från uppkopplade användare.

public class Contacts {//that store the user's online and saved contacts, respectively.
    private ArrayList<User> onlineContacts;
    private ArrayList<User> savedContacts;

    public Contacts() {
        onlineContacts = new ArrayList<>();
        savedContacts = new ArrayList<>();
    }
    //adds a user to the online contacts list if they are not already in the list.
    public void setOnlineContact(User user) {
        if(!onlineContacts.contains(user)) {
            onlineContacts.add(user);
        }
    }//adds a user to the saved contacts list if they are not already in the list.
    public void setSavedContact(User user) {
        if(!savedContacts.contains(user)) {
            savedContacts.add(user);
        }
    }//removes a user from the online contacts list.
    // It first creates a temporary list to hold the contacts to be removed and then iterates over the online contacts list to find the user. If the user is found, it is added to the temporary list. Finally, all the contacts in the temporary list are removed from the online contacts list.
    public void removeOnlineContact(User user) {
        ArrayList<User> contactsToRemove = new ArrayList<>();
        for (User u : onlineContacts) {
            if (u.equals(user)) {
                contactsToRemove.add(u);
            }
        }
        onlineContacts.removeAll(contactsToRemove);
    }//removes the contact at the specified index from the saved contacts list.
    public void removeContact(int index) {
        savedContacts.remove(index);
    }
    //writes the saved contacts list to a file with the specified filename.
    public void writeContactsToFile(String filename) {
        try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            out.writeInt(savedContacts.size());
            for (User u: savedContacts) {
                out.writeObject(u);
            }
            out.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } ;
    }//reads the saved contacts list from a file with the specified filename.
    public void readContactsFromFile(String filename) {
        File file = new File(filename);
        if(file.length() > 0) {
            try {
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
                int n = in.readInt();
                while(n > 0) {
                    setSavedContact((User)in.readObject());
                    n--;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }//returns the list of online contacts.
    public ArrayList<User> getOnlineContacts() {
        return onlineContacts;
    }
    //returns the list of saved contacts.
    public ArrayList<User> getSavedContacts() {
        return savedContacts;
    }
//returns the user at the specified index in the online contacts list.
    public User getContactAt(int index) {
        return onlineContacts.get(index);
    }
//returns the user at the specified index in the saved contacts list.
    public User getSavedContactAt(int index) {
        return savedContacts.get(index);
    }
}
