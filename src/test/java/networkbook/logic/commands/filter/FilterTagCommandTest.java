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
import networkbook.model.person.filter.TagsContainKeyTermsPredicate;
import networkbook.testutil.PersonBuilder;

public class FilterTagCommandTest {

    private Person personA = new PersonBuilder()
            .withName("A")
            .withTags("A")
            .build();
    private Person personB = new PersonBuilder()
            .withName("B")
            .withTags("A", "B")
            .build();

    @Test
    public void equals() {
        FilterTagCommand command = new FilterTagCommand(new TagsContainKeyTermsPredicate(List.of("A", "B")));

        // same object -> returns true
        assertEquals(command, command);

        // same fields -> returns true
        assertEquals(command, new FilterTagCommand(new TagsContainKeyTermsPredicate(List.of("A", "B"))));

        // values don't match -> return false
        assertNotEquals(command, new FilterTagCommand(new TagsContainKeyTermsPredicate(List.of("C"))));

        // null -> return false
        assertNotEquals(command, null);

        // different type -> return false
        assertNotEquals(command, 5);
    }

    @Test
    public void toStringTest() {
        TagsContainKeyTermsPredicate predicate = new TagsContainKeyTermsPredicate(List.of("A"));
        String expected = FilterTagCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";

        assertEquals(expected, new FilterTagCommand(predicate).toString());
    }

    @Test
    public void execute_noTerms_noPersonFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personA);
        testBook.addPerson(personB);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        TagsContainKeyTermsPredicate pred = new TagsContainKeyTermsPredicate(Collections.emptyList());

        FilterTagCommand command = new FilterTagCommand(pred);

        String expectedMessage = String.format(FilterTagCommand.MESSAGE_SUCCESS, "")
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

        TagsContainKeyTermsPredicate pred = new TagsContainKeyTermsPredicate(List.of("C"));

        FilterCommand command = new FilterTagCommand(pred);

        String expectedMessage = String.format(FilterTagCommand.MESSAGE_SUCCESS, "\"C\"")
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

        TagsContainKeyTermsPredicate pred = new TagsContainKeyTermsPredicate(List.of("A"));

        FilterCommand command = new FilterTagCommand(pred);

        String expectedMessage = String.format(FilterTagCommand.MESSAGE_SUCCESS, "\"A\"")
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

        TagsContainKeyTermsPredicate pred = new TagsContainKeyTermsPredicate(List.of("A"));

        FilterCommand command = new FilterTagCommand(pred);

        String expectedMessage = String.format(FilterTagCommand.MESSAGE_SUCCESS, "\"A\"")
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

        TagsContainKeyTermsPredicate pred = new TagsContainKeyTermsPredicate(List.of("a"));

        FilterCommand command = new FilterTagCommand(pred);

        String expectedMessage = String.format(FilterTagCommand.MESSAGE_SUCCESS, "\"a\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }
}
