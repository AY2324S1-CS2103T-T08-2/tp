package networkbook.logic.parser;

import static networkbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static networkbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import networkbook.logic.Messages;
import networkbook.logic.commands.CommandTestUtil;
import networkbook.logic.commands.edit.EditCommand;
import networkbook.logic.commands.edit.EditCommandUtil;
import networkbook.logic.commands.edit.EditCourseAction;
import networkbook.logic.commands.edit.EditEmailAction;
import networkbook.logic.commands.edit.EditGraduationAction;
import networkbook.logic.commands.edit.EditLinkAction;
import networkbook.logic.commands.edit.EditNameAction;
import networkbook.logic.commands.edit.EditPhoneAction;
import networkbook.logic.commands.edit.EditPriorityAction;
import networkbook.logic.commands.edit.EditSpecialisationAction;
import networkbook.logic.commands.edit.EditTagAction;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Name;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.testutil.TypicalIndexes;

public class EditCommandParserTest {
    private static final EditCommandParser PARSER = new EditCommandParser();
    private static final String USAGE_MESSAGE = String.format(
            Messages.MESSAGE_INVALID_COMMAND_FORMAT,
            EditCommand.MESSAGE_USAGE
    );

    @Test
    public void parse_noPreamble_failure() {
        assertParseFailure(PARSER, "", USAGE_MESSAGE);
        assertParseFailure(PARSER, CliSyntax.PREFIX_INDEX + " 1", USAGE_MESSAGE);
        assertParseFailure(PARSER,
                CliSyntax.PREFIX_PHONE + " 123456 " + CliSyntax.PREFIX_INDEX + " 1", USAGE_MESSAGE);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        assertParseFailure(PARSER, "-1", USAGE_MESSAGE);
        assertParseFailure(PARSER, "-1 " + CliSyntax.PREFIX_PHONE
                + " 123456 " + CliSyntax.PREFIX_INDEX + " 1", USAGE_MESSAGE);
        assertParseFailure(PARSER, "0", USAGE_MESSAGE);
        assertParseFailure(PARSER, "1 quack", USAGE_MESSAGE);
        assertParseFailure(PARSER, "1 quack " + CliSyntax.PREFIX_PHONE + " 123456 "
                + CliSyntax.PREFIX_INDEX + " 1", USAGE_MESSAGE);
    }

    @Test
    public void parse_noFieldSpecified_failure() {
        assertParseFailure(PARSER, "1", Messages.MESSAGE_EXACTLY_ONE_FIELD);
        assertParseFailure(PARSER, "1 " + CliSyntax.PREFIX_INDEX + " 1", Messages.MESSAGE_EXACTLY_ONE_FIELD);
    }

