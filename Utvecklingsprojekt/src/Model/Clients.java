package Model;

import java.util.HashMap;

//här vi sparar varje client i en map och koppla det med en key(en user)
// för att kunna söka/hitta den lätt när vi klickar på användarnamn i listan.
public class Clients {

    private HashMap<User, MessageServer.ClientHandler> clients;

    public Clients() {
        this.clients = new HashMap<>();
    }
//The "put" method adds a new client to the collection by putting a new key-value pair into the HashMap
    public synchronized void put(User user, MessageServer.ClientHandler client) {
        clients.put(user,client);
    }
//The "get" method retrieves the Server.Model.ClientHandler object associated with a particular Model.User key
    public synchronized MessageServer.ClientHandler get(User user) {
        return clients.get(user);
    }
//The "containsKey" method returns a boolean indicating whether the HashMap contains a particular Model.User key.
    public synchronized boolean containsKey(User user) {
        return clients.containsKey(user);
    }
//The "remove" method removes a client from the collection by removing the corresponding key-value pair from the HashMap.
    public synchronized void remove(User user) {
        clients.remove(user);
    }

}
