package networkbook.logic.parser;

import static networkbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static networkbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import networkbook.commons.core.index.Index;
import networkbook.logic.Messages;
import networkbook.logic.commands.AddCommand;
import networkbook.logic.commands.CommandTestUtil;
import networkbook.model.person.Course;
import networkbook.model.person.Email;
import networkbook.model.person.Graduation;
import networkbook.model.person.Link;
import networkbook.model.person.Phone;
import networkbook.model.person.Priority;
import networkbook.model.person.Specialisation;
import networkbook.model.person.Tag;
import networkbook.testutil.AddPersonDescriptorBuilder;
import networkbook.testutil.TypicalIndexes;

public class AddCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, AddCommand.MESSAGE_USAGE);

    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, CommandTestUtil.VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", AddCommand.MESSAGE_NO_INFO);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + CommandTestUtil.NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + CommandTestUtil.NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 /prior string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_PHONE_DESC,
                Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_EMAIL_DESC,
                Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_LINK_DESC,
                Link.MESSAGE_CONSTRAINTS); // invalid link
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_GRADUATION_DESC,
                Graduation.MESSAGE_CONSTRAINTS); // invalid Graduating Year
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_COURSE_DESC,
                Course.MESSAGE_CONSTRAINTS); // invalid Course
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_SPECIALISATION_DESC,
                Specialisation.MESSAGE_CONSTRAINTS); // invalid Specialisation
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_TAG_DESC,
                Tag.MESSAGE_CONSTRAINTS); // invalid tag
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_PRIORITY_DESC,
                Priority.MESSAGE_CONSTRAINTS); // invalid priority

        // invalid phone followed by valid email
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_PHONE_DESC + CommandTestUtil.EMAIL_DESC_AMY,
                Phone.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser,
                "1" + CommandTestUtil.INVALID_EMAIL_DESC
                        + CommandTestUtil.VALID_LINK_AMY + CommandTestUtil.VALID_PHONE_AMY,
                Email.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nameSpecified_failure() {
        Index index = TypicalIndexes.INDEX_FIRST_PERSON;
        String userInput = index.getOneBased() + CommandTestUtil.VALID_NAME_DESC;
        assertParseFailure(parser, userInput, AddCommandParser.MESSAGE_MULTIPLE_NAMES);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = TypicalIndexes.INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.TAG_DESC_HUSBAND
                + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.LINK_DESC_AMY
                + CommandTestUtil.GRADUATION_DESC_AMY + CommandTestUtil.COURSE_DESC_AMY
                + CommandTestUtil.SPECIALISATION_DESC_AMY
                + CommandTestUtil.TAG_DESC_FRIEND;

        AddCommand.AddPersonDescriptor descriptor = new AddPersonDescriptorBuilder()
                .withPhone(CommandTestUtil.VALID_PHONE_BOB)
                .withEmail(CommandTestUtil.VALID_EMAIL_AMY)
                .withLink(CommandTestUtil.VALID_LINK_AMY)
                .withGraduation(CommandTestUtil.VALID_GRADUATION_AMY)
                .withCourse(CommandTestUtil.VALID_COURSE_AMY)
                .withSpecialisation(CommandTestUtil.VALID_SPECIALISATION_AMY)
                .withTags(CommandTestUtil.VALID_TAG_HUSBAND, CommandTestUtil.VALID_TAG_FRIEND)
                .build();
        AddCommand expectedCommand = new AddCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = TypicalIndexes.INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.EMAIL_DESC_AMY;

        AddCommand.AddPersonDescriptor descriptor = new AddPersonDescriptorBuilder()
                .withPhone(CommandTestUtil.VALID_PHONE_BOB)
                .withEmail(CommandTestUtil.VALID_EMAIL_AMY).build();
        AddCommand expectedCommand = new AddCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        Index targetIndex = TypicalIndexes.INDEX_THIRD_PERSON;

        // phone
        String userInput = targetIndex.getOneBased() + CommandTestUtil.PHONE_DESC_AMY;
        AddCommand.AddPersonDescriptor descriptor = new AddPersonDescriptorBuilder()
                .withPhone(CommandTestUtil.VALID_PHONE_AMY).build();
        AddCommand expectedCommand = new AddCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + CommandTestUtil.EMAIL_DESC_AMY;
        descriptor = new AddPersonDescriptorBuilder().withEmail(CommandTestUtil.VALID_EMAIL_AMY).build();
        expectedCommand = new AddCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // link
        userInput = targetIndex.getOneBased() + CommandTestUtil.LINK_DESC_AMY;
        descriptor = new AddPersonDescriptorBuilder().withLink(CommandTestUtil.VALID_LINK_AMY).build();
        expectedCommand = new AddCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // graduation date
        userInput = targetIndex.getOneBased() + CommandTestUtil.GRADUATION_DESC_AMY;
        descriptor = new AddPersonDescriptorBuilder().withGraduation(CommandTestUtil.VALID_GRADUATION_AMY)
                    .build();
        expectedCommand = new AddCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // course
        userInput = targetIndex.getOneBased() + CommandTestUtil.COURSE_DESC_AMY;
        descriptor = new AddPersonDescriptorBuilder().withCourse(CommandTestUtil.VALID_COURSE_AMY).build();
        expectedCommand = new AddCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // specialisation
        userInput = targetIndex.getOneBased() + CommandTestUtil.SPECIALISATION_DESC_AMY;
        descriptor = new AddPersonDescriptorBuilder().withSpecialisation(CommandTestUtil.VALID_SPECIALISATION_AMY)
                    .build();
        expectedCommand = new AddCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // tags
        userInput = targetIndex.getOneBased() + CommandTestUtil.TAG_DESC_FRIEND;
        descriptor = new AddPersonDescriptorBuilder().withTags(CommandTestUtil.VALID_TAG_FRIEND).build();
        expectedCommand = new AddCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonTagValue_failure()

        // valid followed by invalid
        Index targetIndex = TypicalIndexes.INDEX_FIRST_PERSON;

        // mulltiple valid fields repeated
        String userInput = targetIndex.getOneBased() + CommandTestUtil.PHONE_DESC_AMY + CommandTestUtil.LINK_DESC_AMY
                + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.TAG_DESC_FRIEND + CommandTestUtil.PHONE_DESC_AMY
                + CommandTestUtil.LINK_DESC_AMY + CommandTestUtil.EMAIL_DESC_AMY + CommandTestUtil.TAG_DESC_FRIEND
                + CommandTestUtil.PHONE_DESC_BOB + CommandTestUtil.LINK_DESC_BOB + CommandTestUtil.EMAIL_DESC_BOB
                + CommandTestUtil.TAG_DESC_HUSBAND;

        assertParseFailure(parser, userInput, ParserUtil.MESSAGE_PHONE_DUPLICATE);
    }
}
