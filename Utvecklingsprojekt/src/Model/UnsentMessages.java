package Model;

import Model.Message;

import java.util.ArrayList;
import java.util.HashMap;

/*
 * Lagrar meddelanden i en Hashmap som inte kan levereras pga mottagare inte är online.
 */
public class UnsentMessages {
    private HashMap<String, ArrayList<Message>> unsent = new HashMap<>();

    /*
     * Lägger till användarnamn och meddelande i hashmapen
     */
    public synchronized void put(String username, Message message) {
        if(get(username) == null) {
            unsent.put(username, new ArrayList<Message>());
        }
        get(username).add(message);
    }

    public synchronized void clear() {
        unsent.clear();
    }

    /*
     * Hämtar ArrayList med meddelanden som väntar på att bli levererade till given användare
     * Inparameter String
     */
    public synchronized ArrayList<Message> get(String username) {
        return unsent.get(username);
    }
}
