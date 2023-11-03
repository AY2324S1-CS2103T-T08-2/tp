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
import networkbook.model.person.filter.GradEqualsOneOfPredicate;
import networkbook.testutil.PersonBuilder;

public class FilterGradCommandTest {
    private Person personA = new PersonBuilder()
            .withName("A")
            .withGraduation("AY2223-S1")
            .build();
    private Person personB = new PersonBuilder()
            .withName("B")
            .withGraduation("AY2223-S2")
            .build();

    @Test
    public void equals() {
        FilterGradCommand command = new FilterGradCommand(new GradEqualsOneOfPredicate(List.of(2000, 2001)));

        // same object -> returns true
        assertEquals(command, command);

        // same fields -> returns true
        assertEquals(command, new FilterGradCommand(new GradEqualsOneOfPredicate(List.of(2000, 2001))));

        // values don't match -> return false
        assertNotEquals(command, new FilterGradCommand(new GradEqualsOneOfPredicate(List.of(2003, 2001))));

        // null -> return false
        assertNotEquals(command, null);

        // different type -> return false
        assertNotEquals(command, 5);
    }

    @Test
    public void toStringTest() {
        GradEqualsOneOfPredicate predicate = new GradEqualsOneOfPredicate(List.of(2000));
        String expected = FilterGradCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";

        assertEquals(expected, new FilterGradCommand(predicate).toString());
    }

    @Test
    public void execute_noYears_noPersonFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personA);
        testBook.addPerson(personB);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        GradEqualsOneOfPredicate pred = new GradEqualsOneOfPredicate(Collections.emptyList());

        FilterCommand command = new FilterGradCommand(pred);

        String expectedMessage = String.format(FilterGradCommand.MESSAGE_SUCCESS, "")
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

        GradEqualsOneOfPredicate pred = new GradEqualsOneOfPredicate(List.of(2000));

        FilterCommand command = new FilterGradCommand(pred);

        String expectedMessage = String.format(FilterGradCommand.MESSAGE_SUCCESS, "\"2000\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_matchesAyButNotMatchingSem_noPersonFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personA);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        // Because person A graduated in semester 1, they graduated in 2022, not 2023
        GradEqualsOneOfPredicate pred = new GradEqualsOneOfPredicate(List.of(2023));

        FilterCommand command = new FilterGradCommand(pred);

        String expectedMessage = String.format(FilterGradCommand.MESSAGE_SUCCESS, "\"2023\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_matchesAyAndMatchesSem_personFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personA);
        testBook.addPerson(personB);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        // Only person A is counted since person B graduated in 2023
        GradEqualsOneOfPredicate pred = new GradEqualsOneOfPredicate(List.of(2022));

        FilterCommand command = new FilterGradCommand(pred);

        String expectedMessage = String.format(FilterGradCommand.MESSAGE_SUCCESS, "\"2022\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_multipleMatches_personFound() {
        NetworkBook testBook = new NetworkBook();
        testBook.addPerson(personA);
        testBook.addPerson(personB);

        Model model = new ModelManager(testBook, new UserPrefs());
        Model expectedModel = new ModelManager(testBook, new UserPrefs());

        // Only person A is counted since person B graduated in 2023
        GradEqualsOneOfPredicate pred = new GradEqualsOneOfPredicate(List.of(2022, 2023));

        FilterCommand command = new FilterGradCommand(pred);

        String expectedMessage = String.format(FilterGradCommand.MESSAGE_SUCCESS, "\"2022\", \"2023\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 2);

        expectedModel.updateDisplayedPersonList(pred, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }
}
