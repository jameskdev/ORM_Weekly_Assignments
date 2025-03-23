package org.example.view;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddContactsDialog extends JDialog {
    private JPanel mDialogPanel;
    private JButton mOKButton;
    private JButton mCancelButton;
    private JPanel mDialogButtonPanel;
    private JPanel mDialogItemPanel;
    private JTextField mNameField;
    private JTextField mPhoneNumberField;
    private JPanel mRadioGroupPanel;
    private ButtonGroup mSaveAsGroup;
    private JRadioButton mSaveAsBusinessContact;
    private JRadioButton mSaveAsPersonalContact;
    private JLabel mNameLabel;
    private JLabel mPhoneNumberLabel;
    private JLabel mAdditionalInfoLabel;
    private JTextField mAdditionalInfoField;
    private ContactDialogAddCallback mCallback;

    public interface ContactDialogAddCallback {
        void onAdded(String name, String phoneNo, int type, String additionalInfo);
    }

    public AddContactsDialog(Frame owner, ContactDialogAddCallback cb) {
        super(owner, "Add a new contact");
        mDialogPanel = new JPanel();
        mDialogItemPanel = new JPanel();
        mNameField = new JTextField();
        mNameLabel = new JLabel();
        mPhoneNumberLabel = new JLabel();
        mPhoneNumberField = new JTextField();
        mRadioGroupPanel = new JPanel();
        mSaveAsGroup = new ButtonGroup();
        mSaveAsBusinessContact = new JRadioButton();
        mSaveAsPersonalContact = new JRadioButton();
        mAdditionalInfoLabel = new JLabel();
        mAdditionalInfoField = new JTextField();
        mDialogButtonPanel = new JPanel();
        mCancelButton = new JButton();
        mOKButton = new JButton();

        createDialogUI();
    }

    private void onOK() {
        int addType = 0;
        if (mSaveAsPersonalContact.isSelected()) {
            addType = 1;
        }
        mCallback.onAdded(mNameField.getText(), mPhoneNumberField.getText(), addType, mAdditionalInfoField.getText());
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    /**
     * Configure the UI, and make it visible
     */
    private void createDialogUI() {
        setContentPane(mDialogPanel);

        mDialogPanel.setLayout(new GridBagLayout());
        mDialogItemPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mDialogPanel.add(mDialogItemPanel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mDialogItemPanel.add(mNameField, gbc);

        mNameLabel.setText("Name: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        mDialogItemPanel.add(mNameLabel, gbc);

        mPhoneNumberLabel.setText("Phone : ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        mDialogItemPanel.add(mPhoneNumberLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mDialogItemPanel.add(mPhoneNumberField, gbc);

        mRadioGroupPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        mDialogItemPanel.add(mRadioGroupPanel, gbc);

        mSaveAsBusinessContact.setText("Business");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        mRadioGroupPanel.add(mSaveAsBusinessContact, gbc);

        mSaveAsPersonalContact.setText("Personal");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        mRadioGroupPanel.add(mSaveAsPersonalContact, gbc);

        mSaveAsGroup.add(mSaveAsBusinessContact);
        mSaveAsGroup.add(mSaveAsPersonalContact);
        mSaveAsBusinessContact.setSelected(true);

        mSaveAsBusinessContact.addActionListener(e -> mAdditionalInfoLabel.setText("Company Information: "));
        mSaveAsPersonalContact.addActionListener(e -> mAdditionalInfoLabel.setText("Relationship Information: "));

        mAdditionalInfoLabel.setText("Company Information:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        mDialogItemPanel.add(mAdditionalInfoLabel, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mDialogItemPanel.add(mAdditionalInfoField, gbc);

        mDialogButtonPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        mDialogPanel.add(mDialogButtonPanel, gbc);

        mCancelButton.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mDialogButtonPanel.add(mCancelButton, gbc);

        mOKButton.setText("OK");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mDialogButtonPanel.add(mOKButton, gbc);

        setModalityType(ModalityType.APPLICATION_MODAL);
        getRootPane().setDefaultButton(mOKButton);

        mOKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        mCancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // Cancel when the X button is clicked.
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        setMinimumSize(new Dimension(500, 300));
        pack();
        setVisible(true);
    }
}