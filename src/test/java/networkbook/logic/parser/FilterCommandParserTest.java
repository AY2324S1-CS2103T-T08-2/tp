package networkbook.logic.parser;

import static networkbook.logic.parser.CommandParserTestUtil.assertParseFailure;
import static networkbook.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import networkbook.logic.Messages;
import networkbook.logic.commands.filter.FilterCommand;
import networkbook.model.person.filter.CourseContainsKeyTermsPredicate;
import networkbook.model.person.filter.CourseIsStillBeingTakenPredicate;

public class FilterCommandParserTest {

    private FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "    ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_noFields_throwsParseException() {
        assertParseFailure(parser, "filter /with ",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand(
                new CourseContainsKeyTermsPredicate(List.of("Alice", "Bob")),
                new CourseIsStillBeingTakenPredicate(LocalDate.now()),
                false);

        // no whitespaces
        assertParseSuccess(parser, "filter /with Alice Bob", expectedCommand);

        // multiple whitespaces
        assertParseSuccess(parser, "filter /with  Alice    Bob", expectedCommand);
    }

    @Test
    public void parse_takenFieldNotTrueOrFalse_throwsParseException() {
        assertParseFailure(parser, "filter /with Alice /taken no",
                String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validTakenField_success() {
        FilterCommand expectedCommand = new FilterCommand(
                new CourseContainsKeyTermsPredicate(List.of("Alice", "Bob")),
                new CourseIsStillBeingTakenPredicate(LocalDate.now()),
                true);

        assertParseSuccess(parser, "filter /with Alice Bob /taken true", expectedCommand);
    }
}
