package networkbook.logic.commands;

import static networkbook.logic.commands.CommandTestUtil.assertCommandFailure;
import static networkbook.logic.commands.CommandTestUtil.assertCommandSuccess;
import static networkbook.testutil.Assert.assertThrowsAssertionError;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import networkbook.commons.core.GuiSettings;
import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.ReadOnlyNetworkBook;
import networkbook.model.ReadOnlyUserPrefs;
import networkbook.model.UserPrefs;
import networkbook.model.person.Link;
import networkbook.model.person.Person;
import networkbook.testutil.TypicalIndexes;
import networkbook.testutil.TypicalPersons;

public class OpenLinkCommandTest {
    private static final Model MODEL = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());

    private static final Model modelStubThrowingIoExceptionForOpeningLink = new Model() {
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new UnsupportedOperationException();
        }

        public ReadOnlyUserPrefs getUserPrefs() {
            throw new UnsupportedOperationException();
        }

        public GuiSettings getGuiSettings() {
            throw new UnsupportedOperationException();
        }

        public void setGuiSettings(GuiSettings guiSettings) {
            throw new UnsupportedOperationException();
        }

        public Path getNetworkBookFilePath() {
            throw new UnsupportedOperationException();
        }

        public void setNetworkBookFilePath(Path networkBookFilePath) {
            throw new UnsupportedOperationException();
        }

        public ReadOnlyNetworkBook getNetworkBook() {
            return TypicalPersons.getTypicalNetworkBook();
        }

        public void setNetworkBook(ReadOnlyNetworkBook networkBook) {
            throw new UnsupportedOperationException();
        }

        public boolean hasPerson(Person person) {
            throw new UnsupportedOperationException();
        }

        public void deletePerson(Person target) {
            throw new UnsupportedOperationException();
        }

        public void addPerson(Person person) {
            throw new UnsupportedOperationException();
        }

        public void setItem(Person target, Person edited) {
            throw new UnsupportedOperationException();
        }

        public void undoNetworkBook() throws CommandException {
            throw new UnsupportedOperationException();
        }

        public void redoNetworkBook() throws CommandException {
            throw new UnsupportedOperationException();
        }

        public boolean isValidLinkIndex(Index personIndex, Index linkIndex) {
            return true;
        }

        public Link openLink(Index personIndex, Index linkIndex) throws IOException {
            throw new IOException();
        }

        public ObservableList<Person> getFilteredPersonList() {
            return TypicalPersons.getTypicalNetworkBook().getPersonList();
        }

        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new UnsupportedOperationException();
        }

        public void updateSortedPersonList(Comparator<Person> comparator) {
            throw new UnsupportedOperationException();
        }
    };

    @Test
    public void constructor_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> new OpenLinkCommand(null, null));
        assertThrowsAssertionError(() -> new OpenLinkCommand(TypicalIndexes.INDEX_FIRST_PERSON, null));
        assertThrowsAssertionError(() -> new OpenLinkCommand(null, CommandTestUtil.VALID_LINK_INDEX_AMY));
    }

    @Test
    public void constructor_nonNullInputs_success() {
        new OpenLinkCommand(TypicalIndexes.INVALID_INDEX, CommandTestUtil.INVALID_LINK_INDEX_AMY);
    }

    @Test
    public void execute_nullModel_throwsAssertionError() {
        assertThrowsAssertionError(() -> new OpenLinkCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.VALID_LINK_INDEX_AMY)
                .execute(null));
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        OpenLinkCommand openLinkCommand = new OpenLinkCommand(
                TypicalIndexes.INVALID_INDEX, CommandTestUtil.VALID_LINK_INDEX_AMY);
        assertCommandFailure(openLinkCommand, MODEL, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidLinkIndex_throwsCommandException() {
        OpenLinkCommand openLinkCommand = new OpenLinkCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.INVALID_LINK_INDEX_AMY);
        assertCommandFailure(openLinkCommand, MODEL,
                String.format(OpenLinkCommand.MESSAGE_INVALID_LINK_INDEX,
                        TypicalIndexes.INDEX_FIRST_PERSON.getOneBased(),
                        CommandTestUtil.INVALID_LINK_INDEX_AMY.getOneBased()));
    }

    @Test
    public void execute_validIndices_success() {
        OpenLinkCommand openLinkCommand = new OpenLinkCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.VALID_LINK_INDEX_AMY);
        assertCommandSuccess(openLinkCommand, MODEL,
                String.format(OpenLinkCommand.MESSAGE_SUCCESS, "nknguyenhc.github.io"), MODEL);
    }

    @Test
    public void execute_modelThrowingIoException_throwsCommandException() {
        OpenLinkCommand openLinkCommand = new OpenLinkCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.VALID_LINK_INDEX_AMY);
        assertCommandFailure(openLinkCommand, modelStubThrowingIoExceptionForOpeningLink,
                OpenLinkCommand.MESSAGE_CANNOT_OPEN_LINK);
    }

    @Test
    public void equals() {
        OpenLinkCommand command = new OpenLinkCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        OpenLinkCommand sameCommand = new OpenLinkCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        OpenLinkCommand differentCommand = new OpenLinkCommand(Index.fromOneBased(2), Index.fromOneBased(1));

        assertTrue(command.equals(command));
        assertFalse(command.equals(1));
        assertTrue(command.equals(sameCommand));
        assertFalse(command.equals(differentCommand));
    }

    @Test
    public void toStringMethod() {
        OpenLinkCommand command = new OpenLinkCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.VALID_LINK_INDEX_AMY);
        String expectedString = String.format("%s{personIndex=%s, linkIndex=%s}",
                command.getClass().getCanonicalName(),
                TypicalIndexes.INDEX_FIRST_PERSON,
                CommandTestUtil.VALID_LINK_INDEX_AMY);
        assertEquals(command.toString(), expectedString);
    }
}
