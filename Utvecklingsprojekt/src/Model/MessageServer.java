package Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Denna klass är en flertrådad Server som implements {@link Runnable där varje uppkopplad klient ska hanteras av en egen tråd.
 * För att kunna upprätthålla uppkopplingar med klienter och samtidigt lyssna på nya uppkopplingar måste dessa två processer parallelliseras med hjälp av trådar.
 * Därför att denna klass har en ClientHandler, som är en inre klass som körs med egna tråd genom att ärva Thread.
 */
public class MessageServer implements Runnable {
    private int port;
    private Clients clients;
    private UnsentMessages unsent;
    private ArrayList<User> users;
    private Logger logger;
    private ConcurrentHashMap<String, ArrayList<Message>> unsentMessages = new ConcurrentHashMap<>();
    private boolean done;

    public MessageServer(int port)  {
        this.port = port;
        clients = new Clients();
        users = new ArrayList<>();
        unsent = new UnsentMessages();
        logger = new Logger();
        logger.CreateLoggerGUI();
        new Thread(this).start();
    }

    /**
     * en run()-metod där den väntar på uppkopplingar av klienter (serverSocket.accept()).
     * Vid uppkoppling av en klient skapas en instans av en ClientHandler som hanterar
     * uppkopplingen med just den klienten. Detta sker genom att ServerSocket med
     * förbindelsen används som parameter när en ny ClientHandler skapas
     */
    @Override
    public void run() {
        System.out.println("Server started");
        System.out.println("Waiting for connection...");
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//run()

    public void broadcastUser(User user) throws IOException {
        for (User u: users) {
            clients.get(u).getOos().writeObject(user);
        }
    }

    public void removeUser(User user){
        clients.remove(user);
        users.remove(user);
    }

    /**
     * En inre klass som ärver {@link Thread
     * I den inre klassen lyssnar servern på inkommande uppkopplingar från klienter (serverSocket.accept())
     */

    public class ClientHandler extends Thread {
        private Socket socket;
        private User user; //den användare som just kopplat upp sig
        private ObjectInputStream ois;
        private ObjectOutputStream oos;

        /**
         * En Konstruktor som har ServerSocket som parameter och skapar respektive Input- och OutputStream.
         * @param socket en instansvariabel av {@link Socket för att hämta ut streams från servern
         * @throws IOException
         */

        public ClientHandler(Socket socket) throws IOException {
            System.out.println("A new Client connected");
            this.socket = socket;
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());//Vid uppkoppling skapas ObjectInputStream som kopplas till server-sockets InputStream
        }

        /**
         * En run()-metod som skriver ut meddelanden genom oos.writeObject.
         * klienten ska vara uppkopplad mot servern genom hela exekveringen av programmet.
         */
        @Override
        public void run() { //väntar för meddelande från den uppkopplade client--> skickar den till andra clients.
            System.out.println("ClientHandler run starting...");
            try {
                user = (User) ois.readObject();
                broadcastUser(user);
                checkNewMessages(user);
                unsent.clear();
                clients.put(user, this);
                users.add(user);
                for (User u : users) {
                    oos.writeObject(u);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //Om objektet är ett användarobjekt behandlas det som ett inloggnings- eller utloggningsmeddelande,
            // och metoden removeUser eller broadcastUser anropas efter behov.
            // Om objektet är ett meddelandeobjekt levereras det till alla mottagare med metoden deliveryMessage.
            try {
                //servern måste hela tiden kunna koppla nya Clients så därför behöver hela tiden ha den här loopen för att kunna läsa user-obejct
                while (true){
                    Object  obj = ois.readObject();
                    if (obj instanceof User) {
                        if (((User) obj).isConnected()) {
                            logger.LogConnect(((User) obj).getUsername());
                            clients.put((User) obj, this);
                            users.add((User) obj);
                            for (User u : users) {
                                oos.writeObject(u);
                                oos.flush();
                            }
                        } else {
                            removeUser((User) obj);
                            broadcastUser((User) obj);
                            break;
                        }
                    } else if (obj instanceof Message) {
                        ((Message) obj).setDelivered();
                        deliverMessage((Message) obj);
                    }
                }//while-loopen
                close();
            } catch (IOException | ClassNotFoundException e) {
                close();

            }
            System.out.println("Klient nerkopplad");
            logger.LogDisconnect(user.getUsername());
        }//run()

        private void close() {
            try {
                if (oos != null) {
                    oos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(ois != null) {
                    ois.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if(socket != null) {
                    socket.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Check om det finns olevererade meddelanden till ett användarnamn och levererar dem om de finns!
        private void checkNewMessages(User user) {
            ArrayList<Message> messages = unsent.get(user.getUsername());

            if (messages != null) {
                for (Message message : messages) {
                    try {
                        Message msg = new Message(message.getSender(), message.getReceivers(),"Oläst meddelande : " + message.getText(), message.getImage());
                        msg.setDelivered();
                        oos.writeObject(msg);
                        oos.flush();
                        logger.LogMessage(message); //tips! ta ut den från loopen
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //Levererar meddelande till mottagaren.
        //Om mottagaren är offline sparas meddelandet tills att mottagaren bli online igen!
        private void deliverMessage(Message message) {
            for (User receiver : message.getReceivers()) {
                for (User u : users) {
                    if (u.equals(receiver)) {
                        try {
                            clients.get(u).getOos().writeObject(message);
                            message.setReceived();
                            logger.LogMessage(message);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        unsent.put(receiver.getUsername(), message);
                    }
                }
            }
        }

        public ObjectOutputStream getOos() {
            return oos;
        }


    }//ClientHandler

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        MessageServer messageServer = new MessageServer(1234);
        //System.out.println(InetAddress.getLocalHost().getHostAddress());


    }
}