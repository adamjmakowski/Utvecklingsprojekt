package View;

import Controller.ClientController;
import Controller.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ContactsView extends JPanel {
    private JList<String> onlineUserList;
    private JList<String> savedContactsList;
    private JButton btnAdd;
    private JButton btnSend;
    private JButton btnDel;
    private int width = 500;
    private int height = 500;
    private JPanel pnlMain;
    private ClientController client;
    private JScrollPane onlineUsersPane;
    private JScrollPane savedContactsPane;
    private JFrame frame;

    public ContactsView(ClientController client) {
        initializeMainPanel();
        this.client = client;
    }

    public void createFrame() {
        JFrame frame = new JFrame();
        frame.setSize(new Dimension(width, height));
        frame.setTitle("Contacts");
        frame.setContentPane(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void initializeMainPanel() {
        setLayout(new BorderLayout());
        initializePanels();
        setLayouts();
        createMainPanel();
        addButtonListeners();
    }

    private void initializePanels() {
        pnlMain = new JPanel();
    }

    private void setLayouts() {
        pnlMain.setLayout(new GridBagLayout());
    }

    private void createMainPanel() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Online users
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        onlineUserList = new JList<>();
        onlineUserList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        onlineUsersPane = new JScrollPane(onlineUserList);
        onlineUsersPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        onlineUsersPane.setBorder(new TitledBorder("Online users"));
        pnlMain.add(onlineUsersPane, gbc);

        // Saved contacts
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        savedContactsList = new JList<>();
        savedContactsPane = new JScrollPane(savedContactsList);
        savedContactsPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        savedContactsPane.setBorder(new TitledBorder("Your contacts"));
        pnlMain.add(savedContactsPane, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        btnAdd = new JButton("Add contact");
        btnSend = new JButton("Send to");
        btnDel = new JButton("Delete contact");

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnSend);
        buttonsPanel.add(btnDel);
        pnlMain.add(buttonsPanel, gbc);

        add(pnlMain, BorderLayout.CENTER);
        addListListener();
    }

    public void setOnlineUserList(String[] userName) {
        onlineUserList.setListData(userName);
    }

    public void setSavedContactList(String[] username) {
        savedContactsList.setListData(username);
    }

    private void addButtonListeners() {
        ActionListener listener = new addButtonListener();
        btnAdd.addActionListener(listener);
        btnSend.addActionListener(listener);
        btnDel.addActionListener(listener);
    }

    public int getListIndex() {
        return onlineUserList.getSelectedIndex();
    }

    public int getContactListIndex() {
        return savedContactsList.getSelectedIndex();
    }

    private void addListListener() {
        onlineUserList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int index = onlineUserList.getSelectedIndex();
                if (!e.getValueIsAdjusting() && index > -1) {
                    client.contactListIndicesChanged(index);
                }
            }
        });
    }

    private class addButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == btnAdd) {
                client.buttonPressed(ButtonType.ContactsAdd);
            } else if (e.getSource() == btnSend) {
                client.buttonPressed(ButtonType.ContactsSend);
            } else if (e.getSource() == btnDel) {
                client.buttonPressed(ButtonType.ContactsDelete);
            }
        }
    }
}