package networkbook.logic.parser;

import static networkbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static networkbook.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.logic.Messages;
import networkbook.logic.commands.filter.FilterCommand;
import networkbook.logic.commands.filter.FilterCourseCommand;
import networkbook.logic.commands.filter.FilterGradCommand;
import networkbook.logic.commands.filter.FilterSpecCommand;
import networkbook.logic.commands.filter.FilterTagCommand;
import networkbook.model.person.filter.CourseContainsKeyTermsPredicate;
import networkbook.model.person.filter.CourseIsStillBeingTakenPredicate;
import networkbook.model.person.filter.GradEqualsOneOfPredicate;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ",
                String.format(FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noWithFields_throwsParseException() {
        assertParseFailure(parser, "filter /by a",
                String.format(FilterCommandParser.MISSING_FIELD));
    }

    @Test
    public void parse_noByFields_throwsParseException() {
        assertParseFailure(parser, "filter /with a",
                String.format(FilterCommandParser.MISSING_FIELD));
    }

    @Test
    public void parse_noArgs_throwsParseException() {
        assertParseFailure(parser, "filter /by course /with ",
                String.format(FilterCommandParser.MISSING_FIELD));
    }

    @Test
    public void parse_unknownField_throwsParseException() {
        assertParseFailure(parser, "filter /by what /with what",
                String.format(FilterCommandParser.UNKNOWN_FIELD));
    }

    @Test
    public void parse_fields_returnsMatchingType() throws Exception {
        FilterCommand courseCommand = parser.parse("filter /by course /with a");
        FilterCommand specCommand = parser.parse("filter /by spec /with a");
        FilterCommand gradCommand = parser.parse("filter /by grad /with 2000");
        FilterCommand tagCommand = parser.parse("filter /by tag /with a b c");

        assertTrue(courseCommand instanceof FilterCourseCommand);
        assertTrue(specCommand instanceof FilterSpecCommand);
        assertTrue(gradCommand instanceof FilterGradCommand);
        assertTrue(tagCommand instanceof FilterTagCommand);
    }

    @Test
    public void parseCourse_emptyString_throwsParseException() {
        assertParseFailure(parser, "filter /by course /with ",
                FilterCommandParser.MISSING_FIELD);
    }

    @Test
    public void parseCourse_blank_throwsParseException() {
        // slightly different to the above example
        assertParseFailure(parser, "filter /by course /with",
                FilterCommandParser.MISSING_FIELD);
    }

    @Test
    public void parseCourse_noArgs_throwsParseException() {
        assertParseFailure(parser, "filter /by course /with /taken false",
                FilterCommandParser.MISSING_FIELD);
    }

    @Test
    public void parseCourse_validArgs_returnsFilterCourseCommand() {
        FilterCommand expectedCommand = new FilterCourseCommand(
                new CourseContainsKeyTermsPredicate(List.of("Alice", "Bob")),
                new CourseIsStillBeingTakenPredicate(LocalDate.now()),
                false);

        // no whitespaces
        assertParseSuccess(parser, "filter /by course /with Alice Bob", expectedCommand);

        // multiple whitespaces
        assertParseSuccess(parser, "filter /by   course   /with  Alice    Bob", expectedCommand);
    }

    @Test
    public void parseCourse_takenFieldNotTrueOrFalse_throwsParseException() {
        assertParseFailure(parser, "filter /by course /with Alice /taken no",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseCourse_validTakenField_success() {
        FilterCommand expectedCommand = new FilterCourseCommand(
                new CourseContainsKeyTermsPredicate(List.of("Alice", "Bob")),
                new CourseIsStillBeingTakenPredicate(LocalDate.now()),
                true);

        assertParseSuccess(parser, "filter /by course /with Alice Bob /taken true", expectedCommand);
    }

    @Test
    public void parseGrad_noArgs_throwsParseException() {
        assertParseFailure(parser, "filter /by grad /with ", FilterCommandParser.MISSING_FIELD);
    }

    @Test
    public void parseGrad_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "filter /by grad /with ahhh", FilterGradCommand.ALL_NUMBERS);
    }

    @Test
    public void parseGrad_validArgs_returnsFilterGradCommand() {
        FilterGradCommand expectedCommand = new FilterGradCommand(new GradEqualsOneOfPredicate(List.of(2022, 2023)));

        // no whitespaces
        assertParseSuccess(parser, "filter /by grad /with 2022 2023", expectedCommand);

        // multiple whitespaces
        assertParseSuccess(parser, "filter /by grad /with 2022   2023", expectedCommand);
    }
}
