package Model;

import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class MessageClient {

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String name;


    public MessageClient(String ipAddress, int port, String name) throws IOException {

        try {
            System.out.println("Client started");

            this.socket = new Socket(ipAddress, port); //där den ska kopplar upp sig
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.name = name;
            new Listener().start();//--> körs med egen tråd (Listener ärver Thread)
            sendMessage();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendMessage() throws IOException {
        try {
            oos.writeObject(name);
            oos.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                oos.writeObject(name + ": " + messageToSend);
                oos.flush();
            }
        } catch (IOException e) {
        }
    }

    private class Listener extends Thread {

        public void run() { // waiting for massages from server's broadcastMessage()

            String messages;
            System.out.println("Listener run starting...");

            while (socket.isConnected()) {

                try {

                    messages = (String) ois.readObject();
                    System.out.println(messages);

                } catch (Exception e) {
                    System.out.println(e);
                }
            }//while

        }//run()

    }//Listener

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username!");
        String username =  scanner.nextLine();
        MessageClient messageClient = new MessageClient("127.0.0.1", 3343, username);

    }//main
}//MessageClient