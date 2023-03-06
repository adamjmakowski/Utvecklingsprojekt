package View;
import Controller.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class ChatView extends JFrame{
    private JFrame frame;
    private JList<String> chatbox;
    private JList<String> contactlist;
    private JTextArea messageInputTextArea;
    private JTextArea messageOutputTextArea;
    private JButton sendButton;
    private JButton attachButton;
    private JButton contactListButton;
    private JButton deleteButton;
    private ImageIcon userImage;
    private ImageIcon attachImage;
    private JScrollPane messageScrollPanel;
    private JScrollPane chatBoxScrollPanel;
    private JScrollPane contactScrollPanel;
    private Controller controller;

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
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null); // center the frame on the screen

        // Create main panel and its layout
        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        // Create top-left panel and add contacts
        JPanel topLeftPanel = new JPanel(new BorderLayout());
        gbc.weightx = 1.0;
        gbc.weighty = 0.8; // changed from 0.5
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        mainPanel.add(topLeftPanel, gbc);

        contactlist = new JList<>();
        JScrollPane contactlistScrollPane = new JScrollPane(contactlist);
        topLeftPanel.add(contactlistScrollPane, BorderLayout.CENTER);

        // Create top-right panel and add chatbox
        JPanel topRightPanel = new JPanel(new BorderLayout());
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        mainPanel.add(topRightPanel, gbc);

        messageOutputTextArea = new JTextArea(10, 40);
        messageOutputTextArea.setEditable(false); // disable editing
        chatBoxScrollPanel = new JScrollPane(messageOutputTextArea);
        topRightPanel.add(chatBoxScrollPanel, BorderLayout.CENTER);

        // Create bottom-left panel and add user image
        JPanel bottomLeftPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel(userImage);
        userLabel.setIcon(userImage);

        userLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.2; // changed from 0.5
        mainPanel.add(bottomLeftPanel, gbc);
        bottomLeftPanel.add(userLabel, BorderLayout.CENTER);

        // Create bottom-right panel and add messageScrollPane
        JPanel bottomRightPanel = new JPanel(new BorderLayout());
        messageInputTextArea = new JTextArea(5, 5);
        messageScrollPanel = new JScrollPane(messageInputTextArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        mainPanel.add(bottomRightPanel, gbc);
        bottomRightPanel.add(messageScrollPanel, BorderLayout.CENTER);

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

        deleteButton = new JButton("Delete");
        bottomPanel.add(deleteButton);
        deleteButtonListener();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.weighty = 0.0;
        gbc.anchor = GridBagConstraints.WEST; // align button to the left
        mainPanel.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0; // changed from 0.5
        mainPanel.add(bottomPanel, gbc);

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
        return messageInputTextArea.getText();
    }

    public void clearMessageText() {
        messageInputTextArea.setText("");
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

    public void deleteButtonListener() {
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int index = contactlist.getSelectedIndex();
                if (index >= 0) {
                    //contactlist.removeSelectedItem(index);
                }
            }
        });
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
        return JOptionPane.showInputDialog(frame,"Enter Username:");
    }


    public void sendButtonListener(){
        sendButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    if(!Objects.equals(messageInputTextArea.getText(), "")) {
                        messageOutputTextArea.append(messageInputTextArea.getText() + "\n");
                        messageOutputTextArea.setCaretPosition(messageOutputTextArea.getDocument().getLength());

                        controller.setMessage(messageInputTextArea.getText());
                        addMessage(messageInputTextArea.getText());
                        messageInputTextArea.setText("");

                        controller.handleMessage();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }




    public <Message> void addMessage(Message message) {
        messageInputTextArea.append(message.toString() + "\n");
        chatBoxScrollPanel.getVerticalScrollBar().setValue(chatBoxScrollPanel.getVerticalScrollBar().getMaximum());
    }

    public JList<String> getContactlist() {
        return contactlist;
    }

    public void setContactList(String[] usernames) {
        contactlist.setListData(usernames);
    }


}


