package org.example.view;

import org.example.controller.ContactsController;
import org.example.controller.IContactsController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.IOException;

public class ContactsView {
    private final JFrame mFrame;
    private final JTextField mSearchBox;
    private final JPanel mPanel;
    private final JRadioButton mSearchByName;
    private final JRadioButton mSearchByPhoneNumber;
    private final JList<ListViewItem> mContactList;
    private final JTextArea mContactInfo;
    private final JButton mDeleteButton;
    private final JButton mAddButton;
    private final JLabel mCurrentFileStatusLabel;
    private final JLabel mContactListLabel;
    private final JLabel mContactInfoLabel;
    private final JLabel mSearchByLabel;
    private final JLabel mSearchForLabel;
    private final JButton mImportJsonFile;
    private final JButton mSaveToJsonFile;
    private final ButtonGroup mSearchByGroup;
    private IContactsController mController;

    public static class ListViewItem {
        private final String mUniqueId;
        private final String mName;
        private final String mPhoneNo;

        public ListViewItem(String uniqueId, String name, String phone) {
            mUniqueId = uniqueId;
            mName = name;
            mPhoneNo = phone;
        }

        @Override
        public String toString() {
            return "Name: " + getName() + ", Phone Number: " + getPhoneNo();
        }

        public String getUniqueId() {
            return mUniqueId;
        }

        public String getName() {
            return mName;
        }

        public String getPhoneNo() {
            return mPhoneNo;
        }
    }

    public ContactsView() {
        mFrame = new JFrame("Contacts App (Oreumi Week 3 Project)");

        mPanel = new JPanel();

        mCurrentFileStatusLabel = new JLabel();
        mCurrentFileStatusLabel.setText("A new contact");

        mSearchForLabel = new JLabel();
        mSearchForLabel.setText("Search for: ");

        mSearchBox = new JTextField();

        mSearchByLabel = new JLabel();
        mSearchByLabel.setText("Search By:");

        mSearchByName = new JRadioButton();
        mSearchByName.setText("Name");

        mSearchByPhoneNumber = new JRadioButton();
        mSearchByPhoneNumber.setText("Phone Number");

        mSearchByGroup = new ButtonGroup();

        mContactListLabel = new JLabel();
        mContactListLabel.setText("Contact List: ");

        mContactList = new JList<>();

        mContactInfoLabel = new JLabel();
        mContactInfoLabel.setText("Contact Info:");

        mContactInfo = new JTextArea();

        mDeleteButton = new JButton();
        mDeleteButton.setText("Delete");

        mAddButton = new JButton();
        mAddButton.setText("Add new contact");

        mSaveToJsonFile = new JButton();
        mSaveToJsonFile.setText("Save current contact list");

        mImportJsonFile = new JButton();
        mImportJsonFile.setText("Open saved contact file");

        mController = new IContactsController() {
            @Override
            public void onFileSelected(String filePath) {
            }

            @Override
            public void onFileSaveSelected(String filePath) {
            }

            public void onItemDeleted(String name) {
            }

            @Override
            public void onSearch(String query, int type) {
            }

            @Override
            public void onItemSelected(String selectedItem) {
            }

            @Override
            public void onItemAdded(String name, String phoneNo, int type, String additionalInfo) {
            }
        };
    }

    public void setController(IContactsController cc) {
        mController = cc;
    }

    public void setFileNameDisplay(String name) {
        mCurrentFileStatusLabel.setText(name);
    }

    private void searchBoxEntered() {
        if (mSearchByName.isSelected()) {
            mController.onSearch(mSearchBox.getText(), 0);
        } else {
            mController.onSearch(mSearchBox.getText(), 1);
        }
    }

    /**
     * Configure the components, pack the window, and make it visible!
     * Must be invoked on the Swing Event Dispatcher Thread!!!
     */
    private void createUI() {
        mFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        mPanel.setLayout(new GridBagLayout());
        mFrame.setContentPane(mPanel);

        GridBagConstraints gbc;

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mPanel.add(mCurrentFileStatusLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        mPanel.add(mSearchForLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mPanel.add(mSearchBox, gbc);
        mSearchBox.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchBoxEntered();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                searchBoxEntered();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Seriously, why does this even exist?
            }
        });

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        mPanel.add(mSearchByLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        mPanel.add(mSearchByName, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.weightx = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        mPanel.add(mSearchByPhoneNumber, gbc);

        mSearchByGroup.add(mSearchByName);
        mSearchByGroup.add(mSearchByPhoneNumber);
        mSearchByName.setSelected(true);

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mPanel.add(mContactList, gbc);

        mContactList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mContactList.addListSelectionListener(x -> {
            if (mContactList.isSelectionEmpty()) {
                mContactInfo.setText("");
            } else {
                mController.onItemSelected(mContactList.getSelectedValue().getUniqueId());
            }
        });

        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.BOTH;
        mPanel.add(mContactInfo, gbc);
        mContactInfo.setRows(10);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        mPanel.add(mContactListLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        mPanel.add(mContactInfoLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mPanel.add(mDeleteButton, gbc);
        mDeleteButton.addActionListener(e -> {
            if (!mContactList.isSelectionEmpty()) {
                mController.onItemDeleted(mContactList.getSelectedValue().getUniqueId());
            }
        });

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 4;
        gbc.weightx = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mPanel.add(mAddButton, gbc);

        mAddButton.addActionListener(e -> new AddContactsDialog(mFrame, mController::onItemAdded));

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mPanel.add(mImportJsonFile, gbc);
        mImportJsonFile.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            jfc.addChoosableFileFilter(new FileNameExtensionFilter("JSON File", ".json"));
            if (jfc.showOpenDialog(mFrame) == JFileChooser.APPROVE_OPTION) {
                mController.onFileSelected(jfc.getSelectedFile().getAbsolutePath());
            }
        });

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mPanel.add(mSaveToJsonFile, gbc);
        mSaveToJsonFile.addActionListener(e -> {
            JFileChooser jfc = new JFileChooser();
            jfc.addChoosableFileFilter(new FileNameExtensionFilter("JSON File", ".json"));
            if (jfc.showSaveDialog(mFrame) == JFileChooser.APPROVE_OPTION) {
                mController.onFileSaveSelected(jfc.getSelectedFile().getAbsolutePath());
            }
        });

        mFrame.setMinimumSize(new Dimension(700, 700));
        mFrame.pack();
        mFrame.setVisible(true);
    }

    public void setListModel(ListModel<ListViewItem> model) {
        mContactList.setModel(model);
    }

    public void setContactInfo(String info) {
        mContactInfo.setText(info);
    }

    public void createAndShowGUI() {
        SwingUtilities.invokeLater(this::createUI);
    }
}