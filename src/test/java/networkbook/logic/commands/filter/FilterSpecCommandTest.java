package networkbook.logic.commands.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.logic.commands.CommandTestUtil;
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.NetworkBook;
import networkbook.model.UserPrefs;
import networkbook.model.person.Person;
import networkbook.model.person.filter.SpecContainsKeyTermsPredicate;
import networkbook.testutil.PersonBuilder;

public class FilterSpecCommandTest {

    private Person personA = new PersonBuilder()
            .withName("A")
            .addSpecialisation("A")
            .build();
    private Person personB = new PersonBuilder()
            .withName("B")
            .addSpecialisation("A")
            .addSpecialisation("B")
            .build();

    @Test
    public void equals() {
        FilterSpecCommand command = new FilterSpecCommand(new SpecContainsKeyTermsPredicate(List.of("A", "B")));

        // same object -> returns true
        assertEquals(command, command);

        // same fields -> returns true
        assertEquals(command, new FilterSpecCommand(new SpecContainsKeyTermsPredicate(List.of("A", "B"))));

        // values don't match -> return false
        assertNotEquals(command, new FilterSpecCommand(new SpecContainsKeyTermsPredicate(List.of("C"))));

        // null -> return false
        assertNotEquals(command, null);

        // different type -> return false
        assertNotEquals(command, 5);
    }

    @Test
    public void toStringTest() {
        SpecContainsKeyTermsPredicate predicate = new SpecContainsKeyTermsPredicate(List.of("A"));
        String expected = FilterSpecCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";

        assertEquals(expected, new FilterSpecCommand(predicate).toString());
    }

    @Test
    public void execute_noTerms_noPersonFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personA);
        testBook.addPerson(personB);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        SpecContainsKeyTermsPredicate pred = new SpecContainsKeyTermsPredicate(Collections.emptyList());

        FilterCommand command = new FilterSpecCommand(pred);

        String expectedMessage = String.format(FilterSpecCommand.MESSAGE_SUCCESS, "")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_oneUnmatchedTerm_noPersonFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personA);
        testBook.addPerson(personB);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        SpecContainsKeyTermsPredicate pred = new SpecContainsKeyTermsPredicate(List.of("C"));

        FilterCommand command = new FilterSpecCommand(pred);

        String expectedMessage = String.format(FilterSpecCommand.MESSAGE_SUCCESS, "\"C\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_matchedTerms_personFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personA);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        SpecContainsKeyTermsPredicate pred = new SpecContainsKeyTermsPredicate(List.of("A"));

        FilterCommand command = new FilterSpecCommand(pred);

        String expectedMessage = String.format(FilterSpecCommand.MESSAGE_SUCCESS, "\"A\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_atLeastOneMatchedTerms_personFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personB);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        SpecContainsKeyTermsPredicate pred = new SpecContainsKeyTermsPredicate(List.of("A"));

        FilterCommand command = new FilterSpecCommand(pred);

        String expectedMessage = String.format(FilterSpecCommand.MESSAGE_SUCCESS, "\"A\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_mixedCaseMatchedTerms_personFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personB);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        SpecContainsKeyTermsPredicate pred = new SpecContainsKeyTermsPredicate(List.of("a"));

        FilterCommand command = new FilterSpecCommand(pred);

        String expectedMessage = String.format(FilterSpecCommand.MESSAGE_SUCCESS, "\"a\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }
}
