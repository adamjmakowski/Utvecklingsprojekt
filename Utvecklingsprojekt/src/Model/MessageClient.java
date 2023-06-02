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
    private String userName;


    public MessageClient(String ipAddress, int port, String name) throws IOException {

        try {
            System.out.println("Client started");

            this.socket = new Socket(ipAddress, port); //där den ska kopplar upp sig
            this.oos = new ObjectOutputStream(socket.getOutputStream());
            this.ois = new ObjectInputStream(socket.getInputStream());
            this.userName = name;
            new Listener().start();//--> körs med egen tråd (Listener ärver Thread)
            new Sender().start();//--> körs med egen tråd

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void sendMessage(String message) throws IOException {
        oos.writeObject(userName + ": " + message);
        oos.flush();
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

    private class Sender extends Thread {
        public void run() {
            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()) {
                try {
                    String message = scanner.nextLine();
                    sendMessage(message);
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    //main
}//MessageClient


