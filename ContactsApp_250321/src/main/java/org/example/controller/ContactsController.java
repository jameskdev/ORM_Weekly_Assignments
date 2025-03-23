package org.example.controller;

import org.example.model.ContactsManager;
import org.example.view.ContactsView;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class ContactsController implements  IContactsController{
    private final ContactsManager mContactsManager;
    private final DefaultListModel<ContactsView.ListViewItem> mListModel;
    private final ContactsView mView;

    public ContactsController(ContactsManager manager, ContactsView view) {
        mContactsManager = manager;
        mView = view;
        mListModel = new DefaultListModel<>();
        mView.setListModel(mListModel);
    }

    @Override
    public void onFileSelected(String filePath) {
        SwingWorker<ContactsManager.FileIoStatus, Void> mSaveFile = new SwingWorker<>() {
            @Override
            protected ContactsManager.FileIoStatus doInBackground() throws Exception {
                return mContactsManager.readFromFile(filePath);
            }

            @Override
            protected void done() {
                dataUpdated();
            }
        };
    }

    public void dataUpdated() {
        if (SwingUtilities.isEventDispatchThread()) {

        } else {

        }
    }

    @Override
    public void onSearch(String query, int type) {
        mContactsManager.getContactsContainingName(query);
    }

    @Override
    public void onItemSelected(String selectedItem) {

    }

    @Override
    public void onItemAdded(String name, String phoneNo, int type, String additionalInfo) {
        if (type == 0) {
            mContactsManager.addBusinessContact(name, phoneNo, additionalInfo);
        } else {
            mContactsManager.addPersonalContact(name, phoneNo, additionalInfo);
        }
    }
}
