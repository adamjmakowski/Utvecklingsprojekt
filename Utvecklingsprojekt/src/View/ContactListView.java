package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ContactListView extends JFrame {
    private JPanel panel;
    private JList<String> contactList;
    private JButton addButton;
    private JButton deleteButton;

    public ContactListView(String[] contacts) {
        setTitle("Contacts");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);

        // Create panel and its layout
        panel = new JPanel(new BorderLayout());

        // Create contact list and its scroll pane
        contactList = new JList<>(contacts);
        JScrollPane contactScrollPane = new JScrollPane(contactList);

        // Add contact scroll pane to panel
        panel.add(contactScrollPane, BorderLayout.CENTER);

        // Create add button and add it to panel
        addButton = new JButton("Add");
        panel.add(addButton, BorderLayout.WEST);

        // Create delete button and add it to panel
        deleteButton = new JButton("Delete");
        panel.add(deleteButton, BorderLayout.EAST);

        // Add panel to frame
        add(panel);
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void addDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void setContactList(String[] contacts) {
        contactList.setListData(contacts);
    }

    public int getSelectedContactIndex() {
        return contactList.getSelectedIndex();
    }

}
