import org.example.model.ContactsManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConactsAppTests {
    @Test
    public void testCreationAndSave() {
        ContactsManager cm = new ContactsManager();
        String testFileName = "Testfile_" + System.currentTimeMillis() + ".txt";
        cm.addBusinessContact("James", "010-0000-0000", "XM");
        cm.addPersonalContact("Matthew", "010-0000-0001", "Bro");
        ContactsManager.FileIoStatus is = cm.saveContactList(testFileName);
        assertSame(ContactsManager.FileIoStatus.FILE_IO_SUCCESS, is);
        cm.clearCurrentContactList();
        is = cm.readFromFile(testFileName);
        assertSame(ContactsManager.FileIoStatus.FILE_IO_SUCCESS, is);
        assertTrue(cm.getBusinessContactsByName("James").getFirst().getPhoneNumber().equals("010-0000-0000") &&
                cm.getPersonalContactsByName("Matthew").getFirst().getRelationship().equals("Bro"));
    }
}
