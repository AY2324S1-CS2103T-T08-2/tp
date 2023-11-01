package networkbook.model;

import static networkbook.logic.commands.CommandTestUtil.VALID_COURSE_BOB;
import static networkbook.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static networkbook.testutil.Assert.assertThrows;
import static networkbook.testutil.Assert.assertThrowsAssertionError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networkbook.model.person.Person;
import networkbook.testutil.PersonBuilder;
import networkbook.testutil.TypicalPersons;

public class NetworkBookTest {

    private final NetworkBook networkBook = new NetworkBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), networkBook.getPersonList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> networkBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyNetworkBook_replacesData() {
        NetworkBook newData = TypicalPersons.getTypicalNetworkBook();
        networkBook.resetData(newData);
        assertEquals(newData, networkBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsAssertionError() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE)
                .withCourses(List.of(VALID_COURSE_BOB))
                .withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(TypicalPersons.ALICE, editedAlice);
        NetworkBookStub newData = new NetworkBookStub(newPersons);

        assertThrowsAssertionError(() -> networkBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> networkBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInNetworkBook_returnsFalse() {
        assertFalse(networkBook.hasPerson(TypicalPersons.ALICE));
    }

    @Test
    public void hasPerson_personInNetworkBook_returnsTrue() {
        networkBook.addPerson(TypicalPersons.ALICE);
        assertTrue(networkBook.hasPerson(TypicalPersons.ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInNetworkBook_returnsTrue() {
        networkBook.addPerson(TypicalPersons.ALICE);
        Person editedAlice = new PersonBuilder(TypicalPersons.ALICE)
                .withCourses(List.of(VALID_COURSE_BOB))
                .withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(networkBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> networkBook.getPersonList().remove(0));
    }

    @Test
    public void isSame_sameNetworkBook_returnTrue() {
        NetworkBook testBook = new NetworkBook();
        assertTrue(testBook.isSame(testBook));
    }
    @Test
    public void isSame_anotherEmptyNetworkBook_returnFalse() {
        assertFalse(new NetworkBook().isSame(new NetworkBook()));
    }

    @Test
    public void toStringMethod() {
        String expected = NetworkBook.class.getCanonicalName() + "{persons=" + networkBook.getPersonList() + "}";
        assertEquals(expected, networkBook.toString());
    }

    /**
     * A stub ReadOnlyNetworkBook whose persons list can violate interface constraints.
     */
    private static class NetworkBookStub implements ReadOnlyNetworkBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        NetworkBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }
    }

}
