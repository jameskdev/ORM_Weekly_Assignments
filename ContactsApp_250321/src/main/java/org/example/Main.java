package org.example;

import org.example.controller.ContactsController;
import org.example.model.ContactsManager;
import org.example.view.ContactsView;

public class Main {
    public static void main(String[] args) {
        ContactsManager cm = new ContactsManager();
        ContactsView cv = new ContactsView();
        ContactsController cc = new ContactsController(cm, cv);
        cv.setController(cc);
        cv.createAndShowGUI();
    }
}