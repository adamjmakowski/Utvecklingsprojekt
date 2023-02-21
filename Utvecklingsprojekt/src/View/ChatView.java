package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class ChatView extends JFrame{
    private JFrame frame;
    private JList<String> userlist;
    private JList<String> contactlist;
    private JTextArea messageTextArea;
    private JButton sendButton;
    private JButton attachButton;
    private ImageIcon userImage;
    private ImageIcon attachImage;
    private JScrollPane messageScrollPane;

    public ChatView(ImageIcon userImage, ImageIcon attachImage) {
        this.userImage = userImage;
        this.attachImage = attachImage;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        // Set up frame
        frame = new JFrame("Chat Room");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        // Create main panel and its layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create top panel and add user image and userlist
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel userLabel = new JLabel(userImage);
        userLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(userLabel, BorderLayout.WEST);

        userlist = new JList<>();
        JScrollPane userlistScrollPane = new JScrollPane(userlist);
        topPanel.add(userlistScrollPane, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Create center panel and add contactlist and messageScrollPane
        JPanel centerPanel = new JPanel(new BorderLayout());
        contactlist = new JList<>();
        JScrollPane contactlistScrollPane = new JScrollPane(contactlist);
        centerPanel.add(contactlistScrollPane, BorderLayout.WEST);

        messageTextArea = new JTextArea(5, 30);
        messageScrollPane = new JScrollPane(messageTextArea);
        centerPanel.add(messageScrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Create bottom panel and add sendButton and attachButton
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        attachButton = new JButton("Attach");
        attachButton.setToolTipText("Attach a file");
        bottomPanel.add(attachButton);


        sendButton = new JButton("Send");
        bottomPanel.add(sendButton);

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



    public void setUserlist(String[] usernames) {
        userlist.setListData(usernames);
    }

    public void setContactlist(String[] usernames) {
        contactlist.setListData(usernames);
    }

    public <Message> void addMessage(Message message) {
        messageTextArea.append(message.toString() + "\n");
        messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum());
    }

    public JList<String> getContactlist() {
        return contactlist;
    }

    public void setContactList(Object contactList) {
    }

    public void setMessageView(MessageView messageView) {
    }
}