    @Test
    public void parse_multipleFieldsSpecified_failure() {
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_PHONE_DESC + CommandTestUtil.VALID_COURSE_DESC
                        + CommandTestUtil.VALID_INDEX_DESC,
                Messages.MESSAGE_EXACTLY_ONE_FIELD);
    }

    @Test
    public void parse_multipleIndicesSpecified_failure() {
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_PHONE_DESC + CommandTestUtil.VALID_INDEX_DESC
                        + CommandTestUtil.VALID_INDEX_DESC,
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, CliSyntax.PREFIX_PHONE));
    }

    @Test
    public void parse_listItemFieldWithoutIndexSpecified_failure() {
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_PHONE_DESC,
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, CliSyntax.PREFIX_PHONE));
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_EMAIL_DESC,
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, CliSyntax.PREFIX_EMAIL));
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_LINK_DESC,
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, CliSyntax.PREFIX_LINK));
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_COURSE_DESC,
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, CliSyntax.PREFIX_COURSE));
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_SPECIALISATION_DESC,
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, CliSyntax.PREFIX_SPECIALISATION));
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_TAG_DESC,
                String.format(Messages.MESSAGE_INDEX_MUST_BE_PRESENT, CliSyntax.PREFIX_TAG));
    }

    @Test
    public void parse_singleValueFieldWithIndexSpecified_failure() {
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_NAME_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Messages.MESSAGE_INDEX_CANNOT_BE_PRESENT);
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_GRADUATION_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Messages.MESSAGE_INDEX_CANNOT_BE_PRESENT);
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.VALID_PRIORITY_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Messages.MESSAGE_INDEX_CANNOT_BE_PRESENT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_NAME_DESC,
                Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_PHONE_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_EMAIL_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_LINK_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Link.MESSAGE_CONSTRAINTS); // invalid link
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_GRADUATION_DESC,
                Graduation.MESSAGE_CONSTRAINTS); // invalid Graduating Year
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_COURSE_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Course.NO_COURSE_NAME); // invalid Course
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_SPECIALISATION_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Specialisation.MESSAGE_CONSTRAINTS); // invalid Specialisation
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_TAG_DESC + CommandTestUtil.VALID_INDEX_DESC,
                Tag.MESSAGE_CONSTRAINTS); // invalid tag
        assertParseFailure(PARSER,
                "1" + CommandTestUtil.INVALID_PRIORITY_DESC,
                Priority.MESSAGE_CONSTRAINTS); // invalid priority
    }

    @Test
    public void parse_validName_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_NAME + " " + CommandTestUtil.VALID_NAME_AMY;
        EditNameAction expectedEditAction = new EditNameAction(new Name(CommandTestUtil.VALID_NAME_AMY));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedEditAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }

    @Test
    public void parse_validPhoneAndIndex_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_PHONE + " " + CommandTestUtil.VALID_PHONE_AMY + " "
                + CliSyntax.PREFIX_INDEX + " " + EditCommandUtil.VALID_INDEX.getOneBased();
        EditPhoneAction expectedAction =
                new EditPhoneAction(EditCommandUtil.VALID_INDEX, new Phone(CommandTestUtil.VALID_PHONE_AMY));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }

    @Test
    public void parse_validEmailAndIndex_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_EMAIL + " " + CommandTestUtil.VALID_EMAIL_AMY + " "
                + CliSyntax.PREFIX_INDEX + " " + EditCommandUtil.VALID_INDEX.getOneBased();
        EditEmailAction expectedAction =
                new EditEmailAction(EditCommandUtil.VALID_INDEX, new Email(CommandTestUtil.VALID_EMAIL_AMY));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }

    @Test
    public void parse_validLinkAndIndex_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_LINK + " " + CommandTestUtil.VALID_LINK_AMY + " "
                + CliSyntax.PREFIX_INDEX + " " + EditCommandUtil.VALID_INDEX.getOneBased();
        EditLinkAction expectedAction =
                new EditLinkAction(EditCommandUtil.VALID_INDEX, new Link(CommandTestUtil.VALID_LINK_AMY));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }

    @Test
    public void parse_validGraduation_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_GRADUATION + " " + CommandTestUtil.VALID_GRADUATION_AMY;
        EditGraduationAction expectedAction = new EditGraduationAction(
                new Graduation(CommandTestUtil.VALID_GRADUATION_AMY));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }

    @Test
    public void parse_validCourseAndIndex_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_COURSE + " " + CommandTestUtil.VALID_COURSE_AMY + " "
                + CliSyntax.PREFIX_INDEX + " " + EditCommandUtil.VALID_INDEX.getOneBased();
        EditCourseAction expectedAction =
                new EditCourseAction(TypicalIndexes.INDEX_FIRST_PERSON, new Course(CommandTestUtil.VALID_COURSE_AMY));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }

    @Test
    public void parse_validSpecialisationAndIndex_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_SPECIALISATION + " " + CommandTestUtil.VALID_SPECIALISATION_AMY + " "
                + CliSyntax.PREFIX_INDEX + " " + EditCommandUtil.VALID_INDEX.getOneBased();
        EditSpecialisationAction expectedAction = new EditSpecialisationAction(
                TypicalIndexes.INDEX_FIRST_PERSON, new Specialisation(CommandTestUtil.VALID_SPECIALISATION_AMY));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }

    @Test
    public void parse_validTagAndIndex_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_TAG + " " + CommandTestUtil.VALID_TAG_FRIEND + " "
                + CliSyntax.PREFIX_INDEX + " " + EditCommandUtil.VALID_INDEX.getOneBased();
        EditTagAction expectedAction =
                new EditTagAction(TypicalIndexes.INDEX_FIRST_PERSON, new Tag(CommandTestUtil.VALID_TAG_FRIEND));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }

    @Test
    public void parse_validPriority_success() {
        String userInput = TypicalIndexes.INDEX_FIRST_PERSON.getOneBased() + " "
                + CliSyntax.PREFIX_PRIORITY + " " + CommandTestUtil.VALID_PRIORITY_AMY;
        EditPriorityAction expectedAction = new EditPriorityAction(new Priority(CommandTestUtil.VALID_PRIORITY_AMY));
        EditCommand expectedCommand = new EditCommand(TypicalIndexes.INDEX_FIRST_PERSON, expectedAction);
        assertParseSuccess(PARSER, userInput, expectedCommand);
    }
}
