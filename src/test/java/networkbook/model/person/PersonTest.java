package networkbook.model.person;

import static networkbook.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import networkbook.testutil.PersonBuilder;
import networkbook.testutil.TypicalPersons;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSame() {
        // same object -> returns true
        assertTrue(TypicalPersons.ALICE.isSame(TypicalPersons.ALICE));

        // null -> returns false
        assertFalse(TypicalPersons.ALICE.isSame(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(TypicalPersons.ALICE.isSame(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalPersons.ALICE.isSame(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(TypicalPersons.BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(TypicalPersons.BOB.isSame(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(TypicalPersons.BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(TypicalPersons.BOB.isSame(editedBob));
    }

    @Test
    public void getValue_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, TypicalPersons.ALICE::getValue);
    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(TypicalPersons.ALICE).build();
        assertTrue(TypicalPersons.ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(TypicalPersons.ALICE.equals(TypicalPersons.ALICE));

        // null -> returns false
        assertFalse(TypicalPersons.ALICE.equals(null));

        // different type -> returns false
        assertFalse(TypicalPersons.ALICE.equals(5));

        // different person -> returns false
        assertFalse(TypicalPersons.ALICE.equals(TypicalPersons.BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(TypicalPersons.ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(TypicalPersons.ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(TypicalPersons.ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(TypicalPersons.ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(TypicalPersons.ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(TypicalPersons.ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + TypicalPersons.ALICE.getName()
                + ", phone=" + TypicalPersons.ALICE.getPhone() + ", email=" + TypicalPersons.ALICE.getEmails()
                + ", address=" + TypicalPersons.ALICE.getAddress() + ", tags=" + TypicalPersons.ALICE.getTags() + "}";
        assertEquals(expected, TypicalPersons.ALICE.toString());
    }
}