package networkbook.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import networkbook.logic.Messages;
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.UserPrefs;
import networkbook.model.person.Person;
import networkbook.model.person.PersonSortComparator;
import networkbook.model.person.PersonSortComparator.SortField;
import networkbook.model.person.PersonSortComparator.SortOrder;
import networkbook.testutil.TypicalPersons;

/**
 * Contains integration tests (interaction with the Model) for {@code SortCommand}.
 */
public class SortCommandTest {

    @Test
    public void equals() {
        PersonSortComparator firstComparator = new PersonSortComparator(SortField.NAME, SortOrder.DESCENDING);
        PersonSortComparator secondComparator = new PersonSortComparator(SortField.NONE, SortOrder.ASCENDING);

        SortCommand sortFirstCommand = new SortCommand(firstComparator);
        SortCommand sortSecondCommand = new SortCommand(secondComparator);

        // same object -> returns true
        assertTrue(sortFirstCommand.equals(sortFirstCommand));

        // same values -> returns true
        SortCommand sortFirstCommandCopy = new SortCommand(firstComparator);
        assertTrue(sortFirstCommand.equals(sortFirstCommandCopy));

        // different types -> returns false
        assertFalse(sortFirstCommand.equals(1));

        // null -> returns false
        assertFalse(sortFirstCommand.equals(null));

        // different commans -> returns false
        assertFalse(sortFirstCommand.equals(sortSecondCommand));
    }

    @Test
    public void execute_descendingNameSort_correctlySorted() {
        List<Person> expectedPersons = TypicalPersons.getTypicalPersons();
        Collections.reverse(expectedPersons);
        String expectedMessage = String.format(Messages.MESSAGE_PERSONS_SORTED_OVERVIEW, expectedPersons.size());
        PersonSortComparator comparator = new PersonSortComparator(SortField.NAME, SortOrder.DESCENDING);
        Model model = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        Model expectedModel = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        SortCommand command = new SortCommand(comparator);
        expectedModel.updateDisplayedPersonList(null, comparator);
        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
        ObservableList<Person> expectedList = FXCollections.observableList(expectedPersons);
        assertEquals(
                expectedList,
                model.getDisplayedPersonList()
        );
    }

    @Test
    public void toStringMethod() {
        PersonSortComparator comparator = new PersonSortComparator(SortField.NONE, SortOrder.DESCENDING);
        SortCommand sortCommand = new SortCommand(comparator);
        String expected = SortCommand.class.getCanonicalName() + "{comparator=" + comparator + "}";
        assertEquals(expected, sortCommand.toString());
    }

}
