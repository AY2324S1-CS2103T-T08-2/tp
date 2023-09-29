package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.find.FindEmailCommand;

public class FindEmailCommandParserTest {
    private FindEmailCommandParser parser = new FindEmailCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "   ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEmailCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindEmailCommand() {
        FindEmailCommand expected = new FindEmailCommand("nkn");
        assertParseSuccess(parser, " e/nkn", expected);
        assertParseSuccess(parser, "  e/   nkn  ", expected);
    }
}
