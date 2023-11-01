package networkbook.model;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networkbook.commons.core.GuiSettings;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.person.NameContainsKeywordsPredicate;
import networkbook.model.person.Person;
import networkbook.model.person.PersonSortComparator;
import networkbook.model.person.PersonSortComparator.SortField;
import networkbook.model.person.PersonSortComparator.SortOrder;
import networkbook.testutil.NetworkBookBuilder;
import networkbook.testutil.TypicalPersons;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new NetworkBook(), new NetworkBook(modelManager.getNetworkBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setNetworkBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setNetworkBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setNetworkBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setNetworkBookFilePath(null));
    }

    @Test
    public void setNetworkBookFilePath_validPath_setsNetworkBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setNetworkBookFilePath(path);
        assertEquals(path, modelManager.getNetworkBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInNetworkBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(TypicalPersons.ALICE));
    }

    @Test
    public void hasPerson_personInNetworkBook_returnsTrue() {
        modelManager.addPerson(TypicalPersons.ALICE);
        assertTrue(modelManager.hasPerson(TypicalPersons.ALICE));
    }

    @Test
    public void undoNetworkBook_noPreviousState_throwsIllegalStateChangeException() {
        ModelManager modelManager = new ModelManager();
        assertThrows(CommandException.class, () -> modelManager.undoNetworkBook());
    }

    @Test
    public void undoNetworkBook_hasPreviousState_success() {
        ModelManager modelManager = new ModelManager();
        modelManager.addPerson(TypicalPersons.ALICE);
        try {
            modelManager.undoNetworkBook();
        } catch (CommandException e) {
            fail();
        }
        assertEquals(0, modelManager.getNetworkBook().getPersonList().size());
    }

    @Test
    public void redoNetworkBook_noNextState_throwsIllegalStateChangeException() {
        ModelManager modelManager = new ModelManager();
        assertThrows(CommandException.class, () -> modelManager.redoNetworkBook());
    }

    @Test
    public void redoNetworkBook_hasNextState_success() {
        ModelManager modelManager = new ModelManager();
        modelManager.addPerson(TypicalPersons.ALICE);
        try {
            modelManager.undoNetworkBook();
            modelManager.redoNetworkBook();
        } catch (CommandException e) {
            fail();
        }
        assertEquals(1, modelManager.getNetworkBook().getPersonList().size());
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void equals() {
        NetworkBook networkBook = new NetworkBookBuilder()
                .withPerson(TypicalPersons.ALICE)
                .withPerson(TypicalPersons.BENSON)
                .build();
        NetworkBook differentNetworkBook = new NetworkBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(networkBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(networkBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different networkBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentNetworkBook, userPrefs)));

        // different filter -> returns false
        String[] keywords = TypicalPersons.ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(networkBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        modelManager.updateSortedPersonList(PersonSortComparator.EMPTY_COMPARATOR);

        // different sort -> returns false
        modelManager.updateSortedPersonList(new PersonSortComparator(SortField.NAME, SortOrder.DESCENDING));
        assertFalse(modelManager.equals(new ModelManager(networkBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        modelManager.updateSortedPersonList(PersonSortComparator.EMPTY_COMPARATOR);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setNetworkBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(networkBook, differentUserPrefs)));
    }

    @Test
    public void updateSortedPersonList_descendingNameSort_correctlySorted() {
        PersonSortComparator comparator = new PersonSortComparator(SortField.NAME, SortOrder.DESCENDING);
        Model model = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        model.updateSortedPersonList(comparator);
        List<Person> expectedPersons = TypicalPersons.getTypicalPersons();
        Collections.reverse(expectedPersons);
        ObservableList<Person> expectedList = FXCollections.observableList(expectedPersons);
        assertEquals(
                expectedList,
                model.getFilteredPersonList()
        );
    }

}
