package org.example.model;

import com.google.gson.*;
import org.example.model.data.BusinessContact;
import org.example.model.data.Contact;
import org.example.model.data.PersonalContact;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ContactsManager {
    private final List<Contact> mContactList;
    private final Object mListWriteLock = new Object();

    public enum FileIoStatus {
        FILE_IO_SUCCESS,
        FILE_CANNOT_READ,
        FILE_CANNOT_WRITE,
        FILE_IS_EMPTY,
        FILE_OTHER_IO_ERROR
    }

    public ContactsManager() {
        mContactList = new ArrayList<>();
    }

    public boolean addBusinessContact(String name, String phoneNumber, String companyName) {
        synchronized (mListWriteLock) {
            return mContactList.add(new BusinessContact(name, phoneNumber, companyName));
        }
    }

    public boolean addPersonalContact(String name, String phoneNumber, String relationship) {
        synchronized (mListWriteLock) {
            return mContactList.add(new PersonalContact(name, phoneNumber, relationship));
        }
    }

    public List<Contact> getContactsContainingName(String name) {
        if (name.isEmpty()) {
            return Collections.unmodifiableList(mContactList);
        }
        return mContactList.stream().filter(x -> x.getName().contains(name)).toList();
    }

    public List<Contact> getContactsContainingPhoneNo(String phoneNo) {
        if (phoneNo.isEmpty()) {
            return Collections.unmodifiableList(mContactList);
        }
        return mContactList.stream().filter(x -> x.getPhoneNumber().contains(phoneNo)).toList();
    }

    public List<BusinessContact> getBusinessContactsByName(String name) {
        return mContactList.stream().
                filter(x -> (x.getName().equals(name)) && (x instanceof BusinessContact)).
                map(x -> (BusinessContact) x).
                toList();
    }

    public List<PersonalContact> getPersonalContactsByName(String name) {
        return mContactList.stream().
                filter(x -> (x.getName().equals(name)) && (x instanceof PersonalContact)).
                map(x -> (PersonalContact) x).
                toList();
    }

    public List<BusinessContact> getBusinessContactsByPhoneNo(String phoneNo) {
        return mContactList.stream().
                filter(x -> (x.getPhoneNumber().equals(phoneNo)) && (x instanceof BusinessContact)).
                map(x -> (BusinessContact) x).
                toList();
    }

    public List<PersonalContact> getPersonalContactsByPhoneNo(String phoneNo) {
        return mContactList.stream().
                filter(x -> (x.getPhoneNumber().equals(phoneNo)) && (x instanceof PersonalContact)).
                map(x -> (PersonalContact) x).
                toList();
    }

    public void clearCurrentContactList() {
        synchronized (mListWriteLock) {
            mContactList.clear();
        }
    }

    public FileIoStatus readFromFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (!file.canRead()) {
                return FileIoStatus.FILE_CANNOT_READ;
            } else if (!file.canWrite()) {
                return FileIoStatus.FILE_CANNOT_WRITE;
            }
        } else {
            return FileIoStatus.FILE_CANNOT_READ;
        }

        List<Contact> readList = new ArrayList<>();
        try (BufferedReader bis = new BufferedReader(new FileReader(file))) {
            JsonArray ja = JsonParser.parseReader(bis).getAsJsonArray();
            ja.forEach(ele -> {
                JsonObject curr = ele.getAsJsonObject();
                if (curr.has("mName") && curr.has("mPhoneNumber")) {
                    if (curr.has("mCompanyName")) {
                        readList.add(new BusinessContact(curr.get("mName").getAsString(), curr.get("mPhoneNumber").getAsString(), curr.get("mCompanyName").getAsString()));
                    } else if (curr.has("mRelationship")) {
                        readList.add(new PersonalContact(curr.get("mName").getAsString(), curr.get("mPhoneNumber").getAsString(), curr.get("mRelationship").getAsString()));
                    }
                }
            });
        } catch (FileNotFoundException ex1) {
            return FileIoStatus.FILE_CANNOT_READ;
        } catch (IOException ex2) {
            return FileIoStatus.FILE_OTHER_IO_ERROR;
        }
        if (!readList.isEmpty()) {
            synchronized (mListWriteLock) {
                mContactList.clear();
                mContactList.addAll(readList);
            }
        } else {
            return FileIoStatus.FILE_IS_EMPTY;
        }
        return FileIoStatus.FILE_IO_SUCCESS;
    }

    public Optional<Contact> getContactById(String uid) {
        for (Contact c: mContactList) {
            if (c.getUniqueId().equalsIgnoreCase(uid)) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    public void deleteItem(String name) {
        synchronized (mListWriteLock) {
            int index = -1;
            for (int i = 0; i < mContactList.size(); i++) {
                if (mContactList.get(i).getUniqueId().equalsIgnoreCase(name)) {
                    index = i;
                }
            }
            if (index > -1) {
                mContactList.remove(index);
            }
        }
    }

    public FileIoStatus saveContactList(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (!file.canRead()) {
                return FileIoStatus.FILE_CANNOT_READ;
            } else if (!file.canWrite()) {
                return FileIoStatus.FILE_CANNOT_WRITE;
            }
        }

        JsonArray ja = new JsonArray();
        synchronized (mListWriteLock) { // While this doesn't really modify the data, a lock is used in order to ensure data consistency.
            mContactList.forEach(x -> {
                JsonObject item = new JsonObject();
                if (x instanceof BusinessContact xb) {
                    item.addProperty("mName", xb.getName());
                    item.addProperty("mPhoneNumber", xb.getPhoneNumber());
                    item.addProperty("mCompanyName", xb.getCompanyName());
                } else if (x instanceof PersonalContact xp) {
                    item.addProperty("mName", xp.getName());
                    item.addProperty("mPhoneNumber", xp.getPhoneNumber());
                    item.addProperty("mRelationship", xp.getRelationship());
                }
                ja.add(item);
            });
        }
        try (FileOutputStream fos = new FileOutputStream(file)) {
            byte[] bb = ja.toString().getBytes(StandardCharsets.UTF_8);
            fos.write(bb);
        } catch (FileNotFoundException ex) {
            return FileIoStatus.FILE_CANNOT_WRITE;
        } catch (IOException iex) {
            return FileIoStatus.FILE_OTHER_IO_ERROR;
        }
        return FileIoStatus.FILE_IO_SUCCESS;
    }
}
