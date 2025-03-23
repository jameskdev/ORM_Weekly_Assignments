package org.example.model.data;

public class BusinessContact extends Contact {
    private final String mCompanyName;

    public BusinessContact(String name, String phoneNumber, String companyName) {
        super(name, phoneNumber);
        mCompanyName = companyName;
    }

    public String getCompanyName() {
        return mCompanyName;
    }
}
