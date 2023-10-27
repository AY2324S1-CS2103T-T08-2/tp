package networkbook.logic.parser;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.delete.DeleteCourseAction;
import networkbook.logic.commands.delete.DeleteEmailAction;
import networkbook.logic.commands.delete.DeleteFieldCommand;
import networkbook.logic.commands.delete.DeleteGraduationAction;
import networkbook.logic.commands.delete.DeleteLinkAction;
import networkbook.logic.commands.delete.DeletePersonCommand;
import networkbook.logic.commands.delete.DeletePhoneAction;
import networkbook.logic.commands.delete.DeletePriorityAction;
import networkbook.logic.commands.delete.DeleteSpecialisationAction;
import networkbook.logic.commands.delete.DeleteTagAction;
import networkbook.logic.parser.exceptions.ParseException;
import networkbook.testutil.TypicalIndexes;

/**
 * Test class that contains unit test cases for {@code DeleteCommandParser}.
 */
public class DeleteCommandParserTest {

    private static final DeleteCommandParser parser = new DeleteCommandParser();

    private static final String NAME = CliSyntax.PREFIX_NAME.getPrefix();
    private static final String PHONE = CliSyntax.PREFIX_PHONE.getPrefix();
    private static final String EMAIL = CliSyntax.PREFIX_EMAIL.getPrefix();
    private static final String LINK = CliSyntax.PREFIX_LINK.getPrefix();
    private static final String GRADUATION = CliSyntax.PREFIX_GRADUATION.getPrefix();
    private static final String COURSE = CliSyntax.PREFIX_COURSE.getPrefix();
    private static final String SPECIALISATION = CliSyntax.PREFIX_SPECIALISATION.getPrefix();
    private static final String PRIORITY = CliSyntax.PREFIX_PRIORITY.getPrefix();
    private static final String TAG = CliSyntax.PREFIX_TAG.getPrefix();
    private static final String INDEX = CliSyntax.PREFIX_INDEX.getPrefix();
    private static final String WHITESPACE = " ";
    private static final String LONG_WHITESPACE = "         ";
    private static final String ONE = "1";
    private static final String TWO = "2";
    private static final String RANDOM = "$'fn2";
    private static final String INVALID_PREFIX = "/invalid";
    private static final Index INDEX_ONE = Index.fromOneBased(1);
    private static final Index INDEX_TWO = Index.fromOneBased(2);
    private static final DeletePhoneAction DELETE_FIRST_PHONE_ACTION = new DeletePhoneAction(INDEX_ONE);
    private static final DeleteEmailAction DELETE_FIRST_EMAIL_ACTION = new DeleteEmailAction(INDEX_ONE);
    private static final DeleteLinkAction DELETE_FIRST_LINK_ACTION = new DeleteLinkAction(INDEX_ONE);
    private static final DeleteCourseAction DELETE_FIRST_COURSE_ACTION = new DeleteCourseAction(INDEX_ONE);
    private static final DeleteSpecialisationAction DELETE_FIRST_SPECIALISATION_ACTION =
            new DeleteSpecialisationAction(INDEX_ONE);
    private static final DeleteTagAction DELETE_FIRST_TAG_ACTION = new DeleteTagAction(INDEX_ONE);
    private static final DeleteGraduationAction DELETE_GRADUATION_ACTION = new DeleteGraduationAction();
    private static final DeletePriorityAction DELETE_PRIORITY_ACTION = new DeletePriorityAction();




