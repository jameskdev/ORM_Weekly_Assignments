package org.example.controller;

public interface IContactsController {
    public void onFileSelected(String filePath);
    public void onFileSaveSelected(String filePath);
    public void onSearch(String query, int type);
    public void onItemSelected(String selectedItem);
    public void onItemAdded(String name, String phoneNo, int type, String additionalInfo);
    public void onItemDeleted(String name);

}