package networkbook.logic.commands;

import static networkbook.testutil.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import networkbook.commons.core.index.Index;
import networkbook.logic.commands.exceptions.CommandException;
import networkbook.logic.parser.CliSyntax;
import networkbook.model.Model;
import networkbook.model.NetworkBook;
import networkbook.model.person.NameContainsKeywordsPredicate;
import networkbook.model.person.Person;
import networkbook.testutil.AddPersonDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_LINK_AMY = "linkedin.com/in/Amy-beez";
    public static final Index VALID_LINK_INDEX_AMY = Index.fromZeroBased(0);
    public static final Index VALID_EMAIL_INDEX_AMY = Index.fromZeroBased(0);
    public static final Index INVALID_LINK_INDEX_AMY = Index.fromZeroBased(1);
    public static final Index INVALID_EMAIL_INDEX_AMY = Index.fromZeroBased(1);
    public static final String VALID_LINK_BOB = "github.com/bob2000";
    public static final String VALID_GRADUATION_AMY = "AY9899-S2";
    public static final String VALID_GRADUATION_FULL_AMY = "AY1998/1999 Semester 2";
    public static final String VALID_GRADUATION_BOB = "AY2425-S1";
    public static final String VALID_GRADUATION_FULL_BOB = "AY2024/2025 Semester 1";
    public static final String VALID_COURSE_AMY = "Computer Science";
    public static final String VALID_COURSE_BOB = "Computer Engineering";
    public static final String VALID_SPECIALISATION_AMY = "Game Development";
    public static final String VALID_SPECIALISATION_BOB = "Internet of Things";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_PRIORITY_AMY = "High";

    public static final String NAME_DESC_AMY = " " + CliSyntax.PREFIX_NAME + " " + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + CliSyntax.PREFIX_NAME + " " + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + CliSyntax.PREFIX_PHONE + " " + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + CliSyntax.PREFIX_PHONE + " " + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + CliSyntax.PREFIX_EMAIL + " " + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + CliSyntax.PREFIX_EMAIL + " " + VALID_EMAIL_BOB;
    // amend CliSyntax
    public static final String LINK_DESC_AMY = " " + CliSyntax.PREFIX_LINK + " " + VALID_LINK_AMY;
    public static final String LINK_DESC_BOB = " " + CliSyntax.PREFIX_LINK + " " + VALID_LINK_BOB;
    public static final String GRADUATION_DESC_AMY = " " + CliSyntax.PREFIX_GRADUATION + " "
            + VALID_GRADUATION_AMY;
    public static final String GRADUATION_DESC_BOB = " " + CliSyntax.PREFIX_GRADUATION + " "
            + VALID_GRADUATION_BOB;
    public static final String COURSE_DESC_AMY = " " + CliSyntax.PREFIX_COURSE + " " + VALID_COURSE_AMY;
    public static final String COURSE_DESC_BOB = " " + CliSyntax.PREFIX_COURSE + " " + VALID_COURSE_BOB;
    public static final String SPECIALISATION_DESC_AMY = " " + CliSyntax.PREFIX_SPECIALISATION + " "
            + VALID_SPECIALISATION_AMY;
    public static final String SPECIALISATION_DESC_BOB = " " + CliSyntax.PREFIX_SPECIALISATION + " "
            + VALID_SPECIALISATION_BOB;
    public static final String TAG_DESC_FRIEND = " " + CliSyntax.PREFIX_TAG + " " + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + CliSyntax.PREFIX_TAG + " " + VALID_TAG_HUSBAND;
    public static final String PRIORITY_DESC_AMY = " " + CliSyntax.PREFIX_PRIORITY + " " + "High";
    public static final String PRIORITY_DESC_BOB = " " + CliSyntax.PREFIX_PRIORITY + " " + "Medium";
    public static final String INVALID_NAME_DESC = " " + CliSyntax.PREFIX_NAME
                                                        + " " + "James&"; // '&' not allowed in names
    public static final String VALID_NAME_DESC = " " + CliSyntax.PREFIX_NAME + " " + "James";
    public static final String INVALID_PHONE_DESC = " " + CliSyntax.PREFIX_PHONE
                                                        + " " + "911a"; // 'a' not allowed in phones
    public static final String VALID_PHONE_DESC = " " + CliSyntax.PREFIX_PHONE + " " + "123456";
    public static final String INVALID_EMAIL_DESC = " " + CliSyntax.PREFIX_EMAIL
                                                        + " " + "bob!yahoo"; // missing '@' symbol
    public static final String VALID_EMAIL_DESC = " " + CliSyntax.PREFIX_EMAIL + " " + "quack@gmail.com";
    public static final String INVALID_LINK_DESC =
            " " + CliSyntax.PREFIX_LINK + " "
            + "https://docs.goo_gle.com";
    public static final String VALID_LINK_DESC = " " + CliSyntax.PREFIX_LINK + " " + "www.google.com";
    public static final String INVALID_GRADUATION_DESC =
            " " + CliSyntax.PREFIX_GRADUATION + " " + "2024/2025"; // graduation must follow format
    public static final String VALID_GRADUATION_DESC = " " + CliSyntax.PREFIX_GRADUATION + " " + "AY2425-S1";
    public static final String INVALID_COURSE_DESC =
            " " + CliSyntax.PREFIX_COURSE; // empty string not allowed for course
    public static final String VALID_COURSE_DESC = " " + CliSyntax.PREFIX_COURSE + " " + "CS2103T";
    public static final String INVALID_SPECIALISATION_DESC =
            " " + CliSyntax.PREFIX_SPECIALISATION; // empty string not allowed for specialisation
    public static final String VALID_SPECIALISATION_DESC =
            " " + CliSyntax.PREFIX_SPECIALISATION + " " + "Software Eng";
    public static final String INVALID_TAG_DESC = " " + CliSyntax.PREFIX_TAG
                                                        + " " + "hubby*"; // '*' not allowed in tags
    public static final String VALID_TAG_DESC = " " + CliSyntax.PREFIX_TAG + " " + "talk";
    public static final String INVALID_PRIORITY_DESC = " " + CliSyntax.PREFIX_PRIORITY
                                                        + " " + "hi"; // incorrect priority format
    public static final String VALID_PRIORITY_DESC = " " + CliSyntax.PREFIX_PRIORITY + " " + "high";

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final AddCommand.AddPersonDescriptor DESC_AMY;
    public static final AddCommand.AddPersonDescriptor DESC_BOB;
    public static final String VALID_INDEX_DESC = " " + CliSyntax.PREFIX_INDEX + " " + "1";

    static {
        DESC_AMY = new AddPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withLink(VALID_LINK_AMY)
                .withGraduation(VALID_GRADUATION_AMY).withCourse(VALID_COURSE_AMY)
                .withSpecialisation(VALID_SPECIALISATION_AMY).withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new AddPersonDescriptorBuilder()
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withLink(VALID_LINK_BOB)
                .withGraduation(VALID_GRADUATION_BOB).withCourse(VALID_COURSE_BOB)
                .withSpecialisation(VALID_SPECIALISATION_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
                                            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code Command}, confirms that the command does not throw any exception.
     */
    public static void assertCommandThrowsNothing(Command command, Model model) {
        try {
            command.execute(model);
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the network book, filtered person list and selected person in {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        NetworkBook expectedNetworkBook = new NetworkBook(actualModel.getNetworkBook());
        List<Person> expectedFilteredList = new ArrayList<>(actualModel.getDisplayedPersonList());
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedNetworkBook, actualModel.getNetworkBook());
        assertEquals(expectedFilteredList, actualModel.getDisplayedPersonList());
    }
    /**
     * Updates {@code model}'s filtered list to show only the person at the given {@code targetIndex} in the
     * {@code model}'s network book.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getDisplayedPersonList().size());

        Person person = model.getDisplayedPersonList().get(targetIndex.getZeroBased());
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateDisplayedPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])), null);

        assertEquals(1, model.getDisplayedPersonList().size());
    }

}
