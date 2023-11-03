package networkbook.logic.commands.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.logic.commands.CommandTestUtil;
import networkbook.model.Model;
import networkbook.model.ModelManager;
import networkbook.model.NetworkBook;
import networkbook.model.UserPrefs;
import networkbook.model.person.Person;
import networkbook.model.person.filter.CourseContainsKeyTermsPredicate;
import networkbook.model.person.filter.CourseIsStillBeingTakenPredicate;
import networkbook.testutil.PersonBuilder;
import networkbook.testutil.TypicalPersons;

public class FilterCourseCommandTest {
    private Person personWithDatedCourse = new PersonBuilder()
            .withName("A")
            .addCourse("First", "01-01-2000", "03-01-2000")
            .build();
    private Person personWithUndatedCourse1 = new PersonBuilder()
            .withName("B")
            .addCourse("First")
            .build();
    private Person personWithUndatedCourse2 = new PersonBuilder()
            .withName("C")
            .addCourse("Second")
            .addCourse("Third")
            .build();

    @Test
    public void equals() {
        CourseIsStillBeingTakenPredicate firstTakenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));
        CourseIsStillBeingTakenPredicate secondTakenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 2));
        CourseContainsKeyTermsPredicate firstKeyTermsPredicate =
                new CourseContainsKeyTermsPredicate(List.of("first"));
        CourseContainsKeyTermsPredicate secondKeyTermsPredicate =
                new CourseContainsKeyTermsPredicate(List.of("second"));

        FilterCommand firstCommand = new FilterCourseCommand(firstKeyTermsPredicate, firstTakenPredicate, true);

        // same object -> returns true
        assertEquals(firstCommand, firstCommand);

        // same values -> return true
        assertEquals(firstCommand,
                new FilterCourseCommand(firstKeyTermsPredicate, firstTakenPredicate, true));

        // at least one value doesn't match -> return false
        assertNotEquals(firstCommand,
                new FilterCourseCommand(secondKeyTermsPredicate, firstTakenPredicate, true));
        assertNotEquals(firstCommand,
                new FilterCourseCommand(firstKeyTermsPredicate, secondTakenPredicate, true));
        assertNotEquals(firstCommand,
                new FilterCourseCommand(firstKeyTermsPredicate, firstTakenPredicate, false));

        // null -> returns false
        assertNotEquals(firstCommand, null);

        // different type -> returns false
        assertNotEquals(firstCommand, 5);
    }

    @Test
    public void toStringTest() {
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));
        CourseContainsKeyTermsPredicate keyTermsPredicate =
                new CourseContainsKeyTermsPredicate(List.of("first"));
        String expected = FilterCourseCommand.class.getCanonicalName()
                + "{predicate=" + keyTermsPredicate
                + ", time=" + takenPredicate
                + ", taken=true}";
        assertEquals(expected, new FilterCourseCommand(keyTermsPredicate, takenPredicate, true).toString());
    }

    @Test
    public void execute_noKeyTerms_noPersonFound() {
        Model model = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());
        Model expectedModel = new ModelManager(TypicalPersons.getTypicalNetworkBook(), new UserPrefs());

        String expectedMessage = String.format(FilterCourseCommand.MESSAGE_SUCCESS, "")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);
        CourseContainsKeyTermsPredicate keyPredicate = new CourseContainsKeyTermsPredicate(Collections.emptyList());
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        FilterCommand command = new FilterCourseCommand(keyPredicate, takenPredicate, false);

        expectedModel.updateDisplayedPersonList(keyPredicate, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_singleUnmatchedKeyword_noPersonFound() {
        NetworkBook testNetworkBook = new NetworkBook();
        testNetworkBook.addPerson(personWithDatedCourse);
        testNetworkBook.addPerson(personWithUndatedCourse1);

        Model model = new ModelManager(testNetworkBook, new UserPrefs());
        Model expectedModel = new ModelManager(testNetworkBook, new UserPrefs());

        CourseContainsKeyTermsPredicate keyPredicate = new CourseContainsKeyTermsPredicate(List.of("Fourth"));
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        FilterCommand command = new FilterCourseCommand(keyPredicate, takenPredicate, false);

        String expectedMessage = String.format(FilterCourseCommand.MESSAGE_SUCCESS, "\"Fourth\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);

        expectedModel.updateDisplayedPersonList(keyPredicate, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_singleMatchingKeyword_peopleFound() {
        NetworkBook testNetworkBook = new NetworkBook();
        testNetworkBook.addPerson(personWithDatedCourse);
        testNetworkBook.addPerson(personWithUndatedCourse1);

        Model model = new ModelManager(testNetworkBook, new UserPrefs());
        Model expectedModel = new ModelManager(testNetworkBook, new UserPrefs());

        CourseContainsKeyTermsPredicate keyPredicate = new CourseContainsKeyTermsPredicate(List.of("First"));
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        FilterCommand command = new FilterCourseCommand(keyPredicate, takenPredicate, false);

        String expectedMessage = String.format(FilterCourseCommand.MESSAGE_SUCCESS, "\"First\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 2);

        expectedModel.updateDisplayedPersonList(keyPredicate, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_atLeastOneMatchingKeyword_peopleFound() {
        NetworkBook testNetworkBook = new NetworkBook();
        testNetworkBook.addPerson(personWithDatedCourse);
        testNetworkBook.addPerson(personWithUndatedCourse1);
        testNetworkBook.addPerson(personWithUndatedCourse2);

        Model model = new ModelManager(testNetworkBook, new UserPrefs());
        Model expectedModel = new ModelManager(testNetworkBook, new UserPrefs());

        CourseContainsKeyTermsPredicate keyPredicate = new CourseContainsKeyTermsPredicate(List.of("Fourth", "Third"));
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        FilterCommand command = new FilterCourseCommand(keyPredicate, takenPredicate, false);

        String expectedMessage = String.format(FilterCourseCommand.MESSAGE_SUCCESS, "\"Fourth\", \"Third\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);

        expectedModel.updateDisplayedPersonList(keyPredicate, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_matchingKeywordButDateOutOfRange_noPeopleFound() {
        NetworkBook testNetworkBook = new NetworkBook();
        testNetworkBook.addPerson(personWithDatedCourse);
        testNetworkBook.addPerson(personWithUndatedCourse2);

        Model model = new ModelManager(testNetworkBook, new UserPrefs());
        Model expectedModel = new ModelManager(testNetworkBook, new UserPrefs());

        CourseContainsKeyTermsPredicate keyPredicate = new CourseContainsKeyTermsPredicate(List.of("First"));
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 10));

        FilterCommand command = new FilterCourseCommand(keyPredicate, takenPredicate, true);

        String expectedMessage = String.format(FilterCourseCommand.MESSAGE_SUCCESS, "\"First\"")
                + FilterCommand.MESSAGE_EXCL_FIN
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 0);

        expectedModel.updateDisplayedPersonList(keyPredicate, null);
        expectedModel.updateDisplayedPersonList(
                p -> keyPredicate.getCourses(p)
                        .stream()
                        .anyMatch(c -> takenPredicate.test(c)),
                null
        );

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_matchingKeywordAndDateInRange_peopleFound() {
        NetworkBook testNetworkBook = new NetworkBook();
        testNetworkBook.addPerson(personWithDatedCourse);
        testNetworkBook.addPerson(personWithUndatedCourse1);
        testNetworkBook.addPerson(personWithUndatedCourse2);

        Model model = new ModelManager(testNetworkBook, new UserPrefs());
        Model expectedModel = new ModelManager(testNetworkBook, new UserPrefs());

        // Two people have the course "First" but only one should be counted since only
        // one person has their course within the date range (the person whose course doesn't have a date)
        CourseContainsKeyTermsPredicate keyPredicate = new CourseContainsKeyTermsPredicate(List.of("First"));
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 10));

        FilterCommand command = new FilterCourseCommand(keyPredicate, takenPredicate, true);

        String expectedMessage = String.format(FilterCourseCommand.MESSAGE_SUCCESS, "\"First\"")
                + FilterCommand.MESSAGE_EXCL_FIN
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 1);

        expectedModel.updateDisplayedPersonList(keyPredicate, null);
        expectedModel.updateDisplayedPersonList(
                p -> keyPredicate.getCourses(p)
                        .stream()
                        .anyMatch(c -> takenPredicate.test(c)),
                null
        );

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_partialMatchingKeyword_peopleFound() {
        NetworkBook testNetworkBook = new NetworkBook();
        testNetworkBook.addPerson(personWithDatedCourse);
        testNetworkBook.addPerson(personWithUndatedCourse1);

        Model model = new ModelManager(testNetworkBook, new UserPrefs());
        Model expectedModel = new ModelManager(testNetworkBook, new UserPrefs());

        CourseContainsKeyTermsPredicate keyPredicate = new CourseContainsKeyTermsPredicate(List.of("Fir"));
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        FilterCommand command = new FilterCourseCommand(keyPredicate, takenPredicate, false);

        String expectedMessage = String.format(FilterCourseCommand.MESSAGE_SUCCESS, "\"Fir\"")
                + String.format(FilterCommand.MESSAGE_PERSONS_FOUND_OVERVIEW, 2);

        expectedModel.updateDisplayedPersonList(keyPredicate, null);

        CommandTestUtil.assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void filterCourseCommand_executeWithNull_throwsAssertionError() {
        CourseContainsKeyTermsPredicate keyPredicate = new CourseContainsKeyTermsPredicate(List.of("Fir"));
        CourseIsStillBeingTakenPredicate takenPredicate =
                new CourseIsStillBeingTakenPredicate(LocalDate.ofYearDay(2000, 1));

        FilterCommand command = new FilterCourseCommand(keyPredicate, takenPredicate, false);

        assertThrows(AssertionError.class, () -> command.execute(null));
    }
}
