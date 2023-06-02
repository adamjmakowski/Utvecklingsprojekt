package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ContactListView extends JFrame {
    private JPanel panel;
    private JList<String> contactList;
    private JButton addButton;

    public ContactListView(String[] contacts) {
        setTitle("Contacts");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);

        // Create panel and its layout
        panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create contact list and its scroll pane
        contactList = new JList<>(contacts);
        contactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane contactScrollPane = new JScrollPane(contactList);

        // Add contact scroll pane to panel
        panel.add(contactScrollPane, BorderLayout.CENTER);

        // Create add button and add it to panel
        addButton = new JButton("Add");
        addButton.setPreferredSize(new Dimension(100, 30));
        JPanel addButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        addButtonPanel.add(addButton);
        panel.add(addButtonPanel, BorderLayout.SOUTH);

        // Add panel to frame
        add(panel);
    }

    public void addAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void setContactList(String[] contacts) {
        contactList.setListData(contacts);
    }

    public int getSelectedContactIndex() {
        return contactList.getSelectedIndex();
    }
}
