package View;

import javax.swing.*;
import java.awt.*;
import Controller.*;

public class MessageView extends JFrame {
    private JPanel panel;
    private JTextArea messageArea;
    private JScrollPane messageScrollPane;
    private JList<String> messageList;
    JPanel messagePanel = new JPanel();


    public MessageView() {
        setTitle("Message View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        // Create panel and its layout
        panel = new JPanel(new BorderLayout());

        // Create message area and its scroll pane
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageScrollPane = new JScrollPane(messageArea);

        // Add message scroll pane to panel
        panel.add(messageScrollPane, BorderLayout.CENTER);

        // Add panel to frame
        add(panel);
    }

    public void addMessage(String message) {
        messageArea.append(message + "\n");
        messageScrollPane.getVerticalScrollBar().setValue(messageScrollPane.getVerticalScrollBar().getMaximum());
    }

    public void clearMessageArea() {
        messageArea.setText("");
    }

    public static void main(String[] args) {
        MessageView view = new MessageView();
        view.setVisible(true);
    }
    public void setMessageList(String[] messages) {
        messageList.setListData(messages);
    }

    public void deleteMessage(int index) {
        DefaultListModel<String> model = (DefaultListModel<String>) messageList.getModel();
        model.removeElementAt(index);
    }

    public int getSelectedMessageIndex() {
        return messageList.getSelectedIndex();
    }

    public void addAttachment(String name) {
        JLabel attachmentLabel = new JLabel(name);
        attachmentLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        messagePanel.add(attachmentLabel);
        messagePanel.revalidate();
        messagePanel.repaint();
    }


    /*public void setRecipient(String recipient) {
        // Set recipient label
        recipientLabel.setText("To: " + recipient);
    }

    public void addDeleteButtonListener(Controller.DeleteButtonListener deleteButtonListener) {
        deleteButton.addActionListener(deleteButtonListener);
    } */

}
