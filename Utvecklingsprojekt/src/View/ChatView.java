package View;
import Controller.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class ChatView extends JFrame{
    private JFrame frame;
    private JList<String> chatbox;
    private JList<String> contactlist;
    private JTextArea messageTextArea;
    private JButton sendButton;
    private JButton attachButton;
    private JButton contactListButton;
    private ImageIcon userImage;
    private ImageIcon attachImage;
    private JScrollPane messageScrollPane;
    private Controller controller;
    private JFrame userName;

    public ChatView(ImageIcon userImage, ImageIcon attachImage, Controller controller) {
        this.userImage = userImage;
        this.attachImage = attachImage;
        this.controller = controller;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // Set up frame
        frame = new JFrame("Chat Room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create main panel and its layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create top panel and add contacts and chatbox
        JPanel topPanel = new JPanel(new BorderLayout());

        contactlist = new JList<>();
        JScrollPane contactlistScrollPane = new JScrollPane(contactlist);
        topPanel.add(contactlistScrollPane, BorderLayout.WEST);

        chatbox = new JList<>();
        JScrollPane userlistScrollPane = new JScrollPane(chatbox);
        topPanel.add(userlistScrollPane, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create center panel and add user image and messageScrollPane
        JPanel centerPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel(userImage);
        userLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.add(userLabel, BorderLayout.WEST);

        messageTextArea = new JTextArea(5, 5);
        messageScrollPane = new JScrollPane(messageTextArea);
        centerPanel.add(messageScrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create bottom panel and add sendButton and attachButton
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        attachButton = new JButton("Attach");
        attachButton.setToolTipText("Attach a file");
        bottomPanel.add(attachButton);
        attachButtonListener();

        sendButton = new JButton("Send");
        bottomPanel.add(sendButton);
        sendButtonListener();

        contactListButton = new JButton("Contacts");
        bottomPanel.add(contactListButton);
        contactButtonListener();

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.setVisible(true);
    }


    public void addContactListListener(MouseListener listener) {
        contactlist.addMouseListener(listener);
    }

    public void addSendListener(ActionListener listener) {
        sendButton.addActionListener(listener);
    }

    public JButton getAttachButton() {
        return attachButton;
    }

    public void addAttachListener(ActionListener listener) {
        attachButton.addActionListener(listener);
    }



    public String getMessageText() {
        return messageTextArea.getText();
    }

    public void clearMessageText() {
        messageTextArea.setText("");
    }

    public File showFileOpenDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile;
        }
        return null;
    }

    public void attachButtonListener(){
        attachButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File selectedFile = showFileOpenDialog();
                if (selectedFile != null) {
                    controller.setAttachment(selectedFile);
                    controller.handleAttachment();

                }
            }
        });
    }

    public void contactButtonListener(){
        contactListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.handleContact();
            }
        });
    }

    public String usernameInput(){
        userName = new JFrame();
        String Name =JOptionPane.showInputDialog(userName,"Enter Username:");
        return Name;
    }

    public void sendButtonListener(){
        sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                controller.setMessage(messageTextArea.getText());
                try {
                    controller.handleMessage();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }




    public <Message> void addMessage(Message message) {
        messageTextArea.append(message.toString() + "\n");
        messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum());
    }

    public JList<String> getContactlist() {
        return contactlist;
    }

    public void setContactList(String[] usernames) {
        contactlist.setListData(usernames);
    }


}