    public String generateUserInput(String... parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            sb.append(parts[i]);
            sb.append(WHITESPACE);
        }
        sb.append(parts[parts.length - 1]);
        return sb.toString();
    }

    @Test
    public void parse_validIndex_returnsDeletePersonCommand() {
        CommandParserTestUtil.assertParseSuccess(parser, "1",
                new DeletePersonCommand(TypicalIndexes.INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser, "a",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noPersonIndex_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser, WHITESPACE,
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(NAME),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(INDEX, ONE),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(INDEX),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(PRIORITY),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_deleteName_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, NAME),
                DeleteCommandParser.MESSAGE_DELETE_NAME);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, PHONE, NAME, RANDOM),
                DeleteCommandParser.MESSAGE_DELETE_NAME);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, NAME, INDEX, ONE),
                DeleteCommandParser.MESSAGE_DELETE_NAME);
    }

    @Test
    public void parse_deleteInvalidPrefix_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, INVALID_PREFIX),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, INVALID_PREFIX, INDEX, ONE),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, INVALID_PREFIX, GRADUATION),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, INVALID_PREFIX, TAG, INDEX, ONE),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, INDEX),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, INDEX, ONE),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, INDEX, ONE, INDEX, TWO),
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeletePersonCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_deleteMultiplePrefixes_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, PRIORITY),
                Messages.MESSAGE_EXACTLY_ONE_FIELD);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, TAG),
                Messages.MESSAGE_EXACTLY_ONE_FIELD);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, TAG, GRADUATION, PRIORITY),
                Messages.MESSAGE_EXACTLY_ONE_FIELD);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, TAG, INDEX, ONE),
                Messages.MESSAGE_EXACTLY_ONE_FIELD);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, PRIORITY, INDEX, ONE),
                Messages.MESSAGE_EXACTLY_ONE_FIELD);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, INDEX, ONE, PRIORITY),
                Messages.MESSAGE_EXACTLY_ONE_FIELD);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, RANDOM, PRIORITY),
                Messages.MESSAGE_EXACTLY_ONE_FIELD);
    }

    @Test
    public void parse_deleteSamePrefixMultipleTimes_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, PRIORITY, PRIORITY),
                Messages.MESSAGE_DUPLICATE_PREFIX);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, EMAIL, INDEX, ONE, EMAIL, INDEX, ONE),
                Messages.MESSAGE_DUPLICATE_PREFIX);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, LINK, GRADUATION, LINK),
                Messages.MESSAGE_DUPLICATE_PREFIX);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, COURSE, RANDOM, COURSE, INDEX, PRIORITY),
                Messages.MESSAGE_DUPLICATE_PREFIX);
    }

    @Test
    public void parse_deleteMultiValuedFieldInvalidIndex_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, TAG),
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, TAG));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, SPECIALISATION, INDEX, ONE, INDEX, TWO),
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, SPECIALISATION));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, LINK, INDEX, ONE, INDEX, ONE),
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, LINK));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, COURSE, INDEX, INDEX),
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, COURSE));
    }

    @Test
    public void parse_deleteSingleValuedFieldUsingIndex_throwsParseException() {
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, PRIORITY, INDEX, ONE),
                Messages.MESSAGE_INDEX_CANNOT_BE_PRESENT);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, PRIORITY, INDEX, ONE, INDEX, TWO),
                Messages.MESSAGE_INDEX_CANNOT_BE_PRESENT);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, INDEX),
                Messages.MESSAGE_INDEX_CANNOT_BE_PRESENT);
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, INDEX, RANDOM),
                Messages.MESSAGE_INDEX_CANNOT_BE_PRESENT);
    }

    @Test
    public void parse_deleteWithRedundantValue_throwsParseException() throws ParseException {
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, PRIORITY, RANDOM),
                String.format(Messages.MESSAGE_VALUE_CANNOT_BE_PRESENT, PRIORITY));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, GRADUATION, RANDOM),
                String.format(Messages.MESSAGE_VALUE_CANNOT_BE_PRESENT, GRADUATION));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, TAG, RANDOM, INDEX, ONE),
                String.format(Messages.MESSAGE_VALUE_CANNOT_BE_PRESENT, TAG));
        CommandParserTestUtil.assertParseFailure(parser,
                generateUserInput(ONE, INDEX, ONE, LINK, RANDOM),
                String.format(Messages.MESSAGE_VALUE_CANNOT_BE_PRESENT, LINK));
    }

    @Test
    public void parse_deleteSingleValuedField_success() throws ParseException {
        DeleteFieldCommand expectedGraduationCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_GRADUATION_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(ONE, GRADUATION),
                expectedGraduationCommand);

        DeleteFieldCommand expectedPriorityCommand = new DeleteFieldCommand(INDEX_TWO, DELETE_PRIORITY_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(TWO, PRIORITY),
                expectedPriorityCommand);
    }

    @Test
    public void parse_deleteMultiValuedField_success() throws ParseException {
        DeleteFieldCommand expectedEmailCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_EMAIL_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(ONE, EMAIL, INDEX, ONE),
                expectedEmailCommand);

        DeleteFieldCommand expectedLinkCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_LINK_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(ONE, INDEX, ONE, LINK),
                expectedLinkCommand);

        DeleteFieldCommand expectedPhoneCommand = new DeleteFieldCommand(INDEX_TWO, DELETE_FIRST_PHONE_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(TWO, PHONE, INDEX, ONE),
                expectedPhoneCommand);

        DeleteFieldCommand expectedCourseCommand = new DeleteFieldCommand(INDEX_TWO, DELETE_FIRST_COURSE_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(TWO, INDEX, ONE, COURSE),
                expectedCourseCommand);

        DeleteFieldCommand expectedSpecCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_SPECIALISATION_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(ONE, SPECIALISATION, INDEX, ONE),
                expectedSpecCommand);

        DeleteFieldCommand expectedTagCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_TAG_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(ONE, INDEX, ONE, TAG),
                expectedTagCommand);
    }

    @Test
    public void parse_deleteValidInputWithWhiteSpaces_success() throws ParseException {
        DeleteFieldCommand expectedEmailCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_EMAIL_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(ONE, LONG_WHITESPACE, EMAIL, INDEX, ONE),
                expectedEmailCommand);

        DeleteFieldCommand expectedLinkCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_LINK_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(ONE, INDEX, LONG_WHITESPACE, ONE, LINK),
                expectedLinkCommand);

        DeleteFieldCommand expectedPhoneCommand = new DeleteFieldCommand(INDEX_TWO, DELETE_FIRST_PHONE_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(TWO, PHONE, INDEX, LONG_WHITESPACE, ONE),
                expectedPhoneCommand);

        DeleteFieldCommand expectedCourseCommand = new DeleteFieldCommand(INDEX_TWO, DELETE_FIRST_COURSE_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(LONG_WHITESPACE, TWO, LONG_WHITESPACE, INDEX, ONE, COURSE),
                expectedCourseCommand);

        DeleteFieldCommand expectedSpecCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_SPECIALISATION_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(ONE, SPECIALISATION, INDEX, LONG_WHITESPACE, ONE, LONG_WHITESPACE),
                expectedSpecCommand);

        DeleteFieldCommand expectedTagCommand = new DeleteFieldCommand(INDEX_ONE, DELETE_FIRST_TAG_ACTION);
        CommandParserTestUtil.assertParseSuccess(parser,
                generateUserInput(LONG_WHITESPACE, ONE, LONG_WHITESPACE, INDEX,
                        LONG_WHITESPACE, ONE, LONG_WHITESPACE, TAG),
                expectedTagCommand);
    }
}
