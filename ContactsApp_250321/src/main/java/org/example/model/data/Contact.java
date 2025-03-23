package org.example.model.data;

public class Contact {
    private final String mName;
    private final String mPhoneNumber;
    private final String mUniqueId;

    public Contact(String name, String phoneNumber) {
        mName = name;
        mPhoneNumber = phoneNumber;
        mUniqueId = String.valueOf(System.currentTimeMillis() + Math.random() * 100000);
    }

    public String getName() {
        return mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public String getUniqueId() { return mUniqueId; }
}
