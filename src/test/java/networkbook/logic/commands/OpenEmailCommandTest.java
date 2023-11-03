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
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.ReadOnlyNetworkBook;
import networkbook.model.ReadOnlyUserPrefs;
import networkbook.model.UserPrefs;
import networkbook.model.person.Email;
import networkbook.model.person.Link;
import networkbook.model.person.Person;
import networkbook.testutil.TypicalIndexes;
import networkbook.testutil.TypicalPersons;

public class OpenEmailCommandTest {
    private static final Model MODEL = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());

    private static final Model modelStubThrowingIoExceptionForOpeningEmail = new Model() {
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

        public void undoNetworkBook() {
            throw new UnsupportedOperationException();
        }

        public void redoNetworkBook() {
            throw new UnsupportedOperationException();
        }

        public boolean isValidLinkIndex(Index personIndex, Index linkIndex) {
            throw new UnsupportedOperationException();
        }

        public Link openLink(Index personIndex, Index linkIndex) {
            throw new UnsupportedOperationException();
        }

        public boolean isValidEmailIndex(Index personIndex, Index emailIndex) {
            return true;
        }

        public Email openEmail(Index personIndex, Index emailIndex) throws IOException {
            throw new IOException();
        }

        public ObservableList<Person> getDisplayedPersonList() {
            return TypicalPersons.getTypicalNetworkBook().getPersonList();
        }

        public void updateDisplayedPersonList(Predicate<Person> predicate, Comparator<Person> comparator) {
            throw new UnsupportedOperationException();
        }
    };

    @Test
    public void constructor_null_throwsAssertionError() {
        assertThrowsAssertionError(() -> new OpenEmailCommand(null, CommandTestUtil.VALID_EMAIL_INDEX_AMY));
        assertThrowsAssertionError(() -> new OpenEmailCommand(TypicalIndexes.INDEX_FIRST_PERSON, null));
    }

    @Test
    public void constructor_nonNullInputs_success() {
        new OpenEmailCommand(TypicalIndexes.INVALID_INDEX, CommandTestUtil.INVALID_EMAIL_INDEX_AMY);
    }

    @Test
    public void execute_nullModel_throwsAssertionError() {
        assertThrowsAssertionError(() -> new OpenEmailCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.VALID_EMAIL_INDEX_AMY)
                .execute(null));
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        OpenEmailCommand openEmailCommand = new OpenEmailCommand(
                TypicalIndexes.INVALID_INDEX, CommandTestUtil.VALID_EMAIL_INDEX_AMY);
        assertCommandFailure(openEmailCommand, MODEL, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidEmailIndex_throwsCommandException() {
        OpenEmailCommand openEmailCommand = new OpenEmailCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.INVALID_EMAIL_INDEX_AMY);
        assertCommandFailure(openEmailCommand, MODEL,
                String.format(OpenEmailCommand.MESSAGE_INVALID_EMAIL_INDEX,
                        TypicalIndexes.INDEX_FIRST_PERSON.getOneBased(),
                        CommandTestUtil.INVALID_EMAIL_INDEX_AMY.getOneBased()));
    }

    @Test
    public void execute_validIndices_success() {
        OpenEmailCommand openEmailCommand = new OpenEmailCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.VALID_EMAIL_INDEX_AMY);
        assertCommandSuccess(openEmailCommand, MODEL,
                String.format(OpenEmailCommand.MESSAGE_SUCCESS, "alice@example.com"), MODEL);
    }

    @Test
    public void execute_modelThrowingIoException_throwsCommandException() {
        OpenEmailCommand openEmailCommand = new OpenEmailCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.VALID_EMAIL_INDEX_AMY);
        assertCommandFailure(openEmailCommand, modelStubThrowingIoExceptionForOpeningEmail,
                OpenEmailCommand.MESSAGE_CANNOT_OPEN_EMAIL);
    }

    @Test
    public void equals() {
        OpenEmailCommand command = new OpenEmailCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        OpenEmailCommand sameCommand = new OpenEmailCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        OpenEmailCommand differentCommand = new OpenEmailCommand(Index.fromOneBased(2), Index.fromOneBased(1));

        assertTrue(command.equals(command));
        assertFalse(command.equals(1));
        assertTrue(command.equals(sameCommand));
        assertFalse(command.equals(differentCommand));
    }

    @Test
    public void toStringMethod() {
        OpenEmailCommand command = new OpenEmailCommand(
                TypicalIndexes.INDEX_FIRST_PERSON, CommandTestUtil.VALID_EMAIL_INDEX_AMY);
        String expectedString = String.format("%s{personIndex=%s, emailIndex=%s}",
                command.getClass().getCanonicalName(),
                TypicalIndexes.INDEX_FIRST_PERSON,
                CommandTestUtil.VALID_EMAIL_INDEX_AMY);
        assertEquals(command.toString(), expectedString);
    }
}
