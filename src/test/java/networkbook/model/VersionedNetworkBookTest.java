package networkbook.model;

import static networkbook.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import networkbook.testutil.TypicalPersons;

public class VersionedNetworkBookTest {
    private final NetworkBook networkBook = new NetworkBook();
    @Test
    public void constructor_noArguments() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook();

        assertNotNull(versionedNetworkBook);
        ArrayList<NetworkBook> expected = new ArrayList<>();
        expected.add(new NetworkBook());
        assertEquals(expected, versionedNetworkBook.getNetworkBookStateList());
        assertEquals(0, versionedNetworkBook.getCurrentStatePointer());
    }

    @Test
    public void constructor_withNetworkBook() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook(networkBook);

        assertNotNull(versionedNetworkBook);
        ArrayList<NetworkBook> expected = new ArrayList<>();
        expected.add(networkBook);
        assertEquals(expected, versionedNetworkBook.getNetworkBookStateList());
        assertEquals(0, versionedNetworkBook.getCurrentStatePointer());
    }

    @Test
    public void commit() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook();
        versionedNetworkBook.addPerson(TypicalPersons.ALICE);
        versionedNetworkBook.commit();
        assertEquals(2, versionedNetworkBook.getNetworkBookStateList().size());
        NetworkBook expected = new NetworkBook();
        expected.addPerson(TypicalPersons.ALICE);
        assertEquals(expected, versionedNetworkBook.getNetworkBookStateList().get(1));
        assertEquals(1, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.setFilterPredicate(PREDICATE_SHOW_ALL_PERSONS);
        versionedNetworkBook.commit();
        assertEquals(2, versionedNetworkBook.getCurrentStatePointer());
        assertEquals(versionedNetworkBook.getNetworkBookStateList().get(1),
                    versionedNetworkBook.getNetworkBookStateList().get(2));
    }

    @Test
    public void undo() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook();
        versionedNetworkBook.addPerson(TypicalPersons.ALICE);
        versionedNetworkBook.commit();
        versionedNetworkBook.addPerson(TypicalPersons.BENSON);
        versionedNetworkBook.commit();
        NetworkBook expected = new NetworkBook();
        expected.addPerson(TypicalPersons.ALICE);
        expected.addPerson(TypicalPersons.BENSON);
        assertEquals(expected.getPersonList(), versionedNetworkBook.getPersonList());
        versionedNetworkBook.undo();
        assertEquals(1, versionedNetworkBook.getCurrentStatePointer());
        expected.removePerson(TypicalPersons.BENSON);
        assertEquals(expected.getPersonList(), versionedNetworkBook.getPersonList());
    }

    @Test
    public void redo() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook();
        versionedNetworkBook.addPerson(TypicalPersons.ALICE);
        versionedNetworkBook.commit();
        versionedNetworkBook.addPerson(TypicalPersons.BENSON);
        versionedNetworkBook.commit();
        NetworkBook expected = new NetworkBook();
        expected.addPerson(TypicalPersons.ALICE);
        expected.addPerson(TypicalPersons.BENSON);
        assertEquals(expected.getPersonList(), versionedNetworkBook.getPersonList());
        versionedNetworkBook.undo();
        assertEquals(1, versionedNetworkBook.getCurrentStatePointer());
        expected.removePerson(TypicalPersons.BENSON);
        assertEquals(expected.getPersonList(), versionedNetworkBook.getPersonList());
        versionedNetworkBook.redo();
        expected.addPerson(TypicalPersons.BENSON);
        assertEquals(2, versionedNetworkBook.getCurrentStatePointer());
        assertEquals(expected.getPersonList(), versionedNetworkBook.getPersonList());
    }

    @Test
    public void canUndo() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook();
        assertFalse(versionedNetworkBook.canUndo());
        versionedNetworkBook.commit();
        assertTrue(versionedNetworkBook.canUndo());
        versionedNetworkBook.undo();
        assertFalse(versionedNetworkBook.canUndo());
    }

    @Test
    public void canRedo() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook();
        versionedNetworkBook.commit();
        assertFalse(versionedNetworkBook.canRedo());
        versionedNetworkBook.undo();
        assertTrue(versionedNetworkBook.canRedo());
        versionedNetworkBook.redo();
        assertFalse(versionedNetworkBook.canRedo());
    }

    @Test
    public void getNetworkBookStateList() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook();
        versionedNetworkBook.addPerson(TypicalPersons.ALICE);
        versionedNetworkBook.commit();
        ArrayList<NetworkBook> actual = versionedNetworkBook.getNetworkBookStateList();
        ArrayList<NetworkBook> expected = new ArrayList<>();
        NetworkBook networkBookWithAlice = new NetworkBook();
        networkBookWithAlice.addPerson(TypicalPersons.ALICE);
        expected.add(new NetworkBook());
        expected.add(networkBookWithAlice);
    }

    @Test
    public void getCurrentStatePointer() {
        VersionedNetworkBook versionedNetworkBook = new VersionedNetworkBook();
        assertEquals(0, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.commit();
        assertEquals(1, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.commit();
        assertEquals(2, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.commit();
        assertEquals(3, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.undo();
        assertEquals(2, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.undo();
        assertEquals(1, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.undo();
        assertEquals(0, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.redo();
        assertEquals(1, versionedNetworkBook.getCurrentStatePointer());
        versionedNetworkBook.commit();
        assertEquals(2, versionedNetworkBook.getCurrentStatePointer());
    }
}
