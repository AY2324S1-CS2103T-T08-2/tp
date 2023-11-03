package networkbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.UserPrefs;
import networkbook.model.person.NameContainsKeyTermsPredicate;
import networkbook.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());

    @Test
    public void equals() {
        NameContainsKeyTermsPredicate firstPredicate =
                new NameContainsKeyTermsPredicate(Collections.singletonList("first"));
        NameContainsKeyTermsPredicate secondPredicate =
                new NameContainsKeyTermsPredicate(Collections.singletonList("second"));

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_zeroKeywords_noPersonFound() {
        String expectedMessage = String.format(FindCommand.MESSAGE_SUCCESS, "")
                + String.format(FindCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);
        NameContainsKeyTermsPredicate predicate = preparePredicate(" ");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateDisplayedPersonList(predicate, null);
        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getDisplayedPersonList());
    }

    @Test
    public void execute_singleUnmatchedKeyword_noPersonFound() {
        String expectedMessage = String.format(FindCommand.MESSAGE_SUCCESS, "\"Ulfred\"")
                + String.format(FindCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);
        NameContainsKeyTermsPredicate predicate = preparePredicate("Ulfred");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateDisplayedPersonList(predicate, null);
        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getDisplayedPersonList());
    }

    @Test
    public void execute_multipleUnmatchedKeywords_noPersonFound() {
        String expectedMessage = String.format(FindCommand.MESSAGE_SUCCESS, "\"Ulfred\", \"Snyder\"")
                + String.format(FindCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);
        NameContainsKeyTermsPredicate predicate = preparePredicate("Ulfred  Snyder");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateDisplayedPersonList(predicate, null);
        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getDisplayedPersonList());
    }

    @Test
    public void execute_singleUniqueKeyword_matchingPersonFound() {
        String expectedMessage = String.format(FindCommand.MESSAGE_SUCCESS, "\"Fiona\"")
                + String.format(FindCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);
        NameContainsKeyTermsPredicate predicate = preparePredicate("Fiona");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateDisplayedPersonList(predicate, null);
        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(
                Arrays.asList(TypicalPersons.FIONA),
                model.getDisplayedPersonList()
        );
    }

    @Test
    public void execute_multipleUniqueKeywords_matchingPersonFound() {
        String expectedMessage = String.format(FindCommand.MESSAGE_SUCCESS, "\"Fiona\", \"Kunz\"")
                + String.format(FindCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);
        NameContainsKeyTermsPredicate predicate = preparePredicate("Fiona   Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateDisplayedPersonList(predicate, null);
        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(
                Arrays.asList(TypicalPersons.FIONA),
                model.getDisplayedPersonList()
        );
    }

    @Test
    public void execute_singleSharedKeyword_multiplePersonsFound() {
        String expectedMessage = String.format(FindCommand.MESSAGE_SUCCESS, "\"Ku\"")
                + String.format(FindCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 2);
        NameContainsKeyTermsPredicate predicate = preparePredicate("Ku");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateDisplayedPersonList(predicate, null);
        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(
                Arrays.asList(TypicalPersons.CARL, TypicalPersons.FIONA),
                model.getDisplayedPersonList()
        );
    }

    @Test
    public void execute_multipleKeywords_multiplePersonsFound() {
        String expectedMessage = String.format(FindCommand.MESSAGE_SUCCESS, "\"Kurz\", \"Elle\", \"Kunz\"")
                + String.format(FindCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 3);
        NameContainsKeyTermsPredicate predicate = preparePredicate("Kurz Elle Kunz");
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateDisplayedPersonList(predicate, null);
        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(
                Arrays.asList(TypicalPersons.CARL, TypicalPersons.ELLE, TypicalPersons.FIONA),
                model.getDisplayedPersonList()
        );
    }

    @Test
    public void toStringMethod() {
        NameContainsKeyTermsPredicate predicate = new NameContainsKeyTermsPredicate(Arrays.asList("keyword"));
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName() + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }

    /**
     * Parses {@code userInput} into a {@code NameContainsKeyTermsPredicate}.
     */
    private NameContainsKeyTermsPredicate preparePredicate(String userInput) {
        return new NameContainsKeyTermsPredicate(Arrays.asList(userInput.split("\\s+")));
    }
}
