package networkbook.logic.commands.delete;

import static networkbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static networkbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static networkbook.logic.commands.CommandTestUtil.showPersonAtIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.UserPrefs;
import networkbook.model.person.Person;
import networkbook.testutil.TypicalIndexes;
import networkbook.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeletePersonCommandTest {

    private Model model = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getDisplayedPersonList().get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased());
        DeletePersonCommand deletePersonCommand = new DeletePersonCommand(TypicalIndexes.INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeletePersonCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete))
                + String.format(DeletePersonCommand.MESSAGE_DELETE_PERSON_INDEX, 1);

        ModelManager expectedModel = new ModelManager(model.getNetworkBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deletePersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getDisplayedPersonList().size() + 1);
        DeletePersonCommand deletePersonCommand = new DeletePersonCommand(outOfBoundIndex);

        assertCommandFailure(deletePersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, TypicalIndexes.INDEX_FIRST_PERSON);

        Person personToDelete = model.getDisplayedPersonList().get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased());
        DeletePersonCommand deletePersonCommand = new DeletePersonCommand(TypicalIndexes.INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeletePersonCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete))
                + String.format(DeletePersonCommand.MESSAGE_DELETE_PERSON_INDEX, 1);

        Model expectedModel = new ModelManager(model.getNetworkBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deletePersonCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, TypicalIndexes.INDEX_FIRST_PERSON);

        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of network book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getNetworkBook().getPersonList().size());

        DeletePersonCommand deletePersonCommand = new DeletePersonCommand(outOfBoundIndex);

        assertCommandFailure(deletePersonCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeletePersonCommand deleteFirstCommand = new DeletePersonCommand(TypicalIndexes.INDEX_FIRST_PERSON);
        DeletePersonCommand deleteSecondCommand = new DeletePersonCommand(TypicalIndexes.INDEX_SECOND_PERSON);

        // same object -> returns true
        assertEquals(deleteFirstCommand, deleteFirstCommand);

        // same values -> returns true
        DeletePersonCommand deleteFirstCommandCopy = new DeletePersonCommand(TypicalIndexes.INDEX_FIRST_PERSON);
        assertEquals(deleteFirstCommand, deleteFirstCommandCopy);

        // different types -> returns false
        assertNotEquals(deleteFirstCommand, 1);

        // null -> returns false
        assertNotEquals(deleteFirstCommand, null);

        // different person -> returns false
        assertNotEquals(deleteFirstCommand, deleteSecondCommand);

        // both have null index -> returns true
        DeletePersonCommand deleteNull = new DeletePersonCommand(null);
        DeletePersonCommand deleteNull2 = new DeletePersonCommand(null);
        assertEquals(deleteNull, deleteNull2);

        // one has null index -> returns false
        assertNotEquals(deleteNull, deleteFirstCommand);
        assertNotEquals(deleteFirstCommand, deleteNull);
    }
    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeletePersonCommand deletePersonCommand = new DeletePersonCommand(targetIndex);
        String expected = DeletePersonCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deletePersonCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateDisplayedPersonList(p -> false, null);

        assertTrue(model.getDisplayedPersonList().isEmpty());
    }
}
