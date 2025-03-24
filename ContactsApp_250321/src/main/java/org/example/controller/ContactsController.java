package org.example.controller;

import org.example.model.ContactsManager;
import org.example.model.data.BusinessContact;
import org.example.model.data.Contact;
import org.example.model.data.PersonalContact;
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

    public void onFileSaveSelected(String filePath) {
        SwingWorker<ContactsManager.FileIoStatus, Void> mSaveFile = new SwingWorker<>() {
            @Override
            protected ContactsManager.FileIoStatus doInBackground() throws Exception {
                return mContactsManager.saveContactList(filePath);
            }

            @Override
            protected void done() {
                try {
                    if (get() == ContactsManager.FileIoStatus.FILE_IO_SUCCESS) {
                        dataUpdated();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        mSaveFile.execute();
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
                try {
                    if (get() == ContactsManager.FileIoStatus.FILE_IO_SUCCESS) {
                        dataUpdated();
                        mView.setFileNameDisplay(filePath);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        mSaveFile.execute();
    }

    public void dataUpdated() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> {
                mView.setContactInfo("");
                mListModel.clear();
                for (Contact c: mContactsManager.getContactsContainingName("")) {
                    mListModel.addElement(new ContactsView.ListViewItem(c.getUniqueId(), c.getName(), c.getPhoneNumber()));
                }
            });
        } else {
            mView.setContactInfo("");
            mListModel.clear();
            for (Contact c: mContactsManager.getContactsContainingName("")) {
                mListModel.addElement(new ContactsView.ListViewItem(c.getUniqueId(), c.getName(), c.getPhoneNumber()));
            }
        }
    }

    @Override
    public void onItemDeleted(String query) {
        mContactsManager.deleteItem(query);
        dataUpdated();
    }

    @Override
    public void onSearch(String query, int type) {
        if (type == 0) {
            mView.setContactInfo("");
            mListModel.clear();
            for (Contact c: mContactsManager.getContactsContainingName(query)) {
                mListModel.addElement(new ContactsView.ListViewItem(c.getUniqueId(), c.getName(), c.getPhoneNumber()));
            }
        } else if (type == 1) {
            mView.setContactInfo("");
            mListModel.clear();
            for (Contact c: mContactsManager.getContactsContainingPhoneNo(query)) {
                mListModel.addElement(new ContactsView.ListViewItem(c.getUniqueId(), c.getName(), c.getPhoneNumber()));
            }
        }
    }

    @Override
    public void onItemSelected(String selectedItem) {
        mContactsManager.getContactById(selectedItem).ifPresentOrElse(x -> {
                if (x instanceof BusinessContact xb) {
                    mView.setContactInfo("Contact Type: Business \n" +
                            "Name: " + xb.getName() + "\n" +
                            "Phone Number: " + xb.getPhoneNumber() + "\n" +
                            "Company Name: " + xb.getCompanyName());
                } else if (x instanceof PersonalContact xp) {
                    mView.setContactInfo("Contact Type: Personal \n" +
                            "Name: " + xp.getName() + "\n" +
                            "Phone Number: " + xp.getPhoneNumber() + "\n" +
                            "Relationship: " + xp.getRelationship());
                }
        }, () -> { mView.setContactInfo("Error reading contact!"); });

    }

    @Override
    public void onItemAdded(String name, String phoneNo, int type, String additionalInfo) {
        if (type == 0) {
            mContactsManager.addBusinessContact(name, phoneNo, additionalInfo);
        } else {
            mContactsManager.addPersonalContact(name, phoneNo, additionalInfo);
        }
        dataUpdated();
    }
}
