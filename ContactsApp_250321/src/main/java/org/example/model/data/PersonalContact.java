package org.example.model.data;

public class PersonalContact extends Contact {
    private final String mRelationship;

    public PersonalContact(String name, String phoneNumber, String relationship) {
        super(name, phoneNumber);
        mRelationship = relationship;
    }

    public String getRelationship() {
        return mRelationship;
    }
}
